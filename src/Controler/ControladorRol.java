package Controler;

import Model.Rol;
import Model.RolDAO;
import java.util.List;

public class ControladorRol {
    private RolDAO roldao;

    public ControladorRol() {
        this.roldao = new RolDAO();
        System.out.println("ControladorRol inicializado");
    }

    public List<Rol> listarRoles() {
        return roldao.obtenerTodosRoles();
    }
}
