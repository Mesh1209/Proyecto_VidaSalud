/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
//import java.util.Date;
import java.util.List;

public class CitaDAO extends CConexion {
    
    //METODO PARA BUSCAR Y TRAER DATOS DE PACIENTE
    public List<Paciente> obtenerDatoPaciente(String dni){
        List<Paciente> pacientes = new ArrayList<>();
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT id_paciente,nombre,apellido,tipo_documento,numero_documento,fecha_nacimiento," +
                 "genero,telefono, email, direccion " +
                 "FROM paciente " +
                 "WHERE numero_documento=?";
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, dni);
            rs = ps.executeQuery();
            while (rs.next()){
                Paciente paciente = new Paciente();
                
                paciente.setId_paciente(rs.getInt("id_paciente"));
                paciente.setNombre(rs.getString("apellido"));
                paciente.setApellido(rs.getString("apellido"));
                paciente.setTipo_documento(rs.getString("tipo_documento"));
                paciente.setNumero_documento(rs.getString("numero_documento"));
                
                LocalDate Fecha_nacimiento = rs.getObject("fecha_nacimiento", LocalDate.class);
                paciente.setFecha_nacimiento(
                    Fecha_nacimiento != null ? Fecha_nacimiento : LocalDate.now()
                );
                paciente.setGenero(rs.getString("genero"));
                paciente.setTelefono(rs.getString("telefono"));
                
                paciente.setEmail(rs.getString("email"));                
                paciente.setDireccion(rs.getString("direccion"));
                
                pacientes.add(paciente);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener Paciente: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return pacientes;
    }
    
    public List<ListaHorarioCita> ListaMedicoHorario (){
        List<ListaHorarioCita> listaHorarioCitas = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = estableceConexion();
        
        String sql = "SELECT " +
                 "    m.id_medico, " +
                 "    m.nombre, " +
                 "    m.apellido, " +
                 "    e.nombre AS especialidad, " +
                 "    hm.dia_semana, " +
                 "    hm.hora_inicio, " +
                 "    hm.hora_fin " +
                 "FROM medico m " +
                 "INNER JOIN especialidad e ON m.id_especialidad = e.id_especialidad " +
                 "INNER JOIN horario_medico hm ON m.id_medico = hm.id_medico " +
                 "WHERE m.activo = true " +
                 "  AND NOT EXISTS ( " +
                 "      SELECT 1 FROM cita c " +
                 "      WHERE c.id_medico = m.id_medico " +
                 "        AND c.fecha_hora_inicio = hm.hora_inicio " +
                 "        AND c.estado IN ('Pendiente', 'En curso') " +
                 "  ) " +
                 "ORDER BY hm.dia_semana ASC, hm.hora_inicio ASC";
        
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                
                ListaHorarioCita listaHorarioCita = new ListaHorarioCita();
                listaHorarioCita.setId_medico(rs.getInt("id_medico"));
                listaHorarioCita.setNombre(rs.getString("nombre"));
                listaHorarioCita.setApellido(rs.getString("apellido"));               
                listaHorarioCita.setEspecialidad(rs.getString("especialidad"));                
                String diaSemanaStr = rs.getString("dia_semana");
                listaHorarioCita.setDia_semana(diaSemanaStr != null ? Integer.parseInt(diaSemanaStr) : 0);
                //listaHorarioCita.setDia_semana(Integer.parseInt(rs.getString("dia_semana")));
                
                LocalDateTime horaInicio = rs.getObject("hora_inicio", LocalDateTime.class);
                LocalDateTime horaFin = rs.getObject("hora_fin", LocalDateTime.class);

                // Fecha actual con hora específica
                LocalDateTime defaultInicio = LocalDateTime.now().withHour(8).withMinute(0).withSecond(0);
                LocalDateTime defaultFin = LocalDateTime.now().withHour(18).withMinute(0).withSecond(0);

                listaHorarioCita.setHora_inicio(
                    horaInicio != null ? horaInicio : defaultInicio
                );

                listaHorarioCita.setHora_fin(
                    horaFin != null ? horaFin : defaultFin
                );
                
                listaHorarioCitas.add(listaHorarioCita);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener lista: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaHorarioCitas;
    }
    
    //
    public List<ListaHorarioCita> buscarHorariosMedicos(String nombre, String apellido, String especialidad, LocalDate fecha, LocalTime hora) {

        List<ListaHorarioCita> listaHorarioCitas = new ArrayList<>();

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ")
           .append(" m.id_medico, ")
           .append(" m.nombre, ")
           .append(" m.apellido, ")
           .append(" e.nombre AS especialidad, ")
           .append(" hm.dia_semana, ")
           .append(" hm.hora_inicio, ")
           .append(" hm.hora_fin ")
           .append("FROM medico m ")
           .append("INNER JOIN especialidad e ON m.id_especialidad = e.id_especialidad ")
           .append("INNER JOIN horario_medico hm ON m.id_medico = hm.id_medico ")
           .append("WHERE m.activo = TRUE ");

        List<Object> parametros = new ArrayList<>();

        // Filtro por nombre
        if (nombre != null && !nombre.trim().isEmpty()) {
            sql.append(" AND m.nombre LIKE ? ");
            parametros.add("%" + nombre.trim() + "%");
        }

        // Filtro por apellido
        if (apellido != null && !apellido.trim().isEmpty()) {
            sql.append(" AND m.apellido LIKE ? ");
            parametros.add("%" + apellido.trim() + "%");
        }

        // Filtro por especialidad
        if (especialidad != null && !especialidad.trim().isEmpty()) {
            sql.append(" AND e.nombre LIKE ? ");
            parametros.add("%" + especialidad.trim() + "%");
        }

        // Filtro por fecha
        if (fecha != null) {
            sql.append(" AND DATE(hm.hora_inicio) = ? ");
            parametros.add(java.sql.Date.valueOf(fecha));
        }

        // Filtro por hora
        if (hora != null) {
            sql.append(" AND TIME(?) BETWEEN TIME(hm.hora_inicio) AND TIME(hm.hora_fin) ");
            parametros.add(java.sql.Time.valueOf(hora));
        }

        // Excluir horarios ya ocupados
        sql.append(" AND NOT EXISTS (")
           .append("     SELECT 1 ")
           .append("     FROM cita c ")
           .append("     WHERE c.id_medico = m.id_medico ")
           .append("       AND c.fecha_hora_inicio = hm.hora_inicio ")
           .append("       AND c.estado IN ('Pendiente','En curso')")
           .append(" ) ");

        sql.append(" ORDER BY hm.hora_inicio ASC");

        try (Connection conn = estableceConexion();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    ListaHorarioCita listaHorarioCita = new ListaHorarioCita();

                    listaHorarioCita.setId_medico(rs.getInt("id_medico"));
                    listaHorarioCita.setNombre(rs.getString("nombre"));
                    listaHorarioCita.setApellido(rs.getString("apellido"));
                    listaHorarioCita.setEspecialidad(rs.getString("especialidad"));
                    listaHorarioCita.setDia_semana(rs.getInt("dia_semana"));

                    listaHorarioCita.setHora_inicio(
                            rs.getObject("hora_inicio", LocalDateTime.class));

                    listaHorarioCita.setHora_fin(
                            rs.getObject("hora_fin", LocalDateTime.class));

                    listaHorarioCitas.add(listaHorarioCita);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener lista: " + e.getMessage());
        }

        return listaHorarioCitas;
    }
    
        //
    public int registrarCita(Cita cita) {
        Connection conn = estableceConexion();
        CallableStatement cs = null;
        ResultSet rs = null;
        String sql = "{CALL sp_insertar_cita(?, ?, ?, ?, ?, ?)}";

        try {
            cs = conn.prepareCall(sql);

            cs.setInt(1, cita.getPaciente().getId_paciente());
            cs.setInt(2, cita.getMedico().getId_medico());
            cs.setObject(3, cita.getFecha_hora_inicio());
            cs.setObject(4, cita.getFecha_hora_fin());
            cs.setString(5, cita.getMotivo_consulta());
            cs.setInt(6, cita.getId_usuario_registro());

            boolean hasResultSet = cs.execute();

            if (hasResultSet) {
                rs = cs.getResultSet();
                if (rs.next()) {
                    String estado = rs.getString("estado_registro");

                    if ("OK".equals(estado)) {
                        int idGenerado = rs.getInt("id_cita_generado");
                        System.out.println("Cita registrada con ID: " + idGenerado);

                        // Si además quieres que el objeto cita se actualice internamente:
                        //cita.setId_cita(idGenerado); 

                        return idGenerado; // Retorna el ID generado
                    } else {
                        String mensajeError = rs.getString("mensaje_error");
                        System.err.println("Error en procedimiento: " + mensajeError);
                    }
                }
            }
            return -1; // Retorna -1 si falló la validación del SP
        } catch (SQLException ex) {
            System.err.println("Error al registrar cita: " + ex.getMessage());
            return -1;
        } finally {
            try {
                if (rs != null) rs.close(); // También es buena práctica cerrar el ResultSet
                if (cs != null) cs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public List<Cita> listarCitas() {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT c.id_cita, c.id_paciente, c.id_medico, c.fecha_hora_inicio, c.fecha_hora_fin, "
               + "c.estado, c.motivo_consulta, c.id_usuario_registro, "
               + "p.nombre AS paciente_nombre, p.numero_documento AS paciente_documento " // 
               + "FROM cita c "
               + "INNER JOIN paciente p ON c.id_paciente = p.id_paciente";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = estableceConexion();
        
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Cita cita = new Cita();
                cita.setId_cita(rs.getInt("id_cita"));

                // Mapeamos el Paciente con sus datos completos obtenidos del JOIN
                Paciente p = new Paciente(); 
                p.setId_paciente(rs.getInt("id_paciente"));
                p.setNombre(rs.getString("paciente_nombre"));               
                p.setNumero_documento(rs.getString("paciente_documento"));
                cita.setPaciente(p);

                Medico m = new Medico(); 
                m.setId_medico(rs.getInt("id_medico"));
                cita.setMedico(m);

                cita.setFecha_hora_inicio(rs.getObject("fecha_hora_inicio", LocalDateTime.class));
                cita.setFecha_hora_fin(rs.getObject("fecha_hora_fin", LocalDateTime.class));
                cita.setEstado(rs.getString("estado"));
                cita.setMotivo_consulta(rs.getString("motivo_consulta"));
                cita.setId_usuario_registro(rs.getInt("id_usuario_registro"));

                lista.add(cita);
            }
            
        } catch (SQLException e) {
            System.out.println("Error al listar citas: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, ps, conn);
        }
        System.out.println(lista);
        return lista;
    }
    
    public List<Cita> listarCitasFiltro(Integer idCita, 
        String pacienteNombre, 
        String estado, 
        LocalDateTime fechaInicio, 
        LocalDateTime fechaFin) {
        
        List<Cita> lista = new ArrayList<>();

        // 1. Base de la consulta. Usamos "WHERE 1=1" para añadir filtros dinámicamente con "AND"
        StringBuilder sql = new StringBuilder(
            "SELECT c.id_cita, c.id_paciente, c.id_medico, c.fecha_hora_inicio, c.fecha_hora_fin, "
          + "c.estado, c.motivo_consulta, c.id_usuario_registro, "
          + "p.nombre AS paciente_nombre, p.numero_documento AS paciente_documento "
          + "FROM cita c "
          + "INNER JOIN paciente p ON c.id_paciente = p.id_paciente "
          + "WHERE 1=1 "
        );

        // 2. Evaluamos qué filtros ha enviado el usuario
        if (idCita != null) {
        sql.append("AND c.id_cita = ? ");
        }
        if (pacienteNombre != null && !pacienteNombre.trim().isEmpty()) {
            // Usamos LIKE para búsquedas parciales por nombre
            sql.append("AND LOWER(p.nombre) LIKE LOWER(?) ");
        }
        if (estado != null && !estado.trim().isEmpty()) {
            sql.append("AND c.estado = ? ");
        }
        if (fechaInicio != null) {
            // Citas desde la fecha/hora especificada
            sql.append("AND c.fecha_hora_inicio >= ? ");
        }
        if (fechaFin != null) {
            // Citas hasta la fecha/hora especificada
            sql.append("AND c.fecha_hora_fin <= ? ");
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = estableceConexion();

        try {
            ps = conn.prepareStatement(sql.toString());

            // 3. Asignamos los parámetros en el estricto orden en que fueron añadidos
            int paramIndex = 1;

            if (idCita != null) {
                ps.setInt(paramIndex++, idCita);
            }
            if (pacienteNombre != null && !pacienteNombre.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + pacienteNombre.trim() + "%");
            }
            if (estado != null && !estado.trim().isEmpty()) {
                ps.setString(paramIndex++, estado.trim());
            }
            if (fechaInicio != null) {
                ps.setObject(paramIndex++, fechaInicio);
            }
            if (fechaFin != null) {
                ps.setObject(paramIndex++, fechaFin);
            }

            rs = ps.executeQuery();

            // 4. Tu misma lógica exacta de mapeo (se mantiene intacta)
            while (rs.next()) {
                Cita cita = new Cita();
                cita.setId_cita(rs.getInt("id_cita"));

                Paciente p = new Paciente(); 
                p.setId_paciente(rs.getInt("id_paciente"));
                p.setNombre(rs.getString("paciente_nombre"));                
                p.setNumero_documento(rs.getString("paciente_documento"));
                cita.setPaciente(p);

                Medico m = new Medico(); 
                m.setId_medico(rs.getInt("id_medico"));
                cita.setMedico(m);

                cita.setFecha_hora_inicio(rs.getObject("fecha_hora_inicio", LocalDateTime.class));
                cita.setFecha_hora_fin(rs.getObject("fecha_hora_fin", LocalDateTime.class));
                cita.setEstado(rs.getString("estado"));
                cita.setMotivo_consulta(rs.getString("motivo_consulta"));
                cita.setId_usuario_registro(rs.getInt("id_usuario_registro"));

                lista.add(cita);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar citas: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, ps, conn);
        }

        return lista;
    }
    
    public CitaNotificacion obtenerDatosCitaParaNotificacion(int idCita) {
        CitaNotificacion dto = null;
        String sql = "SELECT "
                + "  p.telefono AS telefono_paciente, "
                + "  CONCAT(p.nombre, ' ', p.apellido) AS nombre_paciente, "
                + "  CONCAT('Dr(a). ', m.nombre, ' ', m.apellido) AS nombre_medico, "
                + "  e.nombre AS nombre_especialidad, "
                + "  DATE_FORMAT(c.fecha_hora_inicio, '%d/%m/%Y') AS fecha_cita, "
                + "  DATE_FORMAT(c.fecha_hora_inicio, '%h:%i %p') AS hora_cita "
                + "FROM cita c "
                + "INNER JOIN paciente p ON c.id_paciente = p.id_paciente "
                + "INNER JOIN medico m ON c.id_medico = m.id_medico "
                + "LEFT JOIN especialidad e ON m.id_especialidad = e.id_especialidad "
                + "WHERE c.id_cita = ?";
        Connection conn = estableceConexion();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCita);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    dto = new CitaNotificacion();
                    dto.setTelefono(rs.getString("telefono_paciente"));
                    dto.setPaciente(rs.getString("nombre_paciente"));
                    dto.setMedico(rs.getString("nombre_medico"));
                    dto.setEspecialidad(rs.getString("nombre_especialidad"));
                    dto.setFecha(rs.getString("fecha_cita"));
                    dto.setHora(rs.getString("hora_cita"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar citas: " + e.getMessage());
        } finally {
            cerrarRecursos(null, null, conn);
        }

        return dto;
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
