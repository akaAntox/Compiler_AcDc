package ast;

import visitor.GenericVisitor;

/**
 * Nodo per l'assegnamento di un'espressione a una variabile.
 */
public class NodeAssign extends NodeStm {
    private NodeId id;
    private NodeExpr expr;

    public NodeAssign(NodeId id, NodeExpr expr) {
        this.id = id;
        this.expr = expr;
    }

    public NodeId getId() {
        return id;
    }

    public NodeExpr getExpr() {
        return expr;
    }

    public void setExpr(NodeExpr expr){
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "[Assign: " + id.toString() + ", " + expr.toString() + "]";
    }

    @Override
    public void accept(GenericVisitor visitor) {
        visitor.visit(this);
    }
}