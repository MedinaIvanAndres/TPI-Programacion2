package src.dao;

import src.db.ConexionDB;
import src.entities.Usuario;
import src.enums.Rol;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public void guardar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, apellido, mail, celular, contrasenia, rol, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getMail());
            stmt.setString(4, usuario.getCelular());
            stmt.setString(5, usuario.getContrasenia());
            stmt.setString(6, usuario.getRol().name());
            stmt.setTimestamp(7, Timestamp.valueOf(usuario.getCreatedAt()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                usuario.setId(rs.getLong(1));
            }
        }
    }

    @Override
    public List<Usuario> listar() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection con = ConexionDB.getConexion();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                usuarios.add(mapear(rs));
            }
        }

        return usuarios;
    }

    @Override
    public Usuario buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

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
    public void actualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nombre = ?, apellido = ?, mail = ?, celular = ?, "
                + "contrasenia = ?, rol = ? WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getMail());
            stmt.setString(4, usuario.getCelular());
            stmt.setString(5, usuario.getContrasenia());
            stmt.setString(6, usuario.getRol().name());
            stmt.setLong(7, usuario.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        String mail = rs.getString("mail");
        String celular = rs.getString("celular");
        String contrasenia = rs.getString("contrasenia");
        Rol rol = Rol.valueOf(rs.getString("rol"));
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

        Usuario usuario = new Usuario(nombre, apellido, mail, celular, contrasenia, rol);
        usuario.setId(id);
        usuario.setCreatedAt(createdAt);

        return usuario;
    }
}