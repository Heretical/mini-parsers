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
import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public abstract class BaseDateTimeParser<G extends BaseParser<DateTime>>
  {
  private static final Logger LOG = LoggerFactory.getLogger( BaseDateTimeParser.class );
  protected Context context = new Context();
  private Rule grammar;

  protected abstract Class<G> getParserClass();

  protected G createParser()
    {
    return Parboiled.createParser( getParserClass() );
    }

  protected abstract Rule getGrammar( G parser );

  protected ParseRunner<DateTime> getParserRunner()
    {
    if( grammar == null )
      grammar = getGrammar( createParser() );

    return createParserRunner( grammar ); // create new runner so we don't accumulate any stats etc
    }

  protected ReportingParseRunner<DateTime> createParserRunner( Rule grammar )
    {
    return new ReportingParseRunner<>( grammar );
    }

  public Result<DateTime> parse( String string )
    {
    long start = System.currentTimeMillis();

    ParsingResult<DateTime> result = getParserRunner().run( string );

    long duration = System.currentTimeMillis() - start;

    return new Result<>( result, duration );
    }

  public Result<DateTime> parseOrFail( String string ) throws ParserSyntaxException
    {
    Result<DateTime> parseResult = parse( string );

    if( parseResult.hasErrors() )
      {
      LOG.warn( parseResult.prettyPrintErrors() );

      throw new ParserSyntaxException( parseResult );
      }

    return parseResult;
    }
  }
