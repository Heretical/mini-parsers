/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.units;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * | Unit | Abbreviation | Example
 * | Milliseconds | ms | 300ms
 * | Seconds | s, sec | 30s 30sec
 * | Minutes | m, min | 20m 20min
 * | Hours | h, hrs | 3h 3hrs
 * | Days | d, days | 5d 5 days
 * | Weeks | w, wks | 2w 2wks
 * | Months | mos | 3mos
 * | Years | y, yrs | 2y 2rs
 */
public enum DurationUnits
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

    y, yrs, year,
    years( ChronoUnit.YEARS, y, yrs, year );

  public static DurationUnits[] ordered()
    {
    Set<DurationUnits> ordered = new TreeSet<>( comparator() );

    ordered.addAll( Arrays.asList( values() ) );

    return ordered.toArray( new DurationUnits[ values().length ] );
    }

  private static Comparator<DurationUnits> comparator()
    {
    return Comparator.comparing( DurationUnits::name ).reversed();
    }

  private TemporalUnit unit;
  private DurationUnits parent;
  private DurationUnits[] abbreviations;

  DurationUnits()
    {
    }

  DurationUnits( TemporalUnit unit, DurationUnits... abbreviations )
    {
    this.unit = unit;
    this.abbreviations = abbreviations;

    for( DurationUnits abbreviation : abbreviations )
      abbreviation.parent = this;
    }

  DurationUnits( DurationUnits... abbreviations )
    {
    this.abbreviations = abbreviations;

    Arrays.sort( this.abbreviations, comparator() );

    for( DurationUnits abbreviation : abbreviations )
      abbreviation.parent = this;
    }

  public TemporalUnit getUnit()
    {
    if( parent != null )
      return parent.unit;

    return unit;
    }

  public boolean hasAbbreviations()
    {
    return abbreviations != null;
    }

  public DurationUnits[] getAbbreviations()
    {
    return abbreviations;
    }
  }
