/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.time.format;

import java.util.ArrayList;
import java.util.List;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.Cached;
import org.parboiled.annotations.DontLabel;
import org.parboiled.annotations.SuppressNode;

/**
 *
 */
public class DateTimePatternGrammar extends BaseParser<Object>
  {
  public DateTimePatternGrammar()
    {
    }

  public Rule DateTimeRoot()
    {
    List<Rule> symbols = new ArrayList<>();

    for( DateTimeFormats.Symbol symbol : DateTimeFormats.Symbol.values() )
      symbols.add( Symbol( symbol ) );

    symbols.add( Literal() );
    symbols.add( OtherChar() );

    return
      OneOrMore(
        FirstOf( symbols.toArray() )
      );
    }

  @Cached
  @DontLabel
  Rule Symbol( DateTimeFormats.Symbol symbol )
    {
    return Sequence(
      String( symbol.getSymbolFormat() ),
      TestNot( symbol.getSymbol() ),
      push( symbol )
    ).label( symbol.getSymbolFormat() );
    }

  @SuppressNode
  @DontLabel
  Rule Literal()
    {
    return
      Sequence(
        '\'',
        Sequence(
          OneOrMore( LetterOrDigit() ),
          push( match() )
        ),
        '\''
      );
    }

  public static final String OTHER = " _:.#?+-/";

  Rule OtherChar()
    {
    return Sequence( AnyOf( OTHER ), push( match() ) );
    }

  Rule LetterOrDigit()
    {
    return FirstOf( CharRange( 'a', 'z' ), CharRange( 'A', 'Z' ), CharRange( '0', '9' ), OTHER );
    }
  }
