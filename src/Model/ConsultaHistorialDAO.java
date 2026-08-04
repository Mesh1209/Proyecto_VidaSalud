package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaHistorialDAO extends CConexion{
    
    public List<CitaPendiente> obtenerCitasParaConsulta() {
        List<CitaPendiente> lista = new ArrayList<>();
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Consulta SQL que une la Cita con el Paciente para traer los datos requeridos
        String sql = "SELECT c.id_cita, c.id_paciente, c.id_medico, "
                   + "CONCAT(p.nombre, ' ', p.apellido) AS nombre_completo, "
                   + "p.numero_documento "
                   + "FROM cita c "
                   + "INNER JOIN paciente p ON c.id_paciente = p.id_paciente "
                   + "WHERE c.estado = 'En curso' ";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                CitaPendiente cita = new CitaPendiente();
                cita.setIdCita(rs.getInt("id_cita"));
                cita.setIdPaciente(rs.getInt("id_paciente"));
                cita.setIdMedico(rs.getInt("id_medico"));
                cita.setNombre(rs.getString("nombre_completo"));
                cita.setNumero_documento(rs.getString("numero_documento"));

                lista.add(cita);
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar consulta: " + e.getMessage());
        } finally {
            cerrarRecursos(null, ps, conn);
        }
        return lista;
    }
    
    public boolean insertar(ConsultaHistorial consulta) {
        Connection conn = estableceConexion();
        CallableStatement cs = null;
        String sql = "{call registrar_consulta_completa(?, ?, ?, ?, ?, ?)}";
        
        try {
            cs = conn.prepareCall(sql);

            // Mapeo de parámetros en el orden definido en el SP
            cs.setInt(1, consulta.getIdPaciente());
            cs.setInt(2, consulta.getIdMedico());
            cs.setInt(3, consulta.getIdCita());
            cs.setString(4, consulta.getSintomas());
            cs.setString(5, consulta.getDiagnostico());
            cs.setString(6, consulta.getTratamientoIndicaciones());

            // Ejecutar el procedimiento
            cs.execute();

            System.out.println("Procedimiento ejecutado con éxito en la BD.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar consulta: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, cs, conn);
        }
    }
    
    public List<ConsultaHistorial> listar() {
        List<ConsultaHistorial> lista = new ArrayList<>();
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT id_consulta, id_paciente, id_medico, id_cita, sintomas, diagnostico, tratamiento_indicaciones, fecha_consulta FROM consulta_historial";

        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ConsultaHistorial c = new ConsultaHistorial();
                c.setIdConsulta(rs.getInt("id_consulta"));
                c.setIdPaciente(rs.getInt("id_paciente"));
                c.setIdMedico(rs.getInt("id_medico"));
                c.setIdCita(rs.getInt("id_cita"));
                c.setSintomas(rs.getString("sintomas"));
                c.setDiagnostico(rs.getString("diagnostico"));
                c.setTratamientoIndicaciones(rs.getString("tratamiento_indicaciones"));
                c.setFechaConsulta(rs.getTimestamp("fecha_consulta"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar consultas: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, ps, conn);
        }
        return lista;
    }
    
    public boolean actualizar(ConsultaHistorial consulta) {
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        String sql = "UPDATE consulta_historial SET id_paciente = ?, id_medico = ?, id_cita = ?, sintomas = ?, diagnostico = ?, tratamiento_indicaciones = ? WHERE id_consulta = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, consulta.getIdPaciente());
            ps.setInt(2, consulta.getIdMedico());
            ps.setInt(3, consulta.getIdCita());
            ps.setString(4, consulta.getSintomas());
            ps.setString(5, consulta.getDiagnostico());
            ps.setString(6, consulta.getTratamientoIndicaciones());
            ps.setInt(7, consulta.getIdConsulta());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar consulta: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, ps, conn);
        }
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
