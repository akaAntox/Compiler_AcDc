package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import ast.NodeProgram;
import ast.TypeDescriptor;
import exception.SyntaxException;
import parser.Parser;
import scanner.Scanner;
import symboltable.SymbolTable;
import visitor.TypeCheckingVisitor;

public class TestTypeCheck {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Test
    public void testRepeatedDeclarations() throws FileNotFoundException, SyntaxException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "testRepeatedDeclarations.txt";
        Scanner scanner = new Scanner(path);
        Parser parser = new Parser(scanner);
        NodeProgram nP = parser.parse();
        var visitor = new TypeCheckingVisitor();
        nP.accept(visitor);
        logger.log(Level.INFO, "{0}", SymbolTable.toStr());
        logger.log(Level.INFO, visitor.getLoggerString());
        assertEquals(TypeDescriptor.ERROR, nP.getResType());
    }

    @Test
    public void testTypeCheckCorrect() throws FileNotFoundException, SyntaxException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "testTypeCheckCorrect.txt";
        Scanner scanner = new Scanner(path);
        Parser parser = new Parser(scanner);
        NodeProgram nP = parser.parse();
        var visitor = new TypeCheckingVisitor();
        nP.accept(visitor);
        logger.log(Level.INFO, "{0}", SymbolTable.toStr());
        logger.log(Level.INFO, visitor.getLoggerString());
        assertEquals(TypeDescriptor.VOID, nP.getResType());
    }

    @Test
    public void testTypeCheckCorrect2() throws FileNotFoundException, SyntaxException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "testTypeCheckCorrect2.txt";
        Scanner scanner = new Scanner(path);
        Parser parser = new Parser(scanner);
        NodeProgram nP = parser.parse();
        var visitor = new TypeCheckingVisitor();
        nP.accept(visitor);
        logger.log(Level.INFO, "{0}", SymbolTable.toStr());
        logger.log(Level.INFO, visitor.getLoggerString());
        assertEquals(TypeDescriptor.VOID, nP.getResType());
    }

    @Test
    public void testIdNotDeclared() throws FileNotFoundException, SyntaxException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "testIdNotDeclared.txt";
        Scanner scanner = new Scanner(path);
        Parser parser = new Parser(scanner);
        NodeProgram nP = parser.parse();
        var visitor = new TypeCheckingVisitor();
        nP.accept(visitor);
        logger.log(Level.INFO, "{0}", SymbolTable.toStr());
        logger.log(Level.INFO, visitor.getLoggerString());
        assertTrue(visitor.hasErrors());
    }

    @Test
    public void testGeneral2() throws FileNotFoundException, SyntaxException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "testTypeGeneral2.txt";
        Scanner scanner = new Scanner(path);
        Parser parser = new Parser(scanner);
        NodeProgram nP = parser.parse();
        var visitor = new TypeCheckingVisitor();
        nP.accept(visitor);
        logger.log(Level.INFO, "{0}", SymbolTable.toStr());
        logger.log(Level.INFO, visitor.getLoggerString());
        assertFalse(visitor.hasErrors());
    }

    @Test
    public void testErrorAssignConvert() throws FileNotFoundException, SyntaxException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "errorAssignConvert.txt";
        Scanner scanner = new Scanner(path);
        Parser parser = new Parser(scanner);
        NodeProgram nP = parser.parse();
        var visitor = new TypeCheckingVisitor();
        nP.accept(visitor);
        logger.log(Level.INFO, "{0}", SymbolTable.toStr());
        logger.log(Level.INFO, visitor.getLoggerString());
        assertTrue(visitor.hasErrors());
    }

    @Test
    public void testErrorOperation() throws FileNotFoundException, SyntaxException {
        String path = "src" + File.separator + "test" + File.separator + "data" + File.separator + "errorOp.txt";
        Scanner scanner = new Scanner(path);
        Parser parser = new Parser(scanner);
        NodeProgram nP = parser.parse();
        var visitor = new TypeCheckingVisitor();
        nP.accept(visitor);
        logger.log(Level.INFO, "{0}", SymbolTable.toStr());
        logger.log(Level.INFO, visitor.getLoggerString());
        assertTrue(visitor.hasErrors());
    }
}
