package Formularios;

import Controler.ControladorMedicamento;
import Controler.ControladorReceta;
import Model.DetalleReceta;
import Model.Medicamento;
import Model.RecetaConsulta;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class FrmGenerarRecetas extends javax.swing.JFrame {

    ControladorReceta controladorReceta = new ControladorReceta();
    ControladorMedicamento controladorMedicamento = new ControladorMedicamento();
    private DefaultTableModel modelo;
    private DefaultTableModel modeloReceta;
    
    public FrmGenerarRecetas() {
        initComponents();
        IniciarModeloTabla();
        TamañoTabla();
        DiseñoTabla1();
        DiseñoTabla2();
    }
    
    private void IniciarModeloTabla(){
        modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
            }
        };
        
        modeloReceta = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
            }
        };
        
        modelo.addColumn("id_medicamento");
        modelo.addColumn("nombre_comercial");
        modelo.addColumn("nombre_generico");
        modelo.addColumn("presentacion");       
        modelo.addColumn("stock");
        modelo.addColumn("precio_venta");
        TablaMedicamento.setModel(modelo);
        
        modeloReceta.addColumn("id_medicamento");
        modeloReceta.addColumn("nombre_comercial");
        modeloReceta.addColumn("nombre_generico");
        modeloReceta.addColumn("presentacion");       
        modeloReceta.addColumn("stock");
        modeloReceta.addColumn("precio_venta");
        TablaDetalleReceta.setModel(modeloReceta);
    }
    
    private void TamañoTabla(){
        TableColumnModel columnModel = TablaMedicamento.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(10);
        columnModel.getColumn(4).setPreferredWidth(10);
        columnModel.getColumn(5).setPreferredWidth(10);
        
        TableColumnModel columnModelReceta = TablaDetalleReceta.getColumnModel();
        columnModelReceta.getColumn(0).setPreferredWidth(10);
        columnModelReceta.getColumn(4).setPreferredWidth(10);
        columnModelReceta.getColumn(5).setPreferredWidth(10); 
    }
    
    private void DiseñoTabla1(){
        TablaMedicamento.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // 2. Personalizamos el Renderizador del Header para forzar el color de fondo
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel headerLabel = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                headerLabel.setBackground(new Color(33, 150, 243)); // Azul Material
                headerLabel.setForeground(Color.WHITE);
                headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
                headerLabel.setHorizontalAlignment(JLabel.CENTER);
                headerLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, new Color(25, 118, 210)));
                headerLabel.setOpaque(true);

                return headerLabel;
            }
        };

        // 3. Aplicamos el nuevo renderizador a todas las columnas
        for (int i = 0; i < TablaMedicamento.getColumnModel().getColumnCount(); i++) {
            TablaMedicamento.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
    }
    
    private void DiseñoTabla2(){
        TablaDetalleReceta.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // 2. Personalizamos el Renderizador del Header para forzar el color de fondo
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel headerLabel = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                headerLabel.setBackground(new Color(33, 150, 243)); // Azul Material
                headerLabel.setForeground(Color.WHITE);
                headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
                headerLabel.setHorizontalAlignment(JLabel.CENTER);
                headerLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, new Color(25, 118, 210)));
                headerLabel.setOpaque(true);

                return headerLabel;
            }
        };

        // 3. Aplicamos el nuevo renderizador a todas las columnas
        for (int i = 0; i < TablaDetalleReceta.getColumnModel().getColumnCount(); i++) {
            TablaDetalleReceta.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
    }
    
    private void cargarDatosTabla(List<Medicamento> medicamentos) {
        // 1. Limpiamos la tabla siempre al inicio
        modelo.setRowCount(0);
        
        // 2. Verificar si la lista que llegó está vacía o es nula
        if (medicamentos == null || medicamentos.isEmpty()) {
            System.out.println("No hay medicamentos para mostrar con los criterios actuales");
            return;
        }

        for (Medicamento h : medicamentos) {
            modelo.addRow(new Object[]{
                h.getId_medicamento(),
                h.getNombre_comercial(), 
                h.getNombre_generico(), 
                h.getPresentacion(),
                h.getStock(), 
                h.getPrecio_venta() 
            });
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        txtIdCita = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaMedicamento = new javax.swing.JTable();
        btnBuscarReceta = new javax.swing.JButton();
        btnAgregarMedicamento = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaDetalleReceta = new javax.swing.JTable();
        btnRegistrar = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        txtNombreMedicamento = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnNombreMedicamento = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtDocumento = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtCodigoReceta = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtIdConsulta = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setText("idCita");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));
        getContentPane().add(txtIdCita, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 90, -1));

        TablaMedicamento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(TablaMedicamento);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, 440, 210));

        btnBuscarReceta.setBackground(new java.awt.Color(0, 153, 204));
        btnBuscarReceta.setText("jButton1");
        btnBuscarReceta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarRecetaActionPerformed(evt);
            }
        });
        getContentPane().add(btnBuscarReceta, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 30, -1));

        btnAgregarMedicamento.setBackground(new java.awt.Color(0, 204, 0));
        btnAgregarMedicamento.setText("Agregar");
        btnAgregarMedicamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarMedicamentoActionPerformed(evt);
            }
        });
        getContentPane().add(btnAgregarMedicamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 200, 170, 30));

        TablaDetalleReceta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(TablaDetalleReceta);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 720, 220));

        btnRegistrar.setBackground(new java.awt.Color(0, 204, 0));
        btnRegistrar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnRegistrar.setText("REGISTRAR");
        btnRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 320, -1, 40));

        jButton6.setBackground(new java.awt.Color(255, 0, 0));
        jButton6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton6.setText("ELIMINAR");
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 370, 110, 40));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("Cantidad");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 150, -1, -1));
        getContentPane().add(txtCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 150, 70, -1));
        getContentPane().add(txtNombreMedicamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 80, 120, -1));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setText("Nombre");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 50, -1, -1));

        btnNombreMedicamento.setBackground(new java.awt.Color(0, 153, 204));
        btnNombreMedicamento.setText("jButton3");
        btnNombreMedicamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNombreMedicamentoActionPerformed(evt);
            }
        });
        getContentPane().add(btnNombreMedicamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 80, 30, -1));

        jLabel4.setText("Nombre");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        txtNombre.setEditable(false);
        txtNombre.setBackground(new java.awt.Color(204, 204, 204));
        getContentPane().add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 170, 130, -1));

        jLabel5.setText("N° Documento");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, -1));

        txtDocumento.setEditable(false);
        txtDocumento.setBackground(new java.awt.Color(204, 204, 204));
        getContentPane().add(txtDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 200, 130, -1));

        jLabel7.setText("codigoreceta");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        txtCodigoReceta.setEditable(false);
        txtCodigoReceta.setBackground(new java.awt.Color(204, 204, 204));
        getContentPane().add(txtCodigoReceta, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 90, 130, -1));

        jLabel1.setText("IdConsulta");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        txtIdConsulta.setEditable(false);
        txtIdConsulta.setBackground(new java.awt.Color(204, 204, 204));
        getContentPane().add(txtIdConsulta, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 130, 130, -1));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setText("Detalle Receta");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, -1));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel9.setText("Medicamento");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, -1, -1));

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Consulta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setFocusable(false);
        jScrollPane3.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 270, 240));

        btnSalir.setBackground(new java.awt.Color(255, 0, 51));
        btnSalir.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSalir.setText("SALIR");
        btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 420, 110, 40));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        // TODO add your handling code here:          
        int idConsulta = Integer.parseInt(txtIdConsulta.getText().trim()); 
        String codigoReceta = txtCodigoReceta.getText().trim();
        int totalFilas = TablaDetalleReceta.getRowCount();
        
        if (totalFilas == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Debe agregar al menos un medicamento a la receta.", 
                "Tabla Vacía", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<DetalleReceta> listaMedicamentos = new ArrayList<>();
        
        for (int i = 0; i < totalFilas; i++){
            DetalleReceta det = new DetalleReceta();
            
            // --- ¡IMPORTANTE! Ajusta el número de columna (0, 1, 2, 3...) según tu JTable ---
            // Columna 0: ID del Medicamento
            int idMedicamento = Integer.parseInt(TablaDetalleReceta.getValueAt(i, 0).toString());
            // Columna 2: Cantidad (Ajusta el índice si es otra columna)
            int cantidad = Integer.parseInt(TablaDetalleReceta.getValueAt(i, 4).toString());
            // Columna 3: Dosis/Instrucciones (Ajusta el índice si es otra columna)
            String dosis = "campoobsoleto";
                    //TablaDetalleReceta.getValueAt(i, 3).toString().trim();

            // Validaciones lógicas rápidas
            if (cantidad <= 0) {
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "La cantidad en la fila " + (i + 1) + " debe ser mayor a 0.", 
                    "Error de cantidad", 
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (dosis.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Las instrucciones en la fila " + (i + 1) + " no pueden estar vacías.", 
                    "Error de instrucciones", 
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            det.setId_medicamento(idMedicamento);
            det.setCantidad(cantidad);
            det.setDosis_instrucciones(dosis);
            
            listaMedicamentos.add(det);
        }
        
        boolean exito = controladorReceta.registrarRecetaCompleta(idConsulta, codigoReceta, listaMedicamentos);

        if (exito) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "¡La receta y sus detalles han sido registrados exitosamente!", 
                "Registro Exitoso", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
            // Método opcional para limpiar tu formulario y resetear la tabla
            //limpiarFormularioReceta(); 
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "No se pudo guardar la receta en la base de datos.", 
                "Error de Registro", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnNombreMedicamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNombreMedicamentoActionPerformed
        // TODO add your handling code here:
        String nombre = txtNombreMedicamento.getText();
        List<Medicamento> todosMedicamentos = controladorMedicamento.listarMedicamentosNombre(nombre);
        cargarDatosTabla(todosMedicamentos);
    }//GEN-LAST:event_btnNombreMedicamentoActionPerformed

    private void btnAgregarMedicamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarMedicamentoActionPerformed
        // TODO add your handling code here:
        int[] filasSeleccionadas = TablaMedicamento.getSelectedRows();
        int cantidad = Integer.parseInt(txtCantidad.getText());
        // 3. Recorrer las filas seleccionadas y agregarlas a la de destino
        for (int i = 0; i < filasSeleccionadas.length; i++) {
            int filaIndex = filasSeleccionadas[i];

            // Suponiendo que tu tabla tiene 3 columnas (ej. ID, Nombre, Precio)
            Object[] fila = new Object[6];
            fila[0] = modelo.getValueAt(filaIndex, 0);
            fila[1] = modelo.getValueAt(filaIndex, 1);
            fila[2] = modelo.getValueAt(filaIndex, 2);
            fila[3] = modelo.getValueAt(filaIndex, 3);
            
            fila[4] = cantidad;
            Double precio = (Double)modelo.getValueAt(filaIndex, 5);
            Double precioFinal = precio * cantidad;
            fila[5] = precioFinal;

            // Agregar la fila al modelo destino
            modeloReceta.addRow(fila);
        }
    }//GEN-LAST:event_btnAgregarMedicamentoActionPerformed

    private void btnBuscarRecetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarRecetaActionPerformed
        // TODO add your handling code here:
        List<RecetaConsulta> recetaConsultas = controladorReceta.listarIdConsulta(Integer.parseInt(txtIdCita.getText()));
        int idCita = recetaConsultas.get(0).getIdCita();
        int idConsulta = recetaConsultas.get(0).getIdConsulta();
        String nombre = recetaConsultas.get(0).getNombre();
        String documento = recetaConsultas.get(0).getDocumento();
        
        txtIdConsulta.setText(idConsulta+"");
        txtCodigoReceta.setText("RCT-"+idConsulta);
        txtNombre.setText(nombre);
        txtDocumento.setText(documento);
    }//GEN-LAST:event_btnBuscarRecetaActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmGenerarRecetas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmGenerarRecetas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmGenerarRecetas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmGenerarRecetas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmGenerarRecetas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaDetalleReceta;
    private javax.swing.JTable TablaMedicamento;
    private javax.swing.JButton btnAgregarMedicamento;
    private javax.swing.JButton btnBuscarReceta;
    private javax.swing.JButton btnNombreMedicamento;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtCodigoReceta;
    private javax.swing.JTextField txtDocumento;
    private javax.swing.JTextField txtIdCita;
    private javax.swing.JTextField txtIdConsulta;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNombreMedicamento;
    // End of variables declaration//GEN-END:variables
}
