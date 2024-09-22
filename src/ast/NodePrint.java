package ast;

import visitor.GenericVisitor;

/**
 * Nodo per la stampa di una variabile.
 */
public class NodePrint extends NodeStm {
    private NodeId id;

    public NodePrint(NodeId id) {
        this.id = id;
    }

    public NodeId getId() {
        return id;
    }

    @Override
    public String toString() {
        return "[Print: " + id.toString() + "]"; 
    }

    @Override
    public void accept(GenericVisitor visitor) {
        visitor.visit(this);
    }
}