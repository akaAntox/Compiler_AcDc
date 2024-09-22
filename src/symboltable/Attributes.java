package symboltable;

import ast.LangType;

/**
 * Classe che descrive o arricchisce gli attributi di una variabile inserita
 * nella {@link SymbolTable}.
 */
public class Attributes {
	private LangType type; // tipo della variabile
	private char register; // chiave per la tabella dei registri (per la generazione del codice)

	public Attributes(LangType type) {
		this.type = type;
	}

	public LangType getType() {
		return type;
	}

	public void setType(LangType type) {
		this.type = type;
	}

	public char getRegister() {
		return register;
	}

	public void setRegister(char register) {
		this.register = register;
	}

	@Override
	public String toString() {
		return type.toString();
	}
}