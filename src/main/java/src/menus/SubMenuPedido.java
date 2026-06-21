package src.menus;

import src.entities.DetallePedido;
import src.entities.Pedido;
import src.entities.Producto;
import src.entities.Usuario;
import src.enums.Estado;
import src.enums.FormaPago;
import src.exception.EntidadNoEncontradaException;
import src.exception.StockInvalidoException;
import src.transacciones.TransaccionPedido;
import src.transacciones.TransaccionProducto;
import src.transacciones.TransaccionUsuario;
import src.validaciones.ValidacionInput;
import java.util.Scanner;

public class SubMenuPedido {

    private final Scanner consola;
    private final TransaccionPedido transaccion;

    public SubMenuPedido(Scanner consola, TransaccionPedido transaccion) {
        this.consola = consola;
        this.transaccion = transaccion;
    }

    public TransaccionPedido getTransaccion() {
        return transaccion;
    }

    public void iniciar(TransaccionUsuario trxUsuarios, TransaccionProducto trxProductos) {
        int opcion;
        do {
            opcion = leerOpcionSubmenu();
            switch (opcion) {
                case 1:
                    this.listarPedido(trxUsuarios);
                    break;
                case 2:
                    this.crearPedido(trxUsuarios, trxProductos);
                    break;
                case 3:
                    this.editarPedido();
                    break;
                case 4:
                    this.eliminarPedido();
                    break;
            }
        } while (opcion != 5);
    }

    private void listarPedido(TransaccionUsuario trxUsuarios) {
        String respuesta;
        boolean valido;
        do {
            System.out.print("¿Desea filtrar por usuario? (S/N): ");
            respuesta = consola.nextLine().trim().toUpperCase();
            valido = respuesta.equals("S") || respuesta.equals("N");
            if (!valido) {
                System.out.println("Opcion invalida. Ingrese nuevamente");
            }
        } while (!valido);
        if (respuesta.equals("S")) {
            System.out.println("\nUSUARIOS EXISTENTES:");
            trxUsuarios.listar();
            if(!trxUsuarios.getUsuarios().isEmpty()){
                long idUsuario = ValidacionInput.ingresarValidarInputLong("ID", "Ingrese el ID del usuario: ", consola);
                transaccion.listarPorUsuario(idUsuario);
            }
        } else {
            transaccion.listar();
        }
    }

    private void crearPedido(TransaccionUsuario trxUsuarios, TransaccionProducto trxProductos) {
        try {
            System.out.println("\nUSUARIOS EXISTENTES:");
            this.listarUsuarios(trxUsuarios);
            long idUsuario = ValidacionInput.ingresarValidarInputLong("ID", "Ingrese el ID del usuario del nuevo pedido: ", consola);
            Usuario usuario = validarUsuario(idUsuario, trxUsuarios);
            this.validarExistenciaProductos(trxProductos);
            Estado estado = seleccionarEstado();
            FormaPago formaPago = seleccionarFormaPago();
            Pedido nuevoPedido = transaccion.crear(estado, formaPago, usuario);
            if (nuevoPedido != null){
                this.agregarDetallePedido(nuevoPedido,trxProductos);
            }
        } catch (EntidadNoEncontradaException | StockInvalidoException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void listarUsuarios(TransaccionUsuario trxUsuarios){
        trxUsuarios.listar();
        if (trxUsuarios.getUsuarios() == null || trxUsuarios.getUsuarios().isEmpty()){
            throw new EntidadNoEncontradaException("Para crear un pedido primero dé de alta algun usuario");
        }
    }

    public void agregarDetallePedido(Pedido pedido, TransaccionProducto trxProductos) {
        String respuesta = "";
        do {
            boolean detalleAgregado = false;
            try {
                System.out.println("\nPRODUCTOS EXISTENTES:");
                this.listarProductos(trxProductos);
                long idProducto = ValidacionInput.ingresarValidarInputLong("ID", "Ingrese el ID del producto a agregar al nuevo pedido: ", consola);
                Producto producto = validarProducto(idProducto, trxProductos);
                int cantidad = ValidacionInput.ingresarValidarInputInt("Cantidad", "Ingrese la cantidad: ", consola);
                this.validarStockProducto(producto, cantidad);
                transaccion.agregarDetalle(pedido.getId(), cantidad, producto);
                detalleAgregado = true;
            } catch (StockInvalidoException | EntidadNoEncontradaException e) {
                System.out.println(e.getMessage());
                respuesta = manejarErrorDetalle(pedido);
            }
            if (detalleAgregado) {
                respuesta = preguntarAgregarOtroProducto();
            }
        } while (respuesta.equals("S"));
        this.validarPedidoCreado(pedido);
    }

    private String manejarErrorDetalle(Pedido pedido) {
        int opcion;
        do {
            System.out.println("¿Que desea hacer?\n1) Intentar agregar otro detalle\n2) Terminar pedido\n3) Cancelar pedido");
            opcion = ValidacionInput.ingresarValidarInputInt("Opcion", "Ingrese opcion: ", consola);
            switch (opcion) {
                case 1:
                    return "S";
                case 2:
                    return "N";
                case 3:
                    this.reponerStockPedidoCancelado(pedido);
                    this.transaccion.eliminar(pedido.getId());
                    return "N";
                default:
                    System.out.println("Respuesta invalida");
            }
        } while (true);
    }

    private String preguntarAgregarOtroProducto() {
        String respuesta;
        boolean valido;
        do {
            System.out.print("¿Desea agregar otro producto al pedido? (S/N): ");
            respuesta = consola.nextLine().trim().toUpperCase();
            valido = respuesta.equals("S") || respuesta.equals("N");
            if (!valido) {
                System.out.println("Opcion invalida. Ingrese nuevamente");
            }
        } while (!valido);
        return respuesta;
    }

    public void listarProductos(TransaccionProducto trxProductos){
        trxProductos.listarGeneral();
        if (trxProductos.getProductos() == null || trxProductos.getProductos().isEmpty()){
            throw new EntidadNoEncontradaException("Primero dé de alta algun Producto");
        }
    }

    public void validarPedidoCreado(Pedido pedido){
        if(!pedido.isEliminado() && pedido.getDetalles().isEmpty()){
            System.out.println("El pedido creado no tiene detalles asociados. Se eliminara para mantenter integridad");
            this.transaccion.eliminar(pedido.getId());
        }
        this.mostrarPedidoCreado(pedido);
    }

    public void mostrarPedidoCreado(Pedido pedido){
        if(!pedido.isEliminado() && !pedido.getDetalles().isEmpty()){
            System.out.println("\nResumen del pedido creado:");
            System.out.println(pedido.toString());
            pedido.listarDetalles();
        }
    }

    public void reponerStockPedidoCancelado(Pedido pedido){
        if(pedido.getDetalles() != null && !pedido.getDetalles().isEmpty()){
            for(DetallePedido detalle : pedido.getDetalles()){
                detalle.getProducto().reponerStock(detalle.getCantidad());
            }
        }
    }

    public void validarExistenciaProductos(TransaccionProducto trxProductos){
        boolean listaVacia = true;
        if (trxProductos.getProductos() != null && !trxProductos.getProductos().isEmpty()){
            for(Producto producto : trxProductos.getProductos()){
                if (!producto.isEliminado()) {
                    listaVacia = false;
                    break;
                }
            }
        }
        if (listaVacia){
            System.out.println("\nPRODUCTOS EXISTENTES:");
            throw new EntidadNoEncontradaException("No hay Productos cargados. No se puede crear pedidos si no existen productos");
        }
    }

    public void validarStockProducto(Producto producto, int cantidad){
        if(cantidad > producto.getStock()){
            throw new StockInvalidoException(String.format("ERROR: El producto %s tiene un stock insuficiente. " +
                    "\nERROR: Usted quiso comprar %d unidades, pero el stock actual es: %d",producto.getNombre(),cantidad,producto.getStock()));
        }
    }

    private void editarPedido() {
        try {
            System.out.println("\nPEDIDOS EXISTENTES:");
            transaccion.listar();
            this.existenPedidos("editarlos");
            long id = ValidacionInput.ingresarValidarInputLong("ID", "Ingrese el ID del pedido a editar: ", consola);
            Estado estado = actualizarEstadoDePedido();
            FormaPago formaPago = actualizarFormaPagoDePedido();
            transaccion.actualizarEstadoYFormaPago(id, estado, formaPago);
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarPedido() {
        try {
            System.out.println("\nPEDIDOS EXISTENTES:");
            transaccion.listar();
            this.existenPedidos("eliminarlos");
            long id = ValidacionInput.ingresarValidarInputLong("ID", "Ingrese el ID a eliminar: ", consola);
            System.out.print("Seleccione 'S' para confirmar la operacion: ");
            String confirmacion = consola.nextLine().trim();
            if (confirmacion.equalsIgnoreCase("S")) {
                transaccion.eliminar(id);
            } else {
                System.out.println("\nATENCION: ¡Operacion no confirmada. Pedido con id #" + id + " no eliminado!");
            }
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Usuario validarUsuario(long id, TransaccionUsuario trxUsuarios) {
        if (trxUsuarios.getUsuarios() != null && !trxUsuarios.getUsuarios().isEmpty()) {
            Usuario usuarioEncontrado = trxUsuarios.buscar(id);
            if (usuarioEncontrado != null){
                return usuarioEncontrado;
            } else{
                throw new EntidadNoEncontradaException("\n ERROR: El usuario con id: #" + id + " no existe o fue eliminado");
            }
        } else{
            throw new EntidadNoEncontradaException("\n ERROR: No hay usuarios creados para asignarle al pedido");
        }
    }

    public Producto validarProducto(long id, TransaccionProducto trxProductos) {
        if (trxProductos.getProductos() != null && !trxProductos.getProductos().isEmpty()) {
            Producto productoEncontrado = trxProductos.buscar(id);
            if (productoEncontrado != null){
                return productoEncontrado;
            } else{
                throw new EntidadNoEncontradaException("\nERROR: El producto con id: #" + id + " no existe o fue eliminado");
            }
        } else{
            throw new EntidadNoEncontradaException("\nERROR: No hay productos creados para asignarle al pedido");
        }
    }

    public Estado seleccionarEstado() {
        Estado[] estados = Estado.values();
        boolean valido;
        int opcion = 0;
        do {
            System.out.println("Seleccione el estado del pedido");
            for (int i = 0; i < estados.length; i++) {
                System.out.println((i + 1) + ") " + estados[i]);
            }
            System.out.print("Seleccione la opcion: ");
            String input = consola.nextLine().trim();
            try {
                opcion = Integer.parseInt(input);
                valido = opcion >= 1 && opcion <= estados.length;
                if (!valido) {
                    System.out.println(String.format("La opcion numero %d esta fuera de rango",opcion));
                }
            } catch (NumberFormatException nfe) {
                valido = false;
                System.out.println("\"" + input + "\" no es una opcion valida");
            }
        } while (!valido);
        return estados[opcion - 1];
    }

    public FormaPago seleccionarFormaPago() {
        FormaPago[] formasPago = FormaPago.values();
        boolean valido;
        int opcion = 0;
        do {
            System.out.println("Seleccione la forma de pago del pedido:");
            for (int i = 0; i < formasPago.length; i++) {
                System.out.println((i + 1) + ") " + formasPago[i]);
            }
            System.out.print("Seleccione la opcion: ");
            String input = consola.nextLine().trim();
            try {
                opcion = Integer.parseInt(input);
                valido = opcion >= 1 && opcion <= formasPago.length;
                if (!valido) {
                    System.out.println(String.format("La opcion numero %d esta fuera de rango",opcion));
                }
            } catch (NumberFormatException nfe) {
                valido = false;
                System.out.println("\"" + input + "\" no es una opcion valida");
            }
        } while (!valido);
        return formasPago[opcion - 1];
    }

    public Estado actualizarEstadoDePedido() {
        String respuesta = this.confirmacionParaEditar("el estado");
        if (respuesta.equals("S")) {
            return seleccionarEstado();
        } else {
            return null;
        }
    }

    public FormaPago actualizarFormaPagoDePedido() {
        String respuesta = this.confirmacionParaEditar("la forma de pago");
        if (respuesta.equals("S")) {
            return seleccionarFormaPago();
        } else {
            return null;
        }
    }

    public String confirmacionParaEditar(String mensaje) {
        String respuesta;
        boolean valido;
        do {
            System.out.print("¿Quiere editar " + mensaje + " del pedido? (S/N): ");
            respuesta = consola.nextLine().trim().toUpperCase();
            valido = respuesta.equals("S") || respuesta.equals("N");
            if (!valido) {
                System.out.println("Opcion invalida. Ingrese nuevamente");
            }
        } while (!valido);
        return respuesta;
    }

    public void existenPedidos(String accion){
        boolean listaVacia = true;
        if (this.transaccion.getPedidos() != null && !this.transaccion.getPedidos().isEmpty()){
            for(Pedido pedido : this.transaccion.getPedidos()){
                if (!pedido.isEliminado()) {
                    listaVacia = false;
                    break;
                }
            }
        }
        if (listaVacia){
            throw new EntidadNoEncontradaException("Tienen que haber pedidos para poder "+accion);
        }
    }

    private int leerOpcionSubmenu() {
        boolean opcionSubMenuInvalida = true;
        String opcionSubMenuString = "";
        int opcionSubMenu = 0;

        while (opcionSubMenuInvalida) {
            boolean opcionEsEntero = false;
            this.mostrarSubMenu();
            opcionSubMenu = 0;
            try {
                opcionSubMenuString = consola.nextLine();
                opcionSubMenu = Integer.parseInt(opcionSubMenuString.trim());
                opcionEsEntero = true;
            } catch (NumberFormatException nfe) {
                System.out.println("Error: " + nfe.getMessage());
            }
            if (opcionSubMenu >= 1 && opcionSubMenu <= 5) {
                opcionSubMenuInvalida = false;
            } else {
                String mensajeTernario = opcionEsEntero ? String.format("La opcion numero %d esta fuera de rango", opcionSubMenu) :
                        String.format(" El caracter \"%s\" no es un valido", opcionSubMenuString);
                System.out.println("Por favor ingresar una opcion valida." + mensajeTernario);
            }
        }
        return opcionSubMenu;
    }

    private void mostrarSubMenu() {
        System.out.println("\n===== SUBMENU PEDIDOS =====");
        System.out.println("1. Listar");
        System.out.println("2. Crear");
        System.out.println("3. Editar");
        System.out.println("4. Eliminar");
        System.out.println("5. Menu principal");
        System.out.print("Seleccione: ");
    }
}

