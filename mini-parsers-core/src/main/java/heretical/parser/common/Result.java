/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.common;

import java.util.ArrayList;
import java.util.List;

import heretical.parser.common.expression.Expression;
import org.parboiled.errors.ErrorUtils;
import org.parboiled.errors.ParseError;
import org.parboiled.support.ParsingResult;

/**
 *
 */
public class Result<E extends Expression>
  {
  private final ParsingResult<E> result;
  private final long parseDuration;

  public Result( ParsingResult<E> result, long parseDuration )
    {
    this.result = result;
    this.parseDuration = parseDuration;
    }

  public ParsingResult<E> getParsingResult()
    {
    return result;
    }

  public long getParseDuration()
    {
    return parseDuration;
    }

  public boolean matched()
    {
    return result.matched;
    }

  public boolean hasErrors()
    {
    return result.hasErrors();
    }

  public E getExpression()
    {
    return result.resultValue;
    }

  public int getNumErrors()
    {
    if( !result.hasErrors() )
      return 0;

    return result.parseErrors.size();
    }

  public int getErrorStartIndex( int index )
    {
    if( !result.hasErrors() )
      throw new IllegalStateException( "has no errors" );

    return result.parseErrors.get( index ).getStartIndex();
    }

  public int getErrorEndIndex( int index )
    {
    if( !result.hasErrors() )
      throw new IllegalStateException( "has no errors" );

    return result.parseErrors.get( index ).getEndIndex();
    }

  public List<String> getErrorMessages()
    {
    List<String> messages = new ArrayList<>();

    for( ParseError parseError : result.parseErrors )
      messages.add( parseError.getErrorMessage() );

    return messages;
    }

  public String prettyPrintErrors()
    {
    return ErrorUtils.printParseErrors( result );
    }
  }
