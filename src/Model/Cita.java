package Model;
import java.time.LocalDateTime;
import lombok.Data;//Libreia para simplificar codigo

@Data
public class Cita {
    private int id_cita;
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime fecha_hora_inicio;
    private LocalDateTime fecha_hora_fin;
    private String estado;
    private String motivo_consulta;
    private int id_usuario_registro;
}
