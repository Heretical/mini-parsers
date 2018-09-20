/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.time.util;

import org.parboiled.support.Var;

/**
 *
 */
public class IntegerVar extends Var<Integer>
  {
  public IntegerVar()
    {
    }

  public IntegerVar( Integer number )
    {
    super( number );
    }

  public boolean set( String integerString )
    {
    try
      {
      return set( Integer.valueOf( integerString ) );
      }
    catch( NumberFormatException exception )
      {
      return false;
      }
    }
  }
