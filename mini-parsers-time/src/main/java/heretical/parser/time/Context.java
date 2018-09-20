/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.time;

import java.time.Clock;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 */
public class Context
  {
  public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone( "UTC" );
  public static final Locale DEFAULT_LOCALE = Locale.US;

  Clock clock = Clock.systemUTC();
  TimeZone timeZone = DEFAULT_TIME_ZONE;
  Locale locale = DEFAULT_LOCALE;
  TemporalField weekField = null;

  /**
   * Uses sane defaults, for testing and simple usage
   */
  public Context()
    {
    }

  public Context( TimeZone timeZone, Locale locale )
    {
    setTimeZone( timeZone );
    setLocale( locale );
    }

  public Context( Clock clock, TimeZone timeZone, Locale locale )
    {
    setClock( clock );
    setTimeZone( timeZone );
    setLocale( locale );
    }

  public Clock getClock()
    {
    return clock;
    }

  private void setClock( Clock clock )
    {
    if( clock != null )
      this.clock = clock;
    }

  private void setTimeZone( TimeZone timeZone )
    {
    if( timeZone != null )
      this.timeZone = timeZone;
    }

  public TimeZone getTimeZone()
    {
    return timeZone;
    }

  private void setLocale( Locale locale )
    {
    if( locale != null )
      {
      this.locale = locale;
      this.weekField = null;
      }
    }

  public Locale getLocale()
    {
    return locale;
    }

  public TemporalField getWeekField()
    {
    if( weekField == null )
      weekField = WeekFields.of( locale ).dayOfWeek();

    return weekField;
    }
  }
