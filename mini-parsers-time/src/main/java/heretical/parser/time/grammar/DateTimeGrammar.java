/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.time.grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import heretical.parser.common.BaseSyntaxGrammar;
import heretical.parser.time.datetime.DateTime;
import heretical.parser.time.datetime.InstantDateTime;
import heretical.parser.time.datetime.OffsetDateTime;
import heretical.parser.time.datetime.OrdinalDateTime;
import heretical.parser.time.format.DateTimeFormats;
import heretical.parser.time.units.CalendarUnits;
import heretical.parser.time.units.RelativeDateUnits;
import heretical.parser.time.util.IntegerVar;
import org.parboiled.Rule;
import org.parboiled.annotations.Cached;
import org.parboiled.annotations.DontLabel;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.support.StringVar;
import org.parboiled.support.Var;

/**
 *
 */
@SuppressWarnings("ALL")
public class DateTimeGrammar extends BaseSyntaxGrammar<DateTime>
  {
  public Rule Root()
    {
    return FirstOf(
      UnaryRelativeDateTime(),
      InstantDateTimeMatch(),
      RelativeDateTime()
    );
    }

  public Rule InstantDateTimeMatch()
    {
    Var<InstantDateTime> var = new Var<>( new InstantDateTime() );
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
        if( symbol.minLength == -1 && symbol.maxLength == -1 )
          return OneOrMore( Letter() );

        if( symbol.maxLength == -1 )
          return Sequence( NTimes( symbol.minLength - 1, Letter() ), OneOrMore( Letter() ) );

        return Length( symbol, Letter() );

      case digit:
        if( symbol.maxLength == -1 )
          return OneOrMore( Digit() );

        return Length( symbol, Digit() );

      case time_digits:
        return FirstOf( 'Z', Length( symbol, TimeDigit() ) );

      case literal:
        return FirstOf( IgnoreCase( symbol.literals ) );
      }

    throw new IllegalStateException( "unsupported char type: " + symbol.getCharType() + " for: " + symbol.getSymbolFormat() );
    }

  Rule Length( DateTimeFormats.Symbol symbol, Rule rule )
    {
    if( symbol.maxLength > 0 )
      return NTimes( symbol.maxLength, rule );

    int iterations = symbol.maxLength + 1;

    Rule result = Optional( rule );

    for( int i = iterations; i < 0; i++ )
      {
      if( i == -1 )
        result = Sequence( rule, result );
      else
        result = Optional( rule, result );
      }

    return result;
    }

  Rule UnaryRelativeDateTime()
    {
    return FirstOf(
      RelativeDate( RelativeDateUnits.now ),
      RelativeDate( RelativeDateUnits.today ),
      RelativeDate( RelativeDateUnits.yesterday ),
      ThisTerm(),
      RelativeLastUnitSymbol( CalendarUnits.months ),
      RelativeLastUnitSymbol( CalendarUnits.days ),
      LastTerm(),
      AgoTerm()
    );
    }

  Rule ThisTerm()
    {
    Var<OffsetDateTime> var = new Var( new OffsetDateTime() );
    StringVar unit = new StringVar();

    return
      Sequence(
        Keyword( "this", FirstOfKeyword( CalendarUnits.values() ), unit ),
        Spacing(),
        var.get().setUnit( CalendarUnits.valueOf( unit.get().toLowerCase() ) ),
        var.get().setOffset( 0 ),
        push( var.get() )
      );
    }

  Rule LastTerm()
    {
    Var<OffsetDateTime> var = new Var( new OffsetDateTime() );
    IntegerVar ordinal = new IntegerVar( 1 );
    StringVar unit = new StringVar();

    return Sequence(
      Keyword( "last", ordinal, FirstOfKeyword( CalendarUnits.values() ), unit ),
      Spacing(),
      var.get().setUnit( CalendarUnits.valueOf( unit.get().toLowerCase() ) ),
      var.get().setOffset( -1 * ordinal.get() ),
      push( var.get() )
    );
    }

  Rule AgoTerm()
    {
    Var<OffsetDateTime> var = new Var( new OffsetDateTime() );
    IntegerVar ordinal = new IntegerVar( 1 );
    StringVar unit = new StringVar();

    return Sequence(
      Keyword( ordinal, FirstOfKeyword( CalendarUnits.values() ), unit, "ago" ),
      Spacing(),
      var.get().setUnit( CalendarUnits.valueOf( unit.get().toLowerCase() ) ),
      var.get().setOffset( -1 * ordinal.get() ),
      push( var.get() )
    );
    }

  Rule RelativeDateTime()
    {
    return Sequence(
      FirstOf(
        RelativeDate( RelativeDateUnits.now ),
        RelativeDate( RelativeDateUnits.yesterday ),
        RelativeDate( RelativeDateUnits.today ),
        RelativeThisTerm(),
        RelativeLastUnitSymbol( CalendarUnits.months ),
        RelativeLastUnitSymbol( CalendarUnits.days ),
        RelativeLastTerm(),
        RelativeAgoTerm()
      ),
      Spacing()
    );
    }

  Rule RelativeDate( RelativeDateUnits dateUnit )
    {
    Var<OffsetDateTime> var = new Var( new OffsetDateTime( dateUnit ) );

    return Sequence(
      Keyword( dateUnit.name() ),
      Spacing(),
      push( var.get() )
    );
    }

  Rule RelativeThisTerm()
    {
    Var<OffsetDateTime> var = new Var( new OffsetDateTime() );
    StringVar unit = new StringVar();

    return Sequence(
      Keyword( "this", FirstOfKeyword( CalendarUnits.values() ), unit ),
      Spacing(),
      var.get().setUnit( CalendarUnits.valueOf( unit.get().toLowerCase() ) ),
      var.get().setOffset( 0 ),
      push( var.get() )
    );
    }

  Rule RelativeLastUnitSymbol( CalendarUnits unit )
    {
    Var<OrdinalDateTime> var = new Var( new OrdinalDateTime(unit) );
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
    Var<OffsetDateTime> var = new Var( new OffsetDateTime() );
    IntegerVar offset = new IntegerVar( 1 );
    StringVar unit = new StringVar();

    return Sequence(
      Keyword( "last", offset, FirstOfKeyword( CalendarUnits.values() ), unit ),
      Spacing(),
      var.get().setUnit( CalendarUnits.valueOf( unit.get().toLowerCase() ) ),
      var.get().setOffset( -1 * offset.get() ),
      push( var.get() )
    );
    }

  Rule RelativeAgoTerm()
    {
    Var<OffsetDateTime> var = new Var( new OffsetDateTime() );
    IntegerVar offset = new IntegerVar( 1 );
    StringVar unit = new StringVar();

    return Sequence(
      Keyword( offset, FirstOfKeyword( CalendarUnits.values() ), unit, "ago" ),
      Spacing(),
      var.get().setUnit( CalendarUnits.valueOf( unit.get().toLowerCase() ) ),
      var.get().setOffset( -1 * offset.get() ),
      push( var.get() )
    );
    }

  @SuppressNode
  @DontLabel
  Rule Keyword( String lhs, IntegerVar ordinal, Rule rhs, StringVar unit )
    {
    return
      Sequence(
        IgnoreCase( lhs ),
        Spacing(),
        Optional( DoubleNumber(), ordinal.set( match() ), Spacing() ),
        rhs,
        unit.set( match().trim() )
      );
    }

  @SuppressNode
  @DontLabel
  Rule Keyword( IntegerVar ordinal, Rule lhs, StringVar unit, String rhs )
    {
    return
      Sequence(
        Optional( DoubleNumber(), ordinal.set( match() ) ),
        Spacing(),
        lhs,
        unit.set( match().trim() ),
        Spacing(),
        IgnoreCase( rhs )
      );
    }

  @SuppressNode
  @DontLabel
  Rule Keyword( String lhs, Rule rhs, StringVar unit )
    {
    return
      Sequence(
        IgnoreCase( lhs ),
        Spacing(),
        rhs,
        unit.set( match().trim() )
      );
    }

  @SuppressNode
  @DontLabel
  Rule Keyword( String keyword )
    {
    return Terminal( keyword, LetterOrDigit() );
    }

  @SuppressNode
  @DontLabel
  Rule LetterOrDigit()
    {
    return FirstOf( CharRange( 'a', 'z' ), CharRange( 'A', 'Z' ), CharRange( '0', '9' ), '_' );
    }

  @SuppressNode
  @DontLabel
  Rule DoubleNumber()
    {
    return Sequence(
      Optional( Ch( '-' ) ),
      OneOrMore( FirstOf( Digit(), ',' ) ), // lax on the commas
      Optional( Ch( '.' ), OneOrMore( Digit() ) )
    );
    }

  @SuppressNode
  @DontLabel
  Rule DoubleNumberList()
    {
    // comma separates items
    return OneOrMore(
      Sequence(
        Optional( Ch( '-' ) ),
        OneOrMore( Digit() ),
        Optional( Ch( '.' ), OneOrMore( Digit() ) ),
        Optional( Ch( ',' ) )
      )
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

  Rule StringLiteral( StringVar term )
    {
    return Sequence(
      '"',
      ZeroOrMore(
        FirstOf(
          Escape(),
          Sequence( TestNot( AnyOf( "\r\n\"\\" ) ), ANY )
        )
      ).suppressSubnodes(),
      term.set( match() ),
      '"',
      Spacing()
    );
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

  Rule Escape()
    {
    return Sequence( '\\', FirstOf( AnyOf( "btnfr\"\'\\" ), OctalEscape(), UnicodeEscape() ) );
    }

  Rule OctalEscape()
    {
    return FirstOf(
      Sequence( CharRange( '0', '3' ), CharRange( '0', '7' ), CharRange( '0', '7' ) ),
      Sequence( CharRange( '0', '7' ), CharRange( '0', '7' ) ),
      CharRange( '0', '7' )
    );
    }

  Rule UnicodeEscape()
    {
    return Sequence( OneOrMore( 'u' ), HexDigit(), HexDigit(), HexDigit(), HexDigit() );
    }

  Rule FirstOfKeyword( Object[] terms )
    {
    List<Rule> rules = new ArrayList<>();

    for( Object level : terms )
      rules.add( Keyword( level.toString() ) );

    return FirstOf( rules.toArray( new Rule[ rules.size() ] ) );
    }
  }
