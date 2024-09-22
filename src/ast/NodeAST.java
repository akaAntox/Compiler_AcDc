package ast;

import visitor.GenericVisitor;

/**
 * Nodo generico dell'AST.
 */
public abstract class NodeAST {
    private TypeDescriptor resType;

    public TypeDescriptor getResType() {
        return resType;
    }

    public void setResType(TypeDescriptor resType) {
        this.resType = resType;
    }

	public abstract void accept(GenericVisitor visitor);
}