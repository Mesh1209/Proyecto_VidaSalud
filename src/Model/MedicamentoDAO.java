package Model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MedicamentoDAO extends CConexion {
    public List<Medicamento> obtenerListaMedicamento(){
        List<Medicamento> medicamentos = new ArrayList<>();
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT id_medicamento ,nombre_comercial,nombre_generico,presentacion,stock,precio_venta FROM medicamento";

        try{
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                Medicamento medicamento = new Medicamento();
                
                medicamento.setId_medicamento(rs.getInt("id_medicamento"));
                medicamento.setNombre_comercial(rs.getString("nombre_comercial"));
                medicamento.setNombre_generico(rs.getString("nombre_generico"));
                medicamento.setPresentacion(rs.getString("presentacion"));
                medicamento.setStock(rs.getInt("stock"));
                medicamento.setPrecio_venta(rs.getDouble("precio_venta"));
                
                medicamentos.add(medicamento);
            }
        }catch (SQLException e) {
            System.out.println("Error al obtener medicamentos: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }  
        System.out.println(medicamentos+ "aca inicio");
        return medicamentos;        
    }
    
    public List<Medicamento> obtenerListaMedicamentoNombre(String nombre){
        List<Medicamento> medicamentos = new ArrayList<>();
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT id_medicamento ,nombre_comercial,nombre_generico,presentacion,stock,precio_venta FROM medicamento where nombre_comercial=?";

        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();
            while (rs.next()){
                Medicamento medicamento = new Medicamento();
                
                medicamento.setId_medicamento(rs.getInt("id_medicamento"));
                medicamento.setNombre_comercial(rs.getString("nombre_comercial"));
                medicamento.setNombre_generico(rs.getString("nombre_generico"));
                medicamento.setPresentacion(rs.getString("presentacion"));
                medicamento.setStock(rs.getInt("stock"));
                medicamento.setPrecio_venta(rs.getDouble("precio_venta"));
                
                medicamentos.add(medicamento);
            }
        }catch (SQLException e) {
            System.out.println("Error al obtener medicamentos: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }  
        System.out.println(medicamentos+ "aca inicio");
        return medicamentos;        
    }
    
    public boolean crearMedicamento(Medicamento medicamento) {
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        String sql = "INSERT INTO medicamento (nombre_comercial, nombre_generico, presentacion, stock, precio_venta,codigo_medicamento) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, medicamento.getNombre_comercial());
            ps.setString(2, medicamento.getNombre_generico());
            ps.setString(3, medicamento.getPresentacion());
            ps.setInt(4, medicamento.getStock());
            ps.setDouble(5, medicamento.getPrecio_venta());
            ps.setString(6, medicamento.getCodigo_medicamento());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; // Retorna true si se insertó con éxito
        } catch (SQLException e) {
            System.out.println("Error al crear medicamento: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean actualizarMedicamento(Medicamento medicamento) {
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        String sql = "UPDATE medicamento SET nombre_comercial = ?, nombre_generico = ?, presentacion = ?, " +
                     "stock = ?, precio_venta = ? WHERE id_medicamento = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, medicamento.getNombre_comercial());
            ps.setString(2, medicamento.getNombre_generico());
            ps.setString(3, medicamento.getPresentacion());
            ps.setInt(4, medicamento.getStock());
            ps.setDouble(5, medicamento.getPrecio_venta());
            ps.setInt(6, medicamento.getId_medicamento());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; // Retorna true si se actualizó con éxito
        } catch (SQLException e) {
            System.out.println("Error al actualizar medicamento: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean eliminarMedicamento(int id_medicamento) {
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        String sql = "DELETE FROM medicamento WHERE id_medicamento = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id_medicamento);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; // Retorna true si se eliminó con éxito
        } catch (SQLException e) {
            System.out.println("Error al eliminar medicamento: " + e.getMessage());
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean guardarExcelMedicamento(List<Medicamento> listaMedicamentos) {
    Connection conn = estableceConexion();
    CallableStatement cs = null;
    // Invocamos al procedimiento almacenado
    String sql = "{CALL sp_guardar_o_actualizar_medicamento(?, ?, ?, ?, ?, ?)}";

    try {
        conn.setAutoCommit(false); // Transacción única para rapidez y seguridad
        cs = conn.prepareCall(sql);

        for (Medicamento m : listaMedicamentos) {
            cs.setString(1, m.getCodigo_medicamento());
            cs.setString(2, m.getNombre_comercial());
            cs.setString(3, m.getNombre_generico());
            cs.setString(4, m.getPresentacion());
            cs.setInt(5, m.getStock());
            cs.setDouble(6, m.getPrecio_venta());

            cs.addBatch(); // Se agrega al lote
        }

        cs.executeBatch(); // Ejecuta todo el lote en la base de datos
        conn.commit();      // Confirma la transacción
        return true;

    } catch (SQLException e) {
        System.err.println("Error al ejecutar procedimiento en lote: " + e.getMessage());
        try {
            if (conn != null) conn.rollback(); // Cancela si ocurre un error
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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
}
