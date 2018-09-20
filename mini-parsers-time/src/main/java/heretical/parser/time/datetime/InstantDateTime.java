/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.time.datetime;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;

import heretical.parser.time.Context;
import heretical.parser.time.DateTimeFormatParseException;
import heretical.parser.time.format.DateTimeFormats;

/**
 *
 */
public class InstantDateTime extends DateTime
  {
  private String value;
  private DateTimeFormats.DateTimeParser parser;
  private int offset;

  public boolean setValue( String value )
    {
    this.value = value;

    return true;
    }

  public boolean setParser( DateTimeFormats.DateTimeParser parser )
    {
    this.parser = parser;

    return true;
    }

  public boolean setOffset( int offset )
    {
    this.offset = offset;

    return true;
    }

  public Instant toInstant( Context context )
    {
    try
      {
      TemporalAccessor parsed;

      try
        {
        parsed = parser.getParser().parse( value );
        }
      catch( DateTimeParseException exception )
        {
        throw new DateTimeFormatParseException( "could not parse: " + value + ", using: " + parser.getPattern() + ", having: " + parser.getParser(), exception );
        }

      Instant instant;

      if( parsed.isSupported( ChronoField.INSTANT_SECONDS ) )
        {
        instant = Instant.ofEpochSecond( parsed.getLong( ChronoField.INSTANT_SECONDS ) );

        if( parsed.isSupported( ChronoField.MILLI_OF_SECOND ) )
          instant = instant.plusMillis( parsed.getLong( ChronoField.MILLI_OF_SECOND ) );
        }
      else
        {
        Temporal moment = LocalDate.now( ZoneOffset.UTC ).atTime( LocalTime.of( 0, 0 ) );

        if( parsed.isSupported( ChronoField.YEAR ) )
          moment = moment.with( ChronoField.YEAR, parsed.getLong( ChronoField.YEAR ) );

        if( parsed.isSupported( ChronoField.MONTH_OF_YEAR ) )
          moment = moment.with( ChronoField.MONTH_OF_YEAR, parsed.get( ChronoField.MONTH_OF_YEAR ) );

        if( parsed.isSupported( ChronoField.DAY_OF_MONTH ) )
          moment = moment.with( ChronoField.DAY_OF_MONTH, parsed.get( ChronoField.DAY_OF_MONTH ) );

        if( parsed.isSupported( ChronoField.HOUR_OF_DAY ) )
          moment = moment.with( ChronoField.HOUR_OF_DAY, parsed.get( ChronoField.HOUR_OF_DAY ) );

        if( parsed.isSupported( ChronoField.MINUTE_OF_HOUR ) )
          moment = moment.with( ChronoField.MINUTE_OF_HOUR, parsed.get( ChronoField.MINUTE_OF_HOUR ) );

        if( parsed.isSupported( ChronoField.SECOND_OF_MINUTE ) )
          moment = moment.with( ChronoField.SECOND_OF_MINUTE, parsed.get( ChronoField.SECOND_OF_MINUTE ) );

        if( parsed.isSupported( ChronoField.MILLI_OF_SECOND ) )
          moment = moment.with( ChronoField.MILLI_OF_SECOND, parsed.get( ChronoField.MILLI_OF_SECOND ) );

        instant = ( (LocalDateTime) moment ).toInstant( ZoneOffset.UTC );
        }

      if( offset != 0 )
        instant = instant.plus( offset, this.parser.getUnit().getChronoUnit() );

      return instant;
      }
    catch( IllegalArgumentException exception )
      {
      throw new RuntimeException( String.format( "unable to parse: %s, using pattern: %s", value, parser.getPattern() ), exception );
      }
    }
  }
