/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.time.units;

/**
 *
 */
public enum RelativeDateUnits
  {
    yesterday( CalendarUnits.days, -1 ),
    today( CalendarUnits.days, 0 ),
    now( CalendarUnits.seconds, 0 ),
    tomorrow( CalendarUnits.days, 1 );

  public final CalendarUnits units;
  public final int offset;

  RelativeDateUnits( CalendarUnits units, int offset )
    {
    this.units = units;
    this.offset = offset;
    }
  }
