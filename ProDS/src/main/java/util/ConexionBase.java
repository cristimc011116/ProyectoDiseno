/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author Cristi Martínez
 */
public class ConexionBase {
    Connection conexion = null;
    String usuario = "usuarion";
    String password = "1234";
    String db = "gestorcuentas";
    String ip = "localhost";
    String puerto = "1433";
    
    
    
    public Connection obtenerConexion()
    {
        try{
            String str = "jdbc:sqlserver://localhost:"+puerto+";"+"databaseName="+db;
            conexion = DriverManager.getConnection(str, usuario, password);
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.toString());
        }
        return conexion;
        
    }
    
    public void desconectar(){
    try{
      conexion.close();
    }catch(Exception e){
      JOptionPane.showMessageDialog(null, "Error: " + e.toString());
    }
  }
    
    public int excSentenciaSQL(String pStrSentenciaSQL){
    try{
      PreparedStatement ps = conexion.prepareStatement(pStrSentenciaSQL);
      ps.execute();
      return 1;
    }catch(SQLException e){
       return 0;
    }
  }
    
   public ResultSet consultas(String pStrSentenciaSQL){
    try{
      PreparedStatement ps = conexion.prepareStatement(pStrSentenciaSQL);
      ResultSet respuesta = ps.executeQuery();
      return respuesta;
    }catch(Exception e){
       JOptionPane.showMessageDialog(null, "Error: " + e.toString());
       return null;
    }
  }
}
