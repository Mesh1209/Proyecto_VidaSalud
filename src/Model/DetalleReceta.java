package Model;

import lombok.Data;

@Data
public class DetalleReceta {
    private int id_detalle_receta;
    private int id_receta;
    private int id_medicamento;
    private int cantidad;
    private String dosis_instrucciones;
}
