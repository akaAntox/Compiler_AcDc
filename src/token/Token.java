package token;

/**
 * Struttura dei token
 * 
 * @param tokenType tipo del token
 * @param value valore del token
 */
public class Token {
    private TokenType tokenType;
    private String value;
    private int row;

	public Token(TokenType tokenType, int row, String value) {
		this.tokenType = tokenType;
		this.row = row;
		this.value = value;
	}
	
	public Token(TokenType tokenType, int row) {
		this.tokenType = tokenType;
		this.row = row;
	}

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getValue() {
        return value;
    }

    public int getRow() {
        return row;
    }

    /**
     * Converte il token nella stringa formato <tokenType, r:row, value>
     * usando uno stringbuiler per evitare concatenazioni multiple
     * e quindi un uso eccessivo di memoria
     * 
     * @return stringa formattata
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        //<tokenType, r:row, value>
        sb.append("<").append(tokenType).append(", r: ").append(row);
        if (value == null) {
            sb.append(">");
        } else {
            sb.append(", ").append(value).append(">");
        }

        return sb.toString();
    }
}
