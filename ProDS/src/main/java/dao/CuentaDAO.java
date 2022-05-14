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
import static java.time.temporal.TemporalQueries.localDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import util.ConexionBase;
import logicadenegocios.Cuenta;
import util.ConexionMongo;
import static util.ConexionMongo.cuenta;
import static util.ConexionMongo.db;
import util.Encriptacion;

/**
 *
 * @author Cristi Martínez
 */
public class CuentaDAO {
    private static ArrayList<Cuenta> cuentas;
    public static void insertarCuentaM(Cuenta pCuenta, LocalDate pFecha)
    {
        String numero = pCuenta.getNumero();
        String numEncrip = Encriptacion.encriptar(numero);
        String pin = pCuenta.getPin();
        String pinEncrip = Encriptacion.encriptar(pin);
        String saldo = pCuenta.getSaldo();
        String saldoEncrip = Encriptacion.encriptar(saldo);
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        con.excSentenciaSQL("INSERT INTO Cuenta VALUES('" + numEncrip + "', '" + pinEncrip + "', '" + pFecha +
                "', '" + saldoEncrip + "', '" + pCuenta.getEstatus() + "')");
        con.desconectar();
    }
    
    public static void insertarCuenta(Cuenta pCuenta, LocalDate pFecha)
    {
        String numero = pCuenta.getNumero();
        String numEncrip = Encriptacion.encriptar(numero);
        String pin = pCuenta.getPin();
        String pinEncrip = Encriptacion.encriptar(pin);
        String saldo = pCuenta.getSaldo();
        String saldoEncrip = Encriptacion.encriptar(saldo);
        String estatus = pCuenta.getEstatus();
        String fecha = pFecha.toString();
        try{
            BasicDBObject document = new BasicDBObject();
            document.put("numero", numEncrip);
            document.put("pin", pinEncrip);
            document.put("fechaCreacion", fecha);
            document.put("saldo", saldoEncrip);
            document.put("estatus", estatus);
            ConexionMongo.conexionMD();
            ConexionMongo.cuenta.insert(document);
        }catch(Exception e)
        {
           JOptionPane.showMessageDialog(null, "Error: " + e.toString());        
        }
        
    }
    
    public static void asignarCuentaClienteM(Cuenta pCuenta, int id)
    {
        String numero = pCuenta.getNumero();
        String numEncrip = Encriptacion.encriptar(numero);
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        con.excSentenciaSQL("INSERT INTO PersonaCuenta VALUES(" + id + ", '" + numEncrip + "')");
        con.desconectar();
    }
    
    public static void asignarCuentaCliente(Cuenta pCuenta, int id)
    {
        String numero = pCuenta.getNumero();
        String numEncrip = Encriptacion.encriptar(numero);
        try
        {
            BasicDBObject document = new BasicDBObject();
            document.put("idCliente", id);
            document.put("numCuenta", numEncrip);
            ConexionMongo.conexionMD();
            ConexionMongo.personaCuenta.insert(document);
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
    }
    
    public static void inactivarCuentaBaseM(String pNumCuenta)
    {
        String numEncrip = Encriptacion.encriptar(pNumCuenta);
        String estado = "inactiva";
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        con.excSentenciaSQL("UPDATE Cuenta SET estatus = '" + estado + "' WHERE numero = '" + numEncrip + "'");
        con.desconectar();
    }
    
    public static void inactivarCuentaBase(String pNumCuenta)
    {
        String numEncrip = Encriptacion.encriptar(pNumCuenta);
        String estado = "inactiva";
        Cuenta cuenta = obtenerCuenta(pNumCuenta);
        String pin = cuenta.getPin();
        LocalDate pFecha = cuenta.getFechaCreacion();
        String fecha = pFecha.toString();
        String saldo = cuenta.getSaldo();
        String saldoEncrip = Encriptacion.encriptar(saldo);
        
        try
        {
            ConexionMongo.conexionMD();
            ConexionMongo.document.put("numero", numEncrip);
            BasicDBObject document_update = new BasicDBObject();
            document_update.put("numero", numEncrip);
            document_update.put("pin", pin);
            document_update.put("fechaCreacion", fecha);
            document_update.put("saldo", saldoEncrip);
            document_update.put("estatus", estado);
            ConexionMongo.cuenta.findAndModify(ConexionMongo.document, document_update);
            
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
    }
    
    public static void actualizarSaldoM(String pNumCuenta, String nuevoSaldo)
    {
        String numEncrip = Encriptacion.encriptar(pNumCuenta);
        String saldoEncrip = Encriptacion.encriptar(nuevoSaldo);
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        con.excSentenciaSQL("UPDATE Cuenta SET saldo = '" + saldoEncrip + "' WHERE numero = '" + numEncrip + "'");
        con.desconectar();
    }
    
    public static void actualizarSaldo(String pNumCuenta, String nuevoSaldo)
    {
        String numEncrip = Encriptacion.encriptar(pNumCuenta);
        String saldoEncrip = Encriptacion.encriptar(nuevoSaldo);
        Cuenta cuenta = obtenerCuenta(pNumCuenta);
        String pin = cuenta.getPin();
        LocalDate pFecha = cuenta.getFechaCreacion();
        String fecha = pFecha.toString();
        String estatus = cuenta.getEstatus();
        try
        {
            ConexionMongo.conexionMD();
            ConexionMongo.document.put("numero", numEncrip);
            BasicDBObject document_update = new BasicDBObject();
            document_update.put("numero", numEncrip);
            document_update.put("pin", pin);
            document_update.put("fechaCreacion", fecha);
            document_update.put("saldo", saldoEncrip);
            document_update.put("estatus", estatus);
            ConexionMongo.cuenta.findAndModify(ConexionMongo.document, document_update);
            
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
    }
    
    public static Cuenta obtenerCuentaM(String strNumero){
        String numEncrip = Encriptacion.encriptar(strNumero);
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        Cuenta cuenta = new Cuenta();
        ResultSet buscar = con.consultas("SELECT * FROM Cuenta WHERE numero = " +"'"+ numEncrip+"'");
        try{
            while(buscar.next()){
              String strNumDes = Encriptacion.desencriptar(numEncrip);
              cuenta.setNumero(strNumDes);
              //String strFecha = buscar.getString("fechaCreacion");
              //LocalDate fecha = LocalDate.parse(strFecha, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
              //cuenta.setFechaCreacion2(fecha);
              String noSaldo = buscar.getString("saldo");
              String strSaldoDes = Encriptacion.desencriptar(noSaldo);
              cuenta.setSaldo(strSaldoDes);
              cuenta.setEstatus(buscar.getString("estatus"));
              cuenta.setPin(buscar.getString("pin"));
              return cuenta;
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return cuenta;
      }
    
    public static Cuenta obtenerCuenta(String strNumero){
        String numEncrip = Encriptacion.encriptar(strNumero);
        Cuenta cuenta = new Cuenta();
        ConexionMongo.conexionMD();
        DBCollection colect = db.getCollection("Cuenta");
        BasicDBObject consulta = new BasicDBObject();
        consulta.put("numero", numEncrip);
        DBCursor cursor = colect.find(consulta);
        try{
            while(cursor.hasNext()){
              String strNumDes = Encriptacion.desencriptar(numEncrip);
              cuenta.setNumero(strNumDes);
              String strFecha = cursor.next().get("fechaCreacion").toString();
              LocalDate fecha = LocalDate.parse(strFecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
              cuenta.setFechaCreacion2(fecha);
              cuenta.setPin(cursor.curr().get("pin").toString());
              String noSaldo = cursor.curr().get("saldo").toString();
              String strSaldoDes = Encriptacion.desencriptar(noSaldo);
              cuenta.setSaldo(strSaldoDes);
              String estatus = cursor.curr().get("estatus").toString();
              cuenta.setEstatus(estatus);
              return cuenta;
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return cuenta;
      }
    
    public static String obtenerCuentasPersonaM(int idPersona)
    {
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        String mensaje = "";
        ResultSet buscar = con.consultas("SELECT * FROM PersonaCuenta WHERE idCliente = " + idPersona);
        try{
            while(buscar.next()){
              String strNumCuenta = buscar.getString("numCuenta");
              String numCuenta = Encriptacion.desencriptar(strNumCuenta);
              mensaje += "\n" + numCuenta + "\n";
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return mensaje;
    }
    
    public static String obtenerCuentasPersona(int idPersona)
    {
        String mensaje = "";
        ConexionMongo.conexionMD();
        DBCollection colect = db.getCollection("PersonaCuenta");
        BasicDBObject consulta = new BasicDBObject();
        consulta.put("idCliente", idPersona);
        DBCursor cursor = colect.find(consulta);
        try{
            while(cursor.hasNext()){
              String strNumCuenta = cursor.next().get("numCuenta").toString();
              String numCuenta = Encriptacion.desencriptar(strNumCuenta);
              mensaje += "\n" + numCuenta + "\n";
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return mensaje;
    }
    
    public static int obtenerPersonaCuentaM(String numCuenta)
    {
        String numEncrip = Encriptacion.encriptar(numCuenta);
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        String mensaje = "";
        int id = 0;
        ResultSet buscar = con.consultas("SELECT * FROM PersonaCuenta WHERE numCuenta = " + "'" + numEncrip + "'");
        try{
            while(buscar.next()){
              String strId = buscar.getString("idCliente");
              id = Integer.parseInt(strId);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return id;
    }
    
    public static int obtenerPersonaCuenta(String numCuenta)
    {
        String numEncrip = Encriptacion.encriptar(numCuenta);
        String mensaje = "";
        int id = 0;
        ConexionMongo.conexionMD();
        DBCollection colect = db.getCollection("PersonaCuenta");
        BasicDBObject consulta = new BasicDBObject();
        consulta.put("numCuenta", numEncrip);
        DBCursor cursor = colect.find(consulta);
        try{
            while(cursor.hasNext()){
              String strId = cursor.next().get("idCliente").toString();
              id = Integer.parseInt(strId);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return id;
    }
    
    public static String obtenerEstatusCuentaM(String numCuenta) {
        String numEncrip = Encriptacion.encriptar(numCuenta);
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        String estatus = "";
        ResultSet buscar = con.consultas("SELECT * FROM Cuenta WHERE numero = " + "'" + numEncrip + "'");
        try{
            while(buscar.next()){
              estatus = buscar.getString("estatus");
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return estatus;
    }
    
    public static String obtenerEstatusCuenta(String numCuenta) {
        String numEncrip = Encriptacion.encriptar(numCuenta);
        String estatus = "";
        ConexionMongo.conexionMD();
        DBCollection colect = db.getCollection("Cuenta");
        BasicDBObject consulta = new BasicDBObject();
        consulta.put("numero", numEncrip);
        DBCursor cursor = colect.find(consulta);
        try{
            while(cursor.hasNext()){
              estatus = cursor.next().get("estatus").toString();
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return estatus;
    }

    public static void cambiarPinCuentaM(String pCuenta, String pPinNuevo)
    {
      pCuenta = Encriptacion.encriptar(pCuenta);
      ConexionBase con = new ConexionBase();
      con.obtenerConexion();
      con.excSentenciaSQL("UPDATE cuenta"
        + "SET pin="+ "'" + pPinNuevo + "'"
          + "WHERE numero="+ "'" + pCuenta + "'");
      con.desconectar();
    }
    
    public static void cambiarPinCuenta(String pCuenta, String pPinNuevo)
    {
      pCuenta = Encriptacion.encriptar(pCuenta);
      try
        {
            ConexionMongo.conexionMD();
            ConexionMongo.document.put("numero", pCuenta);
            BasicDBObject document_update = new BasicDBObject();
            document_update.put("pin", pPinNuevo);
            ConexionMongo.cuenta.findAndModify(ConexionMongo.document, document_update);
            
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
    }
    
    public static ArrayList<Cuenta> getCuentasBDM(){
        cuentas = new ArrayList<>();
        
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        ResultSet resultado;
        Cuenta cuenta = null;
        try{
            resultado = con.consultas("SELECT * FROM Cuenta");
            while(resultado.next()){
                String numeroCuenta = resultado.getString("numero");
                String estatus = resultado.getString("estatus");
                String saldo = resultado.getString("saldo");
                cuenta = new Cuenta(numeroCuenta, estatus, saldo);
                cuentas.add(cuenta);
            }
        }catch(SQLException ex){
           JOptionPane.showMessageDialog(null, ex.toString());
        }
        con.desconectar();
        return cuentas;
    }
    
    public static ArrayList<Cuenta> getCuentasBD(){
        cuentas = new ArrayList<>();
        ConexionMongo.conexionMD();
        DBCollection colect = db.getCollection("Cuenta");
        
        
        Cuenta cuenta = null;
        try{
            DBCursor cursor = colect.find();
            while(cursor.hasNext()){
                String numeroCuenta = cursor.next().get("numero").toString();
                String estatus = cursor.curr().get("estatus").toString();
                String saldo = cursor.curr().get("saldo").toString();
                cuenta = new Cuenta(numeroCuenta, estatus, saldo);
                cuentas.add(cuenta);
            }
        }catch(Exception ex){
           JOptionPane.showMessageDialog(null, ex.toString());
        }
        return cuentas;
    }
    
    public static int contadorOperacionesCuentaM(String numCuenta){
        cuentas = new ArrayList<>();
        String numEncrip = Encriptacion.encriptar(numCuenta);
        int contador = 0;
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        ResultSet buscar = con.consultas("SELECT * FROM CuentaOperacion WHERE cuenta = " +"'"+ numEncrip+"'");
        try{
            while(buscar.next()){
                String idOperacion = buscar.getString("idOperacion");
                ResultSet buscar2 = con.consultas("SELECT * FROM Operacion WHERE id = " + idOperacion + " AND tipo = 'deposito' OR tipo = 'retiro'");
                try{
                    while(buscar2.next()){
                        contador++;
                    }
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Error: " + e.toString());
                }
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return contador;
      }
    
    public static int contadorOperacionesCuenta(String numCuenta){
        cuentas = new ArrayList<>();
        int entId = 0;
        String numEncrip = Encriptacion.encriptar(numCuenta);
        int contador = 0;
        String tipo1 = "deposito";
        String tipo2 = "retiro";
        ConexionMongo.conexionMD();
        DBCollection colect = db.getCollection("CuentaOperacion");
        BasicDBObject consulta = new BasicDBObject();
        consulta.put("cuenta", numEncrip);
        DBCursor cursor = colect.find(consulta);
        try{
            while(cursor.hasNext()){
                String idOperacion = cursor.next().get("idOperacion").toString();
                entId = Integer.parseInt(idOperacion);
                /*DBCollection colect2 = db.getCollection("Operacion");
                BasicDBObject consulta2 = new BasicDBObject();
                consulta2.put("id", idOperacion);
                DBCursor cursor2 = colect2.find(consulta2);
                try{
                    while(cursor2.hasNext()){
                        String id = cursor2.next().get("id").toString();
                        
                        DBCollection colect3 = db.getCollection("Operacion");
                        BasicDBObject consulta3 = new BasicDBObject();
                        consulta3.put("tipo", tipo1);
                        DBCursor cursor3 = colect3.find(consulta3);
                        try{
                            while(cursor3.hasNext()){
                                String id2 = cursor3.next().get("id").toString();
                                contador++;
                            }
                        }
                        catch(Exception e)
                        {
                            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
                        }
                        DBCollection colect4 = db.getCollection("Operacion");
                        BasicDBObject consulta4 = new BasicDBObject();
                        consulta4.put("tipo", tipo2);
                        DBCursor cursor4 = colect4.find(consulta4);
                        try{
                            while(cursor4.hasNext()){
                                String id3 = cursor4.next().get("id").toString();
                                contador++;
                            }
                        }
                        catch(Exception e)
                        {
                            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
                        }
                    }
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, "Error: " + e.toString());
                }*/  
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        JOptionPane.showMessageDialog(null, "ContadorComi: " + entId);
        return entId;
      }
}
    



