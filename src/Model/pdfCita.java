package Model;

import Model.Cita;
import Model.Especialidad;
import Model.Medico;
import Model.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class pdfCita extends CConexion {

    public Cita obtenerCitaPreviaPorId(int idCita) {
        Cita cita = null;
        
        String sql = "SELECT c.id_cita, c.fecha_hora_inicio, c.estado, c.motivo_consulta, "
                   + "p.id_paciente, p.nombre AS p_nombre, p.apellido AS p_apellido, "
                   + "p.tipo_documento, p.numero_documento, p.telefono AS p_telefono, "
                   + "m.id_medico, m.nombre AS m_nombre, m.apellido AS m_apellido, m.id_colegiatura, "
                   + "e.nombre AS especialidad "
                   + "FROM cita c "
                   + "INNER JOIN paciente p ON c.id_paciente = p.id_paciente "
                   + "INNER JOIN medico m ON c.id_medico = m.id_medico "
                   + "LEFT JOIN especialidad e ON m.id_especialidad = e.id_especialidad "
                   + "WHERE c.id_cita = ?";

        // Try-with-resources: Cierra automáticamente conexión, statement y resultSet
        try (Connection con = estableceConexion(); // Ajusta según tu método de conexión
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCita);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 1. Mapear Paciente
                    Paciente paciente = new Paciente();
                    paciente.setId_paciente(rs.getInt("id_paciente"));
                    paciente.setNombre(rs.getString("p_nombre"));
                    paciente.setApellido(rs.getString("p_apellido"));
                    paciente.setTipo_documento(rs.getString("tipo_documento"));
                    paciente.setNumero_documento(rs.getString("numero_documento"));
                    paciente.setTelefono(rs.getString("p_telefono"));

                    // 2. Mapear Especialidad
                    Especialidad especialidad = new Especialidad();
                    especialidad.setNombre(rs.getString("especialidad"));

                    // 3. Mapear Médico
                    Medico medico = new Medico();
                    medico.setId_medico(rs.getInt("id_medico"));
                    medico.setNombre(rs.getString("m_nombre"));
                    medico.setApellido(rs.getString("m_apellido"));
                    medico.setId_colegiatura(rs.getInt("id_colegiatura"));
                    medico.setEspecialidad(especialidad);

                    // 4. Mapear Cita
                    cita = new Cita();
                    cita.setId_cita(rs.getInt("id_cita"));
                    
                    // Convertir Timestamp a LocalDateTime si tu modelo usa LocalDateTime
                    Timestamp timestamp = rs.getTimestamp("fecha_hora_inicio");
                    if (timestamp != null) {
                        cita.setFecha_hora_inicio(timestamp.toLocalDateTime());
                    }

                    cita.setEstado(rs.getString("estado"));
                    cita.setMotivo_consulta(rs.getString("motivo_consulta"));
                    
                    // Asociar objetos hijos a la Cita
                    cita.setPaciente(paciente);
                    cita.setMedico(medico);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar cita por ID: " + e.getMessage());
            e.printStackTrace();
        }

        return cita;
    }
}
