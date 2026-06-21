
package src.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import src.enums.Estado;
import src.enums.FormaPago;
import src.interfaces.Calculable;

/**
 *
 * @author Ivan Andres Medina
 */
public class Pedido extends Base implements Calculable{
    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private List<DetallePedido> detalles;
    private Usuario usuario;

    public Pedido(Estado estado, FormaPago formaPago, Usuario usuario) {
        super();
        this.fecha = LocalDate.now();
        this.estado = estado;
        this.formaPago = formaPago;
        this.detalles = new ArrayList<>();
        this.calcularTotal();
        setUsuario(usuario);
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    @Override
    public void calcularTotal() {
        Double total = 0.0;
        if(!detalles.isEmpty()){
            for (DetallePedido detalle : detalles){
                total += detalle.getSubtotal();
            }    
        }
        this.total = total;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    } 

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario nuevoUsuario) {
        if(this.usuario != nuevoUsuario){
            Usuario viejoUsuario = this.usuario;
            this.usuario = nuevoUsuario; 
            if(viejoUsuario != null){
                viejoUsuario.eliminarPedido(this);
            }
            if(nuevoUsuario != null && !nuevoUsuario.getPedidos().contains(this)){
                nuevoUsuario.agregarPedido(this);
            }
        }
    }
    
    public List<DetallePedido> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }

    public void addDetallePedido(int cantidad,Producto produ) {
        if(produ != null){
            detalles.add(new DetallePedido(cantidad,produ)); // compo
            this.calcularTotal();
        }
    }
    
    public DetallePedido findDetallePedidoByProducto(Producto produ){
        int i = 0;
        DetallePedido detalleEncontrado = null;
        if(produ != null){
            while (i < detalles.size() && detalles.get(i).getProducto().getId() != produ.getId()) {
                i++;
            }
            if (i < detalles.size()) {
                detalleEncontrado = this.detalles.get(i);
            }
        }
        return detalleEncontrado;
    }
    
    public void deleteDetallePedidoByProducto(Producto produ){
        DetallePedido detalleBorrar = findDetallePedidoByProducto(produ);
        if (detalleBorrar != null) {
            detalles.remove(detalleBorrar);
            this.calcularTotal();
        } else {
            String nombreProducto = (produ != null)? produ.getNombre():"null";
            System.out.println(String.format("No se encontro el producto %s en la lista de detalles del pedido id: #%d"
                ,nombreProducto,this.getId()));
        }
    }

    public void listarDetalles(){
        if(this.detalles != null && !this.detalles.isEmpty()){
            for(DetallePedido detalle : detalles){
                System.out.println(detalle.toString());;
            }
        }
    }

    @Override
    public String toString() {
        return String.format("ID: #%d | Estado: %s | Forma De Pago: %s | Total $%.2f | Fecha: %s | Usuario asociado (ID: #%d - %s %s) ",
                this.getId(),estado,formaPago,total,fecha,usuario.getId(),usuario.getNombre(),usuario.getApellido());
    }
}
