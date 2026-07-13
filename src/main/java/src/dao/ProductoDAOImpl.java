package src.dao;

import src.db.ConexionDB;
import src.entities.Categoria;
import src.entities.Producto;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public void guardar(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos (nombre, precio, descripcion, stock, imagen, disponible, categoria_id, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setString(3, producto.getDescripcion());
            stmt.setInt(4, producto.getStock());
            stmt.setString(5, producto.getImagen());
            stmt.setBoolean(6, producto.isDisponible());

            // categoria_id puede ser null si el producto no tiene categoría asignada
            if (producto.getCategoria() != null) {
                stmt.setLong(7, producto.getCategoria().getId());
            } else {
                stmt.setNull(7, Types.BIGINT);
            }

            stmt.setTimestamp(8, Timestamp.valueOf(producto.getCreatedAt()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                producto.setId(rs.getLong(1));
            }
        }
    }

    @Override
    public List<Producto> listar() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre AS cat_nombre, c.descripcion AS cat_descripcion, "
                + "c.created_at AS cat_created_at "
                + "FROM productos p "
                + "LEFT JOIN categorias c ON p.categoria_id = c.id";

        try (Connection con = ConexionDB.getConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                productos.add(mapear(rs));
            }
        }
        return productos;
    }

    @Override
    public Producto buscarPorId(Long id) throws SQLException {
        String sql = "SELECT p.*, c.nombre AS cat_nombre, c.descripcion AS cat_descripcion, "
                + "c.created_at AS cat_created_at "
                + "FROM productos p "
                + "LEFT JOIN categorias c ON p.categoria_id = c.id "
                + "WHERE p.id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapear(rs);
            }
        }

        return null;
    }

    @Override
    public void actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE productos SET nombre = ?, precio = ?, descripcion = ?, stock = ?, "
                + "imagen = ?, disponible = ?, categoria_id = ? WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setString(3, producto.getDescripcion());
            stmt.setInt(4, producto.getStock());
            stmt.setString(5, producto.getImagen());
            stmt.setBoolean(6, producto.isDisponible());

            if (producto.getCategoria() != null) {
                stmt.setLong(7, producto.getCategoria().getId());
            } else {
                stmt.setNull(7, Types.BIGINT);
            }

            stmt.setLong(8, producto.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String nombre = rs.getString("nombre");
        double precio = rs.getDouble("precio");
        String descripcion = rs.getString("descripcion");
        int stock = rs.getInt("stock");
        String imagen = rs.getString("imagen");
        boolean disponible = rs.getBoolean("disponible");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

        // Reconstruir la Categoria si el producto tiene una asignada
        Categoria categoria = null;
        long categoriaId = rs.getLong("categoria_id");
        if (!rs.wasNull()) {
            categoria = new Categoria(
                    rs.getString("cat_nombre"),
                    rs.getString("cat_descripcion")
            );
            categoria.setId(categoriaId);
            categoria.setCreatedAt(rs.getTimestamp("cat_created_at").toLocalDateTime());
        }
        Producto producto = new Producto(nombre, precio, descripcion, stock, imagen, disponible, categoria);
        producto.setId(id);
        producto.setCreatedAt(createdAt);
        return producto;
    }
}