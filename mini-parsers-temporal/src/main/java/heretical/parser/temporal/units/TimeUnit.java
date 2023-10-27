/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.units;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public enum TimeUnit
  {
    second( ChronoUnit.SECONDS, "s", "sec", "secs", "second", "seconds" ),
    minute( ChronoUnit.MINUTES, "m", "min", "minute", "minutes" ),
    hour( ChronoUnit.HOURS, "h", "hr", "hrs", "hour", "hours" ),
    day( ChronoUnit.DAYS, "d", "day", "days" ),
    week( ChronoUnit.WEEKS, "w", "week", "weeks" ),
    month( ChronoUnit.MONTHS, "mon", "month", "months" ),
    //        quarter(ChronoUnit. "q", "qtr", "qtrs", "quarter", "quarters"),
    year( ChronoUnit.YEARS, "y", "yr", "yrs", "year", "years" );

  ChronoUnit unit;
  String[] abbreviations;

  TimeUnit( ChronoUnit unit, String... abbreviations )
    {
    this.unit = unit;
    this.abbreviations = abbreviations;

    // long to short to guide the parser search tree
    Arrays.sort( this.abbreviations, Comparator.comparing( String::length ).reversed() );
    }

  public ChronoUnit unit()
    {
    return unit;
    }

  public String[] abbreviations()
    {
    return abbreviations;
    }

  public static class Token implements Comparable
    {
    String abbreviation;
    TimeUnit unit;

    public Token( String abbreviation, TimeUnit unit )
      {
      this.abbreviation = abbreviation;
      this.unit = unit;
      }

    public String abbreviation()
      {
      return abbreviation;
      }

    public TimeUnit unit()
      {
      return unit;
      }

    public int length()
      {
      return abbreviation.length();
      }

    @Override
    public int compareTo( Object o )
      {
      return abbreviation.compareTo( o.toString() );
      }
    }

  /**
   * In order for the parser to effectively walk down all possible parse paths, we must give the parser all the
   * abbreviations across all units from longest to shortest.
   *
   * @return a Set of all abbreviation to unit pairs
   */
  public static Set<Token> tokens()
    {
    // TreeSet also uses the comparator for identity, so we must provide a compare function when strings
    // are of the same length
    Set<Token> results = new TreeSet<>( Comparator.comparing( Token::length ).reversed().thenComparing( Token::compareTo ) );

    for( TimeUnit unit : values() )
      {
      for( String abbreviation : unit.abbreviations )
        {
        results.add( new Token( abbreviation, unit ) );
        }
      }

    return results;
    }
  }
