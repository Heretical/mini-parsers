/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.time;

import heretical.parser.common.ParserSyntaxException;
import heretical.parser.common.Result;
import heretical.parser.time.datetime.DateTime;
import heretical.parser.time.grammar.DateTimeGrammar;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.errors.ErrorUtils;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  This class is not thread-safe.
 */
public abstract class DateTimeParser
  {
  private static final Logger LOG = LoggerFactory.getLogger( DateTimeParser.class );

  protected Context context = new Context();
  protected ParseRunner<DateTime> runner;

  public DateTimeParser( Context context )
    {
    this.context = context;
    }

  public DateTimeParser()
    {
    }

  protected DateTimeGrammar createParser()
    {
    return Parboiled.createParser( DateTimeGrammar.class );
    }

  protected ParseRunner<DateTime> getParserRunner()
    {
    if( runner == null )
      runner = createParserRunner( getGrammar() );

    return runner;
    }

  protected ReportingParseRunner<DateTime> createParserRunner( Rule grammar )
    {
    return new ReportingParseRunner<>( grammar );
    }

  public Result<DateTime> parse( String timeString )
    {
    long start = System.currentTimeMillis();

    ParsingResult<DateTime> result = getParserRunner().run( timeString );

    long duration = System.currentTimeMillis() - start;

    Result<DateTime> parseResult = new Result<>( result, duration );

    if( result.hasErrors() )
      {
      LOG.warn( ErrorUtils.printParseErrors( result ) );

      throw new ParserSyntaxException( parseResult );
      }

    return parseResult;
    }

  protected abstract Rule getGrammar();
  }
