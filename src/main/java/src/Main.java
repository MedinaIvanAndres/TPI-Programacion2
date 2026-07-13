package src;

import src.dao.UsuarioDAO;
import src.dao.UsuarioDAOImpl;
import src.db.ConexionDB;
import src.entities.Usuario;
import src.enums.Rol;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UsuarioDAO dao = new UsuarioDAOImpl();

        try {
            // Guardar
            Usuario u = new Usuario("Ivan", "Medina", "ivan@mail.com", "1234567890", "pass123", Rol.ADMIN);
            dao.guardar(u);
            System.out.println("Guardado con ID: " + u.getId());

            // Listar
            List<Usuario> lista = dao.listar();
            lista.forEach(System.out::println);

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            ConexionDB.cerrarConexion();
        }
    }
}