package Excel;

import Model.Medicamento;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelMedicamentoReader {
    public static List<Medicamento> leerMedicamentosDesdeExcel(File archivoExcel) {
        List<Medicamento> lista = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(archivoExcel);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            // Saltamos la fila 0 (encabezados)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Medicamento m = new Medicamento();
                
                // Mapeo del Excel a las propiedades de la clase (Ajusta las posiciones de celda según tu Excel)
                m.setCodigo_medicamento(formatter.formatCellValue(row.getCell(0)).trim());
                m.setNombre_comercial(formatter.formatCellValue(row.getCell(1)).trim());
                m.setNombre_generico(formatter.formatCellValue(row.getCell(2)).trim());
                m.setPresentacion(formatter.formatCellValue(row.getCell(3)).trim());
                
                String stockStr = formatter.formatCellValue(row.getCell(4)).trim();
                String precioStr = formatter.formatCellValue(row.getCell(5)).trim().replace(",", ".");

                m.setStock(stockStr.isEmpty() ? 0 : Integer.parseInt(stockStr));
                m.setPrecio_venta(precioStr.isEmpty() ? 0.0 : Double.parseDouble(precioStr));

                // Solo agregamos si el código no viene vacío
                if (!m.getCodigo_medicamento().isEmpty()) {
                    lista.add(m);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al leer Excel: " + e.getMessage());
        }

        return lista;
    }
}
