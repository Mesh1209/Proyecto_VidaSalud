package Controler;

import Model.CitaPendiente;
import Model.ConsultaHistorial;
import Model.ConsultaHistorialDAO;
import java.util.ArrayList;
import java.util.List;

public class ControladorConsulta {
    private ConsultaHistorialDAO consultadao;

    public ControladorConsulta() {
        this.consultadao = new ConsultaHistorialDAO();
        System.out.println("ControladorConsultaHistorial inicializado");
    }
    
    public List<CitaPendiente> listarCitasParaConsulta() {
        if (consultadao == null) {
            System.err.println("ERROR: consultadao es null");
            return new ArrayList<>();
        }
        
        List<CitaPendiente> lista = consultadao.obtenerCitasParaConsulta();
        
        if (lista.isEmpty()) {
            System.out.println("No hay citas pendientes disponibles para consulta actualmente.");
        } else {
            System.out.println("Citas pendientes cargadas exitosamente. Cantidad: " + lista.size());
        }
        
        return lista;
    }
    
    public List<ConsultaHistorial> listarConsultas() {
        if (consultadao == null) {
            System.err.println("ERROR: consultadao es null");
            return new ArrayList<>();
        }
        return consultadao.listar();
    }
    
    public boolean CrearConsulta(int idPaciente, int idMedico, int idCita, String sintomas, String diagnostico, String tratamiento) {
        // Validaciones de negocio básicas
        if (idPaciente <= 0 || idMedico <= 0 || idCita <= 0) {
            System.err.println("Error: Las referencias de paciente, médico y cita deben ser mayores a 0");
            return false;
        }

        if (sintomas == null || sintomas.trim().isEmpty()) {
            System.err.println("Error: Los síntomas no pueden estar vacíos");
            return false;
        }

        if (diagnostico == null || diagnostico.trim().isEmpty()) {
            System.err.println("Error: El diagnóstico no puede estar vacío");
            return false;
        }

        // Crear el objeto ConsultaHistorial
        ConsultaHistorial consulta = new ConsultaHistorial();
        consulta.setIdPaciente(idPaciente);
        consulta.setIdMedico(idMedico);
        consulta.setIdCita(idCita);
        consulta.setSintomas(sintomas);
        consulta.setDiagnostico(diagnostico);
        consulta.setTratamientoIndicaciones(tratamiento); // Este puede ser opcional (NULL en BD)

        // Delegar al DAO
        boolean resultado = consultadao.insertar(consulta);

        if (resultado) {
            System.out.println("Consulta registrada exitosamente");
        } else {
            System.err.println("No se pudo registrar la consulta");
        }

        return resultado;
    }
    
    public boolean ActualizarConsulta(int idConsulta, int idPaciente, int idMedico, int idCita, String sintomas, String diagnostico, String tratamiento) {
        // Validaciones
        if (idConsulta <= 0) {
            System.err.println("Error: ID de consulta inválido");
            return false;
        }

        if (idPaciente <= 0 || idMedico <= 0 || idCita <= 0) {
            System.err.println("Error: Las referencias de paciente, médico y cita deben ser mayores a 0");
            return false;
        }

        if (sintomas == null || sintomas.trim().isEmpty()) {
            System.err.println("Error: Los síntomas no pueden estar vacíos");
            return false;
        }

        if (diagnostico == null || diagnostico.trim().isEmpty()) {
            System.err.println("Error: El diagnóstico no puede estar vacío");
            return false;
        }

        // Crear el objeto con datos actualizados
        ConsultaHistorial consulta = new ConsultaHistorial();
        consulta.setIdConsulta(idConsulta);
        consulta.setIdPaciente(idPaciente);
        consulta.setIdMedico(idMedico);
        consulta.setIdCita(idCita);
        consulta.setSintomas(sintomas);
        consulta.setDiagnostico(diagnostico);
        consulta.setTratamientoIndicaciones(tratamiento);

        // Delegar al DAO
        boolean resultado = consultadao.actualizar(consulta);

        if (resultado) {
            System.out.println("Consulta actualizada exitosamente");
        } else {
            System.err.println("No se pudo actualizar la consulta");
        }

        return resultado;
    }
}
