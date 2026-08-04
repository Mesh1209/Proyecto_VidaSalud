/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Harold
 */
public class CConexion {
    Connection conn = null;;
    
    String Usuario ="root";
    String Clave ="";
    String bd ="vidasalud";
    String puerto ="3306";
    
    String CadenaConexion = "jdbc:mysql://localhost:"+puerto+"/" + bd;
    
    /* Intentar la conexion*/
    public Connection estableceConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(
                    CadenaConexion,
                    Usuario,
                    Clave
            );

            System.out.println("Conexión exitosa");

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e.toString());
        }
        return conn;
    }
}
