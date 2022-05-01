/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import logicadenegocios.ConexionBase;
import logicadenegocios.Persona;
/**
 *
 * @author Cristi Martínez
 */
public class PersonaDAO {
    
    public static boolean estaRegistrada(int idPersona){
        int contador = 0;
        boolean estaRegistrada = false;
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        ResultSet buscarPersona = con.consultas("SELECT COUNT(*) FROM Persona WHERE id = " + idPersona);
        if(buscarPersona == null){
            return false;
        }
        return true;
    }
    
    public static Persona obtenerPersona(int id){
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        Persona persona = new Persona();
        ResultSet buscar = con.consultas("SELECT * FROM Persona WHERE id = " + id);
        try{
            while(buscar.next()){
              persona.setPrimerApellido(buscar.getString("primerApellido"));
              persona.setSegundoApellido(buscar.getString("segundoApellido"));
              persona.setNombre(buscar.getString("nombre"));
              int telefono = Integer.parseInt(buscar.getString("numero"));
              persona.setNumero(telefono);
              persona.setCorreo(buscar.getString("correo"));
              return persona;
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return persona;
      }
}
