package Controler;
import Model.PerfilDAO;
import Model.Usuario;
import javax.swing.JOptionPane;

public class ControladorPerfil {
    private final PerfilDAO modelo;
    
    public ControladorPerfil(PerfilDAO modelo) {
        this.modelo = modelo;
    }
    
    public boolean registrarMedico(
            String username, String password, String nombre, String apellido, 
            int idEspecialidad, String telefono, String email, String colegiatura, int duracionTurno) {
        
        // --- VALIDACIONES DE CAMPOS VACÍOS ---
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Los datos de la cuenta (Usuario y Contraseña) son obligatorios.");
            return false;
        }
        
        if (nombre.trim().isEmpty() || apellido.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre y apellido del médico son obligatorios.");
            return false;
        }
        
        if (colegiatura.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El número de colegiatura es obligatorio.");
            return false;
        }
        
        // Fijamos el rol por defecto del flujo
        String rol = "3";
        
        // --- LLAMADA AL MODELO ---
        // Ejecutamos el DAO que interactúa con el Stored Procedure

        boolean exito = modelo.registrarMedicoConUsuario(
            username, password, rol, nombre, apellido, idEspecialidad, telefono, email, colegiatura, duracionTurno
        );
        
        // --- RESPUESTA AL USUARIO ---
        if (exito) {
            JOptionPane.showMessageDialog(null, "¡Médico y Usuario registrados correctamente!");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Error: No se pudo registrar. Verifique si el usuario, email o colegiatura ya existen.");
            return false;
        }
    }
    
    public boolean registrarUsuario(String username, String passwordHash, int idRol, boolean activo) {
        
        // Validation de campos vacíos
        if (username.trim().isEmpty() || passwordHash.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre de usuario y la contraseña son obligatorios.");
            return false;
        }

        if (idRol <= 0) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un rol válido para el usuario.");
            return false;
        }

        // Creación del objeto
        Usuario usuario = new Usuario();
        usuario.setUsername(username.trim());
        usuario.setPassword_hash(passwordHash.trim());
        usuario.setId_rol(idRol);
        usuario.setActivo(activo);

        // Llamada al Modelo
        boolean exito = modelo.registrarUsuario(usuario);

        // Respuesta al usuario
        if (exito) {
            JOptionPane.showMessageDialog(null, "¡Usuario registrado correctamente!");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Error: No se pudo registrar el usuario. Verifique si el nombre de usuario ya existe.");
            return false;
        }
    }
}
