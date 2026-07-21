package src.dao;

import src.db.ConexionDB;
import src.entities.DetallePedido;
import src.entities.Pedido;
import java.sql.*;

public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public void guardar(Pedido pedido,DetallePedido detalle) throws SQLException {
        try (Connection con = ConexionDB.getConexion()) {
            con.setAutoCommit(false);
            try {
                this.insertarPedido(con, pedido);
                this.insertarDetalles(con, detalle);
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    void insertarPedido(Connection conexion, Pedido pedido) throws SQLException{
        String sql = "INSERT INTO pedidos (fecha, estado, forma_pago, total, usuario_id,created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(pedido.getFecha()));
            stmt.setString(2, pedido.getEstado().name());
            stmt.setString(3, pedido.getFormaPago().name());
            stmt.setDouble(4, pedido.getTotal());
            stmt.setLong(5, pedido.getUsuario().getId());
            stmt.setTimestamp(6, Timestamp.valueOf(pedido.getCreatedAt()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                pedido.setId(rs.getLong(1));
            }
        }
    }

    void insertarDetalles(Connection conexion, DetallePedido detalle) throws SQLException{
        String sql = "INSERT INTO pedidos (fecha, estado, forma_pago, total, usuario_id,created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(pedido.getFecha()));
            stmt.setString(2, pedido.getEstado().name());
            stmt.setString(3, pedido.getFormaPago().name());
            stmt.setDouble(4, pedido.getTotal());
            stmt.setLong(5, pedido.getUsuario().getId());
            stmt.setTimestamp(6, Timestamp.valueOf(pedido.getCreatedAt()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                detalle.setId(rs.getLong(1));
            }
        }
    }
}


