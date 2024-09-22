package ast;

import symboltable.Attributes;
import visitor.GenericVisitor;

/**
 * Nodo per l'identificatore.
 */
public class NodeId extends NodeAST {
    private String name;
    private Attributes definition;

    public Attributes getDefinition() {
        return definition;
    }

    public void setDefinition(Attributes definition) {
        this.definition = definition;
    }

    public NodeId(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "[Id: "+ name + "]";
    }

    @Override
    public void accept(GenericVisitor visitor) {
        visitor.visit(this);
    }
}
