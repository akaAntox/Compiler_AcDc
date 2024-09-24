package visitor;

import ast.*;
import symboltable.*;

/**
 * Classe che implementa GenericVisitor per il type checking dell'AST.
 */
public class TypeCheckingVisitor implements GenericVisitor {

    private StringBuilder logger = new StringBuilder();

    /**
     * Controlla se ci sono errori nell'AST.
     * 
     * @return {@code true} se ci sono errori, altrimenti {@code false}.
     */
    public boolean hasErrors() {
        return !logger.toString().isEmpty();
    }

    /**
     * @return Una stringa contenente tutti gli errori trovati durante la visita.
     */
    public String getLoggerString() {
        return logger.toString();
    }

    /**
     * Visita un nodo {@link NodeProgram}.
     * 
     * Visita tutti i nodi dell'AST.
     * Se ci sono errori, imposta il descrittore del nodo su {@code ERROR},
     * altrimenti su {@code VOID}.
     * 
     * @param node Il nodo radice.
     */
    @Override
    public void visit(NodeProgram node) {
        SymbolTable.init();
        
        for (NodeDecSt nodeDecSt : node) {
            nodeDecSt.accept(this);
        }

        if (hasErrors())
            node.setResType(TypeDescriptor.ERROR);
        else
            node.setResType(TypeDescriptor.VOID);
    }

    /**
     * Visita un nodo {@link NodeId}.
     * 
     * Controlla se l'identificatore è dichiarato nella {@link SymbolTable}.
     * Se è dichiarato imposta il descrittore del nodo su {@code ERROR},
     * altrimenti lo aggiunge alla {@link SymbolTable} e visita il nodo.
     * 
     * @param node Il nodo identificatore.
     */
    @Override
    public void visit(NodeId node) {
        if (SymbolTable.lookup(node.getName()) != null) {
            Attributes attr = SymbolTable.lookup(node.getName());
            node.setResType(TypeDescriptor.valueOf(attr.getType().toString()));
            node.setDefinition(attr);
        } else {
            node.setResType(TypeDescriptor.ERROR);
            logger.append(String.format("Variabile \'%s\' non dichiarata.%n", node.getName()));
        }
    }

    /**
     * Visita un nodo {@link NodeDecl}.
     * 
     * Controlla se è dichiarato nella {@link SymbolTable}.
     * Se è già dichiarato imposta il descrittore del nodo su {@code ERROR},
     * altrimenti lo aggiunge alla {@link SymbolTable} e visita il nodo.
     * 
     * @param node Il nodo dichiarazione.
     */
    @Override
    public void visit(NodeDecl node) {
        if (SymbolTable.lookup(node.getNodeId().getName()) != null) {
            node.setResType(TypeDescriptor.ERROR);
            logger.append(
                    String.format("Variabile \'%s\' già dichiarata.%n", node.getNodeId().getName()));
        } else {
            Attributes attr = new Attributes(node.getType());
            SymbolTable.enter(node.getNodeId().getName(), attr);
            node.getNodeId().accept(this);
        }
    }

    /**
     * Visita un nodo {@link NodeBinOp}.
     * 
     * Visita entrambe le espressioni e controlla se i tipi sono corretti.
     * Se i tipi non sono corretti imposta il descrittore del nodo su {@code ERROR},
     * altrimenti su {@code INT} o {@code FLOAT}.
     * 
     * @param node Il nodo operazione binaria.
     */
    @Override
    public void visit(NodeBinOp node) {
        node.getLeft().accept(this);
        node.getRight().accept(this);
        if (node.getLeft().getResType().equals(TypeDescriptor.ERROR)
                || node.getRight().getResType().equals(TypeDescriptor.ERROR)) {
            node.setResType(TypeDescriptor.ERROR);
            logger.append(String.format(
                    "Atteso tipo FLOAT / INT per le operazioni, ma letto \'%s\' e \'%s\'.%n",
                    node.getLeft().getResType(), node.getRight().getResType()));
        } else {
            node.setResType(node.getLeft().getResType());
        }
    }

    /**
     * Visita un nodo {@link NodeDeref}.
     * 
     * Visita il nodo {@link NodeId} e imposta il descrittore del nodo su {@code ID}.
     * 
     * @param node Il nodo dereferenziato.
     */
    @Override
    public void visit(NodeDeref node) {
        node.getId().accept(this);
        node.setResType(node.getId().getResType());
    }

    /**
     * Visita un nodo {@link NodeConst}.
     * 
     * Visita il nodo costante e imposta il descrittore del nodo sul tipo costante.
     * 
     * @param node Il nodo costante.
     */
    @Override
    public void visit(NodeConst node) {
        var type = TypeDescriptor.valueOf(node.getType().toString());
        if (type != null)
            node.setResType(type);
        else {
            node.setResType(TypeDescriptor.ERROR);
            logger.append(String.format("Tipo inatteso \'%s\' per la costante \'%s\'", node.getType(),
                    node.getValue()));
        }
    }

    /**
     * Visita un nodo {@link NodeAssign}.
     * 
     * Visita il nodo {@link NodeAssign} e imposta il descrittore del nodo sul tipo dell'espressione.
     * 
     * @param node Il nodo assegnamento.
     */
    @Override
    public void visit(NodeAssign node) {
        node.getId().accept(this);
        node.getExpr().accept(this);
        if (isCompatible(node.getId().getResType(), node.getExpr().getResType())) {
            if (!node.getId().getResType().equals(node.getExpr().getResType()))
                convert(node.getExpr());
            node.setResType(node.getExpr().getResType());
        } else {
            node.setResType(TypeDescriptor.ERROR);
            logger.append(String.format("Impossibile assegnare il tipo \'%s\' al tipo \'%s\'.%n",
                    node.getExpr().getResType(), node.getId().getResType()));
        }
    }

    /**
     * Visita un nodo {@link NodeConvert}.
     * 
     * Visita il nodo convertito e imposta il descrittore del nodo sul tipo convertito.
     * 
     * @param node Il nodo convertito.
     */
    @Override
    public void visit(NodeConvert node) {
        node.getNode().accept(this);
        if (node.getNode().getResType().equals(TypeDescriptor.INT))
            node.getNode().setResType(TypeDescriptor.FLOAT);
        else
            node.getNode().setResType(TypeDescriptor.ERROR);
    }

    /**
     * Visita un nodo {@link NodePrint}.
     * 
     * Visita il nodo {@link NodePrint} e imposta il descrittore del nodo sul tipo della variabile.
     * 
     * @param node Il nodo print.
     */
    @Override
    public void visit(NodePrint node) {
        node.getId().accept(this);
        if (node.getId().getResType() != TypeDescriptor.ERROR && SymbolTable.lookup(node.getId().getName()) != null)
            node.setResType(node.getId().getResType());
        else
            node.setResType(TypeDescriptor.ERROR);
    }

    /**
     * Controllo di compatibilità tra due {@link TypeDescriptor}.
     * 
     * @param t1 tipo1 da controllare.
     * @param t2 tipo2 da controllare.
     * @return {@code true} se i tipi sono compatibili, altrimenti {@code false}.
     */
    private boolean isCompatible(TypeDescriptor t1, TypeDescriptor t2) {
        return ((t1.equals(TypeDescriptor.FLOAT) && t2.equals(TypeDescriptor.INT)) || t1.equals(t2))
                && t1 != TypeDescriptor.ERROR && t2 != TypeDescriptor.ERROR;
    }

    /**
     * Converte un'espressione {@code INT} in una {@code FLOAT}.
     * 
     * @param node Il nodo da convertire.
     * @return Il nodo convertito.
     */
    private NodeExpr convert(NodeExpr node) {
        if (node.getResType().equals(TypeDescriptor.FLOAT))
            return node;
            
        NodeConvert nodeConvert = new NodeConvert(node);
        nodeConvert.setResType(TypeDescriptor.FLOAT);
        return nodeConvert;
    }
}
