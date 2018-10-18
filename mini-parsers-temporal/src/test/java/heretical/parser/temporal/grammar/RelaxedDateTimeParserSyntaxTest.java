/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.grammar;

import org.junit.Ignore;
import org.junit.Test;

import static heretical.parser.temporal.grammar.TestDateTimeUtil.*;

/**
 *
 */
@Ignore
public class RelaxedDateTimeParserSyntaxTest extends DateTimeParserSyntaxTestCase
  {
  public RelaxedDateTimeParserSyntaxTest()
    {
    super( false );
    }

  /**
   */
  @Test
  public void test()
    {
    long nowHour = getAbsoluteDate( -1, -1, -1, -1, 0, 0 );
    long nowDay = getAbsoluteDate( -1, -1, -1 );
    long nowMonth = getAbsoluteDate( -1, -1, 1 );
    long nowYear = getAbsoluteDate( -1, 0, 1 );

    assertParse( nowDay, "today" );
    assertParse( getRelativeDate( nowDay, 0, 0, -1 ), "yesterday" );

    assertParse( getRelativeDate( nowHour, 0, 0, 0, -1, 0, 0 ), "last hour" );
    assertParse( getRelativeDate( nowDay, 0, 0, -1 ), "last day" );
    assertParse( getRelativeDate( nowDay, 0, 0, -1 ), "last 1 days" );
    assertParse( getRelativeWeek( nowDay, -1 ), "last week" );
    assertParse( getRelativeDate( nowMonth, 0, -1, 0 ), "last month" );
//    assertParse( getRelativeQuarter( nowMonth, -1 ), "last quarter" );
    assertParse( getRelativeDate( nowYear, -1, 0, 0 ), "last year" );

    assertParse( getRelativeDate( nowHour, 0, 0, 0, -4 ), "last 4 hours" );
    assertParse( getRelativeDate( nowDay, 0, 0, -4 ), "last 4 days" );
    assertParse( getRelativeWeek( nowDay, -4 ), "last 4 week" );
    assertParse( getRelativeDate( nowMonth, 0, -4, 0 ), "last 4 months" );
//    assertParse( getRelativeQuarter( nowMonth, -4 ), "last 4 quarters" );
    assertParse( getRelativeDate( nowYear, -4, 0, 0 ), "last 4 year" );

    assertParse( getRelativeDate( nowHour, 0, 0, 0, -4 ), "4 hours ago" );
    assertParse( getRelativeDate( nowDay, 0, 0, -4 ), "4 days ago" );
    assertParse( getRelativeWeek( nowDay, -4 ), "4 week ago" );
    assertParse( getRelativeDate( nowMonth, 0, -4, 0 ), "4 months ago" );
//    assertParse( getRelativeQuarter( nowMonth, -4 ), "4 quarters ago" );
    assertParse( getRelativeDate( nowYear, -4, 0, 0 ), "4 years ago" );

    assertParse( nowHour, "this hour" );
    assertParse( nowDay, "this day" );
    assertParse( getRelativeWeek( nowDay, 0 ), "this week" );
    assertParse( getAbsoluteDate( -1, -1, 1 ), "this month" );
//    assertParse( getRelativeQuarter( nowMonth, 0 ), "this quarter" );
    assertParse( getAbsoluteDate( -1, 0, 1 ), "this year" );

    assertParse( getRelativeWeek( nowDay, -3 ), "last 3 weeks" );
    assertParse( getRelativeDate( nowHour, 0, 0, 0, -2 ), "2 hours ago" );
    assertParse( getRelativeDate( nowDay, 0, 0, -2 ), "2 days ago" );
    assertParse( getRelativeDate( nowDay, 0, 0, -1 ), "last day" );
    }

  }
