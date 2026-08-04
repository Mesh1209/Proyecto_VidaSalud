package Controler;

import Formularios.FrmInicio_Paciente;
import Model.UsuarioDAO; 
import Model.Usuario;    
import javax.swing.JOptionPane;
import Model.SesionUsuario;

public class ControladorLogin {
    // 1.variables finales
    private final FrmInicio_Paciente vista;
    private final UsuarioDAO modelo; 
    
    // 2. CREAMOS EL CONSTRUCTOR A MANO
    public ControladorLogin(FrmInicio_Paciente vista, UsuarioDAO modelo) {
        this.vista = vista;
        this.modelo = modelo;       
    }
    
    // 3. Tu método de validación intacto
    public boolean validarIngreso(String username, String password){
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese usuario");
            return false;
        }
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese contraseña");
            return false;
        }

        Usuario u = new Usuario();
        u.setUsername(username);
        u.setPassword_hash(password); 

        if (modelo.validar(u)) {  
            // 3. GUARDAMOS EL USUARIO EN LA SESIÓN GLOBAL
            SesionUsuario.getInstancia().setUsuarioLogueado(u);
            return true;        
        } else {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
            return false;
        }
    }
    
    public boolean actualizarUsuario(int id_usuario, String username,String password_hash){
        Usuario usuario = new Usuario();
        usuario.setId_usuario(id_usuario);
        usuario.setPassword_hash(password_hash);
        usuario.setUsername(username);
        
        boolean resultado = modelo.actualizar(usuario);
        if (resultado) {
            System.out.println("usuario actualizado exitosamente");
        } else {
            System.err.println("No se pudo actualizar el usuario");
        }

        return resultado;
    }
}