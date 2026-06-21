
package src.entities;

import src.exception.PrecioInvalidoException;
import src.exception.StockInvalidoException;

import java.time.LocalDateTime;

/**
 *
 * @author Ivan Andres Medina
 */
public class Producto extends Base{
    
    private String nombre; 
    private double precio;
    private String descripcion;
    private int stock;
    private String imagen;
    private boolean disponible;
    private Categoria categoria;
    
    
    public Producto(String nombre, double precio, String descripcion, int stock, String imagen, boolean disponible,Categoria categoria) {
        super();
        setNombre(nombre);
        setPrecio(precio);
        setDescripcion(descripcion);
        setStock(stock);
        setImagen(imagen);
        setDisponible(disponible);
        setCategoria(categoria);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.nombre = nombre;
        } else {
            throw new IllegalArgumentException("Nombre invalido. Producto no creado");
        }
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if(precio >= 0){
            this.precio = precio;
        } else {
            throw new PrecioInvalidoException("precio negativo invalido. Producto no creado");
        }
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            this.descripcion = descripcion;
        } else {
            throw new IllegalArgumentException("descripcion invalida. Producto no creado");
        }  
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) { 
        if(stock >= 0){
            this.stock = stock;
        } else {
            throw new StockInvalidoException("stock negativo invalido. Producto no creado");
        }
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        if (imagen != null && !imagen.trim().isEmpty()) {
            this.imagen = imagen;
        } else {
            throw new IllegalArgumentException("imagen invalida. Producto no creado");
        }  
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoriaNueva) {
        if (this.categoria != categoriaNueva){ // solo hago algo si no tiene la misma categoria que me llega por parametro
            Categoria categoriaVieja = this.categoria;
            this.categoria = categoriaNueva;
            if(categoriaVieja != null){
                categoriaVieja.eliminarProducto(this);
            }
            if(categoriaNueva != null && !categoriaNueva.getProductos().contains(this)){
                categoriaNueva.agregarProducto(this);
            }     
        }
    }

    public void venderUnidades (int vendidas){
        if (this.stock >= vendidas){
            int stockActual = this.stock - vendidas;
            setStock(stockActual);
        } else {
            throw new StockInvalidoException(String.format("El stock del producto #ID: %s es menor a las unidades que se quieren agregar al pedido",this.getId()));
        }
    }

    public void reponerStock(int cantidad){
        if (cantidad >= 0 && (cantidad + this.stock) < Integer.MAX_VALUE ){
            int stockActual = this.stock + cantidad;
            setStock(stockActual);
        } else {
            throw new StockInvalidoException(String.format("cantidad a reponer invalida. Cantidad: %d",cantidad));
        }
    }

    @Override
    public String toString() {
        String nombreCatego = categoria == null ? "SIN CATEGORIA ASIGNADA":categoria.getNombre();
        return String.format("ID: #%d | Producto: %s | Precio $%.2f | Stock: %d | Categoria: %s ",
                this.getId(),nombre,precio,stock,nombreCatego);
    }
}
