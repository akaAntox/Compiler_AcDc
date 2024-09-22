package visitor;

import ast.*;

public interface GenericVisitor {
    public abstract void visit(NodeProgram node); // Nodo radice
    public abstract void visit(NodeId node); // Nodo per l'identificatore
    public abstract void visit(NodeDecl node); // Nodo per la dichiarazione di una variabile
    public abstract void visit(NodeBinOp node); // Nodo per l'operazione binaria
    public abstract void visit(NodeDeref node); // Nodo per la dereferenziazione di un puntatore
    public abstract void visit(NodeConst node); // Nodo per la costante
    public abstract void visit(NodeAssign node); // Nodo per l'assegnamento di un'espressione a una variabile
    public abstract void visit(NodePrint node); // Nodo per la stampa di un'espressione
    public abstract void visit(NodeConvert node); // Nodo per il cast di un'espressione
}