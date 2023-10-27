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
 * <a href="https://docs.splunk.com/Documentation/Splunk/latest/Search/Specifytimemodifiersinyoursearch#Difference_between_relative_time_and_relative_snap_to_time">Splunk Docs</a>
 * <p>
 * On April 28th, you decide to run a search at 14:05.
 */
public class RelativeDateTimeAdjusterRelativeAndRelativeSnapParseTest extends RelativeDateTimeAdjusterParserSyntaxTestCase
  {
  public RelativeDateTimeAdjusterRelativeAndRelativeSnapParseTest()
    {
    super( false, "2022-04-28T14:05:00Z" );
    }

  @Test
  public void example()
    {
    // If you specify earliest=-2d, the search goes back exactly two days, starting at 14:05 on April 26th.
    assertEquals( toInstant( "2022-04-26T14:05:00Z" ), "-2d" );
    assertEquals( toInstant( "2022-04-26T14:05:00Z" ), "2d" );
    // If you specify earliest=-2d@d, the search goes back to two days and snaps to the beginning of the day.
    // The search looks for events starting from 00:00 on April 26th.
    assertEquals( toInstant( "2022-04-26T00:00:00Z" ), "-2d@d" );
    assertEquals( toInstant( "2022-04-26T00:00:00Z" ), "2d@d" );
    }
  }
