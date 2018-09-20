/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.time.datetime;

import java.time.Instant;

import heretical.parser.time.Context;

/**
 *
 */
public abstract class DateTime
  {
  public abstract Instant toInstant( Context context );
  }
