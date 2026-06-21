package src.exception;

public class StockInvalidoException extends RuntimeException{

    public StockInvalidoException() {
    }

    public StockInvalidoException(String message) {
        super(message);
    }

    public StockInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public StockInvalidoException(Throwable cause) {
        super(cause);
    }

    public StockInvalidoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
