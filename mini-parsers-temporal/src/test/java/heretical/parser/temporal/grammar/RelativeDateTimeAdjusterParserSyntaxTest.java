/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.grammar;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import org.junit.Ignore;
import org.junit.Test;

import static java.time.temporal.ChronoField.*;

public class RelativeDateTimeAdjusterParserSyntaxTest extends RelativeDateTimeAdjusterParserSyntaxTestCase
  {
  public RelativeDateTimeAdjusterParserSyntaxTest()
    {
    super( false, "2011-12-03T10:15:30.300Z" );
    }

  @Ignore
  public void testOneOnly()
    {
    Instant time = now.localDateTime().minus( 1, ChronoUnit.MONTHS ).toInstant( ZoneOffset.UTC );
    assertEquals( time, "mon" );
    }

  @Test
  public void testAmount()
    {
    Instant time;

    time = now.minus( 1, ChronoUnit.MINUTES );
    assertEquals( time, "m" );
    assertEquals( time, "-m" );
    assertEquals( time, "-1m" );
    assertEquals( time, "-1min" );
    assertEquals( time, "-1minute" );
    assertEquals( time, "-1minutes" );
    assertNotMatched( "-1minutesw" );
    assertNotEquals( time, "+m" );

    time = now.minus( 1, ChronoUnit.HOURS );
    assertEquals( time, "1h" );
    assertEquals( time, "1hour" );
    assertEquals( time, "-1hours" );
    assertNotEquals( time, "+1h" );

    time = now.minus( 10, ChronoUnit.DAYS );
    assertEquals( time, "10d" );
    assertEquals( time, "10days" );
    assertEquals( time, "-10days" );
    assertNotEquals( time, "+10days" );

    time = now.minus( 10, ChronoUnit.WEEKS );
    assertEquals( time, "10w" );
    assertEquals( time, "-10week" );
    assertEquals( time, "-10weeks" );
    assertNotEquals( time, "+10weeks" );

    time = now.minus( 1, ChronoUnit.MONTHS );
    assertEquals( time, "mon" );
    assertEquals( time, "-1mon" );
    assertEquals( time, "-1month" );
    assertEquals( time, "-1months" );
    assertNotEquals( time, "+10mon" );

    time = now.minus( 1, ChronoUnit.YEARS );
    assertEquals( time, "y" );
    assertEquals( time, "year" );
    assertEquals( time, "years" );
    assertEquals( time, "-1y" );
    assertNotEquals( time, "+1year" );
    }

  @Test
  public void testSnap()
    {
    Instant time;

    time = now.with( t -> t.with( SECOND_OF_MINUTE, 0 ).with( MILLI_OF_SECOND, 0 ) );
    assertEquals( time, "@m" );

    time = now.with( t -> t.with( MINUTE_OF_HOUR, 0 ).with( SECOND_OF_MINUTE, 0 ).with( MILLI_OF_SECOND, 0 ) );
    assertEquals( time, "@h" );

    time = now.with( t -> t.with( HOUR_OF_DAY, 0 ).with( MINUTE_OF_HOUR, 0 ).with( SECOND_OF_MINUTE, 0 ).with( MILLI_OF_SECOND, 0 ) );
    assertEquals( time, "@d" );

    time = now.with( t -> t.with( now.getWeekField(), 1 ).with( HOUR_OF_DAY, 0 ).with( MINUTE_OF_HOUR, 0 ).with( SECOND_OF_MINUTE, 0 ).with( MILLI_OF_SECOND, 0 ) );
    assertEquals( time, "@w" );

    time = now.with( t -> t.with( DAY_OF_MONTH, 1 ).with( HOUR_OF_DAY, 0 ).with( MINUTE_OF_HOUR, 0 ).with( SECOND_OF_MINUTE, 0 ).with( MILLI_OF_SECOND, 0 ) );
    assertEquals( time, "@mon" );

    time = now.with( t -> t.with( MONTH_OF_YEAR, 1 ).with( DAY_OF_MONTH, 1 ).with( HOUR_OF_DAY, 0 ).with( MINUTE_OF_HOUR, 0 ).with( SECOND_OF_MINUTE, 0 ).with( MILLI_OF_SECOND, 0 ) );
    assertEquals( time, "@y" );
    }
  }
