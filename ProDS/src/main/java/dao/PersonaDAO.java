/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;
import logicadenegocios.ConexionBase;
import logicadenegocios.Persona;
/**
 *
 * @author Cristi Martínez
 */
public class PersonaDAO {
    private static ArrayList<Persona> personas;
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
              persona.setCodigo(buscar.getString("codigo"));
              persona.setPrimerApellido(buscar.getString("primerApellido"));
              persona.setSegundoApellido(buscar.getString("segundoApellido"));
              persona.setNombre(buscar.getString("nombre"));
              int idU = Integer.parseInt(buscar.getString("id"));
              persona.setNumero(idU);
              LocalDate fechaNac = LocalDate.parse(buscar.getString("fechaNacimiento"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
              persona.setFechaNacimiento(fechaNac);
              int telefono = Integer.parseInt(buscar.getString("numero"));
              persona.setNumero(telefono);
              persona.setCorreo(buscar.getString("correo"));
              persona.setSegundoApellido(buscar.getString("rol"));
              return persona;
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return persona;
    }
    
    public static ArrayList<Persona> getPersonasBD(){
        personas = new ArrayList<>();
        
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        ResultSet resultado;
        Persona persona = null;
        try{
            resultado = con.consultas("SELECT * FROM Persona");
            while(resultado.next()){
                
                String primerApellido = resultado.getString("primerApellido");
                //JOptionPane.showMessageDialog(null, primerApellido);
                String segundoApellido = resultado.getString("segundoApellido");
                //JOptionPane.showMessageDialog(null, segundoApellido);
                String nombre = resultado.getString("nombre");
                //JOptionPane.showMessageDialog(null, nombre);
                int id = Integer.parseInt(resultado.getString("id"));
                //JOptionPane.showMessageDialog(null, id);
                persona = new Persona(primerApellido, segundoApellido, nombre, id);
                //System.out.println(persona);
                //JOptionPane.showMessageDialog(null, persona);
                personas.add(persona);
                /*if(persona==null)
                {
                    System.out.println(1);
                }*/
                //personas.sort((Persona persona1, Persona persona2)-> persona1.getPrimerApellido().compareTo(persona2.getPrimerApellido()));
                //personas.forEach((es)->System.out.println(es));
            }
        }catch(SQLException ex){
           JOptionPane.showMessageDialog(null, ex.toString());
        }
        con.desconectar();
        return personas;
    }
    
    public static ResultSet recuperarTodosLosUsuariosBD(){
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        return con.consultas("SELECT * FROM Persona");
    }

}
