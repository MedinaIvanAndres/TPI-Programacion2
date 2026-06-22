package src.menus;

import java.util.Scanner;

import src.entities.Categoria;
import src.exception.EliminacionEntidadException;
import src.transacciones.TransaccionCategoria;
import src.exception.EntidadNoEncontradaException;
import src.exception.NombreCategoriaRepetidoException;
import src.validaciones.ValidacionInput;


public class SubMenuCategoria {

    private final Scanner consola;
    private final TransaccionCategoria transaccion;

    public SubMenuCategoria(Scanner consola, TransaccionCategoria transaccion) {
        this.consola = consola;
        this.transaccion = transaccion;
    }

    public TransaccionCategoria getTransaccion() {
        return transaccion;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = leerOpcionSubmenu();
            switch(opcion){
                case 1:
                    transaccion.listar();
                    break;
                case 2:
                    this.crearCategoria();
                    break;
                case 3:
                    this.editarCategoria();
                    break;
                case 4:
                    this.eliminarCategoria();
                    break;
            }
        } while(opcion != 5);
    }

    private void crearCategoria(){
        try{
            String nombre = ValidacionInput.ingresarValidarInputString("Nombre", "Ingrese el nombre de la nueva categoria: ", consola);
            String descripcion = ValidacionInput.ingresarValidarInputString("Descripcion", "Ingrese la descripcion de la nueva categoria: ", consola);
            transaccion.crear(nombre, descripcion);
        } catch (NombreCategoriaRepetidoException ncre){
            System.out.println("Error: " + ncre.getMessage());
        }
    }

    private void editarCategoria(){
        try{
            System.out.println("\nCATEGORIAS EXISTENTES:");
            transaccion.listar();
            this.existenCategorias("editarlas");
            long id = ValidacionInput.ingresarValidarInputLong("ID", "Ingrese el ID de la categoria a editar: ", consola);
            String nombre = ValidacionInput.ingresarValidarInputString("Nombre", "Ingrese el nuevo nombre: ", consola);
            String descripcion = ValidacionInput.ingresarValidarInputString("Descripcion", "Ingrese la nueva descripcion: ", consola);
            transaccion.editar(id, nombre, descripcion);
        } catch (EntidadNoEncontradaException | NombreCategoriaRepetidoException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarCategoria(){
        try{
            System.out.println("\nCATEGORIAS EXISTENTES:");
            transaccion.listar();
            this.existenCategorias("eliminarlas");
            long id = ValidacionInput.ingresarValidarInputLong("ID", "Ingrese el ID a eliminar: ", consola);
            System.out.print("Seleccione 'S' para confirmar la operacion: ");
            String confirmacion = consola.nextLine().trim();
            if(confirmacion.equalsIgnoreCase("S")){
                transaccion.eliminar(id);
            } else {
                System.out.println("¡Operacion no confirmada. Categoria con id #"+id+" no eliminada!");
            }
        } catch(EntidadNoEncontradaException | EliminacionEntidadException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private int leerOpcionSubmenu(){
        boolean opcionSubMenuInvalida = true;
        String opcionSubMenuString = "";
        int opcionSubMenu=0;

        while(opcionSubMenuInvalida){
            boolean opcionEsEntero = false;
            this.mostrarSubMenu();
            opcionSubMenu=0;
            try{
                opcionSubMenuString = consola.nextLine();
                opcionSubMenu = Integer.parseInt(opcionSubMenuString.trim());
                opcionEsEntero = true;
            } catch (NumberFormatException  nfe){
                System.out.println("Error: "+nfe.getMessage());
            }
            if(opcionSubMenu>=1 && opcionSubMenu <=5){
                opcionSubMenuInvalida = false;
            } else{
                String mensajeTernario = opcionEsEntero? String.format("La opcion numero %d esta fuera de rango",opcionSubMenu) :
                        String.format(" El caracter \"%s\" no es un valido",opcionSubMenuString);
                System.out.println("Por favor ingresar una opcion valida."+ mensajeTernario);
            }
        }
        return opcionSubMenu;
    }

    private void mostrarSubMenu(){
        System.out.println("\n===== SUBMENU CATEGORIAS =====");
        System.out.println("1. Listar");
        System.out.println("2. Crear");
        System.out.println("3. Editar");
        System.out.println("4. Eliminar");
        System.out.println("5. Menu principal");
        System.out.print("Seleccione: ");
    }

    public void existenCategorias(String accion){
        boolean listaVacia = true;
        if (this.transaccion.getCategorias() != null && !this.transaccion.getCategorias().isEmpty()){
            for(Categoria categoria : this.transaccion.getCategorias()){
                if (!categoria.isEliminado()) {
                    listaVacia = false;
                    break;
                }
            }
        }
        if (listaVacia){
            throw new EntidadNoEncontradaException("Tienen que haber categorias para poder "+accion);
        }
    }
}