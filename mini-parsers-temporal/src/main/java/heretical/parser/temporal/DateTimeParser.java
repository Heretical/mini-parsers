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

import heretical.parser.temporal.expression.DateTimeExp;
import heretical.parser.temporal.grammar.DateTimeGrammar;

/**
 * This class is not thread-safe.
 */
public abstract class DateTimeParser extends BaseTemporalExpressionParser<Instant, DateTimeExp, DateTimeGrammar>
  {
  public DateTimeParser()
    {
    }

  public DateTimeParser( Context context )
    {
    super( context );
    }

  @Override
  protected Class<DateTimeGrammar> getParserClass()
    {
    return DateTimeGrammar.class;
    }

  @Override
  protected BiFunction<Context, DateTimeExp, Instant> getFunction()
    {
    return ( context, expression ) -> expression.toInstant( context );
    }
  }
