/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.expression;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.function.Supplier;

import heretical.parser.temporal.Context;
import heretical.parser.temporal.units.TimeUnit;

/**
 * Adjusts now.
 * <p>
 * http://docs.splunk.com/Documentation/SplunkCloud/7.0.3/SearchReference/SearchTimeModifiers
 * http://docs.splunk.com/Documentation/Splunk/7.1.3/Search/Specifytimemodifiersinyoursearch
 */
public class AdjusterExp extends DateTimeExp
  {
  final Locale locale = Locale.US; // controls which day is the first day of the week
  final TemporalField weekField = WeekFields.of( locale ).dayOfWeek();

  BinaryOp amountOp = BinaryOp.MINUS; // querying towards the past is assumed
  int amount = 1; // 1 is assumed
  TimeUnit amountUnit;

  TimeUnit snapUnit;
  int snapOrdinal = 0;

  BinaryOp offsetOp;
  int offset = 0;
  TimeUnit offsetUnit;

  public AdjusterExp()
    {
    }

  public BinaryOp getAmountOp()
    {
    return amountOp;
    }

  public boolean setAmountOp( String amountOp )
    {
    return setAmountOp( BinaryOp.lookup( amountOp ) );
    }

  public boolean setAmountOp( BinaryOp amountOp )
    {
    if( amountOp != null )
      {
      this.amountOp = amountOp;
      }

    return true;
    }

  public int getAmount()
    {
    return amount;
    }

  public boolean setAmount( String amount )
    {
    return setAmount( Integer.parseInt( amount ) );
    }

  public boolean setAmount( int amount )
    {
    this.amount = amount;

    return true;
    }

  public TimeUnit getAmountUnit()
    {
    return amountUnit;
    }

  public boolean setAmountUnit( TimeUnit amountUnit )
    {
    this.amountUnit = amountUnit;

    return true;
    }

  public TimeUnit getSnapUnit()
    {
    return snapUnit;
    }

  public boolean setSnapUnit( TimeUnit snapUnit )
    {
    this.snapUnit = snapUnit;

    return true;
    }

  public int getSnapOrdinal()
    {
    return snapOrdinal;
    }

  public boolean setSnapOrdinal( String ordinal )
    {
    return setSnapOrdinal( Integer.parseInt( ordinal ) );
    }

  public boolean setSnapOrdinal( int snapOrdinal )
    {
    this.snapOrdinal = snapOrdinal;

    return true;
    }

  public BinaryOp getOffsetOp()
    {
    return offsetOp;
    }

  public boolean setOffsetOp( String offsetOp )
    {
    return setOffsetOp( BinaryOp.lookup( offsetOp ) );
    }

  public boolean setOffsetOp( BinaryOp offsetOp )
    {
    if( offsetOp != null )
      this.offsetOp = offsetOp;

    return true;
    }

  public int getOffset()
    {
    return offset;
    }

  public boolean setOffset( String offset )
    {
    return setOffset( Integer.parseInt( offset ) );
    }

  public boolean setOffset( int offset )
    {
    this.offset = offset;

    return true;
    }

  public TimeUnit getOffsetUnit()
    {
    return offsetUnit;
    }

  public boolean setOffsetUnit( TimeUnit offsetUnit )
    {
    this.offsetUnit = offsetUnit;

    return true;
    }

  public Instant toInstant( Context context )
    {

    LocalDateTime dateTime = LocalDateTime.now( context.getClock() );

    if( amountUnit != null )
      dateTime = applyBinaryOp( dateTime, amountOp, amount, amountUnit );

    if( snapUnit != null )
      {
      Supplier<Integer> snapOrdinal = new Supplier<Integer>()
        {
        int snapOrdinal = AdjusterExp.this.snapOrdinal;

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

      switch( snapUnit )
        {
        case week:
          dateTime = dateTime.with( t -> t.with( weekField, 1 + snapOrdinal.get() ) );
        }

      switch( snapUnit )
        {
        case year:
          dateTime = dateTime.with( t -> t.with( ChronoField.MONTH_OF_YEAR, 1 + snapOrdinal.get() ) );
        case month:
          dateTime = dateTime.with( t -> t.with( ChronoField.DAY_OF_MONTH, 1 + snapOrdinal.get() ) );
        }

      switch( snapUnit )
        {
        case year:
        case month:
        case week:
        case day:
          dateTime = dateTime.with( t -> t.with( ChronoField.HOUR_OF_DAY, snapOrdinal.get() ) );
        case hour:
          dateTime = dateTime.with( t -> t.with( ChronoField.MINUTE_OF_HOUR, snapOrdinal.get() ) );
        case minute:
          dateTime = dateTime.with( t -> t.with( ChronoField.SECOND_OF_MINUTE, snapOrdinal.get() ) );
        case second:
          dateTime = dateTime.with( t -> t.with( ChronoField.MILLI_OF_SECOND, snapOrdinal.get() ) );
        }

      if( offsetOp != null )
        dateTime = applyBinaryOp( dateTime, offsetOp, offset, offsetUnit );
      }

    return dateTime.toInstant( ZoneOffset.UTC );
    }

  public LocalDateTime applyBinaryOp( LocalDateTime dateTime, BinaryOp op, int amount, TimeUnit unit )
    {
    switch( op )
      {
      case PLUS:
        dateTime = dateTime.plus( amount, unit.unit() );
        break;

      case MINUS:
        dateTime = dateTime.minus( amount, unit.unit() );
        break;

      default:
        throw new IllegalStateException( "unknown op type" );
      }

    return dateTime;
    }
  }
