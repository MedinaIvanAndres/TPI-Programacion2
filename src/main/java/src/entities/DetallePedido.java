
package src.entities;

import src.exception.CantidadDetallePedidoException;
import src.exception.EntidadNoEncontradaException;

import java.time.LocalDateTime;

/**
 *
 * @author Ivan Andres Medina
 */
public class DetallePedido extends Base{
    private int cantidad;
    private Double subtotal;
    private Producto producto;
    private static Long numeradorId = 0L;

    
    public DetallePedido(int cantidad, Producto producto) {
        super(++numeradorId,false,LocalDateTime.now());
        if (cantidad <= 0 || producto == null){
            throw new CantidadDetallePedidoException("La cantidad o producto del nuevo detalle son invalidos");
        }
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if(cantidad >0){
            this.cantidad = cantidad;
            this.subtotal = calcularSubtotal();
        } else {
            throw new IllegalArgumentException("Cantidad del nuevo detalle invalida");
        }
    }

    public double getSubtotal() {
        return subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        if(producto != null){
           this.producto = producto;
           this.subtotal = calcularSubtotal(); 
        } else {
            throw new IllegalArgumentException("Producto del nuevo detalle nulo");
        }
    }

    private Double calcularSubtotal(){
        if (this.producto != null){
            return cantidad*producto.getPrecio();
        } else{
         throw new EntidadNoEncontradaException("El subtotal no puede ser calculado para un Producto nulo");
        }
    }
    
    @Override
    public String toString() {
        return String.format("=> Detalle ID: #%d | (%s) X (%s) | Subtotal: $%.2f"
                ,this.getId(),producto.getNombre(),cantidad,subtotal);
    }
}
