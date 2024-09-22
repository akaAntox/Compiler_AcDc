package test;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import token.Token;
import token.TokenType;

public class TestToken {
	@Test
	public void testTokens() {
		Token assignToken = new Token(TokenType.ASSIGN, 1);
		Token divToken = new Token(TokenType.DIV, 1);
		Token endToken = new Token(TokenType.EOF, 5);
		Token floatToken = new Token(TokenType.FLOAT, 1, "1.0");
		Token identifierToken = new Token(TokenType.ID, 1, "pippo");
		Token intToken1 = new Token(TokenType.INT, 1, "1");
		Token minusToken = new Token(TokenType.MINUS, 1);
		Token plusToken = new Token(TokenType.PLUS, 1);
		Token printToken = new Token(TokenType.PRINT, 11);
		Token semicolonToken = new Token(TokenType.SEMI, 1);
		Token timesToken = new Token(TokenType.TIMES, 1);
		Token floatToken1 = new Token(TokenType.TYFLOAT, 1);
		Token intToken = new Token(TokenType.TYINT, 1);
		assertEquals("<ASSIGN, r: 1>", assignToken.toString());
		assertEquals("<DIV, r: 1>", divToken.toString());
		assertEquals("<EOF, r: 5>", endToken.toString());
		assertEquals("<FLOAT, r: 1, 1.0>", floatToken.toString());
		assertEquals("<ID, r: 1, pippo>", identifierToken.toString());
		assertEquals("<INT, r: 1, 1>", intToken1.toString());
		assertEquals("<MINUS, r: 1>", minusToken.toString());
		assertEquals("<PLUS, r: 1>", plusToken.toString());
		assertEquals("<PRINT, r: 11>", printToken.toString());
		assertEquals("<SEMI, r: 1>", semicolonToken.toString());
		assertEquals("<TIMES, r: 1>", timesToken.toString());
		assertEquals("<TYFLOAT, r: 1>", floatToken1.toString());
		assertEquals("<TYINT, r: 1>", intToken.toString());
	}
}
