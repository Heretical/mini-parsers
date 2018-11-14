/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import heretical.parser.common.BaseSyntaxGrammar;
import heretical.parser.common.util.IntegerVar;
import heretical.parser.temporal.expression.AbsoluteDateTimeExp;
import heretical.parser.temporal.expression.DateTimeExp;
import heretical.parser.temporal.expression.OffsetDateTimeExp;
import heretical.parser.temporal.expression.OrdinalDateTimeExp;
import heretical.parser.temporal.format.DateTimeFormats;
import heretical.parser.temporal.units.CalendarUnit;
import heretical.parser.temporal.units.RelativeDateUnit;
import org.parboiled.Rule;
import org.parboiled.annotations.Cached;
import org.parboiled.annotations.DontLabel;
import org.parboiled.support.StringVar;
import org.parboiled.support.Var;

/**
 *
 */
@SuppressWarnings("ALL")
public class DateTimeGrammar extends BaseSyntaxGrammar<DateTimeExp>
  {
  public Rule Root()
    {
    return FirstOf(
      UnaryRelativeDateTimeEOI(),
      AbsoluteDateTimeEOI(),
      RelativeDateTimeEOI()
    );
    }

  public Rule AbsoluteDateTimeEOI()
    {
    return Sequence( AbsoluteDateTime(), EOI );
    }

  public Rule AbsoluteDateTime()
    {
    Var<AbsoluteDateTimeExp> var = new Var<>( new AbsoluteDateTimeExp() );
    StringVar value = new StringVar();
    Var<DateTimeFormats.DateTimeParser> parser = new Var<>();

    return Sequence(
      DateTimeFormats( parser, value ),
      Spacing(),
      var.get().setValue( value.get() ),
      var.get().setParser( parser.get() ),
      push( var.get() )
    );
    }

  @Cached
  Rule DateTimeFormats( Var<DateTimeFormats.DateTimeParser> parserVar, StringVar valueVar )
    {
    List<Rule> formatRules = new ArrayList<>();

    for( Map.Entry<String, DateTimeFormats.DateTimeParser> entry : DateTimeFormats.getPatternMap().entrySet() )
      formatRules.add( FormatRule( entry.getValue(), valueVar, parserVar ) );

    return FirstOf( formatRules.toArray() );
    }

  @DontLabel
  @Cached
  Rule FormatRule( DateTimeFormats.DateTimeParser parser, StringVar valueVar, Var<DateTimeFormats.DateTimeParser> parserVar )
    {
    List<Rule> sequence = new ArrayList<>();
    List<Object> parse = DateTimeFormats.parsePattern( parser.getPattern() );

    for( Object symbol : parse )
      {
      if( symbol instanceof DateTimeFormats.Symbol )
        sequence.add( DateTimeSymbol( (DateTimeFormats.Symbol) symbol ).label( ( (DateTimeFormats.Symbol) symbol ).getSymbolFormat() ) );
      else
        sequence.add( toRule( symbol ) );
      }

    return Sequence(
      Sequence( sequence.toArray() ),
      valueVar.set( match() ),
      parserVar.set( parser ),
      Spacing()
    ).label( parser.getPattern() );
    }

  @DontLabel
  Rule DateTimeSymbol( DateTimeFormats.Symbol symbol )
    {
    switch( symbol.getCharType() )
      {
      case text:
        return Symbol( symbol, Letter() );

      case digit:
        return Symbol( symbol, Digit() );

      case time_digits:
        return FirstOf( 'Z', Symbol( symbol, TimeDigit() ) );

      case literal:
        return FirstOf( IgnoreCase( symbol.literals ) );
      }

    throw new IllegalStateException( "unsupported char type: " + symbol.getCharType() + " for: " + symbol.getSymbolFormat() );
    }

  Rule Symbol( DateTimeFormats.Symbol symbol, Rule rule )
    {
    if( symbol.minLength == -1 && symbol.maxLength == -1 )
      return OneOrMore( rule );

    if( symbol.maxLength == -1 )
      return Sequence( NTimes( symbol.minLength - 1, rule ), OneOrMore( rule ) );

    if( symbol.minLength == symbol.maxLength && symbol.maxLength > 0 )
      return NTimes( symbol.maxLength, rule );

    return Sequence( NTimes( symbol.minLength, rule ), ZeroOrMore( rule ) );
    }

  public Rule RelativeDateTimeEOI()
    {
    return Sequence( RelativeDateTime(), EOI );
    }

  Rule UnaryRelativeDateTime()
    {
    return FirstOf(
      RelativeDate( RelativeDateUnit.now ),
      RelativeDate( RelativeDateUnit.today ),
      RelativeDate( RelativeDateUnit.yesterday ),
      ThisTerm(),
      RelativeLastUnitSymbol( CalendarUnit.months ),
      RelativeLastUnitSymbol( CalendarUnit.days ),
      LastTerm(),
      AgoTerm()
    );
    }

  Rule ThisTerm()
    {
    Var<OffsetDateTimeExp> var = new Var( new OffsetDateTimeExp() );
    StringVar unit = new StringVar();

    return
      Sequence(
        Keyword( "this", FirstOfKeyword( CalendarUnit.values() ), unit ),
        Spacing(),
        var.get().setUnit( CalendarUnit.valueOf( unit.get().toLowerCase() ) ),
        var.get().setOffset( 0 ),
        push( var.get() )
      );
    }

  Rule LastTerm()
    {
    Var<OffsetDateTimeExp> var = new Var( new OffsetDateTimeExp() );
    IntegerVar ordinal = new IntegerVar( 1 );
    StringVar unit = new StringVar();

    return Sequence(
      Keyword( "last", ordinal, FirstOfKeyword( CalendarUnit.values() ), unit ),
      Spacing(),
      var.get().setUnit( CalendarUnit.valueOf( unit.get().toLowerCase() ) ),
      var.get().setOffset( -1 * ordinal.get() ),
      push( var.get() )
    );
    }

  Rule AgoTerm()
    {
    Var<OffsetDateTimeExp> var = new Var( new OffsetDateTimeExp() );
    IntegerVar ordinal = new IntegerVar( 1 );
    StringVar unit = new StringVar();

    return Sequence(
      Keyword( ordinal, FirstOfKeyword( CalendarUnit.values() ), unit, "ago" ),
      Spacing(),
      var.get().setUnit( CalendarUnit.valueOf( unit.get().toLowerCase() ) ),
      var.get().setOffset( -1 * ordinal.get() ),
      push( var.get() )
    );
    }

  public Rule UnaryRelativeDateTimeEOI()
    {
    return Sequence( UnaryRelativeDateTime(), EOI );
    }

  public Rule RelativeDateTime()
    {
    return Sequence(
      FirstOf(
        RelativeDate( RelativeDateUnit.now ),
        RelativeDate( RelativeDateUnit.yesterday ),
        RelativeDate( RelativeDateUnit.today ),
        RelativeThisTerm(),
        RelativeLastUnitSymbol( CalendarUnit.months ),
        RelativeLastUnitSymbol( CalendarUnit.days ),
        RelativeLastTerm(),
        RelativeAgoTerm()
      ),
      Spacing()
    );
    }

  Rule RelativeDate( RelativeDateUnit dateUnit )
    {
    Var<OffsetDateTimeExp> var = new Var( new OffsetDateTimeExp( dateUnit ) );

    return Sequence(
      Keyword( dateUnit.name() ),
      Spacing(),
      push( var.get() )
    );
    }

  Rule RelativeThisTerm()
    {
    Var<OffsetDateTimeExp> var = new Var( new OffsetDateTimeExp() );
    StringVar unit = new StringVar();

    return Sequence(
      Keyword( "this", FirstOfKeyword( CalendarUnit.values() ), unit ),
      Spacing(),
      var.get().setUnit( CalendarUnit.valueOf( unit.get().toLowerCase() ) ),
      var.get().setOffset( 0 ),
      push( var.get() )
    );
    }

  Rule RelativeLastUnitSymbol( CalendarUnit unit )
    {
    Var<OrdinalDateTimeExp> var = new Var( new OrdinalDateTimeExp( unit ) );
    StringVar value = new StringVar();

    return Sequence(
      Keyword( "last", FirstOfKeyword( unit.getSymbols() ), value ),
      Spacing(),
      var.get().setOrdinal( Integer.valueOf( value.get() ) ),
      push( var.get() )
    );
    }

  Rule RelativeLastTerm()
    {
    Var<OffsetDateTimeExp> var = new Var( new OffsetDateTimeExp() );
    IntegerVar offset = new IntegerVar( 1 );
    StringVar unit = new StringVar();

    return Sequence(
      Keyword( "last", offset, FirstOfKeyword( CalendarUnit.values() ), unit ),
      Spacing(),
      var.get().setUnit( CalendarUnit.valueOf( unit.get().toLowerCase() ) ),
      var.get().setOffset( -1 * offset.get() ),
      push( var.get() )
    );
    }

  Rule RelativeAgoTerm()
    {
    Var<OffsetDateTimeExp> var = new Var( new OffsetDateTimeExp() );
    IntegerVar offset = new IntegerVar( 1 );
    StringVar unit = new StringVar();

    return Sequence(
      Keyword( offset, FirstOfKeyword( CalendarUnit.values() ), unit, "ago" ),
      Spacing(),
      var.get().setUnit( CalendarUnit.valueOf( unit.get().toLowerCase() ) ),
      var.get().setOffset( -1 * offset.get() ),
      push( var.get() )
    );
    }

  public final Rule AND = Keyword( "and" );
  public final Rule OR = Keyword( "or" );

  Rule BooleanType()
    {
    return FirstOf( AND, OR );
    }

  Rule UnaryType()
    {
    return FirstOf( EQU, GE, GT, LE, LT, NOTEQUAL );
    }

  Rule RegEx( StringVar term )
    {
    return Sequence(
      '/',
      OneOrMore(
        FirstOf(
          Escape( '/' ),
          Sequence( TestNot( AnyOf( "\r\n\"\\/" ) ), ANY )
        )
      ).suppressSubnodes(),
      term.set( match() ),
      '/',
      Spacing()
    );
    }

  @Cached
  Rule Escape( Object rule )
    {
    return Sequence( '\\', FirstOf( AnyOf( "btnfr\"\'\\" ), rule, OctalEscape(), UnicodeEscape() ) );
    }

  }
