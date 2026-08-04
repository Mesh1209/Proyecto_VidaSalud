package Controler;

import Model.DetalleReceta;
import Model.Receta;
import Model.RecetaConsulta;
import Model.RecetaDAO;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ControladorReceta {
    private final RecetaDAO recetaDAO;
    
    public ControladorReceta() {
        this.recetaDAO = new RecetaDAO();
    }
    
    public List<RecetaConsulta> listarIdConsulta(int id) {
        if (recetaDAO == null) {
            System.err.println("ERROR: medicamentodao es null");
            return new ArrayList<>();
        }
        return recetaDAO.listarIdConsulta(id);
    }
    
    public List<Receta> listarReceta(Integer idConsulta, String codigoReceta, Date fechaInicio, Date fechaFin) {
        if (recetaDAO == null) {
            System.err.println("ERROR: medicamentodao es null");
            return new ArrayList<>();
        }
        return recetaDAO.buscarConFiltros(idConsulta, codigoReceta, fechaInicio, fechaFin);
    }
    
    public boolean registrarRecetaCompleta(int idConsulta, String codigoReceta, List<DetalleReceta> detallesFront) {
        
        // 1. Validaciones previas de seguridad
        if (idConsulta <= 0) {
            System.out.println("[Controlador] Error: El ID de consulta no es válido.");
            return false;
        }
        
        if (codigoReceta == null || codigoReceta.trim().isEmpty()) {
            System.out.println("[Controlador] Error: El código de receta no puede estar vacío.");
            return false;
        }
        
        if (detallesFront == null || detallesFront.isEmpty()) {
            System.out.println("[Controlador] Error: La receta debe contener al menos un medicamento.");
            return false;
        }

        // 2. Construcción de la cabecera de la Receta
        Receta receta = new Receta();
        receta.setIdConsulta(idConsulta);
        receta.setCodigoReceta(codigoReceta);

        // 3. Envío al DAO para ejecutar la transacción con el Procedure
        System.out.println("[Controlador] Iniciando el registro de la receta en base de datos...");
        boolean exito = recetaDAO.insertarConProcedure(receta, detallesFront);

        if (exito) {
            System.out.println("[Controlador] Receta registrada exitosamente con todos sus medicamentos.");
        } else {
            System.out.println("[Controlador] Falló el registro de la receta.");
        }

        return exito;
    }
}
