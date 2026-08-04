package Model;

import java.sql.*;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;

public class PerfilDAO extends CConexion{
    public boolean registrarMedicoConUsuario(String username, String password, String rol, 
                                             String nombre, String apellido, int idEspecialidad, 
                                             String telefono, String email, String idColegiatura, 
                                             int duracionTurno){
        
        String sql = "{call registrar_medico_con_usuario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try(Connection conn = estableceConexion();
            CallableStatement cstmt = conn.prepareCall(sql)){
            
            cstmt.setString(1, username);
            cstmt.setString(2, password);
            cstmt.setString(3, rol);
            cstmt.setString(4, nombre);
            cstmt.setString(5, apellido);
            cstmt.setInt(6, idEspecialidad);
            cstmt.setString(7, telefono);
            cstmt.setString(8, email);
            cstmt.setString(9, idColegiatura);
            cstmt.setInt(10, duracionTurno);
            
            boolean hasResult = cstmt.execute();
            
            if (hasResult) {
                try (ResultSet rs = cstmt.getResultSet()) {
                    if (rs.next()) {
                        int idMedico = rs.getInt("id_medico_creado");
                        int idUsuario = rs.getInt("id_usuario_creado");
                        System.out.println("Éxito. Medico ID: " + idMedico + ", Usuario ID: " + idUsuario);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error al ejecutar el procedure: " + e.getMessage());
            return false;
        }
    }
    
    public boolean registrarUsuario(Usuario usuario){
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "INSERT INTO usuario (username, password_hash, id_rol, activo) VALUES (?, ?, ?, ?)";
        
        try {
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword_hash());
            ps.setInt(3, usuario.getId_rol());
            ps.setBoolean(4, usuario.isActivo());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    usuario.setId_usuario(rs.getInt(1)); // Asigna el ID autogenerado a la entidad
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, ps, conn);
        }
        return false;
    }
    
    private void cerrarRecursos(ResultSet rs, PreparedStatement ps, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
