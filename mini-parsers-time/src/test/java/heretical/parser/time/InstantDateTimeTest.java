/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.time;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import heretical.parser.time.datetime.InstantDateTime;
import org.junit.Test;

import static heretical.parser.time.format.DateTimeFormats.DateTimeParser.longDateTimeHourMinSec;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class InstantDateTimeTest
  {
  @Test
  public void test()
    {
    InstantDateTime pstDateTime = getInstantDateTime( "february 10th 2015, 02:04:03" ); // in pst
    Instant pst = pstDateTime.toInstant( new Context( Clock.system( ZoneId.of( "PST", ZoneId.SHORT_IDS ) ) ) );

    InstantDateTime utcDateTime = getInstantDateTime( "february 10th 2015, 10:04:03" ); // in utc
    Instant utc = utcDateTime.toInstant( new Context( Clock.systemUTC() ) );

    assertEquals( utc, pst );
    }

  private InstantDateTime getInstantDateTime( String datetime )
    {
    InstantDateTime instantDateTime = new InstantDateTime();

    instantDateTime.setParser( longDateTimeHourMinSec );
    instantDateTime.setValue( datetime );
    return instantDateTime;
    }
  }
