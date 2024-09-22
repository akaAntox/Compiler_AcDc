package main;

import java.io.File;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;

import javax.swing.JFileChooser;

import ast.NodeProgram;
import exception.SyntaxException;
import parser.Parser;
import scanner.Scanner;
import visitor.CodeGeneratorVisitor;
import visitor.TypeCheckingVisitor;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            File inputFile = chooseFile("Select input file");
            if (inputFile != null) {
                processFile(inputFile);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred", e);
        }
    }

    private static File chooseFile(String dialogTitle) throws IOException {
        JFileChooser chooser = new JFileChooser(new File(".").getCanonicalPath());
        chooser.setDialogTitle(dialogTitle);
        int value = chooser.showOpenDialog(null);
        if (value == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    private static void processFile(File inputFile) throws IOException, SyntaxException {
        var scanner = new Scanner(inputFile.getAbsolutePath());
        var parser = new Parser(scanner);
        var parsedNode = parser.parse();

        var typeVisitor = new TypeCheckingVisitor();
        parsedNode.accept(typeVisitor);

        if (!typeVisitor.hasErrors()) {
            generateCodeAndSave(parsedNode);
        } else {
            logger.log(Level.SEVERE, typeVisitor.getLoggerString());
        }
    }

    private static void generateCodeAndSave(NodeProgram parsedNode) throws IOException {
        var codeGenVisitor = new CodeGeneratorVisitor();
        parsedNode.accept(codeGenVisitor);

        File outputFile = chooseFile("Select output file");
        if (outputFile != null) {
            try (var writer = new FileWriter(outputFile.getAbsolutePath())) {
                writer.write(codeGenVisitor.getCode());
                logger.log(Level.INFO, "Code generated and saved successfully.");
            }
            logger.log(Level.INFO, codeGenVisitor.getCode());
        }
    }
}
