package PDF;

import Model.Cita;
import Model.pdfCita;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import static org.apache.commons.io.IOUtils.writer;

import org.openpdf.text.Document;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.Element;
import org.openpdf.text.Rectangle;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.pdf.PdfWriter;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfContentByte;
import org.openpdf.text.pdf.PdfPageEventHelper;
import org.openpdf.text.pdf.ColumnText;

public class BotonPdfEditorRender extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
    private final JButton boton;
    private final JTable tabla;
    private int filaActual;

    public BotonPdfEditorRender(JTable tabla) {
        this.tabla = tabla;
        this.boton = new JButton("Ver PDF");
        
        // Estilos para el botón
        this.boton.setBackground(new Color(41, 128, 185));
        this.boton.setForeground(Color.WHITE);
        this.boton.setFocusPainted(false);
        //Evento click
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
        return "Ver PDF";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Importante: detiene la animación de presionado en la celda
        fireEditingStopped();

        // Obtenemos los datos clave de la fila usando los índices de tus columnas
        Object idCita = tabla.getValueAt(filaActual, 2);       // Columna idCita
        
        abrirVentanaPDF((Integer)idCita);
    }

    private void abrirVentanaPDF(int idCita) {
    // 1. Consultar la información completa de la cita desde la BD
        pdfCita citaDAO = new pdfCita();
        Cita cita = citaDAO.obtenerCitaPreviaPorId(idCita);

        if (cita == null) {
            JOptionPane.showMessageDialog(null, "No se encontró información para la Cita N° " + idCita, "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Formatear datos para el HTML
        String nombrePaciente = (cita.getPaciente() != null) 
                ? cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido() 
                : "No registrado";

        String docPaciente = (cita.getPaciente() != null) 
                ? cita.getPaciente().getTipo_documento() + ": " + cita.getPaciente().getNumero_documento() 
                : "-";

        String medico = (cita.getMedico() != null) 
                ? "Dr(a). " + cita.getMedico().getNombre() + " " + cita.getMedico().getApellido() 
                : "Por asignar";

        String especialidad = (cita.getMedico() != null && cita.getMedico().getEspecialidad() != null) 
                ? cita.getMedico().getEspecialidad().getNombre() 
                : "General";

        // 2. Crear Ventana emergente (Modal)
        JDialog visor = new JDialog((Frame) null, "Expediente Digital - Cita #" + idCita, true);
        visor.setSize(520, 380);
        visor.setLayout(new BorderLayout());
        visor.setLocationRelativeTo(tabla); // Centra la ventana respecto a tu tabla Swing

        // Cuerpo con HTML enriquecido trayendo los datos del DAO
        JLabel lblInfo = new JLabel("<html><center style='padding: 15px;'><h2 style='color:#2c3e50; margin-bottom:5px;'>Consulta Médica Registrada</h2>"
                + "<p style='font-size:12px; line-height: 1.4; color: #34495e;'>"
                + "<b>N° Cita:</b> " + idCita + "<br>"
                + "<b>Paciente:</b> " + nombrePaciente + "<br>"
                + "<b>Documento:</b> " + docPaciente + "<br>"
                + "<b>Médico:</b> " + medico + "<br>"
                + "<b>Especialidad:</b> " + especialidad + "</p></center></html>", SwingConstants.CENTER);
        visor.add(lblInfo, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        JButton btnGenerar = new JButton("Generar y Abrir PDF");
        JButton btnCerrar = new JButton("Cerrar");

        // Estilos del Botón Verde (Generar PDF)
        btnGenerar.setBackground(new Color(46, 204, 113));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        btnGenerar.setFocusPainted(false);
        btnGenerar.setBorderPainted(false);
        btnGenerar.setOpaque(true);
        btnGenerar.setContentAreaFilled(true);

        // Estilos del Botón Gris (Cerrar)
        btnCerrar.setBackground(new Color(189, 195, 199));
        btnCerrar.setForeground(new Color(44, 62, 80));
        btnCerrar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setOpaque(true);
        btnCerrar.setContentAreaFilled(true);

        // Acciones de los botones
        btnGenerar.addActionListener(al -> {
            generarYDescargarPDF(idCita); // Solo enviamos idCita
            visor.dispose();              // Cierra la ventana tras abrir el PDF
        });

        btnCerrar.addActionListener(al -> visor.dispose());

        panelAcciones.add(btnGenerar);
        panelAcciones.add(btnCerrar);
        visor.add(panelAcciones, BorderLayout.SOUTH);

        visor.setVisible(true);
    }
    
    private void generarYDescargarPDF(int idCita) {
        Document document = new Document();
        try {
            // Instancias tu DAO y ejecutas la consulta
            pdfCita pdfCitaDAO = new pdfCita();
            Cita cita = pdfCitaDAO.obtenerCitaPreviaPorId(idCita);

            if (cita == null) {
                JOptionPane.showMessageDialog(null, "No se encontró la cita N° " + idCita, "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 2. EXTRAER TODOS LOS DATOS TRAÍDOS POR TU SQL
            String nombrePaciente = (cita.getPaciente() != null) 
                    ? cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido() 
                    : "No registrado";

            String docPaciente = (cita.getPaciente() != null) 
                    ? cita.getPaciente().getTipo_documento() + ": " + cita.getPaciente().getNumero_documento() 
                    : "-";

            String telPaciente = (cita.getPaciente() != null && cita.getPaciente().getTelefono() != null) 
                    ? cita.getPaciente().getTelefono() 
                    : "No registrado";

            String nombreMedico = (cita.getMedico() != null) 
                    ? "Dr(a). " + cita.getMedico().getNombre() + " " + cita.getMedico().getApellido() 
                    : "Por asignar";

            String especialidad = (cita.getMedico() != null && cita.getMedico().getEspecialidad() != null) 
                    ? cita.getMedico().getEspecialidad().getNombre() 
                    : "General";

            int colegiatura = (cita.getMedico() != null)
                    ? cita.getMedico().getId_colegiatura()
                    : 0;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String fechaHora = (cita.getFecha_hora_inicio() != null) 
                    ? cita.getFecha_hora_inicio().format(formatter) 
                    : "Sin programar";

            String estado = (cita.getEstado() != null) ? cita.getEstado() : "Pendiente";

            String motivo = (cita.getMotivo_consulta() != null && !cita.getMotivo_consulta().trim().isEmpty()) 
                    ? cita.getMotivo_consulta() 
                    : "Consulta médica programada.";
            // Creamos un archivo temporal en la carpeta temporal del Sistema Operativo
            String rutaArchivo = System.getProperty("java.io.tmpdir") + File.separator + "Cita_" + idCita + ".pdf";
            File file = new File(rutaArchivo);
            
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            // Pie de página con numeración
            writer.setPageEvent(new PdfPageEventHelper() {
                @Override
                public void onEndPage(PdfWriter writer, Document document) {
                    PdfContentByte cb = writer.getDirectContent();
                    Phrase footer = new Phrase("Página " + writer.getPageNumber(),
                            FontFactory.getFont(FontFactory.HELVETICA, 8, new Color(120, 120, 120)));
                    ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer,
                            (document.right() + document.left()) / 2, document.bottom() - 20, 0);
                }
            });
            document.open();
            
            // Colores del tema
            Color azulPrincipal = new Color(31, 78, 121);
            Color grisTexto = new Color(80, 80, 80);
            Color grisClaroFondo = new Color(240, 244, 248);

            // ===== ENCABEZADO CON FONDO DE COLOR =====
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);

            PdfPCell headerCell = new PdfPCell();
            headerCell.setBackgroundColor(azulPrincipal);
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setPadding(12);

            Paragraph titulo = new Paragraph("TICKET DE CITA MÉDICA",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.WHITE));
            titulo.setAlignment(Element.ALIGN_CENTER);

            Paragraph subtitulo = new Paragraph("Comprobante de Atención Previa",
                    FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(220, 230, 240)));
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingBefore(3);

            headerCell.addElement(titulo);
            headerCell.addElement(subtitulo);
            headerTable.addCell(headerCell);
            document.add(headerTable);

            // Espaciador
            document.add(crearEspacio(10));
            
            // ===== RESUMEN DE LA CITA (Cajas superiores) =====
            PdfPTable tableResumen = new PdfPTable(3);
            tableResumen.setWidthPercentage(100);
            tableResumen.addCell(crearCeldaHeader("N° Cita", String.valueOf(idCita), azulPrincipal, grisClaroFondo));
            tableResumen.addCell(crearCeldaHeader("Fecha y Hora", fechaHora, azulPrincipal, grisClaroFondo));
            tableResumen.addCell(crearCeldaHeader("Estado", estado, azulPrincipal, grisClaroFondo));
            document.add(tableResumen);

            document.add(crearEspacio(12));
            
            // ===== SECCIÓN 1: DATOS DEL PACIENTE =====
            Paragraph secPaciente = new Paragraph("1. INFORMACIÓN DEL PACIENTE",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, azulPrincipal));
            secPaciente.setSpacingAfter(6);
            document.add(secPaciente);

            PdfPTable tablePaciente = crearTablaDosColumnas();
            agregarFilaInfo(tablePaciente, "Nombre Completo:", nombrePaciente, grisClaroFondo, grisTexto);
            agregarFilaInfo(tablePaciente, "Documento:", docPaciente, Color.WHITE, grisTexto);
            agregarFilaInfo(tablePaciente, "Teléfono:", telPaciente, grisClaroFondo, grisTexto);
            document.add(tablePaciente);

            document.add(crearEspacio(10));

            // ===== SECCIÓN 2: DATOS DEL MÉDICO =====
            Paragraph secMedico = new Paragraph("2. MÉDICO Y ESPECIALIDAD",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, azulPrincipal));
            secMedico.setSpacingAfter(6);
            document.add(secMedico);

            PdfPTable tableMedico = crearTablaDosColumnas();
            agregarFilaInfo(tableMedico, "Médico Tratante:", nombreMedico, grisClaroFondo, grisTexto);
            agregarFilaInfo(tableMedico, "Especialidad:", especialidad, Color.WHITE, grisTexto);
            agregarFilaInfo(tableMedico, "N° Colegiatura:", colegiatura+"", grisClaroFondo, grisTexto);
            document.add(tableMedico);

            document.add(crearEspacio(10));

            // ===== SECCIÓN 3: MOTIVO DE CONSULTA =====
            Paragraph secMotivo = new Paragraph("3. MOTIVO DE CONSULTA",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, azulPrincipal));
            secMotivo.setSpacingAfter(6);
            document.add(secMotivo);

            PdfPTable tableMotivo = new PdfPTable(1);
            tableMotivo.setWidthPercentage(100);

            PdfPCell cellMotivo = new PdfPCell(new Phrase(motivo, FontFactory.getFont(FontFactory.HELVETICA, 9, grisTexto)));
            cellMotivo.setPadding(8);
            cellMotivo.setBackgroundColor(grisClaroFondo);
            cellMotivo.setBorderColor(new Color(220, 220, 220));

            tableMotivo.addCell(cellMotivo);
            document.add(tableMotivo);

            // ===== NOTA FINAL =====
            Paragraph notaFinal = new Paragraph(
                    "\n* Apersonarse en recepción 15 minutos antes de la hora indicada.",
                    FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, new Color(130, 130, 130)));
            notaFinal.setSpacingBefore(15);
            notaFinal.setAlignment(Element.ALIGN_CENTER);
            document.add(notaFinal);

            document.close();

            // Forzar al Sistema Operativo a abrir el PDF generado con su programa predeterminado
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {
                JOptionPane.showMessageDialog(tabla, "PDF guardado en: " + rutaArchivo + " (No se pudo abrir automáticamente).");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(tabla, "Error al procesar el PDF con OpenPDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    // ===== MÉTODOS AUXILIARES PARA EL DISEÑO =====

    private Paragraph crearEspacio(float espacio) {
        Paragraph p = new Paragraph(" ");
        p.setSpacingAfter(espacio);
        return p;
    }
    
    private PdfPTable crearTablaDosColumnas() throws Exception {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{30f, 70f});
        return table;
    }
    private void agregarFilaInfo(PdfPTable table, String etiqueta, String valor, Color fondo, Color colorTexto) {
        PdfPCell cellEtiqueta = new PdfPCell(new Phrase(etiqueta, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, colorTexto)));
        cellEtiqueta.setBackgroundColor(fondo);
        cellEtiqueta.setPadding(5);
        cellEtiqueta.setBorderColor(new Color(220, 220, 220));

        PdfPCell cellValor = new PdfPCell(new Phrase(valor, FontFactory.getFont(FontFactory.HELVETICA, 9, colorTexto)));
        cellValor.setBackgroundColor(fondo);
        cellValor.setPadding(5);
        cellValor.setBorderColor(new Color(220, 220, 220));

        table.addCell(cellEtiqueta);
        table.addCell(cellValor);
    }
    
    private PdfPCell crearCeldaHeader(String titulo, String valor, Color colorBorde, Color fondo) {
        Phrase phrase = new Phrase();
        phrase.add(new Paragraph(titulo, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, Color.GRAY)));
        phrase.add(new Paragraph(valor, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, colorBorde)));

        PdfPCell cell = new PdfPCell(phrase);
        cell.setBackgroundColor(fondo);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(6);
        cell.setBorderColor(colorBorde);
        return cell;
    }
}
