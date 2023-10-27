/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.temporal.expression;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum BinaryOp
  {
    PLUS( "+" ),
    MINUS( "-" );

  static Map<String, BinaryOp> map = new HashMap<>();

  static
    {
    for( BinaryOp unaryOp : BinaryOp.values() )
      {
      map.put( unaryOp.symbol, unaryOp );
      }
    }

  public static BinaryOp lookup( String op )
    {
    return map.get( op );
    }

  public static String chars()
    {
    Optional<String> reduce = Arrays.stream( values() ).map( BinaryOp::charSymbol ).map( c -> Character.toString( c ) ).reduce( String::concat );

    return reduce.orElse( "" );
    }

  String symbol;

  BinaryOp( String symbol )
    {
    this.symbol = symbol;
    }

  char charSymbol()
    {
    return symbol.charAt( 0 );
    }
  }
