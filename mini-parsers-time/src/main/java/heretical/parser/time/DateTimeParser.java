/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.time;

import heretical.parser.time.grammar.DateTimeGrammar;

/**
 * This class is not thread-safe.
 */
public abstract class DateTimeParser extends BaseDateTimeParser<DateTimeGrammar>
  {
  public DateTimeParser( Context context )
    {
    this.context = context;
    }

  public DateTimeParser()
    {
    }

  @Override
  protected Class<DateTimeGrammar> getParserClass()
    {
    return DateTimeGrammar.class;
    }
  }
