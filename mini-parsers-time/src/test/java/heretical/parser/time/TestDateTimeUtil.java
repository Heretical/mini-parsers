/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.time;

import java.util.Calendar;
import java.util.TimeZone;

/**
 *
 */
public class TestDateTimeUtil
  {
  public static long getAbsoluteDate( int year, int month, int day )
    {
    return getAbsoluteDate( year, month, day, 0, 0, 0 );
    }

  public static long getAbsoluteDate( int year, int month, int day, int hour, int min, int sec )
    {
    Calendar instant = Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) );

    int currentYear = instant.get( Calendar.YEAR );
    int currentMonth = instant.get( Calendar.MONTH );
    int currentDay = instant.get( Calendar.DAY_OF_MONTH );
    int currentHour = instant.get( Calendar.HOUR_OF_DAY );
    int currentMin = instant.get( Calendar.MINUTE );
    int currentSec = instant.get( Calendar.SECOND );

    instant.clear();

    if( year != -1 )
      currentYear = year;

    if( month != -1 )
      currentMonth = month;

    if( day != -1 )
      currentDay = day;

    if( hour != -1 )
      currentHour = hour;

    if( min != -1 )
      currentMin = min;

    if( sec != -1 )
      currentSec = sec;

    instant.set( Calendar.YEAR, currentYear );
    instant.set( Calendar.MONTH, currentMonth );
    instant.set( Calendar.DAY_OF_MONTH, currentDay );
    instant.set( Calendar.HOUR_OF_DAY, currentHour );
    instant.set( Calendar.MINUTE, currentMin );
    instant.set( Calendar.SECOND, currentSec );

    return instant.getTimeInMillis();
    }

  protected static long getRelativeDate( int yearOffset, int monthOffset, int dayOffset )
    {
    return getRelativeDate( System.currentTimeMillis(), yearOffset, monthOffset, dayOffset );
    }

  public static long getRelativeDate( long from, int yearOffset, int monthOffset, int dayOffset )
    {
    return getRelativeDate( from, yearOffset, monthOffset, dayOffset, 0, 0, 0 );
    }

  public static long getRelativeDate( long from, int yearOffset, int monthOffset, int dayOffset, int hourOffset )
    {
    return getRelativeDate( from, yearOffset, monthOffset, dayOffset, hourOffset, 0, 0 );
    }

  public static long getRelativeDate( long from, int yearOffset, int monthOffset, int dayOffset, int hourOffset, int minOffset, int secOffset )
    {
    Calendar instant = Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) );

    instant.setTimeInMillis( from );

    instant.add( Calendar.YEAR, yearOffset );
    instant.add( Calendar.MONTH, monthOffset );
    instant.add( Calendar.DAY_OF_MONTH, dayOffset );
    instant.add( Calendar.HOUR_OF_DAY, hourOffset );
    instant.add( Calendar.MINUTE, minOffset );
    instant.add( Calendar.SECOND, secOffset );

    int year = instant.get( Calendar.YEAR );
    int month = instant.get( Calendar.MONTH );
    int day = instant.get( Calendar.DAY_OF_MONTH );
    int hour = instant.get( Calendar.HOUR_OF_DAY );
    int min = instant.get( Calendar.MINUTE );
    int sec = instant.get( Calendar.SECOND );

    instant.set( year, month, day, hour, min, sec );

    return instant.getTimeInMillis();
    }

  public static long getRelativeWeek( long from, int weekOffset )
    {
    Calendar instant = Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) );

    instant.setTimeInMillis( from );

    int currentWeek = instant.get( Calendar.WEEK_OF_YEAR );

    if( ( currentWeek + weekOffset ) != 1 )
      instant.add( Calendar.WEEK_OF_YEAR, weekOffset );

    int year = instant.get( Calendar.YEAR );
    int week = instant.get( Calendar.WEEK_OF_YEAR );

    if( ( currentWeek + weekOffset ) == 1 )
      week = 1;

    instant.clear();

    instant.set( Calendar.YEAR, year );
    instant.set( Calendar.WEEK_OF_YEAR, week );

    //    System.out.println( DateTimeUtil.printCalendar( instant ) );

    return instant.getTimeInMillis();
    }

  public static long getRelativeMonthOfYear( long from, int monthOfYear )
    {
    Calendar instant = Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) );

    instant.setTimeInMillis( from );

    // go back to last years month if requesting a month coming later in the year or is the current month
    if( instant.get( Calendar.MONTH ) <= monthOfYear )
      instant.add( Calendar.YEAR, -1 );

    instant.set( Calendar.MONTH, monthOfYear );

    int year = instant.get( Calendar.YEAR );

    instant.clear();

    instant.set( Calendar.YEAR, year );
    instant.set( Calendar.MONTH, monthOfYear );
    instant.set( Calendar.DAY_OF_MONTH, 1 );

//    System.out.println( DateTimeUtil.printCalendar( instant ) );

    return instant.getTimeInMillis();
    }

  public static long getRelativeDayOfWeek( long from, int dayOfWeek )
    {
    Calendar instant = Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) );

    instant.setTimeInMillis( from );

    // go back to last weeks day if requesting a day coming later in the week or is the current day
    if( instant.get( Calendar.DAY_OF_WEEK ) <= dayOfWeek )
      instant.add( Calendar.WEEK_OF_YEAR, -1 );

    instant.set( Calendar.DAY_OF_WEEK, dayOfWeek );

    int year = instant.get( Calendar.YEAR );
    int month = instant.get( Calendar.MONTH );
    int day = instant.get( Calendar.DAY_OF_MONTH );

    instant.clear();

    instant.set( Calendar.YEAR, year );
    instant.set( Calendar.MONTH, month );
    instant.set( Calendar.DAY_OF_MONTH, day );

    //    System.out.println( DateTimeUtil.printCalendar( instant ) );

    return instant.getTimeInMillis();
    }
  }
