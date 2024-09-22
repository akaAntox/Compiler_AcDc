package scanner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import exception.*;
import token.*;

public class Scanner {
	private static final char EOF = (char) -1; // int 65535
	private int row = 1;
	private PushbackReader buffer;

	private Token token = null;

	private List<Character> skipChars; // ' ', '\n', '\t', '\r', EOF
	private List<Character> letters; // 'a',...'z'
	private List<Character> numbers; // '0',...'9'

	private HashMap<Character, TokenType> operatorsMap = new HashMap<>(); // '+', '-', '*', '/', '=', ';'
	private HashMap<String, TokenType> keyWordsMap = new HashMap<>(); // "print", "float", "int"

	/**
	 * Crea un nuovo {@code Scanner} e imposta i metodi per tokenizzare il file dato.
	 * 
	 * @param nomeFile Il nome del file da leggere
	 * @throws FileNotFoundException
	 */
	public Scanner(String fileName) throws FileNotFoundException {
		this.buffer = new PushbackReader(new FileReader(fileName));
		row = 1;
		skipChars = Arrays.asList(' ', '\n', '\t', '\r', EOF);
		letters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
				'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
		numbers = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');

		operatorsMap.put('+', TokenType.PLUS);
		operatorsMap.put('-', TokenType.MINUS);
		operatorsMap.put('*', TokenType.TIMES);
		operatorsMap.put('/', TokenType.DIV);
		operatorsMap.put('=', TokenType.ASSIGN);
		operatorsMap.put(';', TokenType.SEMI);

		keyWordsMap.put("print", TokenType.PRINT);
		keyWordsMap.put("float", TokenType.TYFLOAT);
		keyWordsMap.put("int", TokenType.TYINT);
	}

	/**
	 * Sbircia il prossimo token senza consumarlo
	 * 
	 * @return Il token attuale; se è null, restituisce il prossimo
	 * @throws IOException
	 * @throws LexicalException
	 */
	public Token peekToken() throws IOException, LexicalException {
		if (token == null)
			token = nextToken();
		return token;
	}

	/**
	 * Restituisce il prossimo token
	 * 
	 * @return Prossimo token
	 * @throws IOException
	 * @throws LexicalException
	 */
	public Token nextToken() throws IOException, LexicalException {
		while (skipChars.contains(peekChar())) {
			if (peekChar() == '\n')
				row++;
			if (peekChar() == EOF) {
				readChar();
				token = new Token(TokenType.EOF, row);
				return token;
			}
			readChar();
		}

		while (peekChar() != '\n') {
			if (numbers.contains(peekChar()) || peekChar() == '.') {
				token = scanNumber();
				return token;
			}

			if (letters.contains(peekChar())) {
				token = scanId();
				return token;
			}

			if (operatorsMap.containsKey(peekChar())) {
				token = new Token(operatorsMap.get(peekChar()), row);
				readChar();
				return token;
			}
			readChar();
		}
		
		throw new LexicalException("Carattere illegale in riga " + row);
	}

	/**
	 * Scansiona un numero (int o float con 5 decimali)
	 * 
	 * @return Il token che rappresenta il numero
	 * @throws IOException
	 * @throws LexicalException
	 */
	private Token scanNumber() throws IOException, LexicalException {
		StringBuilder result = new StringBuilder();
		while (numbers.contains(peekChar()))
			result.append(readChar());

		if (peekChar() != '.')
			return new Token(TokenType.INT, row, result.toString());

		int count = 0;
		result.append(readChar());
		while (numbers.contains(peekChar())) {
			result.append(readChar());
			count++;
		}

		if (count >= 1 && count <= 5)
			return new Token(TokenType.FLOAT, row, result.toString());

		throw new LexicalException("Numero di decimali errato in riga " + row);

	}

	/**
	 * Scansiona un Id (variabile o parola chiave)
	 * 
	 * @return Il token che rappresenta l'Id
	 * @throws IOException
	 */
	private Token scanId() throws IOException {
		StringBuilder sb = new StringBuilder();
		while (letters.contains(peekChar())) {
			sb.append(readChar());
		}

		if (keyWordsMap.containsKey(sb.toString()))
			return new Token(keyWordsMap.get(sb.toString()), row);
		return new Token(TokenType.ID, row, sb.toString());
	}

	/**
	 * Legge un carattere consumandolo
	 * 
	 * @return Il carattere letto
	 * @throws IOException
	 */
	private char readChar() throws IOException {
		return ((char) this.buffer.read());
	}

	/**
	 * Sbircia il prossimo carattere senza consumarlo
	 * 
	 * @return Il carattere letto
	 * @throws IOException
	 */
	private char peekChar() throws IOException {
		char c = (char) buffer.read();
		buffer.unread(c);
		return c;
	}
}
