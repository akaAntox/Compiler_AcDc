package visitor;

import ast.*;
import symboltable.SymbolTable;

/**
 * Rappresenta un visitor che si occupa di generare il codice DC
 */
public class CodeGeneratorVisitor implements GenericVisitor {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private StringBuilder code = new StringBuilder();
    private static char[] register = ALPHABET.toCharArray();
    private static int registerIndex = 0;

    /**
     * Visita l'AST
     * @param node Il nodo contenente l'AST
     */
    @Override
    public void visit(NodeProgram node) {
        SymbolTable.init();
        for (NodeAST nodeAST : node)
            nodeAST.accept(this);
    }

    /**
     * Prende un carattere per identificare il registro associato a una variabile.
     * Incrementa l'indice del registro.
     * 
     * @return Il carattere che identifica il registro.
     */
    private static char newRegister() {
        return register[registerIndex++];
    }

    /**
     * Inserisce uno spazio nel codice.
     */
    private void insertSpace() {
        code.append(" ");
    }

    /**
     * @return Il codice generato.
     */
    public String getCode() {
        return code.toString().trim();
    }

    /**
     * @param node Il nodo rappresentante l'identificatore.
     */
    @Override
    public void visit(NodeId node) {
        // Empty method
    }

    /**
     * Visita un nodo {@link NodeDecl}.
     * Genera il codice per la dichiarazione di una variabile.
     * 
     * @param node Il nodo rappresentante la dichiarazione di una variabile.
     */
    @Override
    public void visit(NodeDecl node) {
        node.getNodeId().getDefinition().setRegister(newRegister());
    }

    /**
     * Visita un nodo {@link NodeBinOp}.
     * Generando il codice per le operazioni binarie.
     * 
     * @param node Il nodo rappresentante l'operazione binaria.
     */
    @Override
    public void visit(NodeBinOp node) {
        node.getLeft().accept(this);
        node.getRight().accept(this);
        switch (node.getOperator()) {
            case DIV:
                code.append("/ ");
                break;
            case MINUS:
                code.append("- ");
                break;
            case PLUS:
                code.append("+ ");
                break;
            case TIMES:
                code.append("* ");
                break;
            default:
                break;
        }
    }

    /**
     * Visita un nodo {@link NodeDeref}.
     * Ottiene il registro associato alla variabile e lo aggiunge al codice.
     * 
     * @param node Il nodo rappresentante il dereferenziamento di una variabile.
     */
    @Override
    public void visit(NodeDeref node) {
        char reg = node.getId().getDefinition().getRegister();
        code.append("l" + reg);
        insertSpace();
    }

    /**
     * Visita un nodo {@link NodeConst}.
     * Ottiene il valore del nodo e lo aggiunge al codice.
     * 
     * @param node Il nodo rappresentante una costante.
     */
    @Override
    public void visit(NodeConst node) {
        code.append(node.getValue());
        insertSpace();
    }

    /**
     * Visita un nodo {@link NodeAssign}.
     * Ottiene il registro associato alla variabile assegnata e lo aggiunge al codice.
     * 
     * @param node Il nodo rappresentante l'assegnamento di un'espressione a una variabile.
     */
    @Override
    public void visit(NodeAssign node) {
        char assignedRegister = node.getId().getDefinition().getRegister();
        node.getExpr().accept(this);
        code.append("s").append(assignedRegister);
        insertSpace();
        code.append("0 k ");
    }

    /**
     * Visita un nodo {@link NodePrint}.
     * Ottiene il registro associato alla variabile e lo aggiunge al codice.
     * 
     * @param node The node representing the print statement.
     */
    @Override
    public void visit(NodePrint node) {
        char reg = node.getId().getDefinition().getRegister();
        code.append("l" + reg + " p P ");
    }

    /**
     * Visita un nodo {@link NodeConvert}.
     * Visita l'espressione e aggiunge al codice la precisione decimale a 5 cifre
     * 
     * @param node Il nodo rappresentante la conversione di un'espressione.
     */
    @Override
    public void visit(NodeConvert node) {
        node.getNode().accept(this);
        code.append("5 k ");
    }
}
