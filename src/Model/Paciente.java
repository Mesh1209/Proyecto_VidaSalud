package Model;
import java.sql.Timestamp;
import java.time.LocalDate;
import lombok.Data;

@Data
public class Paciente {
    private int id_paciente;
    private String nombre;
    private String apellido;
    private String tipo_documento;
    private String numero_documento;
    private LocalDate fecha_nacimiento;
    private String genero;
    private String telefono;
    private String email;
    private String direccion;
    private Timestamp fechaRegistro;
}
