/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.expression;

import java.time.Duration;

import heretical.parser.temporal.Context;

/**
 *
 */
public class ISO8601DurationExp extends DurationExp
  {
  String value;

  public ISO8601DurationExp()
    {
    }

  public boolean setValue( String value )
    {
    this.value = value;

    return true;
    }

  @Override
  public Duration toDuration( Context context )
    {
    return Duration.parse( value );
    }
  }
