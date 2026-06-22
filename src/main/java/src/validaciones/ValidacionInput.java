package src.validaciones;

import java.util.Scanner;

public class ValidacionInput {

    public static String ingresarValidarInputString(String mensaje, String mensajeLargo, Scanner pantalla){
        String texto;
        boolean valido;
        do {
            System.out.print(mensajeLargo);
            texto = pantalla.nextLine().toUpperCase().trim();
            valido = validarInputString(texto);
            if(!valido){
                System.out.println(mensaje+" invalido. Vuelva a ingresarlo por favor");
            }
        } while (!valido);
        return texto;
    }

    public static boolean validarInputString(String texto){
        return texto != null && !texto.trim().isEmpty() && !texto.trim().matches("\\d+");
    }

    public static String ingresarValidarInputAlfanumerico(String mensaje, String mensajeLargo, Scanner pantalla){
        String texto;
        boolean valido;
        do {
            System.out.print(mensajeLargo);
            texto = pantalla.nextLine().toUpperCase();
            valido = validarInputAlfanumerico(texto);
            if(!valido){
                System.out.println(mensaje+" invalido. Vuelva a ingresarlo por favor");
            }
        } while (!valido);
        return texto;
    }

    public static boolean validarInputAlfanumerico(String texto){
        return texto != null && !texto.trim().isEmpty();
    }

    public static long ingresarValidarInputLong(String mensaje,String mensajeLargo,Scanner pantalla){
        long numero = 0;
        boolean valido;
        do {
            System.out.print(mensajeLargo);
            String input = pantalla.nextLine().trim();
            try {
                numero = Long.parseLong(input);
                valido = numero >= 1;
                if (!valido) {
                    System.out.println(mensaje+" invalido. Vuelva a ingresarlo por favor");
                }
            } catch (NumberFormatException e) {
                valido = false;
                System.out.println("Error: "+e.getMessage());
            }
        } while (!valido);
        return numero;
    }

    public static double ingresarValidarInputDouble(String mensaje, String mensajeLargo, Scanner pantalla) {
        double numero = 0;
        boolean valido;
        do {
            System.out.print(mensajeLargo);
            String input = pantalla.nextLine().trim();
            input = input.replace(',', '.');
            try {
                numero = Double.parseDouble(input);
                valido = numero >= 0;
                if (!valido) {
                    System.out.println(mensaje + " invalido. Debe ser positivo");
                }
            } catch (NumberFormatException e) {
                valido = false;
                System.out.println("\"" + input + "\" no es un numero valido.");
            }
        } while (!valido);
        return numero;
    }

    public static int ingresarValidarInputInt(String mensaje, String mensajeLargo, Scanner pantalla){
        int numero = 0;
        boolean valido;
        do {
            System.out.print(mensajeLargo);
            String input = pantalla.nextLine().trim();
            try {
                numero = Integer.parseInt(input);
                valido = numero >= 0;
                if(!valido){
                    System.out.println(mensaje + " invalido. No puede ser negativo");
                }
            } catch(NumberFormatException e){
                valido = false;
                System.out.println("\"" + input + "\" no es un numero entero valido.");
            }
        } while(!valido);
        return numero;
    }

    public static int ingresarValidarInputIntPositivo(String mensaje, String mensajeLargo, Scanner pantalla){
        int numero = 0;
        boolean valido;
        do {
            System.out.print(mensajeLargo);
            String input = pantalla.nextLine().trim();
            try {
                numero = Integer.parseInt(input);
                valido = numero > 0;
                if(!valido){
                    System.out.println(mensaje + " invalido. Debe ser positivo");
                }
            } catch(NumberFormatException e){
                valido = false;
                System.out.println("\"" + input + "\" no es un numero entero valido.");
            }
        } while(!valido);
        return numero;
    }
}
