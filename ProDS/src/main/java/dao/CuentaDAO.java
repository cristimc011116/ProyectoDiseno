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
import logicadenegocios.ConexionBase;
import logicadenegocios.Cuenta;
import logicadenegocios.Persona;

/**
 *
 * @author Cristi Martínez
 */
public class CuentaDAO {
    private static ArrayList<Cuenta> cuentas;
    public static void insertarCuenta(Cuenta pCuenta, LocalDate pFecha)
    {
        String numero = pCuenta.getNumero();
        String numEncrip = Cuenta.encriptar(numero);
        String pin = pCuenta.getPin();
        String pinEncrip = Cuenta.encriptar(pin);
        String saldo = pCuenta.getSaldo();
        String saldoEncrip = Cuenta.encriptar(saldo);
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        con.excSentenciaSQL("INSERT INTO Cuenta VALUES('" + numEncrip + "', '" + pinEncrip + "', '" + pFecha +
                "', '" + saldoEncrip + "', '" + pCuenta.getEstatus() + "')");
        con.desconectar();
    }
    
    public static void asignarCuentaCliente(Cuenta pCuenta, int id)
    {
        String numero = pCuenta.getNumero();
        String numEncrip = Cuenta.encriptar(numero);
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        con.excSentenciaSQL("INSERT INTO PersonaCuenta VALUES(" + id + ", '" + numEncrip + "')");
        con.desconectar();
    }
    
    public static Cuenta obtenerCuenta(String strNumero){
        String numEncrip = Cuenta.encriptar(strNumero);
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        Cuenta cuenta = new Cuenta();
        ResultSet buscar = con.consultas("SELECT * FROM Cuenta WHERE numero = " +"'"+ numEncrip+"'");
        try{
            while(buscar.next()){
              String strNumDes = Cuenta.desencriptar(numEncrip);
              cuenta.setNumero(strNumDes);
              //String strFecha = buscar.getString("fechaCreacion");
              //LocalDate fecha = LocalDate.parse(strFecha, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
              //cuenta.setFechaCreacion2(fecha);
              String noSaldo = buscar.getString("saldo");
              String strSaldoDes = Cuenta.desencriptar(noSaldo);
              cuenta.setSaldo(strSaldoDes);
              cuenta.setEstatus(buscar.getString("estatus"));
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
              String numCuenta = Cuenta.desencriptar(strNumCuenta);
              mensaje += "\n" + numCuenta + "\n";
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return mensaje;
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
                //JOptionPane.showMessageDialog(null, primerApellido);
                String estatus = resultado.getString("estatus");
                //JOptionPane.showMessageDialog(null, segundoApellido);
                String saldo = resultado.getString("saldo");
                //JOptionPane.showMessageDialog(null, nombre);
                cuenta = new Cuenta(numeroCuenta, estatus, saldo);
                //System.out.println(persona);
                //JOptionPane.showMessageDialog(null, persona);
                cuentas.add(cuenta);
            }
        }catch(SQLException ex){
           JOptionPane.showMessageDialog(null, ex.toString());
        }
        con.desconectar();
        return cuentas;
    }
    
    public static int contadorOperacionesCuenta(int numCuenta){
        String strNumero = Integer.toString(numCuenta);
        String numEncrip = Cuenta.encriptar(strNumero);
        int contador = 0;
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        ResultSet buscar = con.consultas("SELECT * FROM CuentaOperacion WHERE numero = " +"'"+ numEncrip+"'");
        try{
            while(buscar.next()){
              contador++;
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return contador;
      }
}
