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
import javax.swing.JOptionPane;
import util.ConexionBase;
import logicadenegocios.Persona;
import util.Encriptacion;
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
    
        public static void insertarCliente(Persona pCliente)
    {
        String apellido1 = pCliente.getPrimerApellido();
        String apellido2 = pCliente.getSegundoApellido();
        String nombre = pCliente.getNombre();
        LocalDate fecha = pCliente.getFechaNacimiento();
        int id = pCliente.getId();
        int telefono = pCliente.getNumero();
        String correo = pCliente.getCorreo();
        String codigo = pCliente.getCodigo();
        String rol = pCliente.getRol();

        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        con.excSentenciaSQL("INSERT INTO Persona VALUES('" + codigo + "', '" + apellido1 + "', '" + apellido2 +
                "', '" + nombre + "', " + id + ", '" + fecha + "', " + telefono + ", '" + correo + "' ,'" + rol + "')");
        con.desconectar();
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
    
    public static int contadorPersonasBD(){
        
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        ResultSet resultado;
        int contador = 1;
        try{
            resultado = con.consultas("SELECT * FROM Persona");
            while(resultado.next()){
                contador++;
            }
        }catch(SQLException ex){
           JOptionPane.showMessageDialog(null, ex.toString());
        }
        con.desconectar();
        return contador;
    }
    
    public static ResultSet recuperarTodosLosUsuariosBD(){
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        return con.consultas("SELECT * FROM Persona");
    }
}
