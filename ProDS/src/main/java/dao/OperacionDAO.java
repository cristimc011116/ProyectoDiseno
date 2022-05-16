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
import logicadenegocios.Operacion;
import util.Encriptacion;

/**
 *
 * @author Cristi Martínez
 */
public class OperacionDAO {
    private static ArrayList<Operacion> operaciones;
    public static void insertarOperacion(Operacion pOperacion, LocalDate pFecha)
    {
        int id = pOperacion.getId();
        String tipo = pOperacion.getTipo();
        boolean esComision = pOperacion.isComision();
        double montoComision = pOperacion.getMontoComision();
        int esc= 0;
        if(esComision)
        {
            esc = 1;
        }
        else
        {
            esc = 0;
        }
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        con.excSentenciaSQL("INSERT INTO Operacion VALUES(" + id + ", '" + pFecha + "', '" + tipo +
                "', " + esc + ", " + montoComision + ")");
        con.desconectar();
    }
    
    public static void asignarOperacionCuenta(Operacion pOperacion, String pNumCuenta)
    {
        int id = pOperacion.getId();
        String numEncrip = Encriptacion.encriptar(pNumCuenta);
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        con.excSentenciaSQL("INSERT INTO CuentaOperacion VALUES('" + numEncrip + "', " + id + ")");
        con.desconectar();
    }
    
    public static int cantOperacionesBD(){
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        ResultSet resultado;
        int contador = 1;
        try{
            resultado = con.consultas("SELECT * FROM Operacion");
            while(resultado.next()){
                contador++;
            }
        }catch(SQLException ex){
           JOptionPane.showMessageDialog(null, ex.toString());
        }
        con.desconectar();
        return contador;
    }
    
    public static ArrayList<Operacion> getOperacionesBD(){
        operaciones = new ArrayList<>();
        
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        ResultSet resultado;
        Operacion operacion = null;
        try{
            resultado = con.consultas("SELECT * FROM Operacion");
            while(resultado.next()){
                String strId = resultado.getString("id");
                int id = Integer.parseInt(strId);
                LocalDate fecha = LocalDate.parse(resultado.getString("fechaOperacion"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String tipo = resultado.getString("tipo");
                String strComision = resultado.getString("comision");
                int comision = Integer.parseInt(strComision);
                String strMontoCom = resultado.getString("montoComision");
                double montoCom = Double.parseDouble(strMontoCom);
                operacion = new Operacion(id, fecha, tipo, (comision==1), montoCom);
                operaciones.add(operacion);
            }
        }catch(SQLException ex){
           JOptionPane.showMessageDialog(null, ex.toString());
        }
        con.desconectar();
        return operaciones;
    }
    
    public static ArrayList<Operacion> getOperacionesCuenta(String numCuenta){
        operaciones = new ArrayList<>();
        String numEncrip = Encriptacion.encriptar(numCuenta);
        int contador = 0;
        Operacion operacion = null;
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        ResultSet buscar = con.consultas("SELECT * FROM CuentaOperacion WHERE cuenta = " +"'"+ numEncrip+"'");
        try{
            while(buscar.next()){
                String idOperacion = buscar.getString("idOperacion");
                ResultSet buscar2 = con.consultas("SELECT * FROM Operacion WHERE id = " + idOperacion);
                try{
                    while(buscar2.next()){
                        String strId = buscar2.getString("id");
                        int id = Integer.parseInt(strId);
                        LocalDate fecha = LocalDate.parse(buscar2.getString("fechaOperacion"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        String tipo = buscar2.getString("tipo");
                        String strComision = buscar2.getString("comision");
                        int intComision = Integer.parseInt(strComision);
                        boolean comision = esComision(intComision);
                        String strMontoComision = buscar2.getString("montoComision");
                        double montoComision = Double.parseDouble(strMontoComision);
                        operacion = new Operacion(id, fecha, tipo, comision, montoComision);
                        operaciones.add(operacion);
                    }
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Error: " + e.toString());
                }
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return operaciones;
      }
    
    public static boolean esComision(int comision)
    {
        return comision==1;
    }
}
