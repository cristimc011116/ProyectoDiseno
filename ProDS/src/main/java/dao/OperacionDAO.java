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
import logicadenegocios.Operacion;
import util.ConexionMongo;
import static util.ConexionMongo.db;
import util.Encriptacion;

/**
 *
 * @author Cristi Martínez
 */
public class OperacionDAO {
    private static ArrayList<Operacion> operaciones;
    public static void insertarOperacionM(Operacion pOperacion, LocalDate pFecha)
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
    
    public static void insertarOperacion(Operacion pOperacion, LocalDate pFecha)
    {
        int id = pOperacion.getId();
        String tipo = pOperacion.getTipo();
        boolean esComision = pOperacion.isComision();
        double montoComision = pOperacion.getMontoComision();
        String fecha = pFecha.toString();
        int esc= 0;
        if(esComision)
        {
            esc = 1;
        }
        else
        {
            esc = 0;
        }
        try{
            BasicDBObject document = new BasicDBObject();
            document.put("id", id);
            document.put("fechaOperacion", fecha);
            document.put("tipo", tipo);
            document.put("comision", esc);
            document.put("montoComision", montoComision);
            ConexionMongo.conexionMD();
            ConexionMongo.operacion.insert(document);
        }catch(Exception e)
        {
           JOptionPane.showMessageDialog(null, "Error: " + e.toString());        
        }
        
    }
    
    public static void asignarOperacionCuentaM(Operacion pOperacion, String pNumCuenta)
    {
        int id = pOperacion.getId();
        String numEncrip = Encriptacion.encriptar(pNumCuenta);
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        con.excSentenciaSQL("INSERT INTO CuentaOperacion VALUES('" + numEncrip + "', " + id + ")");
        con.desconectar();
    }
    
    public static void asignarOperacionCuenta(Operacion pOperacion, String pNumCuenta)
    {
        int id = pOperacion.getId();
        String numEncrip = Encriptacion.encriptar(pNumCuenta);
        try
        {
            BasicDBObject document = new BasicDBObject();
            document.put("cuenta", numEncrip);
            document.put("idOperacion", id);
            ConexionMongo.conexionMD();
            ConexionMongo.personaCuenta.insert(document);
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
    }
    
    
    public static int cantOperacionesBDM(){
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
    
    public static int cantOperacionesBD(){
        int contador = 1;
        ConexionMongo.conexionMD();
        DBCollection colect = db.getCollection("Operacion");
        BasicDBObject consulta = new BasicDBObject();
        DBCursor cursor = colect.find();
        try{
            while(cursor.hasNext()){
                contador++;
            }
        }catch(Exception ex){
           JOptionPane.showMessageDialog(null, ex.toString());
        }
        return contador;
    }
    
    public static ArrayList<Operacion> getOperacionesCuentaM(String numCuenta){
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
    
    
    public static ArrayList<Operacion> getOperacionesCuenta(String numCuenta){
        operaciones = new ArrayList<>();
        String numEncrip = Encriptacion.encriptar(numCuenta);
        int contador = 0;
        Operacion operacion = null;
        ConexionMongo.conexionMD();
        DBCollection colect = db.getCollection("CuentaOperacion");
        BasicDBObject consulta = new BasicDBObject();
        consulta.put("cuenta", numEncrip);
        DBCursor cursor = colect.find(consulta);
        try{
            while(cursor.hasNext()){
                String idOperacion = cursor.next().get("idOperacion").toString();
                DBCollection colect2 = db.getCollection("Operacion");
                BasicDBObject consulta2 = new BasicDBObject();
                consulta2.put("id", idOperacion);
                DBCursor cursor2 = colect2.find(consulta2);
                try{
                    while(cursor2.hasNext()){
                        String strId = cursor2.next().get("id").toString();
                        int id = Integer.parseInt(strId);
                        LocalDate fecha = LocalDate.parse(cursor2.curr().get("fechaOperacion").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        String tipo = cursor2.curr().get("tipo").toString();
                        String strComision = cursor2.curr().get("comision").toString();
                        int intComision = Integer.parseInt(strComision);
                        boolean comision = esComision(intComision);
                        String strMontoComision = cursor2.curr().get("montoComision").toString();
                        double montoComision = Double.parseDouble(strMontoComision);
                        operacion = new Operacion(id, fecha, tipo, comision, montoComision);
                        operaciones.add(operacion);
                    }
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, "Error: " + e.toString());
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return operaciones;
      }
    
    public static boolean esComision(int comision)
    {
        return comision==1;
    }
}
