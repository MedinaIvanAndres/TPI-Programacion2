package src.menus;

import src.entities.Categoria;
import src.entities.Pedido;
import src.entities.Producto;
import src.exception.EliminacionEntidadException;
import src.exception.EntidadNoEncontradaException;
import src.transacciones.TransaccionCategoria;
import src.transacciones.TransaccionProducto;
import src.validaciones.ValidacionInput;
import java.util.List;
import java.util.Scanner;



public class SubMenuProducto {
    private final Scanner consola;
    private final TransaccionProducto transaccion;

    public SubMenuProducto(Scanner consola, TransaccionProducto transaccion) {
        this.consola = consola;
        this.transaccion = transaccion;
    }

    public TransaccionProducto getTransaccion() {
        return transaccion;
    }

    public void iniciar(TransaccionCategoria trxCategoria, List<Pedido> pedidos) {
        int opcion;
        do {
            opcion = leerOpcionSubmenu();
            switch(opcion){
                case 1:
                    this.listarProducto(trxCategoria);
                    break;
                case 2:
                    this.crearProducto(trxCategoria.getCategorias());
                    break;
                case 3:
                    this.editarProducto(trxCategoria.getCategorias());
                    break;
                case 4:
                    this.eliminarProducto(pedidos);
                    break;
            }
        } while(opcion != 5);
    }

    private void listarProducto(TransaccionCategoria trxCategoria){
        String respuesta;
        boolean valido;
        do {
            System.out.print("¿Desea filtrar por categoria? (S/N): ");
            respuesta = consola.nextLine().trim().toUpperCase();
            valido = respuesta.equals("S") || respuesta.equals("N");
            if(!valido){
                System.out.println("Opcion invalida. Ingrese nuevamente");
            }
        } while(!valido);
        if(respuesta.equals("S")){
            trxCategoria.listar();
            if(!trxCategoria.getCategorias().isEmpty()){
                String categoria = ValidacionInput.ingresarValidarInputString("Categoria", "Ingrese el nombre de la categoria de productos que quiere listar: ", consola);
                transaccion.listarPorCategoria(categoria);
            }
        } else {
            transaccion.listarGeneral();
        }
    }


    private void crearProducto(List<Categoria> categorias){
        try{
            long id = ValidacionInput.ingresarValidarInputLong("ID", "Ingrese el ID de la categoria del nuevo producto: ", consola);
            Categoria categoria = validarCategoria(id,categorias);
            String nombre = ValidacionInput.ingresarValidarInputString("Nombre", "Ingrese el nombre del nuevo producto: ", consola);
            String descripcion = ValidacionInput.ingresarValidarInputString("Descripcion", "Ingrese la descripcion del nuevo producto: ", consola);
            String imagen = ValidacionInput.ingresarValidarInputString("Imagen", "Ingrese el archivo imagen del nuevo producto: ", consola);
            double precio = ValidacionInput.ingresarValidarInputDouble("Precio","Ingrese el precio del nuevo producto: ",consola);
            int stock = ValidacionInput.ingresarValidarInputInt("Stock", "Ingrese el stock del nuevo producto: ", consola);
            boolean disponible = ingresarDisponible("diponible", "¿El nuevo producto estara disponible? (S/N): ", consola);
            transaccion.crear(nombre, descripcion, precio, stock, imagen, disponible, categoria);
        } catch (EntidadNoEncontradaException enee){
            System.out.println("Error: " + enee.getMessage());
        }
    }

    private void editarProducto(List<Categoria> categorias){
        try{
            System.out.println("\nPRODUCTOS EXISTENTES:");
            transaccion.listarGeneral();
            this.existenProductos("editarlos");
            long id = ValidacionInput.ingresarValidarInputLong("ID", "Ingrese el ID del produccto a editar: ", consola);
            Categoria categoria = this.actualizarCategoriaDeProducto(categorias);
            double precio = actualizarPrecioDeProducto();
            int stock = actualizarStockDeProducto();
            transaccion.editar(id,precio, stock, categoria);
        } catch (EntidadNoEncontradaException enee){
            System.out.println("Error: " + enee.getMessage());
        }
    }

    private void eliminarProducto(List<Pedido> pedidos){
        try{
            System.out.println("\nPRODUCTOS EXISTENTES:");
            transaccion.listarGeneral();
            this.existenProductos("eliminarlos");
            long id = ValidacionInput.ingresarValidarInputLong("ID", "Ingrese el ID a eliminar: ", consola);
            System.out.print("Seleccione 'S' para confirmar la operacion: ");
            String confirmacion = consola.nextLine().trim();
            if(confirmacion.equalsIgnoreCase("S")){
                transaccion.eliminar(id,pedidos);
            } else {
                System.out.println("¡Operacion no confirmada. Producto con id #"+id+" no eliminado!");
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
        System.out.println("\n===== SUBMENU PRODUCTOS =====");
        System.out.println("1. Listar");
        System.out.println("2. Crear");
        System.out.println("3. Editar");
        System.out.println("4. Eliminar");
        System.out.println("5. Menu principal");
        System.out.print("Seleccione: ");
    }

    public Categoria validarCategoria(long id,List<Categoria> categorias){
        if (categorias != null && !categorias.isEmpty()){
            Categoria categoriaEncontrada = buscarCategoriaParaProducto(id,categorias);
            if (categoriaEncontrada != null){
                return categoriaEncontrada;
            } else{
                throw new EntidadNoEncontradaException("La categoria con id: #"+id+" no existe en el listado de categorias");
            }
        } else {
            throw new EntidadNoEncontradaException("No hay categorias creadas para asignarle a los productos");
        }
    }

    public Categoria buscarCategoriaParaProducto(long id,List<Categoria> categorias){
        int i = 0;
        Categoria categoriaEncontrada = null;
        while(i < categorias.size() && categorias.get(i).getId() != id){
            i++;
        }
        if (i < categorias.size() && !categorias.get(i).isEliminado()){
            categoriaEncontrada = categorias.get(i);
        }
        return  categoriaEncontrada;
    }

    public boolean ingresarDisponible(String mensaje, String mensajeLargo, Scanner pantalla){
        String respuesta;
        do {
            System.out.print(mensajeLargo);
            respuesta = pantalla.nextLine().trim().toUpperCase();
            if(respuesta.equals("S")){
                return true;
            }
            if(respuesta.equals("N")){
                return false;
            }
            System.out.println(mensaje + " invalido. Vuelva a ingresarlo por favor");
        } while(true);
    }

    public Categoria actualizarCategoriaDeProducto(List<Categoria> categorias){
        String respuesta;
        boolean valido;
        do {
            System.out.print("¿Quiere editar la categoria del producto? (S/N): ");
            respuesta = consola.nextLine().trim().toUpperCase();
            valido = respuesta.equals("S") || respuesta.equals("N");
            if(!valido){
                System.out.println("Opcion invalida. Ingrese nuevamente");
            }
        } while(!valido);
        if(respuesta.equals("S")){
            long id = ValidacionInput.ingresarValidarInputLong("ID",
                    "Ingrese el ID da la nueva categoria que quiere asignarle al producto: ", consola);

            return this.validarCategoria(id,categorias);
        } else {
            return null;
        }
    }

    public double actualizarPrecioDeProducto(){
        String respuesta;
        boolean valido;
        do {
            System.out.print("¿Quiere editar el precio del producto? (S/N): ");
            respuesta = consola.nextLine().trim().toUpperCase();
            valido = respuesta.equals("S") || respuesta.equals("N");
            if(!valido){
                System.out.println("Opcion invalida. Ingrese nuevamente");
            }
        } while(!valido);
        if(respuesta.equals("S")){
            return ValidacionInput.ingresarValidarInputDouble("Precio","Ingrese el nuevo precio del producto: ",consola);
        } else {
            return -1;
        }
    }

    public int actualizarStockDeProducto(){
        String respuesta;
        boolean valido;
        do {
            System.out.print("¿Quiere editar el stock del producto? (S/N): ");
            respuesta = consola.nextLine().trim().toUpperCase();
            valido = respuesta.equals("S") || respuesta.equals("N");
            if(!valido){
                System.out.println("Opcion invalida. Ingrese nuevamente");
            }
        } while(!valido);
        if(respuesta.equals("S")){
            return ValidacionInput.ingresarValidarInputInt("stock","Ingrese el nuevo stock del producto: ",consola);
        } else {
            return -1;
        }
    }

    public void existenProductos(String accion){
        boolean listaVacia = true;
        if (this.transaccion.getProductos() != null && !this.transaccion.getProductos().isEmpty()){
            for(Producto producto : this.transaccion.getProductos()){
                if (!producto.isEliminado()) {
                    listaVacia = false;
                    break;
                }
            }
        }
        if (listaVacia){
            throw new EntidadNoEncontradaException("Tienen que haber productos para poder "+accion);
        }
    }
}
