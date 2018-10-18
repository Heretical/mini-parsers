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

import heretical.parser.common.BaseSyntaxGrammar;
import heretical.parser.common.util.DoubleVar;
import heretical.parser.temporal.expression.DurationExp;
import heretical.parser.temporal.expression.ISO8601DurationExp;
import heretical.parser.temporal.expression.NaturalDurationExp;
import heretical.parser.temporal.units.DurationUnits;
import org.parboiled.Rule;
import org.parboiled.annotations.DontLabel;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.support.Var;

/**
 *
 */
public class DurationGrammar extends BaseSyntaxGrammar<DurationExp>
  {

  public Rule Root()
    {
    return FirstOf(
      NaturalDurationEOI(),
      ISO8601DurationEOI() );
    }

  public Rule ISO8601DurationEOI()
    {
    return Sequence( ISO8601DurationMatch(), EOI );
    }

  public Rule ISO8601DurationMatch()
    {
    Var<ISO8601DurationExp> var = new Var<>( new ISO8601DurationExp() );

    return Sequence(
      ISO8601Duration(),
      var.get().setValue( match() ),
      Spacing(),
      push( var.get() )
    );
    }

  /**
   * (+-)P(+-)nDT(+-)nH(+-)nM(+-)n.nS
   */
  protected Rule ISO8601Duration()
    {
    return Sequence(
      Optional( "+-" ),
      IgnoreCase( 'P' ),
      Optional(
        Optional( "+-" ),
        OneOrMore( Digit() ),
        IgnoreCase( 'D' )
      ),
      Optional(
        Sequence( IgnoreCase( 'T' ), Test( Optional( Optional( "+-" ), OneOrMore( Digit() ) ) ),
          Optional(
            Optional( "+-" ),
            OneOrMore( Digit() ),
            IgnoreCase( 'H' )
          ),
          Optional(
            Optional( "+-" ),
            OneOrMore( Digit() ),
            IgnoreCase( 'M' )
          ),
          Optional(
            Optional( "+-" ),
            OneOrMore( Digit() ),
            Optional(
              '.',
              OneOrMore( Digit() )
            ),
            IgnoreCase( 'S' )
          )
        )
      )
    );
    }

  public Rule NaturalDurationEOI()
    {
    return Sequence( NaturalDurationMatch(), EOI );
    }

  public Rule NaturalDurationMatch()
    {
    Var<NaturalDurationExp> var = new Var<>( new NaturalDurationExp() );
    DoubleVar amount = new DoubleVar();
    Var<DurationUnits> unit = new Var<>();

    return Sequence(
      Units( amount, unit ),
      var.get().setAmount( amount.get() ),
      var.get().setUnit( unit.get()),
      push( var.get() )
    );
    }

  protected Rule Units( DoubleVar amount, Var<DurationUnits> unit )
    {
    List<Rule> rules = new ArrayList<>();

    for( DurationUnits durationUnits : DurationUnits.ordered() )
      rules.add( Unit( durationUnits, amount, unit ) );

    return FirstOf( rules.toArray() );
    }

  @SuppressNode
  @DontLabel
  protected Rule Unit( DurationUnits durationUnits, DoubleVar amount, Var<DurationUnits> unit )
    {
    return
      Sequence(
        Optional( DoubleNumber(), amount.set( match() ) ),
        Spacing(),
        IgnoreCase( durationUnits.name() ),
        unit.set( durationUnits ),
        Spacing()
      );
    }

  }
