/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.*;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO extends CConexion{
    public boolean insertar(Paciente p) {
        String sql = "INSERT INTO paciente (nombre, apellido, tipo_documento, numero_documento, fecha_nacimiento, genero, telefono, email, direccion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getApellido());
            ps.setString(3, p.getTipo_documento());
            ps.setString(4, p.getNumero_documento());
            ps.setDate(5, Date.valueOf(p.getFecha_nacimiento()));
            ps.setString(6, p.getGenero());
            ps.setString(7, p.getTelefono());
            ps.setString(8, p.getEmail());
            ps.setString(9, p.getDireccion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(rs, ps, conn);
        }
    }

    // 2. LISTAR TODOS (READ)
    public List<Paciente> listar() {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT id_paciente, nombre, apellido, tipo_documento, numero_documento, fecha_nacimiento, genero, telefono, email, direccion, fecha_registro FROM paciente";
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Paciente p = new Paciente();
                p.setId_paciente(rs.getInt("id_paciente"));
                p.setNombre(rs.getString("nombre"));
                p.setApellido(rs.getString("apellido"));
                p.setTipo_documento(rs.getString("tipo_documento"));
                p.setNumero_documento(rs.getString("numero_documento"));
                
                LocalDate Fecha_nacimiento = rs.getObject("fecha_nacimiento", LocalDate.class);
                p.setFecha_nacimiento(
                    Fecha_nacimiento != null ? Fecha_nacimiento : LocalDate.now()
                );
                
                p.setGenero(rs.getString("genero"));
                p.setTelefono(rs.getString("telefono"));
                p.setEmail(rs.getString("email"));
                p.setDireccion(rs.getString("direccion"));
                p.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, ps, conn);
        }
        return lista;
    }

    // 3. OBTENER POR ID (READ SINGLE)
    public Paciente obtenerPorId(int id) {
        Paciente p = null;
        String sql = "SELECT * FROM paciente WHERE id_paciente = ?";
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                p = new Paciente();
                p.setId_paciente(rs.getInt("id_paciente"));
                p.setNombre(rs.getString("nombre"));
                p.setApellido(rs.getString("apellido"));
                p.setTipo_documento(rs.getString("tipo_documento"));
                p.setNumero_documento(rs.getString("numero_documento"));
                
                LocalDate Fecha_nacimiento = rs.getObject("fecha_nacimiento", LocalDate.class);
                p.setFecha_nacimiento(
                    Fecha_nacimiento != null ? Fecha_nacimiento : LocalDate.now()
                );
                
                p.setGenero(rs.getString("genero"));
                p.setTelefono(rs.getString("telefono"));
                p.setEmail(rs.getString("email"));
                p.setDireccion(rs.getString("direccion"));
                p.setFechaRegistro(rs.getTimestamp("fecha_registro"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, ps, conn);
        }
        return p;
    }

    // 4. ACTUALIZAR (UPDATE)
    public boolean actualizar(Paciente p) {
        String sql = "UPDATE paciente SET nombre = ?, apellido = ?, tipo_documento = ?, numero_documento = ?, fecha_nacimiento = ?, genero = ?, telefono = ?, email = ?, direccion = ? WHERE id_paciente = ?";
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getApellido());
            ps.setString(3, p.getTipo_documento());
            ps.setString(4, p.getNumero_documento());
            ps.setDate(5, Date.valueOf(p.getFecha_nacimiento()));
            ps.setString(6, p.getGenero());
            ps.setString(7, p.getTelefono());
            ps.setString(8, p.getEmail());
            ps.setString(9, p.getDireccion());
            ps.setInt(10, p.getId_paciente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(rs, ps, conn);
        }
    }

    // 5. ELIMINAR (DELETE)
    public boolean eliminar(int id) {
        String sql = "DELETE FROM paciente WHERE id_paciente = ?";
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(rs, ps, conn);
        }
    }
    
    //CERRAR CONEXION
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
