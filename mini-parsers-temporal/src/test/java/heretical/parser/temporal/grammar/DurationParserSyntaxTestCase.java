/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.grammar;

import java.time.Duration;

import heretical.parser.common.ParserTestCase;
import heretical.parser.temporal.Context;
import heretical.parser.temporal.expression.DurationExp;
import org.parboiled.Rule;
import org.parboiled.support.ParsingResult;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class DurationParserSyntaxTestCase extends ParserTestCase<DurationExp, DurationGrammar>
  {
  public DurationParserSyntaxTestCase( boolean useTracingRunning )
    {
    super( useTracingRunning );
    }

  protected void assertParse( Duration expectedDuration, String duration )
    {
    assertParse( expectedDuration.toMillis(), duration );
    }

  protected void assertParse( Long expectedMillis, String duration )
    {
    ParsingResult<DurationExp> parsingResult = assertParse( duration );

    assertEquals( expectedMillis, new Long( parsingResult.resultValue.toDuration( new Context() ).toMillis() ) );
    }

  @Override
  protected Class<DurationGrammar> getParserGrammarClass()
    {
    return DurationGrammar.class;
    }

  @Override
  protected Rule getGrammarRoot( DurationGrammar parser )
    {
    return parser.Root();
    }

  protected void assertISOAndNatural( Duration expected, String naturalDuration )
    {
    assertParse( expected, naturalDuration );
    assertParseISO( expected );
    }

  protected void assertParseISO( Duration expected )
    {
    assertParse( expected, expected.toString() );
    }
  }
