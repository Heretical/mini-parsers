/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal;

import heretical.parser.temporal.grammar.DateTimeGrammar;
import org.parboiled.Rule;

/**
 *
 */
public class AbsoluteDateTimeParser extends DateTimeParser
  {
  public AbsoluteDateTimeParser( Context context )
    {
    super( context );
    }

  @Override
  protected Rule getGrammar( DateTimeGrammar parser )
    {
    return parser.AbsoluteDateTimeEOI();
    }
  }
