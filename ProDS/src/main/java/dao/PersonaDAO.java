/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import util.ConexionBase;
import logicadenegocios.Persona;
import util.ConexionMongo;
import static util.ConexionMongo.db;
/**
 *
 * @author Cristi Martínez
 */
public class PersonaDAO {
    private static ArrayList<Persona> personas;
    public static boolean estaRegistradaM(int idPersona){
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
    
    public static boolean estaRegistrada(int idPersona){
        int contador = 0;
        boolean estaRegistrada = false;
        ConexionMongo.conexionMD();
        DBCollection colect = db.getCollection("Persona");
        BasicDBObject consulta = new BasicDBObject();
        consulta.put("id", idPersona);
        DBCursor cursor = colect.find(consulta);
        if(cursor == null){
            return false;
        }
        return true;
    }
    
    public static Persona obtenerPersonaM(int id){
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
              persona.setId(id);
              LocalDate fechaNac = LocalDate.parse(buscar.getString("fechaNacimiento"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
              persona.setFechaNacimiento(fechaNac);
              int telefono = Integer.parseInt(buscar.getString("numero"));
              persona.setNumero(telefono);
              persona.setCorreo(buscar.getString("correo"));
              persona.setRol(buscar.getString("rol"));
              return persona;
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return persona;
    }
    
    public static Persona obtenerPersona(int id){
        ConexionMongo.conexionMD();
        DBCollection colect = db.getCollection("Persona");
        BasicDBObject consulta = new BasicDBObject();
        consulta.put("id", id);
        DBCursor cursor = colect.find(consulta);
        Persona persona = new Persona();
        try{
            while(cursor.hasNext()){
              persona.setCodigo(cursor.next().get("codigo").toString());
              persona.setPrimerApellido(cursor.curr().get("primerApellido").toString());
              persona.setSegundoApellido(cursor.curr().get("segundoApellido").toString());
              persona.setNombre(cursor.curr().get("nombre").toString());
              persona.setId(id);
              LocalDate fechaNac = LocalDate.parse(cursor.curr().get("fechaNacimiento").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
              persona.setFechaNacimiento(fechaNac);
              int telefono = Integer.parseInt(cursor.curr().get("numero").toString());
              persona.setNumero(telefono);
              persona.setCorreo(cursor.curr().get("correo").toString());
              persona.setRol(cursor.curr().get("rol").toString());
              return persona;
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return persona;
    }
    
    public static ArrayList<Persona> getPersonasBDM(){
        personas = new ArrayList<>();
        
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        ResultSet resultado;
        Persona persona = null;
        try{
            resultado = con.consultas("SELECT * FROM Persona");
            while(resultado.next()){
                
                String primerApellido = resultado.getString("primerApellido");
                String segundoApellido = resultado.getString("segundoApellido");
                String nombre = resultado.getString("nombre");
                int id = Integer.parseInt(resultado.getString("id"));
                persona = new Persona(primerApellido, segundoApellido, nombre, id);
                personas.add(persona);
            }
        }catch(SQLException ex){
           JOptionPane.showMessageDialog(null, ex.toString());
        }
        con.desconectar();
        return personas;
    }
    
    public static ArrayList<Persona> getPersonasBD(){
        personas = new ArrayList<>();
        
        ConexionMongo.conexionMD();
        DBCollection colect = db.getCollection("Persona");
        DBCursor cursor = colect.find();
        Persona persona = null;
        try{
            while(cursor.hasNext()){
                
                String primerApellido = cursor.next().get("primerApellido").toString();
                String segundoApellido = cursor.curr().get("segundoApellido").toString();
                String nombre = cursor.curr().get("nombre").toString();
                int id = Integer.parseInt(cursor.curr().get("id").toString());
                persona = new Persona(primerApellido, segundoApellido, nombre, id);
                personas.add(persona);
            }
        }catch(Exception ex){
           JOptionPane.showMessageDialog(null, ex.toString());
        }
        return personas;
    }
    
    public static ResultSet recuperarTodosLosUsuariosBD(){
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        return con.consultas("SELECT * FROM Persona");
    }
}
