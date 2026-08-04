package Model;
import lombok.Data;

//relizar clean an build
@Data
public class Medico {
    private int id_medico;
    private String nombre;
    private String apellido;
    private Especialidad especialidad;
    private String numero_documento;
    private String telefono;
    private String email;
    private int id_colegiatura;
    private String duracion_turno_minutos;
    private boolean activo;
    
    // CONSTRUCTOR VACÍO
    public Medico() {
    }
    
    // CONSTRUCTOR COMPLETO
    public Medico(int id_medico, String nombre, String apellido, Especialidad especialidad, 
                  String numero_documento, String telefono, String email, int id_colegiatura, 
                  String duracion_turno_minutos, boolean activo) {
        this.id_medico = id_medico;
        this.nombre = nombre;
        this.apellido = apellido;
        this.especialidad = especialidad;
        this.numero_documento = numero_documento;
        this.telefono = telefono;
        this.email = email;
        this.id_colegiatura = id_colegiatura;
        this.duracion_turno_minutos = duracion_turno_minutos;
        this.activo = activo;
    }
    
    // GETTERS
    public int getId_medico() {
        return id_medico;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public Especialidad getEspecialidad() {
        return especialidad;
    }
    
    public String getNumero_documento() {
        return numero_documento;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public String getEmail() {
        return email;
    }
    
    public int getId_colegiatura() {
        return id_colegiatura;
    }
    
    public String getDuracion_turno_minutos() {
        return duracion_turno_minutos;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    // SETTERS
    public void setId_medico(int id_medico) {
        this.id_medico = id_medico;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }
    
    public void setNumero_documento(String numero_documento) {
        this.numero_documento = numero_documento;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setId_colegiatura(int id_colegiatura) {
        this.id_colegiatura = id_colegiatura;
    }
    
    public void setDuracion_turno_minutos(String duracion_turno_minutos) {
        this.duracion_turno_minutos = duracion_turno_minutos;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
