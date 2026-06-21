package src.menus;

import java.util.Scanner;

import src.entities.Pedido;
import src.entities.Usuario;
import src.enums.Rol;
import src.exception.EntidadNoEncontradaException;
import src.exception.MailDeUsuarioRepetidoException;
import src.transacciones.TransaccionUsuario;
import src.validaciones.ValidacionInput;

public class SubMenuUsuario {

    private final Scanner consola;
    private final TransaccionUsuario transaccion;

    public SubMenuUsuario(Scanner consola, TransaccionUsuario transaccion) {
        this.consola = consola;
        this.transaccion = transaccion;
    }

    public TransaccionUsuario getTransaccion() {
        return transaccion;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = leerOpcionSubmenu();
            switch (opcion) {
                case 1:
                    transaccion.listar();
                    break;
                case 2:
                    this.crearUsuario();
                    break;
                case 3:
                    this.editarUsuario();
                    break;
                case 4:
                    this.eliminarUsuario();
                    break;
            }
        } while (opcion != 5);
    }

    private void crearUsuario() {
        boolean mailValido = false;
        String nombre = ValidacionInput.ingresarValidarInputString("Nombre", "Ingrese el nombre del nuevo usuario: ", consola);
        String apellido = ValidacionInput.ingresarValidarInputString("Apellido", "Ingrese el apellido del nuevo usuario: ", consola);
        String celular = validarCelular();
        String contrasenia = ValidacionInput.ingresarValidarInputAlfanumerico("Contrasena", "Ingrese la contrasena del nuevo usuario: ", consola);
        Rol rol = seleccionarRol();
        while(!mailValido){
            try {
                String mail = validarMail();
                transaccion.crear(nombre, apellido, mail, celular, contrasenia, rol);
                mailValido = true;
            } catch (MailDeUsuarioRepetidoException mdure) {
                System.out.println("Error: " + mdure.getMessage());
            }
        }
    }

    private void editarUsuario() {
        try {
            System.out.println("\nUSUARIOS EXISTENTES:");
            transaccion.listar();
            this.existenUsuarios("editarlos");
            long id = ValidacionInput.ingresarValidarInputLong("ID", "Ingrese el ID del usuario a editar: ", consola);
            String nombre = this.actualizarNombreDeUsuario();
            String apellido = this.actualizarApellidoDeUsuario();
            String mail = this.actualizarMailDeUsuario();
            String celular = this.actualizarCelularDeUsuario();
            String contrasenia = this.actualizarContraseniaDeUsuario();
            Rol rol = this.actualizarRolDeUsuario();
            transaccion.editar(id, nombre, apellido, mail, celular, contrasenia, rol);
        } catch (EntidadNoEncontradaException | MailDeUsuarioRepetidoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarUsuario() {
        try {
            System.out.println("\nUSUARIOS EXISTENTES:");
            transaccion.listar();
            this.existenUsuarios("eliminarlos");
            long id = ValidacionInput.ingresarValidarInputLong("ID", "Ingrese el ID a eliminar: ", consola);
            System.out.print("Seleccione 'S' para confirmar la operacion: ");
            String confirmacion = consola.nextLine().trim();
            if (confirmacion.equalsIgnoreCase("S")) {
                transaccion.eliminar(id);
            } else {
                System.out.println("¡Operacion no confirmada. Usuario con id #" + id + " no eliminado!");
            }
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private String validarMail() {
        String mail;
        boolean valido;
        do {
            mail = ValidacionInput.ingresarValidarInputString("Mail", "Ingrese el mail del nuevo usuario: ", consola);
            valido = mail.contains("@");
            if (!valido) {
                System.out.println("Mail invalido. Debe contener '@'");
            }
        } while (!valido);
        return mail;
    }

    public String confirmacionParaEditar(String mensaje) {
        String respuesta;
        boolean valido;
        do {
            System.out.print("¿Quiere editar " + mensaje + " del usuario? (S/N): ");
            respuesta = consola.nextLine().trim().toUpperCase();
            valido = respuesta.equals("S") || respuesta.equals("N");
            if (!valido) {
                System.out.println("Opcion invalida. Ingrese nuevamente");
            }
        } while (!valido);
        return respuesta;
    }

    public String actualizarNombreDeUsuario() {
        String respuesta = this.confirmacionParaEditar("el nombre");
        if (respuesta.equals("S")) {
            return ValidacionInput.ingresarValidarInputString("Nombre", "Ingrese el nuevo nombre: ", consola);
        } else {
            return null;
        }
    }

    public String actualizarApellidoDeUsuario() {
        String respuesta = this.confirmacionParaEditar("el apellido");
        if (respuesta.equals("S")) {
            return ValidacionInput.ingresarValidarInputString("Apellido", "Ingrese el nuevo apellido: ", consola);
        } else {
            return null;
        }
    }

    public String actualizarMailDeUsuario() {
        String respuesta = this.confirmacionParaEditar("el mail");
        if (respuesta.equals("S")) {
            return validarMail();
        } else {
            return null;
        }
    }

    public String actualizarCelularDeUsuario() {
        String respuesta = this.confirmacionParaEditar("el celular");
        if (respuesta.equals("S")) {
            return validarCelular();
        } else {
            return null;
        }
    }

    public String actualizarContraseniaDeUsuario() {
        String respuesta = this.confirmacionParaEditar("la contrasena");
        if (respuesta.equals("S")) {
            return ValidacionInput.ingresarValidarInputAlfanumerico("Contrasena", "Ingrese la nueva contrasena: ", consola);
        } else {
            return null;
        }
    }

    private String validarCelular() {
        String celular;
        boolean valido;
        do {
            celular = ValidacionInput.ingresarValidarInputAlfanumerico("Celular", "Ingrese el celular del nuevo usuario: ", consola);
            valido = celular.matches("\\d+");
            if (!valido) {
                System.out.println("Celular invalido. Debe contener solo numeros");
            }
        } while (!valido);
        return celular;
    }

    public Rol actualizarRolDeUsuario() {
        String respuesta = this.confirmacionParaEditar("el rol");
        if (respuesta.equals("S")) {
            return seleccionarRol();
        } else {
            return null;
        }
    }

    private Rol seleccionarRol() {
        Rol[] roles = Rol.values();
        boolean valido;
        int opcion = 0;
        do {
            System.out.println("Seleccione el rol del usuario:");
            for (int i = 0; i < roles.length; i++) {
                System.out.println((i + 1) + ") " + roles[i]);
            }
            System.out.print("Seleccione la opcion: ");
            String input = consola.nextLine().trim();
            try {
                opcion = Integer.parseInt(input);
                valido = opcion >= 1 && opcion <= roles.length;
                if (!valido) {
                    System.out.println(String.format("La opcion numero %d esta fuera de rango",opcion));
                }
            } catch (NumberFormatException nfe) {
                valido = false;
                System.out.println("\"" + input + "\" no es una opcion valida.");
            }
        } while (!valido);
        return roles[opcion - 1];
    }

    public void existenUsuarios(String accion){
        boolean listaVacia = true;
        if (this.transaccion.getUsuarios() != null && !this.transaccion.getUsuarios().isEmpty()){
            for(Usuario usuario : this.transaccion.getUsuarios()){
                if (!usuario.isEliminado()) {
                    listaVacia = false;
                    break;
                }
            }
        }
        if (listaVacia){
            throw new EntidadNoEncontradaException("Tienen que haber usuarios para poder "+accion);
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
        System.out.println("\n===== SUBMENU USUARIOS =====");
        System.out.println("1. Listar");
        System.out.println("2. Crear");
        System.out.println("3. Editar");
        System.out.println("4. Eliminar");
        System.out.println("5. Menu principal");
        System.out.print("Seleccione: ");
    }
}
