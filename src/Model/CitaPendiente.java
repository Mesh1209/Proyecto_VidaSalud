package Model;

import lombok.Data;

@Data
public class CitaPendiente {
    private int idPaciente;
    private int idMedico;
    private int idCita;
    private String nombre;
    private String numero_documento;
}
