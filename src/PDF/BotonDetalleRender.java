package PDF;

import Model.CConexion;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Botón "Ver Detalle" por fila de un JTable. Al hacer clic, consulta la BD
 * usando el id_cita de ESA fila y abre una ventana con toda la información
 * de la consulta: paciente, médico, diagnóstico, receta y observaciones.
 * Cualquier dato faltante se muestra como "N/A".
 */
public class BotonDetalleRender extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
    private final JButton boton;
    private final JTable tabla;
    private final int colIdCita;
    private int filaActual;

    /**
     * @param tabla      la JTable donde vive el botón
     * @param colIdCita  índice de la columna del MODELO que contiene el id_cita
     */
    public BotonDetalleRender(JTable tabla, int colIdCita) {
        this.tabla = tabla;
        this.colIdCita = colIdCita;
        this.boton = new JButton("Ver Detalle");

        this.boton.setBackground(new Color(41, 128, 185));
        this.boton.setForeground(Color.WHITE);
        this.boton.setFocusPainted(false);
        this.boton.addActionListener(this);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return boton;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.filaActual = row;
        return boton;
    }

    @Override
    public Object getCellEditorValue() {
        return "Ver Detalle";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fireEditingStopped();

        // Convertimos a fila del modelo por si la tabla está ordenada/filtrada
        int filaModelo = tabla.convertRowIndexToModel(filaActual);
        Object valorIdCita = tabla.getModel().getValueAt(filaModelo, colIdCita);
        int idCita = Integer.parseInt(String.valueOf(valorIdCita));

        Connection conexion = new CConexion().estableceConexion();
        if (conexion == null) {
            JOptionPane.showMessageDialog(tabla,
                    "No se pudo establecer conexión con la base de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (conexion) {
            DetalleConsulta detalle = cargarDetalle(conexion, idCita);
            abrirVentanaDetalle(idCita, detalle);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(tabla,
                    "Error al cargar el detalle de la cita #" + idCita + ": " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // =========================================================================
    // Estructura interna con todos los datos a mostrar. Cada campo ya viene
    // "seguro" (nunca null) gracias a naSiVacio(...).
    // =========================================================================
    private static class DetalleConsulta {
        String pacienteNombreCompleto = "N/A";
        String pacienteDocumento = "N/A";
        String pacienteTelefono = "N/A";
        String medicoNombreCompleto = "N/A";
        String especialidad = "N/A";
        String fechaConsulta = "N/A";
        String sintomas = "N/A";
        String diagnostico = "N/A";
        String tratamiento = "N/A";
        String observaciones = "N/A";
        String codigoReceta = "N/A";
        List<String[]> medicamentos = new ArrayList<>(); // {nombre, cantidad, dosis}
    }

    private static String naSiVacio(String valor) {
        return (valor == null || valor.trim().isEmpty()) ? "N/A" : valor.trim();
    }

    // =========================================================================
    // Consulta la BD y arma el DetalleConsulta a partir del id_cita.
    // Ajusta nombres de tabla/columna si en tu BD difieren.
    // =========================================================================
    private DetalleConsulta cargarDetalle(Connection conn, int idCita) throws SQLException {
        DetalleConsulta d = new DetalleConsulta();
        Integer idReceta = null;

        String sql =
            "SELECT p.nombre AS p_nombre, p.apellido AS p_apellido, p.numero_documento, p.telefono AS p_telefono, " +
            "       m.nombre AS m_nombre, m.apellido AS m_apellido, " +
            "       e.nombre AS especialidad, " +
            "       ch.sintomas, ch.diagnostico, ch.tratamiento_indicaciones, ch.fecha_consulta, " +
            "       r.codigo_receta, r.id_receta " +
            "FROM consulta_historial ch " +
            "JOIN paciente p ON p.id_paciente = ch.id_paciente " +
            "JOIN medico m ON m.id_medico = ch.id_medico " +
            "LEFT JOIN especialidad e ON e.id_especialidad = m.id_especialidad " +
            "LEFT JOIN receta r ON r.id_consulta = ch.id_consulta " +
            "WHERE ch.id_cita = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCita);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    d.pacienteNombreCompleto = naSiVacio(rs.getString("p_nombre") + " " + rs.getString("p_apellido"));
                    d.pacienteDocumento = naSiVacio(rs.getString("numero_documento"));
                    d.pacienteTelefono = naSiVacio(rs.getString("p_telefono"));
                    d.medicoNombreCompleto = naSiVacio(rs.getString("m_nombre") + " " + rs.getString("m_apellido"));
                    d.especialidad = naSiVacio(rs.getString("especialidad"));
                    d.sintomas = naSiVacio(rs.getString("sintomas"));
                    d.diagnostico = naSiVacio(rs.getString("diagnostico"));
                    d.tratamiento = naSiVacio(rs.getString("tratamiento_indicaciones"));

                    Timestamp fc = rs.getTimestamp("fecha_consulta");
                    d.fechaConsulta = (fc != null) ? fc.toString() : "N/A";

                    d.codigoReceta = naSiVacio(rs.getString("codigo_receta"));
                    int idr = rs.getInt("id_receta");
                    if (!rs.wasNull()) idReceta = idr;
                }
            }
        }

        if (idReceta != null) {
            String sqlDetalle =
                "SELECT me.nombre_comercial, dr.cantidad, dr.dosis_instrucciones " +
                "FROM detalle_receta dr " +
                "JOIN medicamento me ON me.id_medicamento = dr.id_medicamento " +
                "WHERE dr.id_receta = ?";
            try (PreparedStatement ps2 = conn.prepareStatement(sqlDetalle)) {
                ps2.setInt(1, idReceta);
                try (ResultSet rs2 = ps2.executeQuery()) {
                    while (rs2.next()) {
                        d.medicamentos.add(new String[]{
                            naSiVacio(rs2.getString("nombre_comercial")),
                            String.valueOf(rs2.getInt("cantidad")),
                            naSiVacio(rs2.getString("dosis_instrucciones"))
                        });
                    }
                }
            }
        }

        // Si tienes observaciones en otra tabla (ej. triaje.notas_triaje),
        // agrégalas aquí con otra consulta y asígnalas a d.observaciones.

        return d;
    }

    // =========================================================================
    // Ventana de detalle (solo lectura, sin PDF)
    // =========================================================================
    private void abrirVentanaDetalle(Object idCita, DetalleConsulta d) {
        JDialog visor = new JDialog((Frame) SwingUtilities.getWindowAncestor(tabla),
                "Detalle de Consulta - Cita #" + idCita, true);
        visor.setSize(560, 620);
        visor.setLayout(new BorderLayout());
        visor.setLocationRelativeTo(tabla);

        JLabel lblTitulo = new JLabel("Consulta Médica Registrada", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(44, 62, 80));
        lblTitulo.setBorder(new EmptyBorder(15, 10, 10, 10));
        visor.add(lblTitulo, BorderLayout.NORTH);

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBorder(new EmptyBorder(5, 15, 15, 15));
        contenido.setBackground(Color.WHITE);

        contenido.add(crearSeccion("Datos del Paciente", new String[][]{
                {"Nombre", d.pacienteNombreCompleto},
                {"Documento", d.pacienteDocumento},
                {"Teléfono", d.pacienteTelefono}
        }));

        contenido.add(crearSeccion("Datos del Médico", new String[][]{
                {"Nombre", d.medicoNombreCompleto},
                {"Especialidad", d.especialidad},
                {"Fecha de Consulta", d.fechaConsulta}
        }));

        contenido.add(crearSeccion("Diagnóstico", new String[][]{
                {"Síntomas", d.sintomas},
                {"Diagnóstico", d.diagnostico},
                {"Tratamiento / Indicaciones", d.tratamiento}
        }));

        contenido.add(crearSeccionReceta(d));

        contenido.add(crearSeccion("Observaciones", new String[][]{
                {"Notas", d.observaciones}
        }));

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        visor.add(scroll, BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(al -> visor.dispose());
        panelAcciones.add(btnCerrar);
        visor.add(panelAcciones, BorderLayout.SOUTH);

        visor.setVisible(true);
    }

    private JPanel crearSeccion(String titulo, String[][] campos) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
                new TitledBorder(new LineBorder(new Color(220, 220, 220), 1, true), titulo,
                        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                        new Font("Segoe UI", Font.BOLD, 13), new Color(44, 62, 80)),
                new EmptyBorder(8, 10, 8, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < campos.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            JLabel lblEtiqueta = new JLabel(campos[i][0] + ":");
            lblEtiqueta.setFont(new Font("Segoe UI", Font.BOLD, 12));
            panel.add(lblEtiqueta, gbc);

            gbc.gridx = 1; gbc.weightx = 1;
            JLabel lblValor = new JLabel("<html><div style='width:360px'>" + campos[i][1] + "</div></html>");
            lblValor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            panel.add(lblValor, gbc);
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(panel, BorderLayout.CENTER);
        wrapper.setBorder(new EmptyBorder(0, 0, 10, 0));
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        return wrapper;
    }

    private JPanel crearSeccionReceta(DetalleConsulta d) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
                new TitledBorder(new LineBorder(new Color(220, 220, 220), 1, true),
                        "Receta Médica - Código: " + d.codigoReceta,
                        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                        new Font("Segoe UI", Font.BOLD, 13), new Color(44, 62, 80)),
                new EmptyBorder(8, 10, 8, 10)
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(new CompoundBorder(panel.getBorder(), new EmptyBorder(0, 0, 10, 0)));

        String[] columnas = {"Medicamento", "Cantidad", "Dosis / Instrucciones"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        if (d.medicamentos.isEmpty()) {
            modelo.addRow(new Object[]{"N/A", "N/A", "N/A"});
        } else {
            for (String[] med : d.medicamentos) {
                modelo.addRow(new Object[]{med[0], med[1], med[2]});
            }
        }

        JTable tablaMeds = new JTable(modelo);
        tablaMeds.setRowHeight(24);
        tablaMeds.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaMeds.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaMeds.setFillsViewportHeight(true);

        JScrollPane scrollTabla = new JScrollPane(tablaMeds);
        scrollTabla.setPreferredSize(new Dimension(510, Math.min(160, 30 + modelo.getRowCount() * 24)));
        panel.add(scrollTabla, BorderLayout.CENTER);

        return panel;
    }
}