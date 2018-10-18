/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal;

import java.time.Clock;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Objects;

/**
 *
 */
public class Context
  {
  public static final Locale DEFAULT_LOCALE = Locale.getDefault();

  Clock clock = Clock.systemUTC();
  Locale locale = DEFAULT_LOCALE;

  /**
   * Uses sane defaults, for testing and simple usage
   */
  public Context()
    {
    }

  public Context( ZoneId zoneId, Locale locale )
    {
    setClock( Clock.system( zoneId ) );
    setLocale( locale );
    }

  public Context( Clock clock, Locale locale )
    {
    setClock( clock );
    setLocale( locale );
    }

  public Context( Clock clock )
    {
    this.clock = clock;
    }

  public Clock getClock()
    {
    return clock;
    }

  private void setClock( Clock clock )
    {
    Objects.requireNonNull( clock, "clock may not be null" );

    this.clock = clock;
    }

  private void setLocale( Locale locale )
    {
    Objects.requireNonNull( locale, "locale may not be null" );

    this.locale = locale;
    }

  public Locale getLocale()
    {
    return locale;
    }

  public TemporalField getWeekField()
    {
    return WeekFields.of( getLocale() ).dayOfWeek();
    }
  }
