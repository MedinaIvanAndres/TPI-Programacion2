package src.exception;

public class NombreCategoriaRepetidoException extends RuntimeException{
    public NombreCategoriaRepetidoException() {
    }

    public NombreCategoriaRepetidoException(String message) {
        super(message);
    }

    public NombreCategoriaRepetidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public NombreCategoriaRepetidoException(Throwable cause) {
        super(cause);
    }
}
