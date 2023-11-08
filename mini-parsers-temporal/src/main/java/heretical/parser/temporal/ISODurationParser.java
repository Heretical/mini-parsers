/*
 * Copyright (c) 2018-2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal;

import heretical.parser.temporal.grammar.DurationGrammar;
import org.parboiled.Rule;

/**
 * The ISODurationParser class parses strings that represent durations in ISO-8601 formats.
 * <p/>
 * Where an ISO duration format would look like the following:
 *
 * <pre>
 *   PT20.345S
 *   +PT20.345S
 *   -PT20.345S // negated duration
 * </pre>
 * This class is not thread-safe.
 */
public class ISODurationParser extends DurationParser
  {
  /**
   * Creates a new ISODurationParser instance.
   */
  public ISODurationParser()
    {
    }

  /**
   * Creates a new ISODurationParser instance.
   *
   * @param context of type Context
   */
  public ISODurationParser( Context context )
    {
    super( context );
    }

  @Override
  protected Rule getGrammar( DurationGrammar parser )
    {
    return parser.ISO8601DurationEOI();
    }
  }
