package Model;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HorarioDAO extends CConexion{
    //ESTE SERA MEJOR LA ESTRUCTURA PARA TODOS
    public List<Horario> obtenerHorarioMedico() {
        List<Horario> horarios = new ArrayList<>();
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT m.id_medico, m.nombre, m.apellido," +
                 "h.id_horario,h.dia_semana,h.hora_inicio, h.hora_fin " +
                 "FROM medico m " +
                 "LEFT JOIN horario_medico h ON m.id_medico = h.id_medico";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            int contador = 0;
            while (rs.next()) {
                contador++;
                
                
                Medico medico = new Medico();
                medico.setId_medico(rs.getInt("id_medico"));
                medico.setNombre(rs.getString("nombre"));
                medico.setApellido(rs.getString("apellido"));
                
                Horario horario = new Horario();
                horario.setMedico(medico);
                
                horario.setId_horario(rs.getInt("id_horario"));               
                horario.setDia_semana(rs.getInt("dia_semana"));
                
                // Convertir solo si no son null
                Timestamp horaInicio = rs.getTimestamp("hora_inicio");
                Timestamp horaFin = rs.getTimestamp("hora_fin");

                horario.setHora_inicio(
                    horaInicio != null
                        ? horaInicio.toLocalDateTime()
                        : LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0))
                );

                horario.setHora_fin(
                    horaFin != null
                        ? horaFin.toLocalDateTime()
                        : LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0))
                );
                
                horarios.add(horario);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener horarios: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return horarios;
    }
    //METODO PARA BUSCAR POR APELLIDO
    public List<Horario> obtenerHorarioBusquedaMedico(String apellido) {
        List<Horario> horarios = new ArrayList<>();
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT m.id_medico, m.nombre, m.apellido," +
                 "h.id_horario,h.dia_semana,h.hora_inicio, h.hora_fin " +
                 "FROM medico m " +
                 "LEFT JOIN horario_medico h ON m.id_medico = h.id_medico " +
                 "WHERE m.apellido=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, apellido);
            rs = ps.executeQuery();
            
            while (rs.next()) {                              
                Medico medico = new Medico();
                medico.setId_medico(rs.getInt("id_medico"));
                medico.setNombre(rs.getString("nombre"));
                medico.setApellido(rs.getString("apellido"));
                
                Horario horario = new Horario();
                horario.setMedico(medico);
                
                horario.setId_horario(rs.getInt("id_horario"));               
                horario.setDia_semana(rs.getInt("dia_semana"));
                
                // Convertir solo si no son null
                Timestamp horaInicio = rs.getTimestamp("hora_inicio");
                Timestamp horaFin = rs.getTimestamp("hora_fin");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                horario.setHora_inicio(
                    horaInicio != null
                        ? horaInicio.toLocalDateTime()
                        : LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0))
                );

                horario.setHora_fin(
                    horaFin != null
                        ? horaFin.toLocalDateTime()
                        : LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0))
                );
                
                horarios.add(horario);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener horarios: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return horarios;
    }
    
    //METODO PARA GUARDAR HORARIO DE MEDICO
    public boolean actualizarHorario(Horario horario){
        Connection conn = estableceConexion();
        CallableStatement cs = null;
        String sql = "{CALL GenerarHorariosMedico(?, ?, ?)}";
        //String sql = "{CALL sp_guardar_o_actualizar_horario(?, ?, ?, ?)}";
        try {
            cs = conn.prepareCall(sql);
        
            // Mapeo de parámetros
            cs.setInt(1, horario.getMedico().getId_medico());
            //cs.setInt(2, horario.getDia_semana());
            cs.setObject(2, horario.getHora_inicio());
            cs.setObject(3, horario.getHora_fin());

            // executeUpdate devuelve las filas afectadas (1 si insertó, 2 si actualizó)
            int filasAfectadas = cs.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException ex) {
            System.err.println("Error al actualizar producto: " + ex.getMessage());
            return false;
        } finally {
            try {
                if (cs != null) cs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean eliminarHorario(int idHorario){
        Connection con = estableceConexion();
        PreparedStatement ps = null;
        String sql = "DELETE FROM horario_medico WHERE id_horario = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, idHorario);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException ex) {
            System.err.println("Error al eliminar producto: " + ex.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement:" + e.getMessage());
            }
        }
    }
}
