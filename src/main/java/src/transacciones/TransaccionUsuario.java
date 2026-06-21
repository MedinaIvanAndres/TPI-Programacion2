package src.transacciones;

import src.entities.Usuario;
import src.enums.Rol;
import src.exception.EntidadNoEncontradaException;
import src.exception.MailDeUsuarioRepetidoException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransaccionUsuario {
    private List<Usuario> usuarios = new ArrayList<>();

    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }



    public void listar() {
        boolean listaVacia = true;
        for (Usuario usuario : usuarios) {
            if (!usuario.isEliminado()) {
                listaVacia = false;
                System.out.println(usuario.toString());
            }
        }
        if (listaVacia) {
            System.out.println("\nATENCION: No hay usuarios cargados en el sistema");
        }
    }

    public void crear(String nombre, String apellido, String mail, String celular, String contrasenia, Rol rol) {
        for (Usuario usuario : usuarios) {
            if (!usuario.isEliminado() && usuario.getMail().equalsIgnoreCase(mail)) {
                throw new MailDeUsuarioRepetidoException("\nATENCION: Ya existe un usuario con el mail: " + mail);
            }
        }
        try {
            Usuario nuevoUsuario = new Usuario(nombre, apellido, mail, celular, contrasenia, rol);
            usuarios.add(nuevoUsuario);
            System.out.println("¡¡¡Usuario con id: #" + nuevoUsuario.getId() + " creado exitosamente!!!");
        } catch (IllegalArgumentException iae) {
            System.out.println(iae.getMessage());
        }
    }

    public void editar(long id, String nombre, String apellido, String mail, String celular, String contrasenia, Rol rol) {
        Usuario usuarioParaEditar = this.buscar(id);
        if (usuarioParaEditar == null) {
            throw new EntidadNoEncontradaException(String.format("\nATENCION: Usuario con id #%d no encontrado.", id));
        } else {
            setterAtributos(usuarioParaEditar,id,nombre,apellido,mail,celular,contrasenia,rol);
        }
    }

    public void setterAtributos(Usuario usuario,long id,String nombre, String apellido, String mail, String celular, String contrasenia, Rol rol){
        try {
            if (mail != null && !mail.equalsIgnoreCase(usuario.getMail())) {
                for (Usuario usu : usuarios) {
                    if (!usu.isEliminado() && usu.getMail().equalsIgnoreCase(mail) && usu.getId() != id ) {
                        throw new MailDeUsuarioRepetidoException("\nATENCION: Ya existe un usuario con el mail: " + mail);
                    }
                }
                usuario.setMail(mail);
            } else{
                System.out.println(mail != null ? "\nATENCION:El usuario ya tiene cargado ese mail" : "");
                mail = null;
            }
            if (nombre != null) {
                usuario.setNombre(nombre);
            }
            if (apellido != null) {
                usuario.setApellido(apellido);
            }
            if (celular != null) {
                usuario.setCelular(celular);
            }
            if (contrasenia != null) {
                usuario.setContrasenia(contrasenia);
            }
            if (rol != null) {
                usuario.setRol(rol);
            }
            if (nombre == null && apellido == null && celular == null && contrasenia == null && rol == null && mail == null){
                System.out.println("\nATENCION: No se registraron modificaciones al Usuario con id: #" + id);
            } else{
                System.out.println("¡¡¡Usuario con id: #" + id + " actualizado exitosamente!!!");
            }
        } catch (IllegalArgumentException iae) {
            System.out.println("\n ERROR: " + iae.getMessage());
        }
    }

    public Usuario buscar(long id) {
        int i = 0;
        Usuario usuarioEncontrado = null;
        while (i < usuarios.size() && usuarios.get(i).getId() != id) {
            i++;
        }
        if (i < usuarios.size() && !usuarios.get(i).isEliminado()) {
            usuarioEncontrado = this.usuarios.get(i);
        }
        return usuarioEncontrado;
    }

    public void eliminar(long id) {
        Usuario usuarioParaEliminar = this.buscar(id);
        if (usuarioParaEliminar == null || usuarioParaEliminar.isEliminado()) {
            throw new EntidadNoEncontradaException(String.format("Usuario con id #%d no encontrado o ya eliminado.", id));
        } else {
            usuarioParaEliminar.setEliminado(true);
            System.out.println("¡¡¡Usuario con id: #" + id + " se elimino exitosamente!!!");
        }
    }
}
