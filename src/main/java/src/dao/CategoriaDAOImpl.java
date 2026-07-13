package src.dao;

import src.db.ConexionDB;
import src.entities.Categoria;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOImpl implements CategoriaDAO {

    @Override
    public void guardar(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categorias (nombre, descripcion, created_at) VALUES (?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.setTimestamp(3, Timestamp.valueOf(categoria.getCreatedAt()));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                categoria.setId(rs.getLong(1));
            }
        }
    }

    @Override
    public List<Categoria> listar() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categorias";

        try (Connection con = ConexionDB.getConexion();Statement stmt = con.createStatement();ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categorias.add(mapear(rs));
            }
        }

        return categorias;
    }

    @Override
    public Categoria buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM categorias WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        }
        return null;
    }

    @Override
    public void actualizar(Categoria categoria) throws SQLException {
        String sql = "UPDATE categorias SET nombre = ?, descripcion = ? WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.setLong(3, categoria.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = "DELETE FROM categorias WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private Categoria mapear(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String nombre = rs.getString("nombre");
        String descripcion = rs.getString("descripcion");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

        Categoria categoria = new Categoria(nombre, descripcion);
        categoria.setId(id);
        categoria.setCreatedAt(createdAt);

        return categoria;
    }
}