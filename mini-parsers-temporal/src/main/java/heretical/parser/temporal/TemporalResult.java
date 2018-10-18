/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal;

import java.util.function.BiFunction;

import heretical.parser.common.Result;
import heretical.parser.common.expression.Expression;
import org.parboiled.support.ParsingResult;

/**
 *
 */
public class TemporalResult<E extends Expression, R> extends Result<E>
  {
  private final Context context;
  private final BiFunction<Context, Expression, R> function;

  public TemporalResult( ParsingResult<E> result, long parseDuration, Context context, BiFunction<Context, Expression, R> function )
    {
    super( result, parseDuration );
    this.context = context;
    this.function = function;
    }

  public R getResult()
    {
    return function.apply( context, getExpression() );
    }
  }
