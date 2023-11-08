/*
 * Copyright (c) 2018-2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.common.util;

import org.parboiled.support.Var;

/**
 *
 */
public class DoubleVar extends Var<Double>
  {
  public DoubleVar()
    {
    }

  public DoubleVar( Double number )
    {
    super( number );
    }

  public boolean set( String doubleString )
    {
    doubleString = doubleString.replaceAll( ",", "" );

    try
      {
      return set( Double.valueOf( doubleString ) );
      }
    catch( NumberFormatException exception )
      {
      return false;
      }
    }
  }
