/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.expression;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

import heretical.parser.temporal.Context;
import heretical.parser.temporal.units.TimeUnit;
import heretical.parser.temporal.util.FixedClockRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RelativeTimeAdjusterExpTest
  {
  @Rule
  public FixedClockRule now = new FixedClockRule();

  public Context context = new Context( now.clock() );

  protected AdjusterExp relativeTime()
    {
    return new AdjusterExp();
    }

  @Test
  public void amountMinutes()
    {
    Instant time = now.clock().instant().minus( 1, ChronoUnit.MINUTES );
    AdjusterExp adjusterExp = relativeTime();

    adjusterExp.setAmountUnit( TimeUnit.minute );

    assertEquals( time, adjusterExp.toInstant( context ) );

    adjusterExp = relativeTime();
    adjusterExp.setAmount( 1 );
    adjusterExp.setAmountUnit( TimeUnit.minute );

    assertEquals( time, adjusterExp.toInstant( context ) );

    adjusterExp = relativeTime();
    adjusterExp.setAmountOp( BinaryOp.MINUS );
    adjusterExp.setAmount( 1 );
    adjusterExp.setAmountUnit( TimeUnit.minute );

    assertEquals( time, adjusterExp.toInstant( context ) );
    }

  @Test
  public void amountDays()
    {
    Instant time = now.clock().instant().minus( 10, ChronoUnit.DAYS );
    AdjusterExp adjusterExp = relativeTime();

    adjusterExp.setAmountOp( BinaryOp.MINUS );
    adjusterExp.setAmount( 10 );
    adjusterExp.setAmountUnit( TimeUnit.day );

    assertEquals( time, adjusterExp.toInstant( context ) );
    }

  @Test
  public void snapMinute()
    {
    AdjusterExp adjusterExp = relativeTime();

    adjusterExp.setSnapUnit( TimeUnit.minute );

    assertEquals( now.with( t -> t.with( ChronoField.SECOND_OF_MINUTE, 0 ) ), adjusterExp.toInstant( context ) );
    }

  @Test
  public void snapHour()
    {
    AdjusterExp adjusterExp;

    adjusterExp = relativeTime();
    adjusterExp.setSnapUnit( TimeUnit.hour );

    Instant hour = now.with(
      m -> m.with( ChronoField.MINUTE_OF_HOUR, 0 )
        .with( ChronoField.SECOND_OF_MINUTE, 0 )
    );
    assertEquals( hour, adjusterExp.toInstant( context ) );
    }

  @Test
  public void snapDay()
    {
    AdjusterExp adjusterExp = relativeTime();

    adjusterExp.setSnapUnit( TimeUnit.day );

    Instant day = now.with(
      d -> d.with( ChronoField.HOUR_OF_DAY, 0 )
        .with( ChronoField.MINUTE_OF_HOUR, 0 )
        .with( ChronoField.SECOND_OF_MINUTE, 0 )
    );
    assertEquals( day, adjusterExp.toInstant( context ) );
    }

  @Test
  public void snapMonth()
    {
    AdjusterExp adjusterExp = relativeTime();

    adjusterExp.setSnapUnit( TimeUnit.month );

    Instant month = now.with(
      mon -> mon.with( ChronoField.DAY_OF_MONTH, 1 )
        .with( ChronoField.HOUR_OF_DAY, 0 )
        .with( ChronoField.MINUTE_OF_HOUR, 0 )
        .with( ChronoField.SECOND_OF_MINUTE, 0 )
    );
    assertEquals( month, adjusterExp.toInstant( context ) );
    }

  @Test
  public void snapYear()
    {
    AdjusterExp adjusterExp = relativeTime();

    adjusterExp.setSnapUnit( TimeUnit.year );

    Instant year = now.with(
      y -> y.with( ChronoField.MONTH_OF_YEAR, 1 )
        .with( ChronoField.DAY_OF_MONTH, 1 )
        .with( ChronoField.HOUR_OF_DAY, 0 )
        .with( ChronoField.MINUTE_OF_HOUR, 0 )
        .with( ChronoField.SECOND_OF_MINUTE, 0 )
    );
    assertEquals( year, adjusterExp.toInstant( context ) );
    }

  @Test
  public void snapWeek()
    {
    AdjusterExp adjusterExp = relativeTime();

    adjusterExp.setSnapUnit( TimeUnit.week );

    TemporalField weekField = WeekFields.of( Locale.US ).dayOfWeek();

    Instant week = now.with( t -> t.with( weekField, 1 )
      .with( ChronoField.HOUR_OF_DAY, 0 )
      .with( ChronoField.MINUTE_OF_HOUR, 0 )
      .with( ChronoField.SECOND_OF_MINUTE, 0 )
    );
    assertEquals( week, adjusterExp.toInstant( context ) );
    }

  @Test
  public void snapMinuteOffset()
    {
    AdjusterExp adjusterExp = relativeTime();

    adjusterExp.setSnapUnit( TimeUnit.minute );

    assertEquals( now.with( t -> t.with( ChronoField.SECOND_OF_MINUTE, 0 ) ), adjusterExp.toInstant( context ) );
    }
  }
