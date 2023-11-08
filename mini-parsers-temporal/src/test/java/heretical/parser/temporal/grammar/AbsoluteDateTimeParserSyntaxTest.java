/*
 * Copyright (c) 2018-2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.grammar;

import org.junit.Ignore;
import org.junit.Test;

import static heretical.parser.temporal.grammar.TestDateTimeUtil.getAbsoluteDate;
import static java.lang.String.format;

/**
 *
 */
public class AbsoluteDateTimeParserSyntaxTest extends DateTimeParserSyntaxTestCase
  {
  public AbsoluteDateTimeParserSyntaxTest()
    {
    super( false );
    }

  @Ignore
  public void testOnlyOne() throws Exception
    {
    long nowHour = getAbsoluteDate( -1, -1, -1, -1, 0, 0 );
    long nowDay = getAbsoluteDate( -1, -1, -1 );
    long nowMonth = getAbsoluteDate( -1, -1, 1 );
    long nowYear = getAbsoluteDate( -1, 0, 1 );

    assertParse( getAbsoluteDate( 2018, 10, 2, 20, 0, 0 ), "November 2nd 2018, 20:00:00.000" );
    }

  @Test
  public void test()
    {
    // support for millis since epoch
    assertParse( getAbsoluteDate( 1970, 1, 1 ), format( "%d", getAbsoluteDate( 1970, 1, 1 ) ) );
    assertParse( getAbsoluteDate( 2000, 0, 1 ), format( "%d", getAbsoluteDate( 2000, 0, 1 ) ) );
    assertParse( getAbsoluteDate( 2015, 1, 10 ), format( "%d", getAbsoluteDate( 2015, 1, 10 ) ) );
    assertParse( getAbsoluteDate( 2015, 1, 11 ), format( "%d", getAbsoluteDate( 2015, 1, 11 ) ) );

    assertParse( getAbsoluteDate( 2015, 1, 10 ), "2015-02-10" );
    assertParse( getAbsoluteDate( 2015, 1, 10 ), "02/10/15" );

    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "02/10/15 02:04" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "Feb/10/15 02:04" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "feb/10/15 02:04" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "February/10/15 02:04" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "february/10/15 02:04" );

    // can't seem to handle am/pm lowercase
    //    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "02/10/15 02:04am" );
    //    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "02/10/15 02:04 am" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "02/10/15 02:04AM" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "02/10/15 02:04 AM" );

    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 0, 0 ), "February 10th 2015, 02" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 0, 0 ), "Feb 10th 2015, 02" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 0, 0 ), "february 10th 2015, 02" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "february 10th 2015, 02:04" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 3 ), "february 10th 2015, 02:04:03" );
    assertParse( getAbsoluteDate( 2018, 10, 9, 20, 0, 0 ), "November 9th 2018, 20:00:00" );
    assertParse( getAbsoluteDate( 2018, 10, 9, 20, 0, 0 ), "November 09th 2018, 20:00:00" );
    assertParse( getAbsoluteDate( 2018, 10, 9, 20, 0, 0 ), "November 9th 2018, 20:00:00.000" );
    assertParse( getAbsoluteDate( 2018, 10, 2, 20, 0, 0 ), "November 2nd 2018, 20:00:00.000" );

    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 0, 0 ), "02/10/15 02" );

    assertParse( getAbsoluteDate( 2015, 1, 10 ), "20150210" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 30 ), "20150210T020430Z" );

    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 30 ), "2015-02-10T02:04:30+00:00" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 30 ), "2015-02-10 02:04:30+00:00" );
    }
  }
