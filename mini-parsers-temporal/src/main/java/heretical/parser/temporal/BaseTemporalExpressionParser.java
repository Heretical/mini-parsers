/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal;

import java.util.function.BiFunction;

import heretical.parser.common.ParserSyntaxException;
import heretical.parser.common.expression.Expression;
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
public abstract class BaseTemporalExpressionParser<R, E extends Expression, G extends BaseParser<E>>
  {
  private static final Logger LOG = LoggerFactory.getLogger( BaseTemporalExpressionParser.class );

  private final Context context;
  private Rule grammar;

  public BaseTemporalExpressionParser()
    {
    this( new Context() );
    }

  public BaseTemporalExpressionParser( Context context )
    {
    this.context = context;
    }

  public Context getContext()
    {
    return context;
    }

  protected abstract Class<G> getParserClass();

  protected G createParser()
    {
    return Parboiled.createParser( getParserClass() );
    }

  protected abstract Rule getGrammar( G parser );

  protected ParseRunner<E> getParserRunner()
    {
    if( grammar == null )
      grammar = getGrammar( createParser() );

    return createParserRunner( grammar ); // create new runner so we don't accumulate any stats etc
    }

  protected abstract BiFunction<Context, E, R> getFunction();

  protected ReportingParseRunner<E> createParserRunner( Rule grammar )
    {
    return new ReportingParseRunner<>( grammar );
    }

  public TemporalResult<E, R> parse( String string )
    {
    long start = System.currentTimeMillis();

    ParsingResult<E> result = getParserRunner().run( string );

    long parseDuration = System.currentTimeMillis() - start;

    return new TemporalResult<>( result, parseDuration, context, (BiFunction<Context, Expression, R>) getFunction() );
    }

  public TemporalResult<E, R> parseOrFail( String string ) throws ParserSyntaxException
    {
    TemporalResult<E, R> parseResult = parse( string );

    if( parseResult.hasErrors() )
      {
      LOG.warn( parseResult.prettyPrintErrors() );

      throw new ParserSyntaxException( parseResult );
      }

    return parseResult;
    }
  }
