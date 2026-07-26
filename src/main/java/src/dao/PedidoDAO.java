package src.dao;

import src.entities.Pedido;
import src.enums.Estado;
import src.enums.FormaPago;

import java.sql.SQLException;
import java.util.List;

public interface PedidoDAO {
    void guardar(Pedido pedido) throws SQLException;
    List<Pedido> listar() throws SQLException;
    List<Pedido> listarPorUsuario(Long usuarioId) throws SQLException;
    Pedido buscarPorId(Long id) throws SQLException;
    void actualizar(Pedido pedido) throws SQLException;
    void eliminar(Long id) throws SQLException;
}
