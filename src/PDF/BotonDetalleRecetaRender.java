package PDF;

import Model.CConexion;
import Model.RecetaDAO;                 // AJUSTA el paquete si tu RecetaDAO vive en otro lugar
import Model.Receta;                  // AJUSTA el paquete si Receta vive en otro lugar
import Model.DetalleReceta;           // AJUSTA nombre/paquete si la clase de detalle se llama distinto

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Botón "Ver Detalle" por fila de un JTable. Al hacer clic, toma el id_receta
 * de ESA fila y muestra en una ventana la receta (código, fecha de emisión)
 * y su lista de medicamentos/dosis. Cualquier dato faltante se muestra como "N/A".
 */
public class BotonDetalleRecetaRender extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
    private final JButton boton;
    private final JTable tabla;
    private final int colIdReceta;
    private int filaActual;
    private final RecetaDAO recetaDAO = new RecetaDAO();

    /**
     * @param tabla        la JTable donde vive el botón
     * @param colIdReceta  índice de la columna del MODELO que contiene el id_receta
     */
    public BotonDetalleRecetaRender(JTable tabla, int colIdReceta) {
        this.tabla = tabla;
        this.colIdReceta = colIdReceta;
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
        Object valorIdReceta = tabla.getModel().getValueAt(filaModelo, colIdReceta);
        int idReceta = Integer.parseInt(String.valueOf(valorIdReceta));

        // buscarPorId ya maneja su propia conexión internamente (RecetaDAO extends CConexion)
        Receta receta = recetaDAO.buscarPorId(idReceta);

        if (receta == null) {
            JOptionPane.showMessageDialog(tabla,
                    "No se encontró la receta #" + idReceta + ".",
                    "Sin resultados", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Map<Integer, String> nombresMedicamentos = new HashMap<>();
        try {
            nombresMedicamentos = cargarNombresMedicamentos(receta.getDetalles());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(tabla,
                    "Error al cargar los nombres de los medicamentos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        abrirVentanaDetalle(receta, nombresMedicamentos);
    }

    private static String naSiVacio(String valor) {
        return (valor == null || valor.trim().isEmpty()) ? "N/A" : valor.trim();
    }

    // Resuelve nombre_comercial para cada id_medicamento distinto de la lista de detalles.
    private Map<Integer, String> cargarNombresMedicamentos(List<DetalleReceta> detalles) throws SQLException {
        Map<Integer, String> nombres = new HashMap<>();
        if (detalles == null || detalles.isEmpty()) {
            return nombres;
        }

        Connection conn = new CConexion().estableceConexion();
        if (conn == null) {
            return nombres;
        }

        try (conn) {
            String sql = "SELECT nombre_comercial FROM medicamento WHERE id_medicamento = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (DetalleReceta det : detalles) {
                    int idMed = det.getId_medicamento();
                    if (nombres.containsKey(idMed)) continue;

                    ps.setInt(1, idMed);
                    try (ResultSet rs = ps.executeQuery()) {
                        nombres.put(idMed, rs.next() ? naSiVacio(rs.getString("nombre_comercial")) : "N/A");
                    }
                }
            }
        }
        return nombres;
    }

    // =========================================================================
    // Ventana de detalle (solo lectura, solo receta + medicamentos)
    // =========================================================================
    private void abrirVentanaDetalle(Receta receta, Map<Integer, String> nombresMedicamentos) {
        JDialog visor = new JDialog((Frame) SwingUtilities.getWindowAncestor(tabla),
                "Detalle de Receta #" + receta.getIdReceta(), true);
        visor.setSize(520, 480);
        visor.setLayout(new BorderLayout());
        visor.setLocationRelativeTo(tabla);

        JLabel lblTitulo = new JLabel("Receta Médica", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(44, 62, 80));
        lblTitulo.setBorder(new EmptyBorder(15, 10, 10, 10));
        visor.add(lblTitulo, BorderLayout.NORTH);

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBorder(new EmptyBorder(5, 15, 15, 15));
        contenido.setBackground(Color.WHITE);

        String fechaEmision = (receta.getFechaEmision() != null) ? receta.getFechaEmision().toString() : "N/A";

        contenido.add(crearSeccion("Datos de la Receta", new String[][]{
                {"Código de Receta", naSiVacio(receta.getCodigoReceta())},
                {"Fecha de Emisión", fechaEmision}
        }));

        contenido.add(crearSeccionMedicamentos(receta.getDetalles(), nombresMedicamentos));

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
            JLabel lblValor = new JLabel("<html><div style='width:340px'>" + campos[i][1] + "</div></html>");
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

    private JPanel crearSeccionMedicamentos(List<DetalleReceta> detalles, Map<Integer, String> nombresMedicamentos) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
                new TitledBorder(new LineBorder(new Color(220, 220, 220), 1, true), "Medicamentos",
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

        if (detalles == null || detalles.isEmpty()) {
            modelo.addRow(new Object[]{"N/A", "N/A", "N/A"});
        } else {
            for (DetalleReceta det : detalles) {
                String nombreMed = nombresMedicamentos.getOrDefault(det.getId_medicamento(), "N/A");
                modelo.addRow(new Object[]{
                        nombreMed,
                        String.valueOf(det.getCantidad()),
                        naSiVacio(det.getDosis_instrucciones())
                });
            }
        }

        JTable tablaMeds = new JTable(modelo);
        tablaMeds.setRowHeight(24);
        tablaMeds.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaMeds.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaMeds.setFillsViewportHeight(true);

        JScrollPane scrollTabla = new JScrollPane(tablaMeds);
        scrollTabla.setPreferredSize(new Dimension(470, Math.min(200, 30 + modelo.getRowCount() * 24)));
        panel.add(scrollTabla, BorderLayout.CENTER);

        return panel;
    }
}