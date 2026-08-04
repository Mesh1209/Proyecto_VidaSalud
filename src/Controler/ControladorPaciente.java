package Controler;

import Model.Paciente;
import Model.PacienteDAO;
import java.time.LocalDate;
import java.util.List;

public class ControladorPaciente {
    private PacienteDAO pacientedao;
    
    public ControladorPaciente(){
        this.pacientedao = new PacienteDAO();
        System.out.println("controlador pacientedao inicializado");
    }
    
    // Registrar un nuevo paciente
    public boolean registrarPaciente(String nombre, String apellido, String tipoDocumento, 
                                     String numeroDocumento, LocalDate fechaNacimiento, 
                                     String genero, String telefono, String email, String direccion) {
        
        Paciente paciente = new Paciente();
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setTipo_documento(tipoDocumento);
        paciente.setNumero_documento(numeroDocumento);
        paciente.setFecha_nacimiento(fechaNacimiento);
        paciente.setGenero(genero);
        paciente.setTelefono(telefono);
        paciente.setEmail(email);
        paciente.setDireccion(direccion);

        boolean resultado = pacientedao.insertar(paciente);
        if (resultado) {
            System.out.println("Paciente registrado exitosamente");
        } else {
            System.err.println("No se pudo registrar el Paciente");
        }

        return resultado;
    }
    
    public boolean actualizarPaciente(int idPaciente, String nombre, String apellido, 
                                      String tipoDocumento, String numeroDocumento, 
                                      LocalDate fechaNacimiento, String genero, String telefono, 
                                      String email, String direccion) {
        
        Paciente paciente = new Paciente();
        paciente.setId_paciente(idPaciente);
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setTipo_documento(tipoDocumento);
        paciente.setNumero_documento(numeroDocumento);
        paciente.setFecha_nacimiento(fechaNacimiento);
        paciente.setGenero(genero);
        paciente.setTelefono(telefono);
        paciente.setEmail(email);
        paciente.setDireccion(direccion);

        boolean resultado = pacientedao.actualizar(paciente);
        if (resultado) {
            System.out.println("Paciente actualizado exitosamente");
        } else {
            System.err.println("No se pudo actualizar el Paciente");
        }

        return resultado;
    }
}
