/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal;

/**
 * The DateTimeFormatParseException class is thrown when a date time format string cannot be parsed.
 */
public class DateTimeFormatParseException extends RuntimeException
  {
  /**
   * Creates a new DateTimeFormatParseException instance.
   */
  public DateTimeFormatParseException()
    {
    }

  /**
   * @param message of type String
   */
  public DateTimeFormatParseException( String message )
    {
    super( message );
    }

  /**
   * @param message of type String
   * @param cause   of type Throwable
   */
  public DateTimeFormatParseException( String message, Throwable cause )
    {
    super( message, cause );
    }

  /**
   * @param cause of type Throwable
   */
  public DateTimeFormatParseException( Throwable cause )
    {
    super( cause );
    }

  /**
   * @param message            of type String
   * @param cause              of type Throwable
   * @param enableSuppression  of type boolean
   * @param writableStackTrace of type boolean
   */
  public DateTimeFormatParseException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace )
    {
    super( message, cause, enableSuppression, writableStackTrace );
    }
  }
