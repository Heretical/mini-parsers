/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.grammar;

import org.junit.Test;

import static heretical.parser.temporal.util.FixedClockRule.toInstant;

/**
 * <a href="https://docs.splunk.com/Documentation/Splunk/latest/Search/Specifytimemodifiersinyoursearch#Examples_of_relative_time_modifiers">Splunk Modifiers</a>
 * <p/>
 * For these examples, the current time is Wednesday, 09 February 2022, 01:37:05 P.M.
 * Also note that 24h is usually but not always equivalent to 1d because of Daylight Savings Time boundaries.
 */
public class RelativeDateTimeAdjusterRelativeParseTest extends RelativeDateTimeAdjusterParserSyntaxTestCase
  {
  public RelativeDateTimeAdjusterRelativeParseTest()
    {
    super( false, "2022-02-09T13:37:05Z" );
    }

  @Test
  public void example()
    {
    // past
    // now	Now, the current time	Wednesday, 09 February 2022, 01:37:05 P.M.
    assertEquals( toInstant( "2022-02-09T13:37:05Z" ), "now" );
    // -60m	60 minutes ago	Wednesday, 09 February 2022, 12:37:05 P.M.	-60m@s
    assertEquals( toInstant( "2022-02-09T12:37:05Z" ), "-60m" );
    assertEquals( toInstant( "2022-02-09T12:37:05Z" ), "-60m@s" );
    assertEquals( toInstant( "2022-02-09T12:37:05Z" ), "60m@s" );
    // -1h@h	1 hour ago, to the hour	Wednesday, 09 February 2022, 12:00:00 P.M.
    assertEquals( toInstant( "2022-02-09T12:00:00Z" ), "-1h@h" );
    // -1d@d	Yesterday	Tuesday, 08 February 2022, 12:00:00 A.M.
    assertEquals( toInstant( "2022-02-08T00:00:00Z" ), "-1d@d" );
    assertEquals( toInstant( "2022-02-08T00:00:00Z" ), "1d@d" );
    // -24h	24 hours ago (yesterday)	Tuesday, 08 February 2022, 01:37:05 P.M.	-24h@s
    assertEquals( toInstant( "2022-02-08T13:37:05Z" ), "-24h" );
    assertEquals( toInstant( "2022-02-08T13:37:05Z" ), "-24h@s" );
    // -7d@d	7 days ago, 1 week ago today	Wednesday, 02 February 2022, 12:00:00 A.M.
    assertEquals( toInstant( "2022-02-02T00:00:00Z" ), "-7d@d" );
    // -7d@m	7 days ago, snap to minute boundary	Wednesday, 02 February 2022, 01:37:00 P.M.
    assertEquals( toInstant( "2022-02-02T13:37:00Z" ), "-7d@m" );

    // week
    // @w0	Beginning of the current week	Sunday, 06 February 2022, 12:00:00 A.M.
    assertEquals( toInstant( "2022-02-06T00:00:00Z" ), "@w0" );

    // future
    // +1d@d	Tomorrow	Thursday, 10 February 2022, 12:00:00 A.M.
    assertEquals( toInstant( "2022-02-10T00:00:00Z" ), "+1d@d" );
    // +24h	24 hours from now, tomorrow	Thursday, 10 February 2022, 01:37:05 P.M.	+24h@s
    assertEquals( toInstant( "2022-02-10T13:37:05Z" ), "+24h" );
    assertEquals( toInstant( "2022-02-10T13:37:05Z" ), "+24h@s" );
    }

  @Test
  public void exampleChained()
    {
    // past
    // @d-2h	Snap to the beginning of today (12 A.M.) and subtract 2 hours from that time.	10 P.M. last night.
    assertEquals( toInstant( "2022-02-08T22:00:00Z" ), "@d-2h" );
    // -mon@mon+7d	One month ago, snapped to the first of the month at midnight, and add 7 days.	The 8th of last month at 12 A.M.
    assertEquals( toInstant( "2022-01-08T00:00:00Z" ), "-mon@mon+7d" );
    }
  }
