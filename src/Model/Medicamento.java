package Model;
import lombok.Data;

@Data
public class Medicamento {
    private int id_medicamento;
    private String nombre_comercial;
    private String nombre_generico;
    private String presentacion;
    private int stock;
    private double precio_venta;
    private String codigo_medicamento;
}
