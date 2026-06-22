
package src.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import src.enums.Rol;

/**
 *
 * @author Ivan Andres Medina
 */
public class Usuario extends Base{
    private String nombre;
    private String apellido;
    private String mail;
    private String celular;
    private String contrasenia;
    private Rol rol;
    private List<Pedido> pedidos;

    
    public Usuario(String nombre, String apellido, String mail, String celular, String contrasenia, Rol rol) {
        super();
        setNombre(nombre);
        setApellido(apellido);
        setMail(mail);
        setCelular(celular);
        setContrasenia(contrasenia);
        this.rol = rol;
        this.pedidos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.nombre = nombre;
        } else {
            throw new IllegalArgumentException("Nombre invalido. Usuario no creado");
        }
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        if (apellido != null && !apellido.trim().isEmpty()) {
            this.apellido = apellido;
        } else {
            throw new IllegalArgumentException("Apellido invalido. Usuario no creado");
        }
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        if (mail != null && !mail.trim().isEmpty()) {
            this.mail = mail;
        } else {
            throw new IllegalArgumentException("mail invalido. Usuario no creado");
        }
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        if (celular != null && !celular.trim().isEmpty()) {
            this.celular = celular;
        } else {
            throw new IllegalArgumentException("celular invalido. Usuario no creado");
        }
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        if (contrasenia != null && !contrasenia.trim().isEmpty()) {
            this.contrasenia = contrasenia;
        } else {
            throw new IllegalArgumentException("contrasenia invalido. Usuario no creado");
        }
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Pedido> getPedidos() {
        return Collections.unmodifiableList(pedidos);
    }

    public void agregarPedido (Pedido newPedido){
        if (newPedido != null && !pedidos.contains(newPedido)){
            pedidos.add(newPedido);
            if (newPedido.getUsuario()!= this){
                newPedido.setUsuario(this);
            }       
        }
    }
    
    public void eliminarPedido (Pedido pedido){
        if(pedido != null && pedidos.contains(pedido)){
            pedidos.remove(pedido);
            if(pedido.getUsuario() == this){
                pedido.setUsuario(null);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("ID: #%d | Nombre: %s | Apellido: %s | Mail: %s | Rol: %s",this.getId(),nombre,apellido,mail,rol);
    }
}
