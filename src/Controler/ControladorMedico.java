package Controler;

import Model.Especialidad;
import Model.MedicoDAO;
import Model.Medico;
import java.util.List;

public class ControladorMedico {
    
    private MedicoDAO medicodao;
    
    public ControladorMedico() {
        this.medicodao = new MedicoDAO();
        System.out.println("ControladorMedico inicializado");
    }
    
    public List<Medico> listarMedico() {
        if (medicodao == null) {
            System.err.println("ERROR: medicodao es null");
            return null;
        }
        List<Medico> lista = medicodao.listarMedicos();
        return lista;
    }
    
    public List<Medico> listarMedicoApellido(String apellido) {
        if (medicodao == null) {
            System.err.println("ERROR: medicodao es null");
            return null;
        }
        List<Medico> lista = medicodao.listarMedicoApellido(apellido);
        return lista;
    }

    // 2. Registrar un nuevo Médico
    public boolean RegistrarMedico(String nombre, String apellido, int id_especialidad, 
                                   String numero_documento, String telefono, String email, 
                                   int id_colegiatura, String duracion_turno_minutos, boolean activo) {
        
        if (medicodao == null) {
            System.err.println("ERROR: medicodao es null");
            return false;
        }

        Especialidad especialidad = new Especialidad();
        especialidad.setId_especialidad(id_especialidad);

        Medico medico = new Medico();
        medico.setNombre(nombre);
        medico.setApellido(apellido);
        medico.setEspecialidad(especialidad);
        medico.setNumero_documento(numero_documento);
        medico.setTelefono(telefono);
        medico.setEmail(email);
        medico.setId_colegiatura(id_colegiatura);
        medico.setDuracion_turno_minutos(duracion_turno_minutos);
        medico.setActivo(activo);

        System.out.println(medico);

        boolean resultado = medicodao.registrarMedico(medico);
        if (resultado) {
            System.out.println("Médico registrado exitosamente");
        } else {
            System.err.println("No se pudo registrar el Médico");
        }

        return resultado;
    }

    // 3. Actualizar datos de un Médico
    public boolean ActualizarMedico(int id_medico, String nombre, String apellido, int id_especialidad, 
                                   String numero_documento, String telefono, String email, 
                                   int id_colegiatura, String duracion_turno_minutos, boolean activo) {
        
        if (medicodao == null) {
            System.err.println("ERROR: medicodao es null");
            return false;
        }

        Especialidad especialidad = new Especialidad();
        especialidad.setId_especialidad(id_especialidad);

        Medico medico = new Medico();
        medico.setId_medico(id_medico);
        medico.setNombre(nombre);
        medico.setApellido(apellido);
        medico.setEspecialidad(especialidad);
        medico.setNumero_documento(numero_documento);
        medico.setTelefono(telefono);
        medico.setEmail(email);
        medico.setId_colegiatura(id_colegiatura);
        medico.setDuracion_turno_minutos(duracion_turno_minutos);
        medico.setActivo(activo);

        boolean resultado = medicodao.actualizarMedico(medico);
        if (resultado) {
            System.out.println("Médico actualizado exitosamente");
        } else {
            System.err.println("No se pudo actualizar el Médico");
        }

        return resultado;
    }

    // 4. Eliminar un Médico por ID
    public boolean EliminarMedico(int id_medico) {
        if (medicodao == null) {
            System.err.println("ERROR: medicodao es null");
            return false;
        }

        boolean resultado = medicodao.eliminarMedico(id_medico);
        if (resultado) {
            System.out.println("Médico eliminado exitosamente");
        } else {
            System.err.println("No se pudo eliminar el Médico");
        }

        return resultado;
    }

    // 5. Listar médicos con filtros (ID, Nombre/Apellido, Colegiatura, Especialidad, Estado)
//    public List<Medico> listarMedicoFiltro(Integer idMedico, String nombreApellido, Integer idColegiatura, Integer idEspecialidad, Boolean activo) {
//        if (medicodao == null) {
//            System.err.println("ERROR: medicodao es null");
//            return null;
//        }
//        List<Medico> lista = medicodao.listarMedicosFiltro(idMedico, nombreApellido, idColegiatura, idEspecialidad, activo);
//        return lista;
//    }

    // 6. Listar las especialidades disponibles para cargar listas o ComboBoxes
//    public List<Especialidad> listarEspecialidades() {
//        if (medicodao == null) {
//            System.err.println("ERROR: medicodao es null");
//            return null;
//        }
//        List<Especialidad> lista = medicodao.listarEspecialidades();
//        return lista;
//    }
}
