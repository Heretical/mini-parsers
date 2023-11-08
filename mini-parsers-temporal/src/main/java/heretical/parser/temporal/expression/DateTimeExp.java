/*
 * Copyright (c) 2018-2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.expression;

import java.time.Instant;

import heretical.parser.common.expression.Expression;
import heretical.parser.temporal.Context;

/**
 *
 */
public abstract class DateTimeExp implements Expression
  {
  public abstract Instant toInstant( Context context );
  }
