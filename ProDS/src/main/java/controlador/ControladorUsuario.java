/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import dao.CuentaDAO;
import dao.PersonaDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import logicadenegocios.Cuenta;
import logicadenegocios.Persona;
import util.Ordenamiento;
import validacion.ExpresionesRegulares;

/**
 *
 * @author Cristi Martínez
 */
public class ControladorUsuario {
    
    //Punto 2
    public static int insertarCuenta(String pPin, int pMonto, int pId)
    {
        int numero = Cuenta.generarNumCuenta();
        LocalDate fecha = Cuenta.setFechaCreacion();
        Cuenta cuenta = new Cuenta(numero, pPin, fecha, pMonto, "activo");
        CuentaDAO.insertarCuenta(cuenta,fecha);
        CuentaDAO.asignarCuentaCliente(cuenta, pId);
        
        return numero;
    }
    
    public static String imprimirCuenta(int pNum){
        Cuenta cuenta = CuentaDAO.obtenerCuenta(pNum);
        String mensaje = "Número de cuenta: " + pNum + "\nEstatus de la cuenta: " + cuenta.getEstatus() + "\nSaldo actual: " + cuenta.getSaldo();
        return mensaje;
    }
    
    public static String imprimirPersona(int id){
        Persona persona = PersonaDAO.obtenerPersona(id);
        String mensaje = "Nombre del dueño de la cuenta: " + persona.getNombre() + " " + persona.getPrimerApellido() + " " + 
                persona.getSegundoApellido() + "\nNúmero de teléfono 'asociado' a la cuenta: " + 
                persona.getNumero() + "\nDirección de correo elctrónico 'asociada' a la cuenta: " + persona.getCorreo();
        return mensaje;
    }
    
    public static boolean auxIdP1(String strid){
        boolean esCorrecto = false;
        boolean esNum = ExpresionesRegulares.esNumero(strid);
        if(esNum == true)
        {
            int id = Integer.parseInt(strid);
            esCorrecto = auxIdP2(id);
        }
        return esCorrecto;
    }
    
    public static boolean auxIdP2(int id){
        boolean esNum = false;
        Persona persona = PersonaDAO.obtenerPersona(id);
        String nombre = persona.getNombre();
        if(null != nombre){
            esNum = true;
        }
        
        return esNum;
    }
    
    //Punto 3
    public static ArrayList<Persona> ordenarPersonas(){
        ArrayList<Persona> listaPersonas = PersonaDAO.getPersonasBD();
        Ordenamiento.ordenar(listaPersonas);
        return listaPersonas;
    }
    
}
