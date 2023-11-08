/*
 * Copyright (c) 2018-2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.units;

/**
 *
 */
public enum RelativeDateUnit
  {
    yesterday( CalendarUnit.days, -1 ),
    today( CalendarUnit.days, 0 ),
    now( CalendarUnit.seconds, 0 ),
    tomorrow( CalendarUnit.days, 1 );

  public final CalendarUnit units;
  public final int offset;

  RelativeDateUnit( CalendarUnit units, int offset )
    {
    this.units = units;
    this.offset = offset;
    }
  }
