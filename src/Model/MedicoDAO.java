package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO extends CConexion{
    public List<Medico> listarMedicos() {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT * FROM medico";
        try (Connection con = estableceConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Medico m = new Medico();
                m.setId_medico(rs.getInt("id_medico"));
                m.setNombre(rs.getString("nombre"));
                m.setApellido(rs.getString("apellido"));
                
                // Mapeamos el objeto Especialidad dentro de Medico
                Especialidad esp = new Especialidad();
                esp.setId_especialidad(rs.getInt("id_especialidad"));
                esp.setNombre(rs.getString("nombre_especialidad"));
                m.setEspecialidad(esp);

                m.setNumero_documento(rs.getString("numero_documento"));
                m.setTelefono(rs.getString("telefono"));
                m.setEmail(rs.getString("email"));
                m.setId_colegiatura(rs.getInt("id_colegiatura"));
                m.setDuracion_turno_minutos(rs.getString("duracion_turno_minutos"));
                m.setActivo(rs.getBoolean("activo"));

                lista.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar: " + e.getMessage());
        }
        return lista;
    }
    
    public List<Medico> listarMedicoApellido(String apellido) {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT m.*, e.nombre AS nombre_especialidad " +
                     "FROM medico m " +
                     "INNER JOIN especialidad e ON m.id_especialidad = e.id_especialidad " +
                     "WHERE m.apellido LIKE ?";

        try (Connection con = estableceConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + apellido + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Medico m = new Medico();
                    m.setId_medico(rs.getInt("id_medico"));
                    m.setNombre(rs.getString("nombre"));
                    m.setApellido(rs.getString("apellido"));

                    Especialidad esp = new Especialidad();
                    esp.setId_especialidad(rs.getInt("id_especialidad"));
                    esp.setNombre(rs.getString("nombre_especialidad"));
                    m.setEspecialidad(esp);
                    m.setTelefono(rs.getString("telefono"));
                    m.setEmail(rs.getString("email"));
                    m.setId_colegiatura(rs.getInt("id_colegiatura"));
                    m.setDuracion_turno_minutos(rs.getString("duracion_turno_minutos"));
                    m.setActivo(rs.getBoolean("activo"));

                    lista.add(m);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar médico por apellido: " + e.getMessage());
        }
        return lista;
    }

    public boolean registrarMedico(Medico m) {
        String sql = "INSERT INTO medico (nombre, apellido, id_especialidad, telefono, email, id_colegiatura, duracion_turno_minutos, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = estableceConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, m.getNombre());
            ps.setString(2, m.getApellido());
            ps.setInt(3, m.getEspecialidad().getId_especialidad());
            ps.setString(4, m.getTelefono());
            ps.setString(5, m.getEmail());
            ps.setInt(6, m.getId_colegiatura());
            ps.setString(7, m.getDuracion_turno_minutos());
            ps.setBoolean(8, m.isActivo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarMedico(Medico m) {
        String sql = "UPDATE medico SET nombre=?, apellido=?, id_especialidad=?, numero_documento=?, telefono=?, email=?, id_colegiatura=?, duracion_turno_minutos=?, activo=? WHERE id_medico=?";
        try (Connection con = estableceConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, m.getNombre());
            ps.setString(2, m.getApellido());
            ps.setInt(3, m.getEspecialidad().getId_especialidad());
            ps.setString(4, m.getNumero_documento());
            ps.setString(5, m.getTelefono());
            ps.setString(6, m.getEmail());
            ps.setInt(7, m.getId_colegiatura());
            ps.setString(8, m.getDuracion_turno_minutos());
            ps.setBoolean(9, m.isActivo());
            ps.setInt(10, m.getId_medico());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarMedico(int id) {
        String sql = "DELETE FROM medico WHERE id_medico=?";
        try (Connection con = estableceConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }
}
