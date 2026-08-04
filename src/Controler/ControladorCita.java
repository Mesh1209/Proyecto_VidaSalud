package Controler;

import Model.Cita;
import Model.CitaDAO;
import Model.CitaNotificacion;
import Model.ListaHorarioCita;
import Model.Medico;
import Model.Paciente;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ControladorCita {   
    private CitaDAO citadao;
    
    public ControladorCita() {
        this.citadao = new CitaDAO();
        System.out.println("ControladorHorario inicializado");
    }
    
    //Traer datos de paciente recordar cambiar nombre
    public List<Paciente> listarHorariosBusqueda(String dni){
        if (citadao == null) {
            System.err.println("ERROR: listarHorariosBusqueda es null");
            return null;
        }
        List<Paciente> lista = citadao.obtenerDatoPaciente(dni);
        return lista;
    }
    
    public List<ListaHorarioCita> buscarHorariosMedicos(String nombre, String apellido, String especialidad, LocalDate fecha, LocalTime hora){
        if (citadao == null) {
            System.err.println("ERROR: listarMedicoApellido es null");
            return null;
        }
        List<ListaHorarioCita> lista = citadao.buscarHorariosMedicos(nombre, apellido, especialidad, fecha, hora);
        System.out.println(" 1:"+nombre+" 2:"+apellido+" 3:"+especialidad+" 4:"+fecha+" 5:"+hora);
        return lista;
    }
    
    public List<ListaHorarioCita> listarMedicoHorario(){
        List<ListaHorarioCita> lista = citadao.ListaMedicoHorario();
        return lista;
    }
    
    public int RegistrarCita(int id_paciente, int id_medico, LocalDateTime fecha_hora_inicio
            , LocalDateTime fecha_hora_fin, String motivo_consulta, int id_usuario_registro){
        
        Paciente paciente = new Paciente();
        paciente.setId_paciente(id_paciente);
        
        Medico medico = new Medico();
        medico.setId_medico(id_medico);
        
        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setFecha_hora_inicio(fecha_hora_inicio);
        cita.setFecha_hora_fin(fecha_hora_fin);
        cita.setMotivo_consulta(motivo_consulta);
        cita.setId_usuario_registro(id_usuario_registro);
        
        System.out.println(cita);
        
        int resultado = citadao.registrarCita(cita);
        if (resultado > 0) {
            System.out.println("Cita registrada exitosamente");
        } else {
            System.err.println("No se pudo registar la Cita");
        }
        
        return resultado;
    }
    
    public List<Cita> listarCita(){
        if (citadao == null) {
            System.err.println("ERROR: horariodao es null");
            return null;
        }
        List<Cita> lista = citadao.listarCitas();
        return lista;
    }
    
    public List<Cita> listarCitaFiltro(Integer idCita , String pacienteNombre, String estado , LocalDateTime fechaInicio,LocalDateTime fechaFin){
        if (citadao == null) {
            System.err.println("ERROR: horariodao es null");
            return null;
        }
        List<Cita> lista = citadao.listarCitasFiltro(idCita, pacienteNombre, estado, fechaInicio,fechaFin);
        return lista;
    }
    
    //no usaremos para notificacion mycho codigo por cambiar mejor un uso directo
}
