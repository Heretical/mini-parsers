/*
 * Copyright (c) 2018-2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.grammar;

import heretical.parser.common.ParserTestCase;
import heretical.parser.temporal.Context;
import heretical.parser.temporal.expression.DateTimeExp;
import org.parboiled.Rule;
import org.parboiled.support.ParsingResult;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class DateTimeParserSyntaxTestCase extends ParserTestCase<DateTimeExp, DateTimeGrammar>
  {
  public DateTimeParserSyntaxTestCase( boolean useTracingRunning )
    {
    super( useTracingRunning );
    }

  protected void assertParse( Long expectedMillis, String dateTime )
    {
    ParsingResult<DateTimeExp> parsingResult = assertParse( dateTime );

    assertEquals( expectedMillis, Long.valueOf( parsingResult.resultValue.toInstant( context() ).toEpochMilli() ) );
    }

  protected Context context()
    {
    return new Context();
    }

  @Override
  protected Class<DateTimeGrammar> getParserGrammarClass()
    {
    return DateTimeGrammar.class;
    }

  @Override
  protected Rule getGrammarRoot( DateTimeGrammar parser )
    {
    return parser.Root();
    }
  }
