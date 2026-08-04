package Model;

import java.sql.Timestamp;
import java.util.List;
import lombok.Data;


@Data
public class Receta {
    private int idReceta;
    private int idConsulta;
    private String codigoReceta;
    private Timestamp fechaEmision;
    private List<DetalleReceta> detalles;
}
