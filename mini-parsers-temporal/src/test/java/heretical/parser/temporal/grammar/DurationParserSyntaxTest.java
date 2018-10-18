/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.grammar;

import java.time.Period;
import java.time.temporal.ChronoUnit;

import org.junit.Ignore;
import org.junit.Test;

import static java.time.Duration.ZERO;

/**
 *
 */
public class DurationParserSyntaxTest extends DurationParserSyntaxTestCase
  {
  public DurationParserSyntaxTest()
    {
    super( false );
    }

  @Ignore
  public void testOne()
    {
    assertISOAndNatural( ZERO.plus( 10_000, ChronoUnit.SECONDS ), "10,000 seconds" );
    }

  @Ignore
  public void fractional()
    {
    assertISOAndNatural( ZERO.plus( 1, ChronoUnit.HOURS ).plus( 30, ChronoUnit.MINUTES ), "1.5 hour" );
    }

  @Test
  public void isoAndNatural()
    {
    assertISOAndNatural( ZERO.plus( 90, ChronoUnit.SECONDS ), "90 seconds" );
    assertISOAndNatural( ZERO.plus( 10_000, ChronoUnit.SECONDS ), "10,000 seconds" );

    assertISOAndNatural( ZERO.plus( 1, ChronoUnit.MINUTES ), "1 min" );
    assertISOAndNatural( ZERO.plus( 10, ChronoUnit.MINUTES ), "10 mInutes" );
    assertISOAndNatural( ZERO.plus( 100, ChronoUnit.MINUTES ), "100m" );

    assertISOAndNatural( ZERO.plus( 1, ChronoUnit.HOURS ), "1 hour" );
    assertISOAndNatural( ZERO.plus( 2, ChronoUnit.HOURS ), "2 hours" );
    assertISOAndNatural( ZERO.plus( 3, ChronoUnit.HOURS ), "3h" );

    assertISOAndNatural( ZERO.plus( 3, ChronoUnit.DAYS ), "3D" );

    assertISOAndNatural( ZERO.plusDays( Period.ofWeeks( 3 ).getDays() ), "3wks" );

    assertISOAndNatural( ZERO.plusDays( Period.ofMonths( 3 ).getDays() ), "3 months" );

    assertISOAndNatural( ZERO.plusDays( Period.ofYears( 3 ).getDays() ), "3 years" );
    }
  }
