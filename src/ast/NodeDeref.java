package ast;

import visitor.GenericVisitor;

/**
 * Nodo che rappresenta il dereferenziamento di un puntatore.
 */
public class NodeDeref extends NodeExpr {
    private NodeId id;

    public NodeDeref(NodeId id) {
        this.id = id;
    }

    public NodeId getId() {
        return id;
    }

    @Override
    public String toString() {
        return "[Deref: " + id.toString() + "]";
    }

    @Override
    public void accept(GenericVisitor visitor) {
        visitor.visit(this);
    }
}