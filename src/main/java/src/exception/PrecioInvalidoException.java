package src.exception;

public class PrecioInvalidoException extends RuntimeException{
    public PrecioInvalidoException() {
    }

    public PrecioInvalidoException(String message) {
        super(message);
    }

    public PrecioInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrecioInvalidoException(Throwable cause) {
        super(cause);
    }
}
