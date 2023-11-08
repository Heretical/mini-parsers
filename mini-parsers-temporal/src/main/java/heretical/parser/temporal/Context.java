/*
 * Copyright (c) 2018-2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
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
  /**
   * The default locale used by the parser. Defaults to the system default.
   * <p/>
   * {@code Locale.getDefault()
   */
  public static final Locale DEFAULT_LOCALE = Locale.getDefault();

  Clock clock = Clock.systemUTC();
  Locale locale = DEFAULT_LOCALE;

  /**
   * Uses sane defaults, for testing and simple usage
   */
  public Context()
    {
    }

  /**
   * Uses the given zoneId and the default locale.
   *
   * @param zoneId the zoneId to use
   * @param locale the locale to use
   */
  public Context( ZoneId zoneId, Locale locale )
    {
    setClock( Clock.system( zoneId ) );
    setLocale( locale );
    }

  /**
   * Uses the given clock and the default locale.
   * <p/>
   * Most commonly used for testing.
   *
   * @param clock  the clock to use
   * @param locale the locale to use
   */
  public Context( Clock clock, Locale locale )
    {
    setClock( clock );
    setLocale( locale );
    }

  /**
   * Uses the given clock and the default locale.
   * <p/>
   * Most commonly used for testing.
   *
   * @param clock the clock to use
   */
  public Context( Clock clock )
    {
    this.clock = clock;
    }

  /**
   * @return the clock used by the parser
   */
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

  /**
   * @return the locale used by the parser
   */
  public Locale getLocale()
    {
    return locale;
    }

  /**
   * @return the week field for the locale
   */
  public TemporalField getWeekField()
    {
    return WeekFields.of( getLocale() ).dayOfWeek();
    }
  }
