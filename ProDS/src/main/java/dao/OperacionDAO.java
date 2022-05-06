/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import logicadenegocios.ConexionBase;
import logicadenegocios.Cuenta;
import logicadenegocios.Operacion;

/**
 *
 * @author Cristi Martínez
 */
public class OperacionDAO {
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
        String numEncrip = Cuenta.encriptar(pNumCuenta);
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
}
