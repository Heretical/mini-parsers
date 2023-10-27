/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.format;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import heretical.parser.temporal.units.CalendarUnit;
import org.parboiled.Parboiled;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.support.ParsingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

/**
 * <pre>
 * Symbol  Meaning                     Presentation      Examples
 * ------  -------                     ------------      -------
 * G       era                         text              AD; Anno Domini; A
 * u       year                        year              2004; 04
 * y       year-of-era                 year              2004; 04
 * D       day-of-year                 number            189
 * M/L     month-of-year               number/text       7; 07; Jul; July; J
 * d       day-of-month                number            10
 *
 * Q/q     quarter-of-year             number/text       3; 03; Q3; 3rd quarter
 * Y       week-based-year             year              1996; 96
 * w       week-of-week-based-year     number            27
 * W       week-of-month               number            4
 * E       day-of-week                 text              Tue; Tuesday; T
 * e/c     localized day-of-week       number/text       2; 02; Tue; Tuesday; T
 * F       week-of-month               number            3
 *
 * a       am-pm-of-day                text              PM
 * h       clock-hour-of-am-pm (1-12)  number            12
 * K       hour-of-am-pm (0-11)        number            0
 * k       clock-hour-of-am-pm (1-24)  number            0
 *
 * H       hour-of-day (0-23)          number            0
 * m       minute-of-hour              number            30
 * s       second-of-minute            number            55
 * S       fraction-of-second          fraction          978
 * A       milli-of-day                number            1234
 * n       nano-of-second              number            987654321
 * N       nano-of-day                 number            1234000000
 *
 * V       time-zone ID                zone-id           America/Los_Angeles; Z; -08:30
 * z       time-zone name              zone-name         Pacific Standard Time; PST
 * O       localized zone-offset       offset-O          GMT+8; GMT+08:00; UTC-08:00;
 * X       zone-offset 'Z' for zero    offset-X          Z; -08; -0830; -08:30; -083015; -08:30:15;
 * x       zone-offset                 offset-x          +0000; -08; -0830; -08:30; -083015; -08:30:15;
 * Z       zone-offset                 offset-Z          +0000; -0800; -08:00;
 *
 * p       pad next                    pad modifier      1
 *
 * '       escape for text             delimiter
 * ''      single quote                literal           '
 * [       optional section start
 * ]       optional section end
 * #       reserved for future use
 * {       reserved for future use
 * }       reserved for future use
 * </pre>
 */
public class DateTimeFormats
  {
  private static final Logger LOG = LoggerFactory.getLogger( DateTimeFormats.class );

  public enum Char
    {
      text,
      digit,
      time_digits,
      literal
    }

  public enum Symbol
    {
      year2( "y", Char.digit, 2 ),
      year4( "y", Char.digit, 4 ),
      month_digit( "M", Char.digit, 2 ),
      month_text3( "M", Char.text, 3 ),
      month_text4( "M", Char.text, 4, 4, -1 ), // May triggers MMM and MMMM
      day_of_month_simple( "d", Char.digit, 1, 1, 2 ),
      day_of_month( "d", Char.digit, 2 ),
      //      day_of_month_short( "d", Char.digit, 3, -2 ),
      day_of_month_postfix( "o", Char.literal, 1, "nd", "rd", "st", "th" ),
      hour_of_day( "H", Char.digit, 2 ),
      clock_hour_halfday( "h", Char.digit, 2, 1, 2 ),
      minute_of_hour( "m", Char.digit, 2 ),
      second_of_minute( "s", Char.digit, 2 ),
      fraction_of_second( "S", Char.digit, 3 ),
      millis_in_epoch( "S", Char.digit, 13, 9, 13 ), // make large so floats to the top
      time_zone_id( "X", Char.literal, 1, "Z" ),
      time_zone1( "Z", Char.time_digits, 1, 5 ), // +0000
      time_zone2( "Z", Char.time_digits, 2, 5 ),
      time_zone3( "Z", Char.time_digits, 3, 6 ), // +00:00
      time_zone4( "Z", Char.text, 4, -1 ),
      half_day( "a", Char.literal, 1, "am", "pm" );

    String symbol;
    Char charType;
    int cardinality;
    public int minLength;
    public int maxLength;
    public String[] literals;

    Symbol( String symbol, Char charType, int cardinality )
      {
      this.symbol = symbol;
      this.charType = charType;
      this.cardinality = cardinality;
      this.minLength = cardinality;
      this.maxLength = cardinality;
      }

    Symbol( String symbol, Char charType, int cardinality, int maxLength )
      {
      this.symbol = symbol;
      this.charType = charType;
      this.cardinality = cardinality;
      this.minLength = maxLength;
      this.maxLength = maxLength;
      }

    Symbol( String symbol, Char charType, int cardinality, int minLength, int maxLength )
      {
      this.symbol = symbol;
      this.charType = charType;
      this.cardinality = cardinality;
      this.minLength = minLength;
      this.maxLength = maxLength;
      }

    Symbol( String symbol, Char charType, int cardinality, String... literals )
      {
      this.symbol = symbol;
      this.charType = charType;
      this.cardinality = cardinality;
      this.literals = literals;
      }

    public String getSymbol()
      {
      return symbol;
      }

    public Char getCharType()
      {
      return charType;
      }

    public int getCardinality()
      {
      return cardinality;
      }

    public String getSymbolFormat()
      {
      return new String( new char[ cardinality ] ).replace( "\0", symbol );
      }

    }

  public static List<Object> parsePattern( String dateFormat )
    {
    // these guys are not thread safe
    DateTimePatternGrammar parser = Parboiled.createParser( DateTimePatternGrammar.class );
    ParseRunner<?> runner = new BasicParseRunner<>( parser.DateTimeRoot() );

    ParsingResult<?> result = runner.run( dateFormat );

    LinkedList<Object> list = new LinkedList<>();

    for( Object value : result.valueStack )
      list.add( value );

    Collections.reverse( list );

    return list;
    }

  public enum DateTimeParser
    {
      millisInEpoch( "SSSSSSSSSSSSS", CalendarUnit.milliseconds, new DateTimeFormatterBuilder()
        .appendValue( ChronoField.INSTANT_SECONDS, 6, 19, SignStyle.NEVER )
        .appendValue( ChronoField.MILLI_OF_SECOND, 3 )
        .toFormatter() ), // allows for cut and past millisecond support

      // ISO FORMATS
      basicDate( "yyyyMMdd", CalendarUnit.days, DateTimeFormatter.BASIC_ISO_DATE ),
      basicDateTimeNoMillis( "yyyyMMdd'T'HHmmssZ", CalendarUnit.seconds ),
      basicTimeNoMillis( "HHmmssZ", CalendarUnit.seconds ),

      date( "yyyy-MM-dd", CalendarUnit.days, ISO_LOCAL_DATE ),
      dateHour( "yyyy-MM-dd'T'HH", CalendarUnit.hours ),
      dateHourMinute( "yyyy-MM-dd'T'HH:mm", CalendarUnit.minutes ),
      dateHourMinuteSecond( "yyyy-MM-dd'T'HH:mm:ss", CalendarUnit.seconds, DateTimeFormatter.ISO_LOCAL_DATE_TIME ),
      dateTimeNoMillis( "yyyy-MM-dd'T'HH:mm:ssZZZ", CalendarUnit.seconds, DateTimeFormatter.ISO_OFFSET_DATE_TIME ),
      dateTime( "yyyy-MM-dd'T'HH:mm:ss.SSSZZZ", CalendarUnit.seconds, DateTimeFormatter.ISO_OFFSET_DATE_TIME ),
      hour( "HH", CalendarUnit.hours ),
      hourMinute( "HH:mm", CalendarUnit.minutes ),
      hourMinuteSecond( "HH:mm:ss", CalendarUnit.seconds, ISO_LOCAL_TIME ),
      timeNoMillis( "HH:mm:ssZZ", CalendarUnit.seconds ),
      year( "yyyy", CalendarUnit.years ),
      yearMonth( "yyyy-MM", CalendarUnit.months ),

      // RELAXED / COMMON FORMATS

      // additional time zone coverage
      basicDateTimeNoMillisZ( "yyyyMMdd'T'HHmmssX", CalendarUnit.seconds ),
      dateTimeNoMillisZ( "yyyy-MM-dd'T'HH:mm:ssX", CalendarUnit.seconds ),

      // like iso, but space, no T
      basicDateTimeNoMillisSpace( "yyyyMMdd HHmmssZ", CalendarUnit.seconds ),
      basicDateTimeNoMillisSpaceZZZ( "yyyyMMdd HHmmssZZZ", CalendarUnit.seconds ),
      yearMonthDayHour( "yyyy-MM-dd HH", CalendarUnit.hours ),
      yearMonthDayHourMin( "yyyy-MM-dd HH:mm", CalendarUnit.minutes ),
      yearMonthDayHourMinSec( "yyyy-MM-dd HH:mm:ss", CalendarUnit.seconds ),
      yearMonthDayHourMinSecZ( "yyyy-MM-dd HH:mm:ssZ", CalendarUnit.seconds, new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append( ISO_LOCAL_DATE )
        .appendLiteral( ' ' )
        .append( ISO_LOCAL_TIME )
        .appendOffsetId()
        .toFormatter() ),
      yearMonthDayHourMinSecZZZ( "yyyy-MM-dd HH:mm:ssZZZ", CalendarUnit.seconds, new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append( ISO_LOCAL_DATE )
        .appendLiteral( ' ' )
        .append( ISO_LOCAL_TIME )
        .appendOffsetId()
        .toFormatter() ),

      hourMinSecZ( "HH:mm:ssZ", CalendarUnit.seconds ), // timeNoMillis with Z

      // US style dates - month first
      // TODO: add target Locale value so that if the system is configured in US, these parse values become enabled
      // TODO: allowing the system to be more internationalized
      monthDayYear2( "MM/dd/yy", CalendarUnit.days ),
      monthDayYear2Hour( "MM/dd/yy HH", CalendarUnit.hours ),
      monthDayYear2HourMin( "MM/dd/yy HH:mm", CalendarUnit.minutes ),
      monthDayYear2HourMinSec( "MM/dd/yy HH:mm:ss", CalendarUnit.seconds ),
      monthDayYear2HourMinSecZ( "MM/dd/yy HH:mm:ssZ", CalendarUnit.seconds ),
      monthDayYear2HourMinSecZZ( "MM/dd/yy HH:mm:ssZZ", CalendarUnit.seconds ),

      monthDayYear4( "MM/dd/yyyy", CalendarUnit.days ),
      monthDayYear4Hour( "MM/dd/yyyy HH", CalendarUnit.hours ),
      monthDayYear4HourMin( "MM/dd/yyyy HH:mm", CalendarUnit.minutes ),
      monthDayYear4HourMinSec( "MM/dd/yyyy HH:mm:ss", CalendarUnit.seconds ),
      monthDayYear4HourMinSecZ( "MM/dd/yyyy HH:mm:ssZ", CalendarUnit.seconds ),
      monthDayYear4HourMinSecZZ( "MM/dd/yyyy HH:mm:ssZZ", CalendarUnit.seconds ),

      monthDayYear2HourHalf( "MM/dd/yy hha", CalendarUnit.hours ),
      monthDayYear2HourMinHalf( "MM/dd/yy hh:mma", CalendarUnit.minutes ),
      monthDayYear2HourMinSecHalf( "MM/dd/yy hh:mm:ssa", CalendarUnit.seconds ),

      monthDayYear2HourHalfSpace( "MM/dd/yy hh a", CalendarUnit.hours ),
      monthDayYear2HourMinHalfSpace( "MM/dd/yy hh:mm a", CalendarUnit.minutes ),
      monthDayYear2HourMinSecHalfSpace( "MM/dd/yy hh:mm:ss a", CalendarUnit.seconds ),

      month3DayYear2( "MMM/dd/yy", CalendarUnit.days, insensitive( "MMM/dd/yy" ) ),
      month3DayYear2Hour( "MMM/dd/yy HH", CalendarUnit.hours, insensitive( "MMM/dd/yy HH" ) ),
      month3DayYear2HourMin( "MMM/dd/yy HH:mm", CalendarUnit.minutes, insensitive( "MMM/dd/yy HH:mm" ) ),
      month3DayYear2HourMinSec( "MMM/dd/yy HH:mm:ss", CalendarUnit.seconds, insensitive( "MMM/dd/yy HH:mm:ss" ) ),
      month3DayYear2HourMinSecZ( "MMM/dd/yy HH:mm:ssZ", CalendarUnit.seconds, insensitive( "MMM/dd/yy HH:mm:ssZ" ) ),
      month3DayYear2HourMinSecZZ( "MMM/dd/yy HH:mm:ssZZ", CalendarUnit.seconds, insensitive( "MMM/dd/yy HH:mm:ssZZ" ) ),

      month3DayYear4( "MMM/dd/yyyy", CalendarUnit.days, insensitive( "MMM/dd/yyyy" ) ),
      month3DayYear4Hour( "MMM/dd/yyyy HH", CalendarUnit.hours, insensitive( "MMM/dd/yyyy HH" ) ),
      month3DayYear4HourMin( "MMM/dd/yyyy HH:mm", CalendarUnit.minutes, insensitive( "MMM/dd/yyyy HH:mm" ) ),
      month3DayYear4HourMinSec( "MMM/dd/yyyy HH:mm:ss", CalendarUnit.seconds, insensitive( "MMM/dd/yyyy HH:mm:ss" ) ),
      month3DayYear4HourMinSecZ( "MMM/dd/yyyy HH:mm:ssZ", CalendarUnit.seconds, insensitive( "MMM/dd/yyyy HH:mm:ssZ" ) ),
      month3DayYear4HourMinSecZZ( "MMM/dd/yyyy HH:mm:ssZZ", CalendarUnit.seconds, insensitive( "MMM/dd/yyyy HH:mm:ssZZ" ) ),

      month3DayYear2HourHalf( "MMM/dd/yy hha", CalendarUnit.hours, insensitive( "MMM/dd/yy hha" ) ),
      month3DayYear2HourMinHalf( "MMM/dd/yy hh:mma", CalendarUnit.minutes, insensitive( "MMM/dd/yy hh:mma" ) ),
      month3DayYear2HourMinSecHalf( "MMM/dd/yy hh:mm:ssa", CalendarUnit.seconds, insensitive( "MMM/dd/yy hh:mm:ssa" ) ),

      month3DayYear2HourHalfSpace( "MMM/dd/yy hh a", CalendarUnit.hours, insensitive( "MMM/dd/yy hh a" ) ),
      month3DayYear2HourMinHalfSpace( "MMM/dd/yy hh:mm a", CalendarUnit.minutes, insensitive( "MMM/dd/yy hh:mm a" ) ),
      month3DayYear2HourMinSecHalfSpace( "MMM/dd/yy hh:mm:ss a", CalendarUnit.seconds, insensitive( "MMM/dd/yy hh:mm:ss a" ) ),

      month4DayYear2( "MMMM/dd/yy", CalendarUnit.days, insensitive( "MMMM/dd/yy" ) ),
      month4DayYear2Hour( "MMMM/dd/yy HH", CalendarUnit.hours, insensitive( "MMMM/dd/yy HH" ) ),
      month4DayYear2HourMin( "MMMM/dd/yy HH:mm", CalendarUnit.minutes, insensitive( "MMMM/dd/yy HH:mm" ) ),
      month4DayYear2HourMinSec( "MMMM/dd/yy HH:mm:ss", CalendarUnit.seconds, insensitive( "MMMM/dd/yy HH:mm:ss" ) ),
      month4DayYear2HourMinSecZ( "MMMM/dd/yy HH:mm:ssZ", CalendarUnit.seconds, insensitive( "MMMM/dd/yy HH:mm:ssZ" ) ),
      month4DayYear2HourMinSecZZ( "MMMM/dd/yy HH:mm:ssZZ", CalendarUnit.seconds, insensitive( "MMMM/dd/yy HH:mm:ssZZ" ) ),

      month4DayYear4( "MMMM/dd/yyyy", CalendarUnit.days, insensitive( "MMMM/dd/yyyy" ) ),
      month4DayYear4Hour( "MMMM/dd/yyyy HH", CalendarUnit.hours, insensitive( "MMMM/dd/yyyy HH" ) ),
      month4DayYear4HourMin( "MMMM/dd/yyyy HH:mm", CalendarUnit.minutes, insensitive( "MMMM/dd/yyyy HH:mm" ) ),

      month4DayYear4HourMinSec( "MMMM/dd/yyyy HH:mm:ss", CalendarUnit.seconds, insensitive( "MMMM/dd/yyyy HH:mm:ss" ) ),
      month4DayYear4HourMinSecZ( "MMMM/dd/yyyy HH:mm:ssZ", CalendarUnit.seconds, insensitive( "MMMM/dd/yyyy HH:mm:ssZ" ) ),
      month4DayYear4HourMinSecZZ( "MMMM/dd/yyyy HH:mm:ssZZ", CalendarUnit.seconds, insensitive( "MMMM/dd/yyyy HH:mm:ssZZ" ) ),

      month4DayYear2HourHalf( "MMMM/dd/yy hha", CalendarUnit.hours, insensitive( "MMMM/dd/yy hha" ) ),
      month4DayYear2HourMinHalf( "MMMM/dd/yy hh:mma", CalendarUnit.minutes, insensitive( "MMMM/dd/yy hh:mma" ) ),
      month4DayYear2HourMinSecHalf( "MMMM/dd/yy hh:mm:ssa", CalendarUnit.seconds, insensitive( "MMMM/dd/yy hh:mm:ssa" ) ),

      month4DayYear2HourHalfSpace( "MMMM/dd/yy hh a", CalendarUnit.hours, insensitive( "MMMM/dd/yy hh a" ) ),
      month4DayYear2HourMinHalfSpace( "MMMM/dd/yy hh:mm a", CalendarUnit.minutes, insensitive( "MMMM/dd/yy hh:mm a" ) ),
      month4DayYear2HourMinSecHalfSpace( "MMMM/dd/yy hh:mm:ss a", CalendarUnit.seconds, insensitive( "MMMM/dd/yy hh:mm:ss a" ) ),

      // OTHER USEFUL FORMATS
      apacheLogDateTime( "dd/MMM/yyyy:HH:mm:ss X", CalendarUnit.seconds, insensitive( "dd/MMM/yyyy:HH:mm:ss X" ) ),

      longDateTimeHourMinSecMillis( "MMMM do yyyy, HH:mm:ss.SSS", CalendarUnit.seconds, insensitive( "MMMM d['nd']['rd']['st']['th'] yyyy, HH:mm:ss.SSS" ) ),
      longDateTimeHourMinSec( "MMMM do yyyy, HH:mm:ss", CalendarUnit.seconds, insensitive( "MMMM d['nd']['rd']['st']['th'] yyyy, HH:mm:ss" ) ),
      longDateTimeHourMin( "MMMM do yyyy, HH:mm", CalendarUnit.minutes, insensitive( "MMMM d['nd']['rd']['st']['th'] yyyy, HH:mm" ) ),
      longDateTimeHour( "MMMM do yyyy, HH", CalendarUnit.hours, insensitive( "MMMM d['nd']['rd']['st']['th'] yyyy, HH" ) ),

      shortDateTimeHourMinSecMillis( "MMM do yyyy, HH:mm:ss.SSS", CalendarUnit.seconds, insensitive( "MMM d['nd']['rd']['st']['th'] yyyy, HH:mm:ss.SSS" ) ),
      shortDateTimeHourMinSec( "MMM do yyyy, HH:mm:ss", CalendarUnit.seconds, insensitive( "MMM d['nd']['rd']['st']['th'] yyyy, HH:mm:ss" ) ),
      shortDateTimeHourMin( "MMM do yyyy, HH:mm", CalendarUnit.minutes, insensitive( "MMM d['nd']['rd']['st']['th'] yyyy, HH:mm" ) ),
      shortDateTimeHour( "MMM do yyyy, HH", CalendarUnit.hours, insensitive( "MMM d['nd']['rd']['st']['th'] yyyy, HH" ) );

    private static DateTimeFormatter insensitive( String format )
      {
      return new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendPattern( format )
        .toFormatter();
      }

    String pattern;
    CalendarUnit unit;
    DateTimeFormatter parser;

    DateTimeParser( String pattern, CalendarUnit unit )
      {
      this.pattern = pattern;
      this.unit = unit.getCanonicalUnit();
      try
        {
        this.parser = DateTimeFormatter.ofPattern( pattern ); // parse now to catch errors on initialization
        }
      catch( RuntimeException exception )
        {
        LOG.error( "failed on pattern: {}", pattern, exception );
        throw exception;
        }
      }

    DateTimeParser( String pattern, CalendarUnit unit, DateTimeFormatter parser )
      {
      this.pattern = pattern;
      this.unit = unit;
      this.parser = parser;
      }

    public String getPattern()
      {
      return pattern;
      }

    public CalendarUnit getUnit()
      {
      return unit;
      }

    public DateTimeFormatter getParser()
      {
      return parser;
      }
    }

  public static final Comparator<String> longToShort = ( lhs, rhs ) ->
    {
    lhs = lhs.replace( "/'", "" );
    rhs = rhs.replace( "/'", "" );

    int lhsLen = lhs.length();
    int rhsLen = rhs.length();
    int len = rhsLen - lhsLen;

    if( len != 0 )
      return len;

    return lhs.compareTo( rhs );
    };

  static Map<String, DateTimeParser> patternMap = new TreeMap<>( longToShort );

  static
    {
    // TODO: filter here for server side supported locales when supported
    for( DateTimeParser dateTimeParser : DateTimeParser.values() )
      patternMap.put( dateTimeParser.getPattern(), dateTimeParser );
    }

  public static Map<String, DateTimeParser> getPatternMap()
    {
    return patternMap;
    }
  }
