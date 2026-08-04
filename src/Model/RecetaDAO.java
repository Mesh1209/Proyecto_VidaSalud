package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecetaDAO extends CConexion{
    
    public List<RecetaConsulta> listarIdConsulta(int idCita) {
        List<RecetaConsulta> lista = new ArrayList<>();
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select c.id_cita,ch.id_consulta,p.nombre , p.numero_documento From paciente p " +
                    "inner join cita c on p.id_paciente = c.id_paciente " +
                    "INNER JOIN consulta_historial ch on ch.id_cita =?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idCita);
            rs = ps.executeQuery();
            while (rs.next()) {
                RecetaConsulta det = new RecetaConsulta();
                det.setIdCita(rs.getInt("id_cita"));
                det.setIdConsulta(rs.getInt("id_consulta"));
                det.setNombre(rs.getString("nombre"));
                det.setDocumento(rs.getString("numero_documento"));
                lista.add(det);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar detalles: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, ps, conn);
        }
        return lista;
    }
    
    public boolean insertarConProcedure(Receta receta, List<DetalleReceta> detalles) {
        Connection conn = estableceConexion();
        CallableStatement csReceta = null;
        CallableStatement csDetalle = null;
        
        // Llamadas a los procedimientos almacenados usando la sintaxis {call nombre_sp(?, ?, ...)}
        String sqlReceta = "{call sp_insertar_receta(?, ?, ?)}";
        String sqlDetalle = "{call sp_insertar_detalle_receta(?, ?, ?, ?)}";
        
        try {
            // Desactivamos autocommit para controlar la transacción en Java
            conn.setAutoCommit(false);
            
            // 1. Ejecutar procedimiento de la Receta Cabecera
            csReceta = conn.prepareCall(sqlReceta);
            csReceta.setInt(1, receta.getIdConsulta());
            csReceta.setString(2, receta.getCodigoReceta());
            
            // Registramos el tercer parámetro como de SALIDA (OUT) para recibir el ID generado
            csReceta.registerOutParameter(3, Types.INTEGER);
            
            csReceta.execute();
            
            // Obtenemos el ID generado que nos devuelve la base de datos
            int idRecetaGenerado = csReceta.getInt(3);
            
            if (idRecetaGenerado <= 0) {
                conn.rollback();
                System.out.print("el id no existe");
                return false;
            }
            
            // 2. Ejecutar procedimiento de los detalles en lote (Batch)
            csDetalle = conn.prepareCall(sqlDetalle);
            for (DetalleReceta det : detalles) {
                csDetalle.setInt(1, idRecetaGenerado);
                csDetalle.setInt(2, det.getId_medicamento());
                csDetalle.setInt(3, det.getCantidad());
                csDetalle.setString(4, det.getDosis_instrucciones());
                csDetalle.addBatch(); // Se agrega al lote de ejecución
            }
            
            csDetalle.executeBatch(); // Ejecuta todos los detalles de un solo golpe
            
            // Si todo salió bien, guardamos cambios en la BD de forma definitiva
            conn.commit();
            return true;
            
        }catch (SQLException e) {
            System.out.println("Error al insertar consulta: " + e.getMessage());
            return false;
        } finally {
            try { if (csReceta != null) csReceta.close(); } catch (Exception e) {}
            try { if (csDetalle != null) csDetalle.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
    
    public Receta buscarPorId(int idReceta) {
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Receta receta = null;
        String sql = "SELECT id_receta, id_consulta, codigo_receta, fecha_emision FROM receta WHERE id_receta = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idReceta);
            rs = ps.executeQuery();
            if (rs.next()) {
                receta = new Receta();
                receta.setIdReceta(rs.getInt("id_receta"));
                receta.setIdConsulta(rs.getInt("id_consulta"));
                receta.setCodigoReceta(rs.getString("codigo_receta"));
                receta.setFechaEmision(rs.getTimestamp("fecha_emision"));
                
                // Cargamos de una vez la lista de sus detalles
                receta.setDetalles(listarDetallesPorReceta(idReceta));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar receta: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, ps, conn);
        }
        return receta;
    }

    // Método de apoyo para leer los detalles de la receta
    private List<DetalleReceta> listarDetallesPorReceta(int idReceta) {
        List<DetalleReceta> lista = new ArrayList<>();
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT id_detalle_receta, id_receta, id_medicamento, cantidad, dosis_instrucciones FROM detalle_receta WHERE id_receta = ?";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idReceta);
            rs = ps.executeQuery();
            while (rs.next()) {
                DetalleReceta det = new DetalleReceta();
                det.setId_detalle_receta(rs.getInt("id_detalle_receta"));
                det.setId_receta(rs.getInt("id_receta"));
                det.setId_medicamento(rs.getInt("id_medicamento"));
                det.setCantidad(rs.getInt("cantidad"));
                det.setDosis_instrucciones(rs.getString("dosis_instrucciones"));
                lista.add(det);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar detalles: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, ps, conn);
        }
        return lista;
    }

    // ==========================================
    // 3. UPDATE: Modificar datos de la receta
    // ==========================================
    public boolean actualizar(Receta receta) {
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        String sql = "UPDATE receta SET id_consulta = ?, codigo_receta = ? WHERE id_receta = ?";
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, receta.getIdConsulta());
            ps.setString(2, receta.getCodigoReceta());
            ps.setInt(3, receta.getIdReceta());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar receta: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, ps, conn);
        }
    }

    // ==========================================
    // 4. DELETE: Eliminar Receta y sus detalles en Cascada
    // ==========================================
    public boolean eliminar(int idReceta) {
        Connection conn = estableceConexion();
        PreparedStatement psDetalle = null;
        PreparedStatement psReceta = null;
        
        String sqlDetalle = "DELETE FROM detalle_receta WHERE id_receta = ?";
        String sqlReceta = "DELETE FROM receta WHERE id_receta = ?";
        
        try {
            conn.setAutoCommit(false);
            
            // A. Primero borramos los detalles por la llave foránea
            psDetalle = conn.prepareStatement(sqlDetalle);
            psDetalle.setInt(1, idReceta);
            psDetalle.executeUpdate();
            
            // B. Luego borramos la cabecera
            psReceta = conn.prepareStatement(sqlReceta);
            psReceta.setInt(1, idReceta);
            int filasAfectadas = psReceta.executeUpdate();
            
            conn.commit();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.out.println("Error en rollback: " + ex.getMessage());
            }
            System.out.println("Error al eliminar receta: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, psDetalle, null);
            cerrarRecursos(null, psReceta, conn);
        }
    }
    
    //Testeando
    public List<Receta> buscarConFiltros(Integer idConsulta, String codigoReceta, Date fechaInicio, Date fechaFin) {
        List<Receta> listaRecetas = new ArrayList<>();
        Connection conn = estableceConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Base de la consulta
        StringBuilder sql = new StringBuilder("SELECT id_receta, id_consulta, codigo_receta, fecha_emision FROM receta WHERE 1=1");
        List<Object> parametros = new ArrayList<>();

        // Agregamos filtros dinámicamente si vienen con valor
        if (idConsulta != null && idConsulta > 0) {
            sql.append(" AND id_consulta = ?");
            parametros.add(idConsulta);
        }
        if (codigoReceta != null && !codigoReceta.trim().isEmpty()) {
            sql.append(" AND codigo_receta LIKE ?");
            parametros.add("%" + codigoReceta.trim() + "%");
        }
        if (fechaInicio != null) {
            sql.append(" AND fecha_emision >= ?");
            parametros.add(new java.sql.Timestamp(fechaInicio.getTime()));
        }
        if (fechaFin != null) {
            sql.append(" AND fecha_emision <= ?");
            parametros.add(new java.sql.Timestamp(fechaFin.getTime()));
        }

        try {
            ps = conn.prepareStatement(sql.toString());

            // Asignamos los parámetros dinámicos a la consulta
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                Receta receta = new Receta();
                receta.setIdReceta(rs.getInt("id_receta"));
                receta.setIdConsulta(rs.getInt("id_consulta"));
                receta.setCodigoReceta(rs.getString("codigo_receta"));
                receta.setFechaEmision(rs.getTimestamp("fecha_emision"));

                // Opcional: Cargar los detalles de cada receta encontrada
                //receta.setDetalles(listarDetallesPorReceta(receta.getIdReceta()));

                listaRecetas.add(receta);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar recetas con filtro: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, ps, conn);
        }

        return listaRecetas;
    }
    // Método helper de limpieza de recursos
    private void cerrarRecursos(ResultSet rs, PreparedStatement ps, Connection conn) {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (ps != null) ps.close(); } catch (Exception e) {}
        try { if (conn != null) conn.close(); } catch (Exception e) {}
    }
}
