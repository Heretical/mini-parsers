/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal;

/**
 *
 */
public class DateTimeFormatParseException extends RuntimeException
  {
  public DateTimeFormatParseException()
    {
    }

  public DateTimeFormatParseException( String message )
    {
    super( message );
    }

  public DateTimeFormatParseException( String message, Throwable cause )
    {
    super( message, cause );
    }

  public DateTimeFormatParseException( Throwable cause )
    {
    super( cause );
    }

  public DateTimeFormatParseException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace )
    {
    super( message, cause, enableSuppression, writableStackTrace );
    }
  }
