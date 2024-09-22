package ast;

import visitor.GenericVisitor;

/**
 * Nodo per la rappresentazione di una costante.
 */
public class NodeConst extends NodeExpr {
    private String value;
    private LangType type;

    public NodeConst(String value, LangType type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public LangType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "[Const: "+ type.toString() + ", " + value + "]";
    }

    @Override
    public void accept(GenericVisitor visitor) {
        visitor.visit(this);
    }
}