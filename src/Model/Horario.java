package Model;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Data;


public class Horario {
    private int id_horario;
    private Medico medico;
    private int dia_semana;
    private LocalDateTime  hora_inicio;
    private LocalDateTime  hora_fin;
    
    public Horario() {
    }
    
    // CONSTRUCTOR CON PARÁMETROS
    public Horario(int id_horario, Medico medico, int dia_semana, LocalDateTime hora_inicio, LocalDateTime hora_fin) {
        this.id_horario = id_horario;
        this.medico = medico;
        this.dia_semana = dia_semana;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
    }
    
    // GETTERS
    public int getId_horario() {
        return id_horario;
    }
    
    public Medico getMedico() {
        return medico;
    }
    
    public int getDia_semana() {
        return dia_semana;
    }
    
    public LocalDateTime getHora_inicio() {
        return hora_inicio;
    }
    
    public LocalDateTime getHora_fin() {
        return hora_fin;
    }
    
    // SETTERS
    public void setId_horario(int id_horario) {
        this.id_horario = id_horario;
    }
    
    public void setMedico(Medico medico) {
        this.medico = medico;
    }
    
    public void setDia_semana(int dia_semana) {
        this.dia_semana = dia_semana;
    }
    
    public void setHora_inicio(LocalDateTime hora_inicio) {
        this.hora_inicio = hora_inicio;
    }
    
    public void setHora_fin(LocalDateTime hora_fin) {
        this.hora_fin = hora_fin;
    }
}
