/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

public class SesionUsuario {
    // 1. Corregido: El tipo de la variable estática debe ser SesionUsuario
    private static SesionUsuario instancia;
    private Usuario usuarioLogueado; 
    private boolean modoTema;

    // 2. Corregido: El constructor DEBE llamarse igual que la clase (SesionUsuario)
    private SesionUsuario() {
        //INICIA EN MODO CLARO
        this.modoTema = true;
    }

    // 3. Corregido: El método debe devolver un SesionUsuario
    public static SesionUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SesionUsuario(); // Aquí creamos la sesion
        }
        return instancia;
    }
    
    public Usuario getUsuarioLogueado() { return usuarioLogueado; }
    public void setUsuarioLogueado(Usuario usuario) { this.usuarioLogueado = usuario; }
    
    public boolean isModoTema() { 
        return modoTema; 
    }

    public void setModoTema(boolean modoTema) { 
        this.modoTema = modoTema; 
    }
}