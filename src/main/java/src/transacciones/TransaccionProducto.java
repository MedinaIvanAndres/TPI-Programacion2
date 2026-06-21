package src.transacciones;

import src.entities.Categoria;
import src.entities.DetallePedido;
import src.entities.Pedido;
import src.entities.Producto;
import src.exception.EliminacionEntidadException;
import src.exception.EntidadNoEncontradaException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransaccionProducto {
    private final List<Producto> productos = new ArrayList<>();

    public List<Producto> getProductos() {
        return Collections.unmodifiableList(productos);
    }

    public void listarGeneral(){
        boolean listaVacia = true;
        for(Producto producto : productos){
            if(!producto.isEliminado()){
                listaVacia = false;
                System.out.println(producto.toString());
            }
        }
        if (listaVacia){
            System.out.println("No hay productos cargados");
        }
    }

    public void listarPorCategoria(String nombreCategoria){
        boolean listaVacia = true;
        for(Producto producto : productos){
            if(producto.getCategoria() != null && !producto.isEliminado() && producto.getCategoria().getNombre().equalsIgnoreCase(nombreCategoria)){
                listaVacia = false;
                System.out.println(producto.toString());
            }
        }
        if (listaVacia){
            System.out.println("No hay productos cargados que pertenezcan a la categoria: "+nombreCategoria);
        }
    }

    public void crear(String nombre, String descripcion, double precio, int stock, String imagen, boolean disponible, Categoria categoria){
        try{
            Producto nuevoProducto = new Producto(nombre,precio,descripcion,stock,imagen,disponible,categoria);
            productos.add(nuevoProducto);
            System.out.println("¡¡¡Producto con id: #" + nuevoProducto.getId() + " creado exitosamente!!!");
        } catch (IllegalArgumentException iae){
            System.out.println("Error: "+ iae.getMessage());
            System.out.println("Producto NO creado");
        }
    }

    public void editar(long id,double precio,int stock,Categoria nuevaCategoria){
        Producto productoParaEditar = this.buscar(id);
        if (productoParaEditar == null){
            throw new EntidadNoEncontradaException(String.format("Producto con id #%d no encontrado.",id));
        } else {
            try{
                if(precio != -1){
                    productoParaEditar.setPrecio(precio);
                }
                if(stock != -1){
                    productoParaEditar.setStock(stock);
                }
                if(nuevaCategoria != null){
                    productoParaEditar.setCategoria(nuevaCategoria);
                }
                if (precio == -1 && stock == -1 && nuevaCategoria == null){
                    System.out.println("No se registraron modificaciones al Producto con id: #" + id);
                } else{
                    System.out.println("¡¡¡Producto con id: #" + id + " actualizado exitosamente!!!");
                }
            } catch (IllegalArgumentException iae){
                System.out.println("Error: "+ iae.getMessage());
            }
        }
    }

    public Producto buscar(long id){
        int i = 0;
        Producto productoEncontrada = null;
        while(i < productos.size() && productos.get(i).getId() != id){
            i++;
        }
        if (i < productos.size() && !productos.get(i).isEliminado()){
            productoEncontrada = this.productos.get(i);
        }
        return  productoEncontrada;
    }

    public void eliminar (long id, List<Pedido> pedidos){
        Producto productoParaEliminar = this.buscar(id);
        if(productoParaEliminar == null){
            throw new EntidadNoEncontradaException(String.format("Producto con id #%d no encontrado o ya eliminado",id));
        } else{
            if(!productoExisteEnDetalle(productoParaEliminar,pedidos)){
                productoParaEliminar.setEliminado(true);
                System.out.println("¡¡¡Producto con id: #" + id + " se elimino exitosamente!!!");
            } else{
                throw new EliminacionEntidadException("El producto que se quiere eliminar esta asociado al detalle de algun pedido");
            }
        }
    }

    public boolean productoExisteEnDetalle(Producto producto, List<Pedido> pedidos){
        if(pedidos != null && !pedidos.isEmpty()){
            for(Pedido pedido : pedidos){
                if (!pedido.isEliminado()){
                    for(DetallePedido detalle : pedido.getDetalles()) {
                        if (detalle.getProducto().equals(producto)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
