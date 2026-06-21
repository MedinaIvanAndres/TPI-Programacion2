package src.menus;


import java.util.Scanner;
import src.transacciones.TransaccionCategoria;
import src.transacciones.TransaccionPedido;
import src.transacciones.TransaccionProducto;
import src.transacciones.TransaccionUsuario;

public class MenuPrincipal {

    private final Scanner consola;
    private final SubMenuCategoria subMenuCategoria;
    private final SubMenuProducto subMenuProducto;
    private final SubMenuUsuario subMenuUsuario;
    private final SubMenuPedido subMenuPedido;

    public MenuPrincipal() {
        this.consola = new Scanner(System.in);
        TransaccionCategoria transaccionCategoria = new TransaccionCategoria();
        TransaccionProducto transaccionProducto = new TransaccionProducto();
        TransaccionUsuario transaccionUsuario = new TransaccionUsuario();
        TransaccionPedido transaccionPedido = new TransaccionPedido();
        this.subMenuPedido = new SubMenuPedido(consola, transaccionPedido);
        this.subMenuCategoria = new SubMenuCategoria(consola, transaccionCategoria);
        this.subMenuProducto = new SubMenuProducto(consola, transaccionProducto);
        this.subMenuUsuario = new SubMenuUsuario(consola, transaccionUsuario);
    }

    public void iniciar() {
        int opcionMenu;
        do {
            opcionMenu = leerOpcionMenuPrincipal();
            switch (opcionMenu) {
                case 1:
                    subMenuCategoria.iniciar();
                    break;
                case 2:
                    subMenuProducto.iniciar(subMenuCategoria.getTransaccion(),subMenuPedido.getTransaccion().getPedidos());
                    break;
                case 3:
                    subMenuUsuario.iniciar();
                    break;
                case 4:
                    subMenuPedido.iniciar(subMenuUsuario.getTransaccion(),subMenuProducto.getTransaccion());
                    break;
            }
        } while (opcionMenu != 0);
    }

    private int leerOpcionMenuPrincipal() {
        int opcion = 5;
        boolean opcionMenuInvalida = true;
        String opcionMenuString = "";

        while (opcionMenuInvalida) {
            boolean opcionEsEntero = false;
            opcion = 5;
            this.mostrarMenu();
            try {
                opcionMenuString = consola.nextLine();
                opcion = Integer.parseInt(opcionMenuString.trim());
                opcionEsEntero = true;
            } catch (NumberFormatException nfe) {
                System.out.println("Error: " + nfe.getMessage());
            }
            if (opcion >= 0 && opcion <= 4) {
                opcionMenuInvalida = false;
            } else {
                String mensajeTernario = opcionEsEntero ? String.format("La opcion numero %d esta fuera de rango", opcion) :
                        String.format(" El caracter \"%s\" no es un valido", opcionMenuString);
                System.out.println("Por favor ingresar una opcion valida." + mensajeTernario);
            }
        }
        return opcion;
    }

    private void mostrarMenu() {
        System.out.println("\n========== SISTEMA DE PEDIDOS (FOOD STORE) ==========");
        System.out.println("1. Categorias");
        System.out.println("2. Productos");
        System.out.println("3. Usuarios");
        System.out.println("4. Pedidos");
        System.out.println("0. Salir");
        System.out.print("Seleccione: ");
    }
}
