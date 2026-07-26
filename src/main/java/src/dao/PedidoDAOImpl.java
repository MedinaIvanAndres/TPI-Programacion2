package src.dao;

import src.db.ConexionDB;
import src.entities.*;
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
                pedidos.add(mapear(rs,con));
            }
        }
        return pedidos;
    }

    private Pedido mapear(ResultSet rs, Connection con) throws SQLException {
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
        List<DetallePedido> detalles = this.buscarDetalles(idPedido,con);
        Pedido pedido = new Pedido(estado,pago,usuario);
        pedido.setId(idPedido);
        pedido.setFecha(fecha);
        this.agregarDetalles(pedido,detalles);
        pedido.setCreatedAt(createdAt);

        return pedido;
    }

    private List<DetallePedido> buscarDetalles(Long idPedido,Connection con) throws SQLException{
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT id, cantidad, subtotal, producto_id, created_at FROM detalles_pedido  " +
                "WHERE pedido_id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, idPedido);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                detalles.add(mapearDetalles(rs));
            }
        }
        return detalles;
    }

    private DetallePedido mapearDetalles(ResultSet rs) throws SQLException {
        Long idDetalle = rs.getLong("id");
        int cantidad = rs.getInt("cantidad");
        Double subtotal = rs.getDouble("subtotal");
        Long idProducto = rs.getLong("producto_id");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

        Producto producto = new Producto(idProducto);
        DetallePedido detalle = new DetallePedido(cantidad, subtotal, producto);
        detalle.setId(idDetalle);
        detalle.setCreatedAt(createdAt);

        return detalle;
    }

    private void agregarDetalles(Pedido pedido, List<DetallePedido> detalles){
        for(DetallePedido detalle : detalles){
            pedido.addDetallePedido(detalle);
        }
    }

    @Override
    public List<Pedido> listarPorUsuario(Long idUsuario) throws SQLException{
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT ped.id, ped.fecha, ped.estado, ped.forma_pago, ped.total,ped.usuario_id, ped.created_at, " +
                "usu.id AS usu_id, usu.nombre AS usu_nombre,usu.apellido AS usu_apellido FROM pedidos ped " +
                "INNER JOIN usuarios usu ON ped.usuario_id = usu.id WHERE ped.usuario_id = ?";

        try (Connection con = ConexionDB.getConexion();PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pedidos.add(mapear(rs,con));
            }
        }
        return pedidos;
    }

    @Override
    public Pedido buscarPorId(Long idPedido) throws SQLException {
        String sql = "SELECT ped.id, ped.fecha, ped.estado, ped.forma_pago, ped.total,ped.usuario_id, ped.created_at, " +
                "usu.id AS usu_id, usu.nombre AS usu_nombre,usu.apellido AS usu_apellido FROM pedidos ped " +
                "INNER JOIN usuarios usu ON ped.usuario_id = usu.id WHERE ped.id = ?";

        try (Connection con = ConexionDB.getConexion();PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, idPedido);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapear(rs,con);
            }
        }
        return null;
    }

    @Override
    public void actualizar(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedidos SET estado = ?, forma_pago = ? WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, pedido.getEstado().name());
            stmt.setString(2, pedido.getFormaPago().name());
            stmt.setLong(3, pedido.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        try (Connection con = ConexionDB.getConexion()) {
            con.setAutoCommit(false);
            try {
                // Primero eliminar los detalles
                String sqlDetalles = "DELETE FROM detalles_pedido WHERE pedido_id = ?";
                try (PreparedStatement stmt = con.prepareStatement(sqlDetalles)) {
                    stmt.setLong(1, id);
                    stmt.executeUpdate();
                }
                // Después eliminar el pedido
                String sqlPedido = "DELETE FROM pedidos WHERE id = ?";
                try (PreparedStatement stmt = con.prepareStatement(sqlPedido)) {
                    stmt.setLong(1, id);
                    stmt.executeUpdate();
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }
}


