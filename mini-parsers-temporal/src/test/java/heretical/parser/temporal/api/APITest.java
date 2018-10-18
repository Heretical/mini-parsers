/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.api;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;

import heretical.parser.common.ParserSyntaxException;
import heretical.parser.temporal.AbsoluteDateTimeParser;
import heretical.parser.temporal.Context;
import heretical.parser.temporal.DurationParser;
import heretical.parser.temporal.ISODurationParser;
import heretical.parser.temporal.NaturalDurationParser;
import heretical.parser.temporal.TemporalResult;
import heretical.parser.temporal.expression.DateTimeExp;
import heretical.parser.temporal.expression.DurationExp;
import org.junit.Test;

import static java.time.Duration.ZERO;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class APITest
  {
  private Context context = new Context();

  @Test
  public void absolute()
    {
    TemporalResult<DateTimeExp, Instant> result = new AbsoluteDateTimeParser( context ).parseOrFail( "2015-02-10T02:04:30+00:00" );

    TemporalAccessor parsed = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse( "2015-02-10T02:04:30+00:00" );
    Instant instant = Instant.ofEpochSecond( parsed.getLong( ChronoField.INSTANT_SECONDS ) )
      .plusMillis( parsed.getLong( ChronoField.MILLI_OF_SECOND ) );

    assertEquals( instant, result.getResult() );
    }

  @Test
  public void duration()
    {
    DurationParser durationParser = new DurationParser( context );
    Duration expected = ZERO.plus( 1, ChronoUnit.MINUTES );

    assertEquals( expected, durationParser.parseOrFail( "1 min" ).getResult() );
    assertEquals( expected, durationParser.parseOrFail( "PT1M" ).getResult() );
    }

  @Test
  public void durationISO()
    {
    TemporalResult<DurationExp, Duration> result = new ISODurationParser( context ).parseOrFail( "PT20.345S" );

    assertEquals( Duration.parse( "PT20.345S" ), result.getResult() );
    }

  @Test(expected =  ParserSyntaxException.class)
  public void durationISOFail()
    {
    TemporalResult<DurationExp, Duration> result = new ISODurationParser( context ).parseOrFail( "2 hours" );

    assertEquals( Duration.parse( "PT20.345S" ), result.getResult() );
    }

  @Test
  public void durationNatural()
    {
    TemporalResult<DurationExp, Duration> result = new NaturalDurationParser( context ).parseOrFail( "2 hours" );

    assertEquals( ZERO.plus( 2, ChronoUnit.HOURS ), result.getResult() );
    }

  @Test(expected = ParserSyntaxException.class)
  public void durationNaturalFail()
    {
    TemporalResult<DurationExp, Duration> result = new NaturalDurationParser( context ).parseOrFail( "PT20.345S" );

    assertEquals( ZERO.plus( 2, ChronoUnit.HOURS ), result.getResult() );
    }
  }
