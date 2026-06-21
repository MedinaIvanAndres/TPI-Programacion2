
package src;

import src.exception.EntidadNoEncontradaException;
import src.exception.NombreCategoriaRepetidoException;
import src.menus.MenuPrincipal;
import src.transacciones.TransaccionCategoria;
import java.util.Scanner;

/**
 *
 * @author Ivan Andres Medina
 */
public class Main {

    public static void main(String[] args) {

        MenuPrincipal menuPrincipal = new MenuPrincipal();
        menuPrincipal.iniciar();

        System.out.println("Ejecucion finalizada. Gracias por usar nuestro sistema!!!");
    }
}
