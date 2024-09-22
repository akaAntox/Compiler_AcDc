package test;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ast.NodeProgram;
import ast.TypeDescriptor;
import parser.Parser;
import scanner.Scanner;
import visitor.CodeGeneratorVisitor;
import visitor.TypeCheckingVisitor;

public class TestCodeGenerator {

    /**
     * Test di generazione del codice per un programma che esegue una serie di operazioni
     * aritmetiche e di assegnamento.
     * 
     * @throws IOException
     */
    @Test
    public void testGeneral() throws IOException {
        var logger = Logger.getLogger(this.getClass().getName());

        String inputFilePath = "src" + File.separator + "test" + File.separator + "data" + File.separator + "testTypeGeneral2.txt";
        Scanner scanner = new Scanner(inputFilePath);
        
        Parser parser = new Parser(scanner);
        NodeProgram nP = assertDoesNotThrow(parser::parse);
        
        var typeVisitor = new TypeCheckingVisitor();
        nP.accept(typeVisitor);
        
        logger.log(Level.INFO, "nP value: {0}", nP);
        assertEquals(TypeDescriptor.VOID, nP.getResType());
        
        var codeGenVisitor = new CodeGeneratorVisitor();
        nP.accept(codeGenVisitor);
        
        String outputFilePath = "src" + File.separator + "test" + File.separator + "data" + File.separator + "output.txt";
        try (var writer = new FileWriter(outputFilePath)) {
            writer.write(codeGenVisitor.getCode());
        }
        
        String expected = "1.0 6 / sb 0 k lb p P 1 6 / sa 0 k la p P la sb 0 k";
        assertEquals(expected, codeGenVisitor.getCode());
    }
}
