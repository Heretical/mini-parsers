/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal;

import java.time.Duration;
import java.util.function.BiFunction;

import heretical.parser.temporal.expression.DurationExp;
import heretical.parser.temporal.grammar.DurationGrammar;
import org.parboiled.Rule;

/**
 * The DurationParser class parses strings that represent durations.
 * <p>
 * This grammar can distinguish between either ISO-8601 duration strings, like `PT20.345S`, or simplified
 * natural language duration strings, like `10 days` or `15min`, and resolve them into `java.time.Duration` instances.
 * <p>
 * See {@link ISODurationParser} or {@link NaturalDurationParser} for more specific parsers.
 * <p>
 * This class is not thread-safe.
 */
public class DurationParser extends BaseTemporalExpressionParser<Duration, DurationExp, DurationGrammar>
  {
  public DurationParser()
    {
    }

  public DurationParser( Context context )
    {
    super( context );
    }

  @Override
  protected Class<DurationGrammar> getParserClass()
    {
    return DurationGrammar.class;
    }

  @Override
  protected Rule getGrammar( DurationGrammar parser )
    {
    return parser.Root();
    }

  @Override
  protected BiFunction<Context, DurationExp, Duration> getFunction()
    {
    return ( context, expression ) -> expression.toDuration( context );
    }
  }
