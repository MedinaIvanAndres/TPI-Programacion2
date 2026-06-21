package src.transacciones;

import src.entities.Categoria;
import src.exception.EliminacionEntidadException;
import src.exception.EntidadNoEncontradaException;
import src.exception.NombreCategoriaRepetidoException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransaccionCategoria {
    private List<Categoria> categorias = new ArrayList<>();

    public List<Categoria> getCategorias() {
        return Collections.unmodifiableList(categorias); // probar si esto no me afecta al momento de asignar o cambiar la categoria de un producto
    }



    public void listar (){
        boolean listaVacia = true;
        for(Categoria categoria : categorias){
            if(!categoria.isEliminado()){
                listaVacia = false;
                System.out.println(categoria.toString());
            }
        }
        if (listaVacia){
            System.out.println("No hay categorias cargadas");
        }
    }

    public void crear(String nombre,String descripcion){
        for (Categoria catego : categorias) {
            if (!catego.isEliminado() && catego.getNombre().equalsIgnoreCase(nombre)) {
                throw new NombreCategoriaRepetidoException("Ya existe una categoría con el nombre: "+nombre);
            }
        }
        try{
            Categoria nuevaCategoria = new Categoria(nombre, descripcion);
            categorias.add(nuevaCategoria);
            System.out.println("¡Categoria con id: #" + nuevaCategoria.getId() + " creada exitosamente!");
        } catch (IllegalArgumentException iae){
            System.out.println("Error: "+ iae.getMessage());
        }
    }

    public void editar(long id,String nombre,String descri){
        Categoria cateParaEditar = this.buscar(id);
        if (cateParaEditar == null){
            throw new EntidadNoEncontradaException(String.format("Categoria con id #%d no encontrada.",id));
        } else if(cateParaEditar.isEliminado()){
            throw new EntidadNoEncontradaException(String.format("Categoria %s con id #%d eliminada",
                    cateParaEditar.getNombre(),cateParaEditar.getId()));
        } else {
            try{
                cateParaEditar.setNombre(nombre);
                cateParaEditar.setDescripcion(descri);
                System.out.println("¡¡¡Categoria con id: #" + id + " actualizada exitosamente!!!");
            } catch (IllegalArgumentException iae){
                System.out.println("Error: "+ iae.getMessage());
            }
        }
    }

    public Categoria buscar(long id){
        int i = 0;
        Categoria categoriaEncontrada = null;
        while(i < categorias.size() && categorias.get(i).getId() != id){
            i++;
        }
        if (i < categorias.size()){
            categoriaEncontrada = this.categorias.get(i);
        }
        return  categoriaEncontrada;
    }

    public void eliminar (long id){
        Categoria cateParaEliminar = this.buscar(id);
        if(cateParaEliminar == null || cateParaEliminar.isEliminado()){
            throw new EntidadNoEncontradaException(String.format("Categoria con id #%d no encontrada o ya eliminada",id));
        } else{
            if(cateParaEliminar.getProductos().isEmpty()){
                cateParaEliminar.setEliminado(true);
                System.out.println("¡¡¡Categoria con id: #" + id + " se elimino exitosamente!!!");
            } else{
                throw new EliminacionEntidadException("La categoria que se quiere eliminar tiene productos asociados");
            }
        }
    }
}
