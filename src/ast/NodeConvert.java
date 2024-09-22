package ast;

import visitor.GenericVisitor;

/**
 * Nodo per la rappresentazione di un cast.
 */
public class NodeConvert extends NodeExpr {
    private NodeExpr node;
    
    public NodeConvert(NodeExpr node){
        this.node = node;
    }

    public NodeExpr getNode() {
        return node;
    }

    @Override
    public String toString() {
        return "[Convert: " + node.toString() + "]" ;
    }

    @Override
    public void accept(GenericVisitor visitor) {
        visitor.visit(this);
    }
}