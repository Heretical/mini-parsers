/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal;

import java.time.Instant;
import java.util.function.BiFunction;

import heretical.parser.temporal.expression.AdjusterExp;
import heretical.parser.temporal.grammar.DateTimeAdjusterGrammar;
import org.parboiled.Rule;

/**
 * The RelativeDateTimeAdjusterParser class will adjust the current time based on a simple adjustment syntax, and return an
 * {@link Instant}.
 * <p>
 * The syntax is adopted from <a href="https://docs.splunk.com/Documentation/Splunk/latest/Search/Specifytimemodifiersinyoursearch">Splunk Docs</a>.
 * <p>
 * Where a time adjuster format would look like the following:
 *
 * <pre>
 *   -1min
 *   10days
 *   1y
 * </pre>
 * <p>
 * This class is not thread-safe.
 */
public class RelativeDateTimeAdjusterParser extends BaseTemporalExpressionParser<Instant, AdjusterExp, DateTimeAdjusterGrammar>
  {
  /**
   * Creates a new RelativeDateTimeAdjusterParser instance.
   */
  public RelativeDateTimeAdjusterParser()
    {
    }

  /**
   * Creates a new RelativeDateTimeAdjusterParser instance.
   *
   * @param context of type Context
   */
  public RelativeDateTimeAdjusterParser( Context context )
    {
    super( context );
    }

  @Override
  protected Class<DateTimeAdjusterGrammar> getParserClass()
    {
    return DateTimeAdjusterGrammar.class;
    }

  @Override
  protected Rule getGrammar( DateTimeAdjusterGrammar parser )
    {
    return parser.Root();
    }

  @Override
  protected BiFunction<Context, AdjusterExp, Instant> getFunction()
    {
    return ( context, expression ) -> expression.toInstant( context );
    }
  }
