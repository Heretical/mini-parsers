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

import org.parboiled.errors.ParseError;
import org.parboiled.support.ParsingResult;

/**
 *
 */
public class Result<T>
  {
  private final ParsingResult<T> result;
  private final long parseDuration;

  public Result( ParsingResult<T> result, long parseDuration )
    {
    this.result = result;
    this.parseDuration = parseDuration;
    }

  public ParsingResult<T> getResult()
    {
    return result;
    }

  public long getParseDuration()
    {
    return parseDuration;
    }

  public boolean hasErrors()
    {
    return result.hasErrors();
    }

  public List<String> getErrorMessages()
    {
    List<String> messages = new ArrayList<>();

    for( ParseError parseError : result.parseErrors )
      messages.add( parseError.getErrorMessage() );

    return messages;
    }

  public T getResultValue()
    {
    return result.resultValue;
    }
  }
