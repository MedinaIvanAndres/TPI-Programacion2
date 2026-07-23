package src.dao;

import src.db.ConexionDB;
import src.entities.Categoria;
import src.entities.DetallePedido;
import src.entities.Pedido;
import src.entities.Usuario;
import src.enums.Estado;
import src.enums.FormaPago;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public void guardar(Pedido pedido) throws SQLException {
        try (Connection con = ConexionDB.getConexion()) {
            con.setAutoCommit(false);
            try {
                this.insertarPedido(con, pedido);
                this.insertarDetalles(con, pedido.getDetalles(),pedido.getId());
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    private void insertarPedido(Connection conexion, Pedido pedido) throws SQLException{
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

    private void insertarDetalles(Connection conexion, List<DetallePedido> detalles,Long idPedido) throws SQLException{
        if (detalles != null && !detalles.isEmpty()){
            for (DetallePedido detalle: detalles){
                this.insertarDetalle(conexion,detalle,idPedido);
            }
        }
    }

    private void insertarDetalle(Connection conexion, DetallePedido detalle,Long idPedido) throws SQLException{
        String sql = "INSERT INTO detalles_pedido (cantidad, subtotal, pedido_id, producto_id,created_at) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, detalle.getCantidad());
            stmt.setDouble(2, detalle.getSubtotal());
            stmt.setLong(3, idPedido);
            stmt.setLong(4, detalle.getProducto().getId());
            stmt.setTimestamp(5, Timestamp.valueOf(detalle.getCreatedAt()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                detalle.setId(rs.getLong(1));
            }
        }
    }

    @Override
    public List<Pedido> listar() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT ped.id, ped.fecha, ped.estado, ped.forma_pago, ped.total,ped.usuario_id, ped.created_at, " +
                "usu.id AS usu_id, usu.nombre AS usu_nombre,usu.apellido AS usu_apellido FROM pedidos ped " +
                "INNER JOIN usuarios usu ON ped.usuario_id = usu.id";

        try (Connection con = ConexionDB.getConexion();Statement stmt = con.createStatement();ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pedidos.add(mapear(rs));
            }
        }

        return pedidos;
    }

    private Pedido mapear(ResultSet rs) throws SQLException {
        Long idPedido = rs.getLong("id");
        LocalDate fecha = rs.getDate("fecha").toLocalDate();
        Estado estado = Estado.valueOf(rs.getString("estado"));
        FormaPago pago = FormaPago.valueOf(rs.getString("forma_pago"));
        Double total = rs.getDouble("total");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        Long idUsuario = rs.getLong("usu_id");
        String nombre = rs.getString("usu_nombre");
        String apellido = rs.getString("usu_apellido");

        Usuario usuario = new Usuario(nombre,apellido);
        usuario.setId(idUsuario);
        Pedido pedido = new Pedido(estado,pago,usuario);
        pedido.setId(idPedido);
        pedido.setFecha(fecha);
        pedido.setTotal(total);
        pedido.setCreatedAt(createdAt);

        return pedido;
    }
}


