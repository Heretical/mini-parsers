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
public class RelativeDateTimeParserSyntaxTest extends DateTimeParserSyntaxTestCase
  {
  public RelativeDateTimeParserSyntaxTest()
    {
    super( false );
    }

  @Test
  public void test()
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
  }
