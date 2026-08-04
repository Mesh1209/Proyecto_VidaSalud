package Model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Data;

@Data
public class ListaHorarioCita {
    private int id_medico;
    private String nombre;
    private String apellido;
    private String especialidad;
    private int dia_semana;
    private LocalDateTime hora_inicio;
    private LocalDateTime hora_fin;
}
