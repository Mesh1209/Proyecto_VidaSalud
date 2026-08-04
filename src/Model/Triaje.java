package Model;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Triaje {
    private int id_triaje;
    private int id_cita;
    private int id_enfermero;
    private double peso_kg; 
    private double talla_cm; 
    private double temperatura_c; 
    private String presion_arterial; 
    private int frecuencia_cardiaca; 
    private int saturacion_oxigeno; 
    private String notas_triaje; 
    private LocalDateTime fecha_registro; 
}
