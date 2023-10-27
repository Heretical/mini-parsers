/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.util;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.WeekFields;
import java.util.Locale;

import org.junit.rules.ExternalResource;

/**
 *
 */
public class FixedClockRule extends ExternalResource
  {
  public static final String NOW = "2022-12-03T10:15:30Z";

  protected Locale locale = Locale.US;
  protected TemporalField weekField;
  private String now = NOW;

  public FixedClockRule()
    {
    }

  public FixedClockRule( String now )
    {
    this.now = now;
    }

  public Locale getLocale()
    {
    return locale;
    }

  public void setLocale( Locale locale )
    {
    this.locale = locale;
    this.weekField = null;
    }

  public TemporalField getWeekField()
    {
    if( weekField == null )
      {
      weekField = WeekFields.of( locale ).dayOfWeek();
      }

    return weekField;
    }

  public void setNow( String now )
    {
    this.now = now;
    }

  public Instant getNow()
    {
    return toInstant( now );
    }

  public static Instant toInstant( String isoInstant )
    {
    return Instant.from( DateTimeFormatter.ISO_INSTANT.parse( isoInstant ) );
    }

  public Clock clock()
    {
    Instant instant = getNow();
    return Clock.fixed( instant, ZoneOffset.UTC );
    }

  public Instant instant()
    {
    return clock().instant();
    }

  public LocalDateTime localDateTime()
    {
    return LocalDateTime.ofInstant( instant(), ZoneOffset.UTC );
    }

  public Instant with( TemporalAdjuster adjuster )
    {
    return localDateTime().with( adjuster ).toInstant( ZoneOffset.UTC );
    }

  public Instant plus( long amountToAdd, TemporalUnit unit )
    {
    return localDateTime().plus( amountToAdd, unit ).toInstant( ZoneOffset.UTC );
    }

  public Instant minus( long amountToSubtract, TemporalUnit unit )
    {
    return localDateTime().minus( amountToSubtract, unit ).toInstant( ZoneOffset.UTC );
    }
  }
