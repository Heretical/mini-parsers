/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.time;

import heretical.parser.common.ParserTestCase;
import heretical.parser.time.datetime.DateTime;
import heretical.parser.time.grammar.DateTimeGrammar;
import org.junit.Ignore;
import org.junit.Test;
import org.parboiled.Rule;
import org.parboiled.support.ParsingResult;

import static heretical.parser.time.TestDateTimeUtil.*;
import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class DateTimeParserSyntaxTest extends ParserTestCase<DateTimeGrammar>
  {
  public DateTimeParserSyntaxTest()
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

    assertParse( getRelativeDayOfWeek( nowDay, 1 ), "last sunday" );
    }

  @Test
  public void testAbsoluteDates()
    {
    // support for millis since epoch
    assertParse( getAbsoluteDate( 2015, 1, 10 ), format( "%d", getAbsoluteDate( 2015, 1, 10 ) ) );
    assertParse( getAbsoluteDate( 2015, 1, 11 ), format( "%d", getAbsoluteDate( 2015, 1, 11 ) ) );

    assertParse( getAbsoluteDate( 2015, 1, 10 ), "2015-02-10" );
    assertParse( getAbsoluteDate( 2015, 1, 10 ), "02/10/15" );

    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "02/10/15 02:04" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "02/10/15 02:04AM" );
//    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "02/10/15 02:04am" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "02/10/15 02:04 AM" );
//    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "02/10/15 02:04 am" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "Feb/10/15 02:04" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "feb/10/15 02:04" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "February/10/15 02:04" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 0 ), "february/10/15 02:04" );

    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 0, 0 ), "02/10/15 02" );

    assertParse( getAbsoluteDate( 2015, 1, 10 ), "20150210" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 30 ), "20150210T020430Z" );

    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 30 ), "2015-02-10T02:04:30+00:00" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 30 ), "2015-02-10T02:04:30+00:00" );
    assertParse( getAbsoluteDate( 2015, 1, 10, 2, 4, 30 ), "2015-02-10 02:04:30+00:00" );
    }

  @Ignore
  public void testRelaxedRelativeDates()
    {
    long nowDay = getAbsoluteDate( -1, -1, -1 );

    // these tests confirm if the current months is X, the time frame use will be
    assertParse( getRelativeMonthOfYear( nowDay, 0 ), "last January" );
    assertParse( getRelativeMonthOfYear( nowDay, 1 ), "last February" );
    assertParse( getRelativeMonthOfYear( nowDay, 2 ), "last March" );
    assertParse( getRelativeMonthOfYear( nowDay, 3 ), "last April" );
    assertParse( getRelativeMonthOfYear( nowDay, 4 ), "last May" );
    assertParse( getRelativeMonthOfYear( nowDay, 5 ), "last June" );
    assertParse( getRelativeMonthOfYear( nowDay, 6 ), "last July" );
    assertParse( getRelativeMonthOfYear( nowDay, 7 ), "last August" );
    assertParse( getRelativeMonthOfYear( nowDay, 8 ), "last September" );
    assertParse( getRelativeMonthOfYear( nowDay, 9 ), "last October" );
    assertParse( getRelativeMonthOfYear( nowDay, 10 ), "last November" );
    assertParse( getRelativeMonthOfYear( nowDay, 11 ), "last December" );

    assertParse( getRelativeDayOfWeek( nowDay, 1 ), "last sunday" );
    assertParse( getRelativeDayOfWeek( nowDay, 2 ), "last monday" );
    assertParse( getRelativeDayOfWeek( nowDay, 3 ), "last tuesday" );
    assertParse( getRelativeDayOfWeek( nowDay, 4 ), "last wednesday" );
    assertParse( getRelativeDayOfWeek( nowDay, 5 ), "last thursday" );
    assertParse( getRelativeDayOfWeek( nowDay, 6 ), "last friday" );
    assertParse( getRelativeDayOfWeek( nowDay, 7 ), "last saturday" );
    }

  /**
   */
  @Ignore
  public void testRelativeDates()
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

  protected void assertParse( Long expectedMillis, String dateTime )
    {
    ParsingResult<DateTime> parsingResult = assertParse( dateTime );

    assertEquals( expectedMillis, new Long( parsingResult.resultValue.toInstant( new Context(  ) ).toEpochMilli() ) );
    }

  @Override
  protected Class<DateTimeGrammar> getParserGrammarClass()
    {
    return DateTimeGrammar.class;
    }

  @Override
  protected Rule getGrammarRoot( DateTimeGrammar parser )
    {
    return parser.Root();
    }
  }
