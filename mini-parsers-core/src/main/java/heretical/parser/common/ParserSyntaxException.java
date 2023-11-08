/*
 * Copyright (c) 2018-2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.common;

import java.util.List;

import org.parboiled.errors.DefaultInvalidInputErrorFormatter;
import org.parboiled.errors.ErrorUtils;
import org.parboiled.errors.InvalidInputError;
import org.parboiled.errors.ParseError;

/**
 *
 */
public class ParserSyntaxException extends RuntimeException
  {
  public static final String SEARCH_PARSE_ERROR = "parser syntax error";
  private Result result;
  private List<String> errorMessages;
  private static final DefaultInvalidInputErrorFormatter formatter = new DefaultInvalidInputErrorFormatter();

  public ParserSyntaxException( Result result )
    {
    super( makeMessage( result ) );
    this.result = result;
    this.errorMessages = result.getErrorMessages();
    }

  public ParserSyntaxException( List<String> errorMessages )
    {
    super( makeMessage( errorMessages ) );

    this.errorMessages = errorMessages;
    }

  public String getSyntaxError()
    {
    if( result == null )
      return null;

    return getSyntaxError( result );
    }

  public List<String> getErrorMessages()
    {
    return errorMessages;
    }

  public List<ParseError> getParserErrorObjects()
    {
    if( result == null )
      return null;

    return result.getParsingResult().parseErrors;
    }

  // Parboiled doesn't provide a clean error message
  // independent from their logging needs, so isolating this utility method to extract it for our REST API. The below
  // was inferred from the Parboiled source.
  public static String getErrorMessage( ParseError error )
    {
    if( error.getErrorMessage() != null )
      return error.getErrorMessage();

    if( error instanceof InvalidInputError )
      return formatter.format( (InvalidInputError) error );

    return error.getClass().getSimpleName();
    }

  private static String makeMessage( Result result )
    {
    String errors = getSyntaxError( result );

    return String.format( "%s: %s", SEARCH_PARSE_ERROR, errors );
    }

  private static String getSyntaxError( Result result )
    {
    return ErrorUtils.printParseErrors( result.getParsingResult() );
    }

  public static String makeMessage( List<String> errorMessages )
    {
    if( errorMessages == null || errorMessages.isEmpty() || isEmpty( errorMessages.get( 0 ) ) )
      return SEARCH_PARSE_ERROR;

    return SEARCH_PARSE_ERROR + ": " + errorMessages.get( 0 );
    }

  private static boolean isEmpty( String string )
    {
    return string == null || string.isEmpty();
    }
  }
