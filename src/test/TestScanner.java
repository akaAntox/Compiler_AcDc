package test;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import exception.LexicalException;
import scanner.Scanner;
import token.Token;
import token.TokenType;

public class TestScanner {

    @Test
    public void testScanId() throws IOException, LexicalException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "testIdKw.txt";
        Scanner scanner = new Scanner(path);
        Token token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.TYINT);
        assertEquals(1, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.TYFLOAT);
        assertEquals(2, token.getRow());
        token = scanner.nextToken();
        assertFalse(token.getTokenType() == TokenType.TYFLOAT);
        assertTrue(token.getTokenType() == TokenType.ID);
        assertEquals(2, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.PRINT);
        assertEquals(3, token.getRow());
        token = scanner.nextToken();
        assertFalse(token.getTokenType() == TokenType.PRINT);
        assertTrue(token.getTokenType() == TokenType.ID);
        assertEquals(3, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.ID);
        assertEquals(4, token.getRow());
        token = scanner.nextToken();
        assertFalse(token.getTokenType() == TokenType.TYINT);
        assertTrue(token.getTokenType() == TokenType.ID);
        assertEquals(5, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.TYINT);
        assertEquals(6, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.ID);
        assertEquals(6, token.getRow());
    }

    @Test
    public void testScanNumber() throws IOException, LexicalException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "testNumbers.txt";
        Scanner scanner = new Scanner(path);
        Token token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.INT);
        assertEquals("30000", token.getValue());
        assertEquals(1, token.getRow());
        assertThrows(LexicalException.class, scanner::nextToken);
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.FLOAT);
        assertEquals("13.454", token.getValue());
        assertEquals(4, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.FLOAT);
        assertEquals("098.895", token.getValue());
        assertEquals(4, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.INT);
        assertEquals("45668", token.getValue());
        assertEquals(5, token.getRow());
        assertThrows(LexicalException.class, scanner::nextToken);
        assertThrows(LexicalException.class, scanner::nextToken);
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.EOF);
        assertEquals(8, token.getRow());
    }

    @Test
    public void testScanEOF() throws IOException, LexicalException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "testEOF.txt";
        Scanner scanner = new Scanner(path);
        Token token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.EOF);
        assertEquals(3, token.getRow());
    }

    @Test
    public void testPeekToken() throws IOException, LexicalException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "testPeek.txt";
        Scanner scanner = new Scanner(path);
        Token token = scanner.nextToken();
        assertEquals(token, scanner.peekToken());
        assertEquals(1, token.getRow());
        assertEquals("15", token.getValue());
        token = scanner.nextToken();
        assertEquals(token, scanner.peekToken());
        assertEquals(1, token.getRow());
        assertEquals("14", token.getValue());
    }

    @Test
    public void testOperators() throws IOException, LexicalException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "testOperators.txt";
        Scanner scanner = new Scanner(path);
        Token token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.PLUS);
        assertEquals(1, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.MINUS);
        assertEquals(2, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.TIMES);
        assertEquals(2, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.DIV);
        assertEquals(3, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.ASSIGN);
        assertEquals(8, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.SEMI);
        assertEquals(10, token.getRow());
    }

    @Test
    public void testAllTokens() throws IOException, LexicalException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "testGeneral.txt";
        Scanner scanner = new Scanner(path);
        Token token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.TYINT);
        assertEquals(2, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.ID);
        assertEquals(2, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.SEMI);
        assertEquals(2, token.getRow());

        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.ID);
        assertEquals(3, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.ASSIGN);
        assertEquals(3, token.getRow());
        assertThrows(LexicalException.class, scanner::nextToken);
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.SEMI);
        assertEquals(3, token.getRow());

        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.TYFLOAT);
        assertEquals(5, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.ID);
        assertEquals(5, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.SEMI);
        assertEquals(5, token.getRow());

        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.ID);
        assertEquals(6, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.ASSIGN);
        assertEquals(6, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.ID);
        assertEquals(6, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.PLUS);
        assertEquals(6, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.FLOAT);
        assertEquals(6, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.SEMI);
        assertEquals(6, token.getRow());

        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.PRINT);
        assertEquals(7, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.ID);
        assertEquals(7, token.getRow());
        token = scanner.nextToken();
        assertTrue(token.getTokenType() == TokenType.SEMI);
        assertEquals(7, token.getRow());
    }
}
