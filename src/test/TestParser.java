package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import ast.*;
import exception.*;
import parser.*;
import scanner.*;
import token.*;

public class TestParser {
    @Test
    public void testDecParse() throws FileNotFoundException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "testDec.txt";
        Scanner scanner = new Scanner(path);
        Parser parser = new Parser(scanner);
        NodeProgram program = assertDoesNotThrow(parser::parse);
        NodeDecSt node = new NodeDecl(LangType.INT, "a");
        Iterator<NodeDecSt> iterator = program.iterator();
        assertEquals(node.toString(), iterator.next().toString());
        assertEquals(new NodeDecl(LangType.FLOAT, "b").toString(), iterator.next().toString());
        assertEquals(new NodePrint(new NodeId("a")).toString(), iterator.next().toString());
    }

    @Test
    public void testDSsDclStm() throws FileNotFoundException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "testDSsDclStm.txt";
        Scanner scanner = new Scanner(path);
        Parser parser = new Parser(scanner);
        assertThrows(SyntaxException.class, parser::parse);
    }

    @Test
    public void testAddOnHead() {
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(0, new Token(TokenType.EOF, 2));
        assertTrue(TokenType.EOF == tokens.get(0).getTokenType());

        tokens.add(0, new Token(TokenType.INT, 1, "temp"));
        assertTrue(TokenType.INT == tokens.get(0).getTokenType());
        assertEquals("temp", tokens.get(0).getValue());
    }

    @Test
    public void testToString() throws FileNotFoundException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "testDec.txt";
        Scanner scanner = new Scanner(path);
        Parser parser = new Parser(scanner);
        NodeProgram program = assertDoesNotThrow(parser::parse);
        assertEquals("[Program: [Decl: INT, [Id: a]][Decl: FLOAT, [Id: b]][Print: [Id: a]]]", program.toString());
    }

    @Test
    public void testComplete() throws FileNotFoundException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "fileParserCorrect2.txt";
        Scanner scanner = new Scanner(path);
        Parser parser = new Parser(scanner);
        NodeProgram program = assertDoesNotThrow(parser::parse);
        program.toString();
    }

    @Test
    public void testAssociativity() throws FileNotFoundException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "testAssociativity.txt";
        Scanner scanner = new Scanner(path);
        Parser parser = new Parser(scanner);
        NodeProgram program = assertDoesNotThrow(parser::parse);
        program.toString();
    }

    @Test
    public void testAssociativityPlus() throws FileNotFoundException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "testAssociativityPlus.txt";
        Scanner scanner = new Scanner(path);
        Parser parser = new Parser(scanner);
        NodeProgram program = assertDoesNotThrow(parser::parse);
        assertEquals("[Program: [Decl: INT, [Id: b]][Assign: [Id: b], [BinOp: [BinOp: [Const: INT, 3], PLUS, [Const: INT, 2]], PLUS, [Const: INT, 7]]][Print: [Id: b]]]", program.toString());
    }

    @Test
    public void testAssociativityAllSigns() throws FileNotFoundException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "testAssociativityAllSigns.txt";
        Scanner scanner = new Scanner(path);
        Parser parser = new Parser(scanner);
        NodeProgram program = assertDoesNotThrow(parser::parse);
        // Checks if associativity prints (3 - (2 * 4)) - 7
        assertEquals("[Program: [Decl: INT, [Id: b]][Assign: [Id: b], [BinOp: [BinOp: [Const: INT, 3], MINUS, [BinOp: [Const: INT, 2], TIMES, [Const: INT, 4]]], MINUS, [Const: INT, 7]]][Print: [Id: b]]]", program.toString());
    }
}
