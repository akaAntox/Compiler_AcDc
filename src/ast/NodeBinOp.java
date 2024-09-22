package ast;

import visitor.GenericVisitor;

/**
 * Nodo per le operazioni binarie.
 */
public class NodeBinOp extends NodeExpr {
    private NodeExpr left; // operando sinistro
    private NodeExpr right; // operando destro
    private LangOper operator; // operatore

    public NodeBinOp(NodeExpr left, NodeExpr right, LangOper operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public NodeExpr getLeft() {
        return left;
    }

    public NodeExpr getRight() {
        return right;
    }

    public LangOper getOperator() {
        return operator;
    }

    public void setOperator(LangOper operator) {
        this.operator = operator;
    }

    public void setLeft(NodeExpr left) {
        this.left = left;
    }

    public void setRight(NodeExpr right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "[BinOp: " + left.toString() + ", " +  operator.toString() + ", " + right.toString() + "]";
    }

    @Override
    public void accept(GenericVisitor visitor) {
        visitor.visit(this);
    }
}