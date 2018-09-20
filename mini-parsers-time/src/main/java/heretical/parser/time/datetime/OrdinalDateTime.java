/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.time.datetime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.Objects;
import java.util.function.Supplier;

import heretical.parser.time.Context;
import heretical.parser.time.units.CalendarUnits;

/**
 *
 */
public class OrdinalDateTime extends DateTime
  {
  private CalendarUnits unit;
  private int ordinal;

  public OrdinalDateTime( CalendarUnits unit )
    {
    this.unit = unit;
    }

  public boolean setUnit( CalendarUnits unit )
    {
    this.unit = unit;

    return true;
    }

  public boolean setOrdinal( int ordinal )
    {
    this.ordinal = ordinal;

    return true;
    }

  public Instant toInstant( Context context )
    {
    LocalDateTime dateTime = LocalDateTime.now( context.getClock() );

    Supplier<Integer> snapOrdinal = new Supplier<Integer>()
      {
      int snapOrdinal = ordinal;

      @Override
      public Integer get()
        {
        try
          {
          return snapOrdinal;
          }
        finally
          {
          snapOrdinal = 0;
          }
        }
      };

    CalendarUnits canonicalUnit = unit.getCanonicalUnit();

    switch( canonicalUnit )
      {
      case weeks:
        dateTime = dateTime.with( t -> t.with( context.getWeekField(), snapOrdinal.get() ) );
      }

    switch( canonicalUnit )
      {
      case years:
        dateTime = dateTime.with( t -> t.with( ChronoField.YEAR, snapOrdinal.get() ) );
      case months:
        dateTime = dateTime.with( t -> t.with( ChronoField.MONTH_OF_YEAR, snapOrdinal.get() ) );
      }

    switch( canonicalUnit )
      {
      case years:
      case months:
      case weeks:
      case days:
        dateTime = dateTime.with( t -> t.with( ChronoField.DAY_OF_MONTH, snapOrdinal.get() ) );
      case hours:
        dateTime = dateTime.with( t -> t.with( ChronoField.HOUR_OF_DAY, snapOrdinal.get() ) );
      case minutes:
        dateTime = dateTime.with( t -> t.with( ChronoField.MINUTE_OF_HOUR, snapOrdinal.get() ) );
      case seconds:
        dateTime = dateTime.with( t -> t.with( ChronoField.SECOND_OF_MINUTE, snapOrdinal.get() ) );
      case milliseconds:
        dateTime = dateTime.with( t -> t.with( ChronoField.MILLI_OF_SECOND, snapOrdinal.get() ) );
      }

    return dateTime.toInstant( (ZoneOffset) context.getClock().getZone().normalized() );
    }

  @Override
  public boolean equals( Object object )
    {
    if( this == object )
      return true;
    if( object == null || getClass() != object.getClass() )
      return false;
    OrdinalDateTime that = (OrdinalDateTime) object;
    return ordinal == that.ordinal &&
      unit == that.unit;
    }

  @Override
  public int hashCode()
    {
    return Objects.hash( unit, ordinal );
    }

  @Override
  public String toString()
    {
    final StringBuilder sb = new StringBuilder( "OrdinalDateTimeMatch{" );
    sb.append( "unit=" ).append( unit );
    sb.append( ", ordinal=" ).append( ordinal );
    sb.append( '}' );
    return sb.toString();
    }
  }
