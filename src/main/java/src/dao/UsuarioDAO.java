package src.dao;

import src.entities.Usuario;
import java.sql.SQLException;
import java.util.List;

public interface UsuarioDAO {
    void guardar(Usuario usuario) throws SQLException;
    List<Usuario> listar() throws SQLException;
    Usuario buscarPorId(Long id) throws SQLException;
    void actualizar(Usuario usuario) throws SQLException;
    void eliminar(Long id) throws SQLException;
}