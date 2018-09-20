/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.time.datetime;

import java.time.Instant;
import java.util.Objects;

import heretical.parser.time.Context;
import heretical.parser.time.units.CalendarUnits;
import heretical.parser.time.units.RelativeDateUnits;

/**
 *
 */
public class OffsetDateTime extends DateTime
  {
  private RelativeDateUnits relativeDateUnit;
  private CalendarUnits unit;
  private int offset;

  public OffsetDateTime( RelativeDateUnits dateUnit )
    {
    setRelativeDateUnit( dateUnit );
    }

  public OffsetDateTime()
    {
    }

  public boolean setRelativeDateUnit( RelativeDateUnits relativeDateUnit )
    {
    this.relativeDateUnit = relativeDateUnit;

    setUnit( this.relativeDateUnit.units );
    setOffset( this.relativeDateUnit.offset );

    return true;
    }

  public boolean setUnit( CalendarUnits unit )
    {
    this.unit = unit;

    return true;
    }

  public boolean setOffset( int offset )
    {
    this.offset = offset;

    return true;
    }

  public Instant toInstant( Context context )
    {
    throw new UnsupportedOperationException( "unimplemented" );
    }

  @Override
  public boolean equals( Object object )
    {
    if( this == object )
      return true;
    if( object == null || getClass() != object.getClass() )
      return false;
    OffsetDateTime that = (OffsetDateTime) object;
    return offset == that.offset &&
      relativeDateUnit == that.relativeDateUnit &&
      unit == that.unit;
    }

  @Override
  public int hashCode()
    {
    return Objects.hash( relativeDateUnit, unit, offset );
    }

  @Override
  public String toString()
    {
    final StringBuilder sb = new StringBuilder( "OffsetDateTimeMatch{" );
    sb.append( "relativeDateUnit=" ).append( relativeDateUnit );
    sb.append( ", unit=" ).append( unit );
    sb.append( ", offset=" ).append( offset );
    sb.append( '}' );
    return sb.toString();
    }

  }
