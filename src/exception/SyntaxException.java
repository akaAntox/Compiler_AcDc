package exception;

/**
 * Eccezione lanciata in caso di errore sintattico.
 */
public class SyntaxException extends Exception {
    public SyntaxException(String message) {
        super(message);
    }
    
    public SyntaxException(String message, Throwable innerException) {
        super(message, innerException);
    }
}