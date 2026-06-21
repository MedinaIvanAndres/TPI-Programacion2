package src.exception;

public class MailDeUsuarioRepetidoException extends RuntimeException{
    public MailDeUsuarioRepetidoException() {
    }

    public MailDeUsuarioRepetidoException(String message) {
        super(message);
    }

    public MailDeUsuarioRepetidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailDeUsuarioRepetidoException(Throwable cause) {
        super(cause);
    }
}
