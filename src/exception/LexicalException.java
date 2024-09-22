package exception;

/**
 * Eccezione lanciata in caso di errore lessicale.
 */
public class LexicalException extends Exception {
    public LexicalException(String message) {
        super(message);
    }
}