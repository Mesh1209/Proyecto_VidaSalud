package Model;

import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.sql.Date;

public class TriajeDAO extends CConexion{
    public List<Paciente> obtenerDatosPaciente(int idCita) {
        List<Paciente> pacientes = new ArrayList<>();
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "Select nombre, numero_documento, genero, fecha_nacimiento "
                + "from paciente p "
                + "inner join cita c on c.id_paciente = p.id_paciente "
                + "WHERE c.id_cita=?";
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idCita);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Paciente paciente = new Paciente();
                
                paciente.setNombre(rs.getString("nombre"));
                paciente.setNumero_documento(rs.getString("numero_documento"));
                paciente.setGenero(rs.getString("nombre"));
                
                LocalDate Fecha_nacimiento = rs.getObject("fecha_nacimiento", LocalDate.class);
                paciente.setFecha_nacimiento(
                    Fecha_nacimiento != null ? Fecha_nacimiento : LocalDate.now()
                );
                
                pacientes.add(paciente);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener paciente: " + e.getMessage());
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
    
    public boolean ActualizarTriaje(Triaje triaje){
        String sql = "{CALL sp_actualizar_triaje(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = estableceConexion();
            CallableStatement cs = conn.prepareCall(sql)) {

            if (conn == null) return false;

            // Mapeo de parámetros
            cs.setInt(1, triaje.getId_cita());
            cs.setDouble(2, triaje.getPeso_kg());
            cs.setDouble(3, triaje.getTalla_cm());
            cs.setDouble(4, triaje.getTemperatura_c());
            cs.setString(5, triaje.getPresion_arterial());
            cs.setInt(6, triaje.getFrecuencia_cardiaca());
            cs.setDouble(7, triaje.getSaturacion_oxigeno());
            cs.setString(8, triaje.getNotas_triaje());
            cs.setString(9, "En curso");

            // Ejecutamos el Stored Procedure
            cs.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al ejecutar SP sp_actualizar_triaje: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
}