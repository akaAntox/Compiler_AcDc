package ast;

import visitor.GenericVisitor;

/**
 * Nodo per la dichiarazione di una variabile.
 */
public class NodeDecl extends NodeDecSt {
    private NodeId id;
    private LangType type;

    public NodeDecl(LangType type, String name) {
        id = new NodeId(name);
        this.type = type;
    }

    public NodeId getNodeId() {
        return id;
    }

    public LangType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "[Decl: " + type.toString() + ", " + id.toString() + "]" ;
    }

    @Override
    public void accept(GenericVisitor visitor) {
        visitor.visit(this);
    }
}