package src.dao;

import src.entities.Categoria;
import java.sql.SQLException;
import java.util.List;

public interface CategoriaDAO {
    void guardar(Categoria categoria) throws SQLException;
    List<Categoria> listar() throws SQLException;
    Categoria buscarPorId(Long id) throws SQLException;
    void actualizar(Categoria categoria) throws SQLException;
    void eliminar(Long id) throws SQLException;
}