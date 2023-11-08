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
 * The NaturalDurationParser class parses strings that represent durations a more natural language.
 * <p>
 * Where a natural duration format would look like the following:
 *
 * <pre>
 *    90 seconds
 *    10,000 seconds
 *    3 months
 *  </pre>
 * A natural duration string is of the format:
 * <p>
 * <pre>
 * number[:space:]unit
 * </pre>>
 * <p>
 * Where `number` is any natural integer (with commas).
 * <p>
 * And `unit` is either the Unit name or Abbreviation, case-insensitive:
 * <p>
 * <pre>
 * | Unit         | Abbreviation | Example   |
 * |--------------|--------------|-----------|
 * | Milliseconds | ms           | 300ms     |
 * | Seconds      | s, sec       | 30s 30sec |
 * | Minutes      | m, min       | 20m 20min |
 * | Hours        | h, hrs       | 3h 3hrs   |
 * | Days         | d, days      | 5d 5 days |
 * | Weeks        | w, wks       | 2w 2wks   |
 * | Months       | mos          | 3mos      |
 * | Years        | y, yrs       | 2y 2rs    |
 * </pre>
 * <p>
 * This class is not thread-safe.
 */
public class NaturalDurationParser extends DurationParser
  {
  public NaturalDurationParser()
    {
    }

  public NaturalDurationParser( Context context )
    {
    super( context );
    }

  @Override
  protected Rule getGrammar( DurationGrammar parser )
    {
    return parser.NaturalDurationEOI();
    }
  }
