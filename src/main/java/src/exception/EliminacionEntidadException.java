package src.exception;

public class EliminacionEntidadException extends RuntimeException{
    public EliminacionEntidadException() {
    }

    public EliminacionEntidadException(String message) {
        super(message);
    }

    public EliminacionEntidadException(String message, Throwable cause) {
        super(message, cause);
    }

    public EliminacionEntidadException(Throwable cause) {
        super(cause);
    }
}
