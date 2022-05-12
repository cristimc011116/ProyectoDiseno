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
import logicadenegocios.Cuenta;
import util.Encriptacion;

/**
 *
 * @author Cristi Martínez
 */
public class CuentaDAO {
    private static ArrayList<Cuenta> cuentas;
    public static void insertarCuenta(Cuenta pCuenta, LocalDate pFecha)
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
    
    public static void asignarCuentaCliente(Cuenta pCuenta, int id)
    {
        String numero = pCuenta.getNumero();
        String numEncrip = Encriptacion.encriptar(numero);
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        con.excSentenciaSQL("INSERT INTO PersonaCuenta VALUES(" + id + ", '" + numEncrip + "')");
        con.desconectar();
    }
    
    public static void inactivarCuentaBase(String pNumCuenta)
    {
        String numEncrip = Encriptacion.encriptar(pNumCuenta);
        String estado = "inactiva";
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        con.excSentenciaSQL("UPDATE Cuenta SET estatus = '" + estado + "' WHERE numero = '" + numEncrip + "'");
        con.desconectar();
    }
    
    public static void actualizarSaldo(String pNumCuenta, String nuevoSaldo)
    {
        String numEncrip = Encriptacion.encriptar(pNumCuenta);
        String saldoEncrip = Encriptacion.encriptar(nuevoSaldo);
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        con.excSentenciaSQL("UPDATE Cuenta SET saldo = '" + saldoEncrip + "' WHERE numero = '" + numEncrip + "'");
        con.desconectar();
    }
    
    public static Cuenta obtenerCuenta(String strNumero){
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
    
    public static String obtenerCuentasPersona(int idPersona)
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
    
    public static int obtenerPersonaCuenta(String numCuenta)
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
    
    public static String obtenerEstatusCuenta(String numCuenta) {
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

    public static void cambiarPinCuenta(String pCuenta, String pPinNuevo)
    {
      pCuenta = Encriptacion.encriptar(pCuenta);
      ConexionBase con = new ConexionBase();
      con.obtenerConexion();
      con.excSentenciaSQL("UPDATE cuenta"
        + "SET pin="+ "'" + pPinNuevo + "'"
          + "WHERE numero="+ "'" + pCuenta + "'");
      con.desconectar();
    }
    
    public static ArrayList<Cuenta> getCuentasBD(){
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
    
    public static int contadorOperacionesCuenta(String numCuenta){
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
}
    



