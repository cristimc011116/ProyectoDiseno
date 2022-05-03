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
        int numero = pCuenta.getNumero();
        String strNumero = Integer.toString(numero);
        String numEncrip = Cuenta.encriptar(strNumero);
        String pin = pCuenta.getPin();
        String pinEncrip = Cuenta.encriptar(pin);
        double saldo = pCuenta.getSaldo();
        String strSaldo = Double.toString(saldo);
        String saldoEncrip = Cuenta.encriptar(strSaldo);
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        con.excSentenciaSQL("INSERT INTO Cuenta VALUES('" + numEncrip + "', '" + pinEncrip + "', '" + pFecha +
                "', '" + saldoEncrip + "', '" + pCuenta.getEstatus() + "')");
        con.desconectar();
    }
    
    public static void asignarCuentaCliente(Cuenta pCuenta, int id)
    {
        int numero = pCuenta.getNumero();
        String strNumero = Integer.toString(numero);
        String numEncrip = Cuenta.encriptar(strNumero);
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        con.excSentenciaSQL("INSERT INTO PersonaCuenta VALUES(" + id + ", " + numEncrip + ")");
        con.desconectar();
    }
    
    public static Cuenta obtenerCuenta(int numCuenta){
        String strNumero = Integer.toString(numCuenta);
        String numEncrip = Cuenta.encriptar(strNumero);
        ConexionBase con = new ConexionBase();
        con.obtenerConexion();
        Cuenta cuenta = new Cuenta();
        ResultSet buscar = con.consultas("SELECT * FROM Cuenta WHERE numero = " +"'"+ numEncrip+"'");
        try{
            while(buscar.next()){
              String strNumDes = Cuenta.desencriptar(numEncrip);
              int numDes = Integer.parseInt(strNumDes);
              cuenta.setNumero(numDes);
              //String strFecha = buscar.getString("fechaCreacion");
              //LocalDate fecha = LocalDate.parse(strFecha, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
              //cuenta.setFechaCreacion2(fecha);
              String noSaldo = buscar.getString("saldo");
              String strSaldoDes = Cuenta.desencriptar(noSaldo);
              double saldoDes = Double.parseDouble(strSaldoDes);
              cuenta.setSaldo(saldoDes);
              cuenta.setEstatus(buscar.getString("estatus"));
              return cuenta;
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        return cuenta;
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
                int numeroCuenta = Integer.parseInt(resultado.getString("numero"));
                //JOptionPane.showMessageDialog(null, primerApellido);
                String estatus = resultado.getString("estatus");
                //JOptionPane.showMessageDialog(null, segundoApellido);
                double saldo = Double.parseDouble(resultado.getString("saldo"));
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
