/*
 * Copyright (c) 2018-2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.units;

import java.text.DateFormatSymbols;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 */
public enum CalendarUnit
  {
    ms, millisecond,
    milliseconds( ChronoUnit.MILLIS, ms, millisecond ),

    s, sec, second,
    seconds( ChronoUnit.SECONDS, s, sec, second ),

    m, min, minute,
    minutes( ChronoUnit.MINUTES, m, min, minute ),

    h, hrs, hour,
    hours( ChronoUnit.HOURS, h, hrs, hour ),

    d, day,
    days( ChronoUnit.DAYS, d, day ),

    w, wks, week,
    weeks( ChronoUnit.WEEKS, w, wks, week ),

    mos, month,
    months( ChronoUnit.MONTHS, mos, month ),

//    qtr, quarter,
//    quarters( IsoFields.QUARTER_OF_YEAR, qtr, quarter ),

    y, yrs, year,
    years( ChronoUnit.YEARS, y, yrs, year );

  static
    {
    Locale[] locales = new Locale[]{Locale.US, Locale.UK};

    for( Locale locale : locales )
      {
      DateFormatSymbols dateFormatSymbols = new DateFormatSymbols( locale );

      addSymbols( days.symbols, dateFormatSymbols.getWeekdays() );
      addSymbols( days.symbols, dateFormatSymbols.getShortWeekdays() );

      addSymbols( months.symbols, dateFormatSymbols.getMonths() );
      addSymbols( months.symbols, dateFormatSymbols.getShortMonths() );
      }
    }

  private static void addSymbols( Map<String, Integer> map, String[] symbols )
    {
    for( int i = 0; i < symbols.length; i++ )
      {
      String key = symbols[ i ];

      if( key == null || key.isEmpty() )
        continue;

      map.put( key.toLowerCase(), i );
      }
    }

  private ChronoUnit chronoUnit;
  private CalendarUnit parent;
  private CalendarUnit[] abbreviations;

  Map<String, Integer> symbols = new HashMap<>();

  CalendarUnit()
    {
    }

  CalendarUnit( ChronoUnit chronoUnit, CalendarUnit... abbreviations )
    {
    this.chronoUnit = chronoUnit;
    this.abbreviations = abbreviations;

    for( CalendarUnit abbreviation : abbreviations )
      abbreviation.parent = this;
    }

  public CalendarUnit getCanonicalUnit()
    {
    if( parent != null )
      return parent.getCanonicalUnit();

    return this;
    }

  public ChronoUnit getChronoUnit()
    {
    return getCanonicalUnit().chronoUnit;
    }

  public boolean hasAbbreviations()
    {
    return abbreviations != null;
    }

  public CalendarUnit[] getAbbreviations()
    {
    return abbreviations;
    }

  public String[] getSymbols()
    {
    return getCanonicalUnit().symbols.keySet().toArray( new String[ symbols.size() ] );
    }

  public Integer getOrdinal( String symbol )
    {
    Integer ordinal = getCanonicalUnit().symbols.get( symbol.toLowerCase() );

    switch( getCanonicalUnit().chronoUnit )
      {
      case NANOS:
      case MICROS:
      case MILLIS:
      case SECONDS:
      case MINUTES:
      case HOURS:
      case HALF_DAYS:
      case YEARS:
      case DECADES:
      case CENTURIES:
      case MILLENNIA:
      case ERAS:
      case FOREVER:
        break;

      case DAYS:
      case WEEKS:
      case MONTHS:
        ordinal++;
        break;
      }

    return ordinal;
    }
  }
