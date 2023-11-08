/*
 * Copyright (c) 2018-2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal;

import java.time.Instant;

import heretical.parser.temporal.grammar.DateTimeGrammar;
import org.parboiled.Rule;

/**
 * The AbsoluteDateTimeParser class will parse common date time formats, and return an {@link Instant}.
 * <p>
 * Where common formats supported look like:
 * <pre>
 *   02/10/15 02:04
 *   February/10/15 02:04
 *   02/10/15 02:04AM
 *   February 10th 2015, 02
 *   November 09th 2018, 20:00:00
 *   02/10/15 02
 *   20150210
 *   20150210T020430Z
 *   2015-02-10T02:04:30+00:00
 *   2015-02-10 02:04:30+00:00
 * </pre>
 */
public class AbsoluteDateTimeParser extends DateTimeParser
  {
  /**
   * Creates a new AbsoluteDateTimeParser instance.
   */
  public AbsoluteDateTimeParser()
    {
    }

  /**
   * Creates a new AbsoluteDateTimeParser instance.
   *
   * @param context of type Context
   */
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
