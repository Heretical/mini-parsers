/*
 * Copyright (c) 2018-2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.common;

import org.junit.After;
import org.junit.Before;
import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.errors.ErrorUtils;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.parserunners.ProfilingParseRunner;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.parserunners.TracingParseRunner;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public abstract class ParserTestCase<V, P extends BaseParser<V>>
  {
  private static final Logger LOG = LoggerFactory.getLogger( ParserTestCase.class );

  private boolean useTracingRunning = false;
  private boolean printProfile = false;
  private boolean printTree = false;
  private P parser;

  public ParserTestCase( boolean useTracingRunning )
    {
    this.useTracingRunning = useTracingRunning;
    }

  @Before
  public void setUp() throws Exception
    {
    parser = Parboiled.createParser( getParserGrammarClass() );
    }

  protected ParseRunner<V> getRunner()
    {
    if( printProfile )
      return new ProfilingParseRunner<>( getGrammarRoot( parser ) );
    else if( useTracingRunning )
      return new TracingParseRunner<>( getGrammarRoot( parser ) );
    else
      return new ReportingParseRunner<>( getGrammarRoot( parser ) );
    }

  protected abstract Rule getGrammarRoot( P parser );

  protected abstract Class<P> getParserGrammarClass();

  @After
  public void tearDown() throws Exception
    {
    ParseRunner<V> runner = getRunner();
    if( printProfile && runner instanceof ProfilingParseRunner )
      System.out.println( ( (ProfilingParseRunner) runner ).getReport().print() );
    }

  protected ParsingResult<V> assertParse( String query )
    {
    return assertParse( query, true );
    }

  protected ParsingResult<V> assertParse( String query, boolean matched )
    {
    ParsingResult<V> result = getRunner().run( query );

    if( printTree )
      {
      System.out.println( "== parse tree ==" );
      String parseTreePrintOut = ParseTreeUtils.printNodeTree( result );

      System.out.println( parseTreePrintOut );
      }

    if( !result.matched && result.hasErrors() && matched )
      LOG.info( ErrorUtils.printParseErrors( result ) );

    assertEquals( "did not match", matched, result.matched );

    return result;
    }

  protected void assertNotMatched( String query )
    {
    assertParse( query, false );
    }
  }
