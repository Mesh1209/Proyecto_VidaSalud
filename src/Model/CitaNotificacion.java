package Model;

import lombok.Data;

@Data
public class CitaNotificacion {
    private String telefono;
    private String paciente;
    private String medico;
    private String especialidad;
    private String fecha;
    private String hora;
}
