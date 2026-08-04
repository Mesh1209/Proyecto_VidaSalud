/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controler;

import Model.Horario;
import Model.Paciente;
import Model.Triaje;
import Model.TriajeDAO;
import java.util.List;

/**
 *
 * @author Harold
 */
public class ControladorTriaje {
    private TriajeDAO triajedao;
    
    public ControladorTriaje() {
        this.triajedao = new TriajeDAO();
        System.out.println("ControladorHorario inicializado");
    }
    
    public List<Paciente> listarDatosPaciente(int idCita){
        if (triajedao == null) {
            System.err.println("ERROR: triajedao es null");
            return null;
        }
        List<Paciente> lista = triajedao.obtenerDatosPaciente(idCita);
        return lista;
    }
    
    public boolean ActualizarTriaje(int id_cita, double peso_kg, double talla_cm,
        double temperatura_c, String presion_arterial, int frecuencia_cardiaca,
        int saturacion_oxigeno, String notas_triaje){
        
        Triaje triaje = new Triaje();
        
        triaje.setId_cita(id_cita);
        triaje.setPeso_kg(peso_kg);
        triaje.setTalla_cm(talla_cm);
        triaje.setTemperatura_c(temperatura_c);
        triaje.setPresion_arterial(presion_arterial);
        triaje.setFrecuencia_cardiaca(frecuencia_cardiaca);
        triaje.setSaturacion_oxigeno(saturacion_oxigeno);
        triaje.setNotas_triaje(notas_triaje);
        
        boolean resultado = triajedao.ActualizarTriaje(triaje);
        
        if (resultado) {
            System.out.println("Horario actualizado exitosamente");
        } else {
            System.err.println("No se pudo actualizar el horario");
        }
        
        return resultado;
    }
    
}
