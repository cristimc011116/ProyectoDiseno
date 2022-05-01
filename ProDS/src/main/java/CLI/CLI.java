/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CLI;
import java.util.Scanner;
import validacion.ExpresionesRegulares;
import dao.PersonaDAO;
import java.time.LocalDate;
import logicadenegocios.Cuenta;
import logicadenegocios.Persona;
import dao.CuentaDAO;
/**
 *
 * @author Cristi Martínez
 */
public class CLI {
    
    //el main debe ser el menu, las funciones métodos
    
    //public void crearCuenta()
    public static void main(String[] args)
    {
        Scanner sc = new Scanner (System.in);
        boolean esNum = false;
        boolean esNum2 = false;
        boolean esPin = false;
        
        String texto = "Digite su identificacion: ";
        System.out.println(texto);
        String strid = sc.next();
        boolean esCorrecto = pedirIdP1(strid);
        
        while (esCorrecto == false)
        {
            String texto1 = "Digite su identificacion: ";
            System.out.println(texto1);
            strid = sc.next();
            esCorrecto = pedirIdP1(strid);
        }
        
        int id = Integer.parseInt(strid);
        
        String texto2 = "Digite el pin que será asignado a la cuenta: ";
        System.out.println(texto2);
        String pin = sc.next();
        esPin = ExpresionesRegulares.validarPin(pin);
        
        while (esPin == false)
        {
            String texto3 = "Digite el pin que será asignado a la cuenta: ";
            System.out.println(texto3);
            pin = sc.next();
            esPin = ExpresionesRegulares.validarPin(pin);
        }
        
        String texto3 = "Digite el monto de depósito inicial: ";
        System.out.println(texto3);
        String strmonto = sc.next();
        esNum2 = ExpresionesRegulares.esNumero(strmonto);
        
        while (esNum2 == false)
        {
            String texto4 = "Digite el monto de depósito inicial: ";
            System.out.println(texto4);
            strmonto = sc.next();
            esNum2 = ExpresionesRegulares.esNumero(strmonto);
        }
        
        int monto = Integer.parseInt(strmonto);
        
        //Metodo controlador
        int numero = Cuenta.generarNumCuenta();
        LocalDate fecha = Cuenta.setFechaCreacion();
        String strFecha = fecha.toString();
        Cuenta cuenta = new Cuenta(numero, pin, fecha, monto, "activo");
        CuentaDAO.insertarCuenta(cuenta,fecha);
        CuentaDAO.asignarCuentaCliente(cuenta, id);
        //Hasta aca
        
        String texto4 = "Se ha creado una nueva cuenta en el sistema, los datos de la cuenta son: ";
        System.out.println(texto4);
        String texto5 = imprimirCuenta(numero);
        System.out.println(texto5);
        System.out.println("---");
        String texto6 = imprimirPersona(id);
        System.out.println(texto6); 
    }
    
    public static boolean pedirIdP1(String strid){
        boolean esCorrecto = false;
        boolean esNum = ExpresionesRegulares.esNumero(strid);
        if(esNum == true)
        {
            int id = Integer.parseInt(strid);
            esCorrecto = pedirIdP2(id);
        }
        return esCorrecto;
    }
    
    public static boolean pedirIdP2(int id){
        boolean esNum = false;
        Persona persona = PersonaDAO.obtenerPersona(id);
        String nombre = persona.getNombre();
        if(null != nombre){
            esNum = true;
        }
        
        return esNum;
    }
    
    public static int validarId(strid)
    {
        boolean esCorrecto = false;
        while (esCorrecto == false)
        {
            String texto1 = "Digite su identificacion: ";
            System.out.println(texto1);
            strid = sc.next();
            esCorrecto = pedirIdP1(strid);
        }
    }
    
    
    
    //En controlador
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
}
