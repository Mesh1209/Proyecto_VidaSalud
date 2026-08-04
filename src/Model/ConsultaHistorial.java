package Model;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class ConsultaHistorial {
    private int idConsulta;
    private int idPaciente;
    private int idMedico;
    private int idCita;
    private String sintomas;
    private String diagnostico;
    private String tratamientoIndicaciones;
    private Timestamp fechaConsulta;
}
