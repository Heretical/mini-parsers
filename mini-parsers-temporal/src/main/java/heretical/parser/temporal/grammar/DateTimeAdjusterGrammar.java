/*
 * Copyright (c) 2018-2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.grammar;

import java.util.Set;

import heretical.parser.common.BaseSyntaxGrammar;
import heretical.parser.temporal.expression.AdjusterExp;
import heretical.parser.temporal.expression.BinaryOp;
import heretical.parser.temporal.units.TimeUnit;
import org.parboiled.Rule;
import org.parboiled.annotations.Cached;
import org.parboiled.common.Reference;
import org.parboiled.support.Var;

/**
 * <pre>
 * http://docs.splunk.com/Documentation/SplunkCloud/7.0.3/SearchReference/SearchTimeModifiers
 * http://docs.splunk.com/Documentation/Splunk/7.1.3/Search/Specifytimemodifiersinyoursearch
 */
public class DateTimeAdjusterGrammar extends BaseSyntaxGrammar<AdjusterExp>
  {
  @SuppressWarnings("unused")
  public DateTimeAdjusterGrammar()
    {
    }

  public Rule Root()
    {
    Var<AdjusterExp> relativeTime = new Var<>( new AdjusterExp() );

    return Sequence(
      FirstOf(
        Now(), // now
        Sequence(
          Optional(
            Adjust( relativeTime ) ) // [+|-][<time_integer>]<time_unit>
          ,
          Optional(
            Snap( relativeTime ) // @<time_unit>[<ordinal>][[+|-]<time_integer><time_unit>]
          )
        )
      ),
      push( relativeTime.get() ),
      Spacing(),
      EOI
    );
    }

  public Rule Now()
    {
    return IgnoreCase( "now" );
    }

  public Rule Adjust( Var<AdjusterExp> relativeTime )
    {
    Var<TimeUnit> adjustUnit = new Var<>();

    return Sequence(
      Optional( AnyOf( BinaryOp.chars() ), relativeTime.get().setAmountOp( match() ) ),
      Optional( Number(), relativeTime.get().setAmount( match() ) ),
      Units( adjustUnit ), relativeTime.get().setAmountUnit( adjustUnit.get() )
    );
    }

  public Rule Snap( Var<AdjusterExp> relativeTime )
    {
    Var<TimeUnit> snapUnit = new Var<>();
    Var<TimeUnit> offsetUnit = new Var<>();

    return Sequence(
      '@',
      Units( snapUnit ), relativeTime.get().setSnapUnit( snapUnit.get() ),
      Optional( Number(), relativeTime.get().setSnapOrdinal( match() ) ),
      Optional(
        AnyOf( BinaryOp.chars() ), relativeTime.get().setOffsetOp( match() ),
        Number(), relativeTime.get().setOffset( match() ),
        Units( offsetUnit ), relativeTime.get().setOffsetUnit( offsetUnit.get() )
      )
    );
    }

  @Cached
  public Rule Units( Var<TimeUnit> relativeTime )
    {
    Set<TimeUnit.Token> tokens = TimeUnit.tokens();

    Rule[] units = new Rule[ tokens.size() ];

    int count = 0;

    for( TimeUnit.Token token : tokens )
      {
      units[ count++ ] = Unit( relativeTime, token.abbreviation(), token.unit() );
      }

    return FirstOf( units );
    }

  public Rule Unit( Var<TimeUnit> relativeTime, String abbreviation, TimeUnit timeUnit )
    {
    Reference<TimeUnit> ref = new Reference<>( timeUnit );
    return Sequence( abbreviation, relativeTime.set( ref.get() ) );
    }
  }
