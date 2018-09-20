/*
 * Copyright (c) 2018 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package heretical.parser.common;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.DontLabel;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.support.StringVar;

/**
 *
 */
public class BaseSyntaxGrammar<Node> extends BaseParser<Node>
  {
  protected final Rule AT = Terminal( "@" );
  protected final Rule AND_CHAR = Terminal( "&", AnyOf( "=&" ) );
  protected final Rule ANDAND = Terminal( "&&" );
  protected final Rule BANG = Terminal( "!", Ch( '=' ) );
  protected final Rule COLON = Terminal( ":" );
  protected final Rule COMMA = Terminal( "," );
  protected final Rule SLASH = Terminal( "/" );
  protected final Rule DOT = Terminal( "." );
  protected final Rule EQU = Terminal( "=", Ch( '=' ) );
  protected final Rule EQUAL = Terminal( "==" );
  protected final Rule GE = Terminal( ">=" );
  protected final Rule GT = Terminal( ">", AnyOf( "=>" ) );
  protected final Rule HAT = Terminal( "^", Ch( '=' ) );
  protected final Rule LBRK = Terminal( "[" );
  protected final Rule LE = Terminal( "<=" );
  protected final Rule LPAR = Terminal( "(" );
  protected final Rule LPOINT = Terminal( "<" );
  protected final Rule LT = Terminal( "<", AnyOf( "=<" ) );
  protected final Rule LWING = Terminal( "{" );
  protected final Rule MINUS = Terminal( "-", AnyOf( "=-" ) );
  protected final Rule NOTEQUAL = Terminal( "!=" );
  protected final Rule PIPE = Terminal( "|", AnyOf( "=|" ) );
  protected final Rule OROR = Terminal( "||" );
  protected final Rule PLUS = Terminal( "+", AnyOf( "=+" ) );
  protected final Rule QUERY = Terminal( "?" );
  protected final Rule RBRK = Terminal( "]" );
  protected final Rule RPAR = Terminal( ")" );
  protected final Rule RPOINT = Terminal( ">" );
  protected final Rule RWING = Terminal( "}" );
  protected final Rule SEMI = Terminal( ";" );
  protected final Rule TILDA = Terminal( "~" );

  @SuppressNode
  public Rule[] IgnoreCase( String[] literals )
    {
    Rule[] result = new Rule[ literals.length ];

    for( int i = 0; i < literals.length; i++ )
      result[ i ] = IgnoreCase( literals[ i ] );

    return result;
    }

  @SuppressNode
  public Rule Digit()
    {
    return CharRange( '0', '9' );
    }

  @SuppressNode
  public Rule TimeDigit()
    {
    return FirstOf( CharRange( '0', '9' ), AnyOf( "+-:" ) );
    }

  @SuppressNode
  public Rule Letter()
    {
    return FirstOf( CharRange( 'a', 'z' ), CharRange( 'A', 'Z' ) );
    }

  @SuppressNode
  protected Rule HexDigit()
    {
    return FirstOf( CharRange( 'a', 'f' ), CharRange( 'A', 'F' ), CharRange( '0', '9' ) );
    }

  @SuppressNode
  protected Rule Spacing()
    {
    return ZeroOrMore( FirstOf(

      // whitespace
      OneOrMore( AnyOf( " \t\r\n\f" ).label( "Whitespace" ) ),

      // traditional comment
      Sequence( "/*", ZeroOrMore( TestNot( "*/" ), ANY ), "*/" ),

      // end of line comment
      Sequence(
        "//",
        ZeroOrMore( TestNot( AnyOf( "\r\n" ) ), ANY ),
        FirstOf( "\r\n", '\r', '\n', EOI )
      )
    ) );
    }

  Rule ListItem( StringVar term )
    {
    return Sequence(
      OneOrMore(
        Sequence(
          TestNot( AnyOf( " \t\r\n\f)]}," ) ),
          ANY
        )
      ).suppressSubnodes(),
      term.set( match() ),
      Optional( TestNot( " \t\r\n\f" ), "," )
    );
    }

  Rule NotListItem( StringVar term )
    {
    return Sequence(
      OneOrMore(
        Sequence(
          TestNot( AnyOf( " \t\r\n\f)]}," ) ),
          ANY
        )
      ).suppressSubnodes(),
      term.set( match() ),
      Spacing()
    );
    }

  Rule Term( StringVar term )
    {
    return Sequence(
      OneOrMore(
        Sequence(
          TestNot( AnyOf( ": \t\r\n\f)]}" ) ),
          ANY
        )
      ).suppressSubnodes(),
      term.set( match() ),
      Spacing()
    );
    }

  @SuppressNode
  @DontLabel
  Rule Terminal( String string )
    {
    return Sequence( IgnoreCase( string ), Spacing() ).label( '\'' + string + '\'' );
    }

  @SuppressNode
  @DontLabel
  protected Rule Terminal( String string, Rule mustNotFollow )
    {
    return Sequence( IgnoreCase( string ), TestNot( mustNotFollow ), Spacing() ).label( '\'' + string + '\'' );
    }
  }
