package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    private final CConexion cn = new CConexion();

    public boolean validar(Usuario user){
        String sql = "SELECT u.id_usuario, u.username, u.id_rol, u.activo, r.nombre " +
                     "FROM usuario u " +
                     "INNER JOIN rol r ON u.id_rol = r.id_rol " +
                     "WHERE u.username = ? AND u.password_hash = ?";

        // Al declarar las conexiones dentro del try (...), Java las cierra solas pase lo que pase
        try (Connection con = cn.estableceConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword_hash());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user.setId_usuario(rs.getInt("id_usuario"));
                    user.setUsername(rs.getString("username"));
                    user.setId_rol(rs.getInt("id_rol"));
                    user.setActivo(rs.getBoolean("activo"));          // Mapea a boolean
                    user.setNombre_rol(rs.getString("nombre"));  // Nombre del rol traído desde la tabla 'rol'
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en Login DAO: " + e.getMessage());
        }
        return false;
    }
    
    public boolean actualizar(Usuario user) {
        // Opción A: Si actualizas la contraseña junto con los datos
        String sql = "UPDATE usuario SET username = ?, password_hash = ? WHERE id_usuario = ?";

        try (Connection con = cn.estableceConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword_hash());
            ps.setInt(3, user.getId_usuario());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }
}