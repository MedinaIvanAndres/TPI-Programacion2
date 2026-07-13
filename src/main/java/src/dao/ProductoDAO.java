package src.dao;

import src.entities.Producto;
import java.sql.SQLException;
import java.util.List;

public interface ProductoDAO {
    void guardar(Producto producto) throws SQLException;
    List<Producto> listar() throws SQLException;
    Producto buscarPorId(Long id) throws SQLException;
    void actualizar(Producto producto) throws SQLException;
    void eliminar(Long id) throws SQLException;
}