package src.exception;

public class CantidadDetallePedidoException extends RuntimeException{
    public CantidadDetallePedidoException() {
    }

    public CantidadDetallePedidoException(String message) {
        super(message);
    }

    public CantidadDetallePedidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public CantidadDetallePedidoException(Throwable cause) {
        super(cause);
    }
}
