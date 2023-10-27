/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.grammar;

import java.time.Instant;

import heretical.parser.common.ParserTestCase;
import heretical.parser.temporal.Context;
import heretical.parser.temporal.expression.AdjusterExp;
import heretical.parser.temporal.util.FixedClockRule;
import org.junit.Assert;
import org.junit.Rule;
import org.parboiled.support.ParsingResult;

public class RelativeDateTimeAdjusterParserSyntaxTestCase extends ParserTestCase<AdjusterExp, DateTimeAdjusterGrammar>
  {
  @Rule
  public FixedClockRule now = new FixedClockRule();

  public RelativeDateTimeAdjusterParserSyntaxTestCase( boolean useTracingRunning, String now )
    {
    super( useTracingRunning );
    this.now.setNow( now );
    }

  @Override
  protected Class<DateTimeAdjusterGrammar> getParserGrammarClass()
    {
    return DateTimeAdjusterGrammar.class;
    }

  @Override
  protected org.parboiled.Rule getGrammarRoot( DateTimeAdjusterGrammar dateTimeAdjusterGrammar )
    {
    return dateTimeAdjusterGrammar.Root();
    }

  private Context getContext()
    {
    return new Context( now.clock() );
    }

  protected void assertEquals( Instant expected, String string )
    {
    ParsingResult<AdjusterExp> parsingResult = assertParse( string );

    Assert.assertTrue( "did not match: " + string, parsingResult.matched );

    Assert.assertEquals( expected, parsingResult.resultValue.toInstant( getContext() ) );
    }

  protected void assertNotEquals( Instant expected, String query )
    {
    ParsingResult<AdjusterExp> parsingResult = assertParse( query );

    Assert.assertNotEquals( expected, parsingResult.resultValue.toInstant( getContext() ) );
    }
  }
