package Controler;

import Model.Medicamento;
import Model.MedicamentoDAO;
import java.util.ArrayList;
import java.util.List;

public class ControladorMedicamento {
    private MedicamentoDAO medicamentodao;
    
    public ControladorMedicamento() {
        this.medicamentodao = new MedicamentoDAO();
        System.out.println("ControladorMedicamento inicializado");
    }
    
    public List<Medicamento> listarMedicamentos() {
        if (medicamentodao == null) {
            System.err.println("ERROR: medicamentodao es null");
            return new ArrayList<>();
        }
        return medicamentodao.obtenerListaMedicamento();
    }
    
    public List<Medicamento> listarMedicamentosNombre(String nombre) {
        if (medicamentodao == null) {
            System.err.println("ERROR: medicamentodao es null");
            return new ArrayList<>();
        }
        return medicamentodao.obtenerListaMedicamentoNombre(nombre);
    }
    
    public boolean CrearMedicamento(String nombreComercial, String nombreGenerico, String presentacion, int stock, double precioVenta, String codigo_medicamento) {
        // Validaciones de negocio básicas
        if (nombreComercial == null || nombreComercial.trim().isEmpty()) {
            System.err.println("Error: El nombre comercial no puede estar vacío");
            return false;
        }
        
        if (stock < 0) {
            System.err.println("Error: El stock no puede ser negativo");
            return false;
        }
        
        if (precioVenta <= 0) {
            System.err.println("Error: El precio de venta debe ser mayor a 0");
            return false;
        }
        
        // Crear el objeto Medicamento
        Medicamento medicamento = new Medicamento();
        medicamento.setNombre_comercial(nombreComercial);
        medicamento.setNombre_generico(nombreGenerico);
        medicamento.setPresentacion(presentacion);
        medicamento.setStock(stock);
        medicamento.setPrecio_venta(precioVenta);
        medicamento.setCodigo_medicamento(codigo_medicamento);
        // Delegar al DAO
        boolean resultado = medicamentodao.crearMedicamento(medicamento);
        
        if (resultado) {
            System.out.println("Medicamento creado exitosamente");
        } else {
            System.err.println("No se pudo crear el medicamento");
        }
        
        return resultado;
    }
    
    public boolean ActualizarMedicamento(int idMedicamento, String nombreComercial, String nombreGenerico, String presentacion, int stock, double precioVenta) {
        // Validaciones
        if (idMedicamento <= 0) {
            System.err.println("Error: ID de medicamento inválido");
            return false;
        }
        
        if (nombreComercial == null || nombreComercial.trim().isEmpty()) {
            System.err.println("Error: El nombre comercial no puede estar vacío");
            return false;
        }
        
        if (stock < 0) {
            System.err.println("Error: El stock no puede ser negativo");
            return false;
        }
        
        if (precioVenta <= 0) {
            System.err.println("Error: El precio de venta debe ser mayor a 0");
            return false;
        }
        
        // Crear el objeto con los datos actualizados
        Medicamento medicamento = new Medicamento();
        medicamento.setId_medicamento(idMedicamento);
        medicamento.setNombre_comercial(nombreComercial);
        medicamento.setNombre_generico(nombreGenerico);
        medicamento.setPresentacion(presentacion);
        medicamento.setStock(stock);
        medicamento.setPrecio_venta(precioVenta);
        
        // Delegar al DAO
        boolean resultado = medicamentodao.actualizarMedicamento(medicamento);
        
        if (resultado) {
            System.out.println("Medicamento actualizado exitosamente");
        } else {
            System.err.println("No se pudo actualizar el medicamento");
        }
        
        return resultado;
    }
    
    public boolean EliminarMedicamento(int idMedicamento) {
        if (idMedicamento <= 0) {
            System.err.println("Error: ID de medicamento inválido");
            return false;
        }
        
        boolean resultado = medicamentodao.eliminarMedicamento(idMedicamento);
        
        if (resultado) {
            System.out.println("Medicamento eliminado exitosamente");
        } else {
            System.err.println("No se pudo eliminar el medicamento");
        }
        
        return resultado;
    }
}
