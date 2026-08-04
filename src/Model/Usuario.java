
package Model;
import lombok.Data;//Libreia para simplificar codigo

@Data
public class Usuario {
    private int id_usuario;
    private String username;
    private String password_hash;
    private int id_rol;
    private boolean activo;
    private String nombre_rol;
}
