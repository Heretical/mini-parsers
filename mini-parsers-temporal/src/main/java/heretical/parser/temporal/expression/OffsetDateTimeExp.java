/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.expression;

import java.time.Instant;
import java.util.Objects;

import heretical.parser.temporal.Context;
import heretical.parser.temporal.units.CalendarUnit;
import heretical.parser.temporal.units.RelativeDateUnit;

/**
 *
 */
public class OffsetDateTimeExp extends DateTimeExp
  {
  private RelativeDateUnit relativeDateUnit;
  private CalendarUnit unit;
  private int offset;

  public OffsetDateTimeExp( RelativeDateUnit dateUnit )
    {
    setRelativeDateUnit( dateUnit );
    }

  public OffsetDateTimeExp()
    {
    }

  public boolean setRelativeDateUnit( RelativeDateUnit relativeDateUnit )
    {
    this.relativeDateUnit = relativeDateUnit;

    setUnit( this.relativeDateUnit.units );
    setOffset( this.relativeDateUnit.offset );

    return true;
    }

  public boolean setUnit( CalendarUnit unit )
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
    OffsetDateTimeExp that = (OffsetDateTimeExp) object;
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
