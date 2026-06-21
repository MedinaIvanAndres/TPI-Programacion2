
package src.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Ivan Andres Medina
 */
public class Categoria extends Base{
    private String nombre; 
    private String descripcion;
    private List<Producto> productos;
    
    
    public Categoria(String nombre,String descripcion) {
        super();
        setNombre(nombre);
        setDescripcion(descripcion);
        this.productos = new ArrayList<>();
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.nombre = nombre;
        } else {
            throw new IllegalArgumentException("Nombre de la nueva categoria invalido. Categoria no creada");
        }
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            this.descripcion = descripcion;
        } else {
            throw new IllegalArgumentException("Descripcion de la nueva categoria invalida. Categoria no creada");
        }  
    }

    public List<Producto> getProductos() {
        return Collections.unmodifiableList(productos);
    }

    public void agregarProducto (Producto newProduct){
        if (newProduct != null && !productos.contains(newProduct)){
            productos.add(newProduct);
            if (newProduct.getCategoria() != this){
                newProduct.setCategoria(this);
            }       
        }
    }
    
    public void eliminarProducto (Producto produ){
        if(produ != null && productos.contains(produ)){
            productos.remove(produ);
            if(produ.getCategoria() == this){
                produ.setCategoria(null);
            }
        }
    }
    
    @Override
    public String toString() {
        String mensaje;
        mensaje = String.format("ID: #%d | Categoria: %s | Descripcion: %s"
            ,this.getId(),nombre,descripcion);
        return mensaje;
    }
}


