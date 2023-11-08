/*
 * Copyright (c) 2018-2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.expression;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import heretical.parser.temporal.Context;
import org.junit.Test;

import static heretical.parser.temporal.format.DateTimeFormats.DateTimeParser.longDateTimeHourMinSec;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class AbsoluteDateTimeExpTest
  {
  @Test
  public void test()
    {
    AbsoluteDateTimeExp pstDateTime = getInstantDateTime( "february 10th 2015, 02:04:03" ); // in pst
    Instant pst = pstDateTime.toInstant( new Context( Clock.system( ZoneId.of( "PST", ZoneId.SHORT_IDS ) ) ) );

    AbsoluteDateTimeExp utcDateTime = getInstantDateTime( "february 10th 2015, 10:04:03" ); // in utc
    Instant utc = utcDateTime.toInstant( new Context( Clock.systemUTC() ) );

    assertEquals( utc, pst );
    }

  private AbsoluteDateTimeExp getInstantDateTime( String datetime )
    {
    AbsoluteDateTimeExp instantDateTime = new AbsoluteDateTimeExp();

    instantDateTime.setParser( longDateTimeHourMinSec );
    instantDateTime.setValue( datetime );

    return instantDateTime;
    }
  }
