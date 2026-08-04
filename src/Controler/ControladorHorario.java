package Controler;
import Model.Horario;
import Model.HorarioDAO;
import Model.Medico;
import java.time.LocalDateTime;
import java.util.List;

public class ControladorHorario {
    private HorarioDAO horariodao;
    
    // CONSTRUCTOR - Inicializa el DAO
    public ControladorHorario() {
        this.horariodao = new HorarioDAO();
        System.out.println("ControladorHorario inicializado");
    }
    public List<Horario> listarHorarios(){
        if (horariodao == null) {
            System.err.println("ERROR: horariodao es null");
            return null;
        }
        List<Horario> lista = horariodao.obtenerHorarioMedico();
        return lista;
    }
    
    public List<Horario> listarHorariosBusqueda(String apellido){
        if (horariodao == null) {
            System.err.println("ERROR: horariodao es null");
            return null;
        }
        List<Horario> lista = horariodao.obtenerHorarioBusquedaMedico(apellido);
        return lista;
    }
    
    public boolean ActualizarHorario(int idMedico, int diaSemana, LocalDateTime horaInicio, LocalDateTime horaFin){
        if (idMedico <= 0) {
            System.err.println("Error: ID de médico inválido");
            return false;
        }
        
        if (diaSemana < 1 || diaSemana > 7) {
            System.err.println("Error: Día de semana inválido (1-7)");
            return false;
        }
        
        if (horaInicio == null || horaFin == null) {
            System.err.println("Error: Horas no pueden ser null");
            return false;
        }
        
        if (horaFin.isBefore(horaInicio)) {
            System.err.println("Error: La hora de fin debe ser después de la hora de inicio");
            return false;
        }
        
        // Crear objetos
        Medico medico = new Medico();
        medico.setId_medico(idMedico);
        
        Horario horario = new Horario();
        horario.setMedico(medico);
        horario.setDia_semana(diaSemana);
        horario.setHora_inicio(horaInicio);
        horario.setHora_fin(horaFin);
        
        // Delegar al DAO
        boolean resultado = horariodao.actualizarHorario(horario);
        
        if (resultado) {
            System.out.println("Horario actualizado exitosamente");
        } else {
            System.err.println("No se pudo actualizar el horario");
        }
        
        return resultado;
    }
    
    public boolean EliminarHorario(int idHorario){
        if( horariodao.eliminarHorario(idHorario)){
            return true;  
        }
        else{
            return false;
        }
    }
}
