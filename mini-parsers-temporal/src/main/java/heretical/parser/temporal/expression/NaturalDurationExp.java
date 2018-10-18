/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.expression;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;

import heretical.parser.temporal.Context;
import heretical.parser.temporal.units.DurationUnits;

/**
 *
 */
public class NaturalDurationExp extends DurationExp
  {
  double amount;
  DurationUnits unit;

  public NaturalDurationExp()
    {
    }

  public boolean setAmount( double amount )
    {
    this.amount = amount;

    return true;
    }

  public boolean setUnit( DurationUnits unit )
    {
    this.unit = unit;

    return true;
    }

  @Override
  public Duration toDuration( Context context )
    {
    int wholeAmount = (int) Math.floor( amount );

    Duration duration = getDuration( wholeAmount, unit.getUnit() );

    double remainder = amount - wholeAmount;

    if( remainder == 0 )
      return duration;

    // get fractional part from DurationUnits
    throw new UnsupportedOperationException( "fractional units are unsupported" );
    }

  private Duration getDuration( int wholeAmount, TemporalUnit unit )
    {
    if( unit instanceof ChronoUnit )
      {
      switch( (ChronoUnit) unit )
        {
        case NANOS:
        case MICROS:
        case MILLIS:
        case SECONDS:
        case MINUTES:
        case HOURS:
        case HALF_DAYS:
        case DAYS:
          return Duration.ZERO.plus( wholeAmount, unit );
        case WEEKS:
          return Duration.ZERO.plusDays( Period.ofWeeks( wholeAmount ).getDays() );
        case MONTHS:
          return Duration.ZERO.plusDays( Period.ofMonths( wholeAmount ).getDays() );
        case YEARS:
          return Duration.ZERO.plusDays( Period.ofYears( wholeAmount ).getDays() );
        case DECADES:
        case CENTURIES:
        case MILLENNIA:
        case ERAS:
        case FOREVER:
          break;
        }
      }

    throw new UnsupportedTemporalTypeException( "unsupported: " + this.unit );
    }
  }
