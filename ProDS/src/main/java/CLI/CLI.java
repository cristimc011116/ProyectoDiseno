/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CLI;
import controlador.ControladorUsuario;
import java.util.Scanner;
import validacion.ExpresionesRegulares;
import dao.PersonaDAO;
import java.time.LocalDate;
import logicadenegocios.Cuenta;
import logicadenegocios.Persona;
import dao.CuentaDAO;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
/**
 *
 * @author Cristi Martínez
 */
public class CLI {
    
    //el main debe ser el menu, las funciones métodos
    
    public void crearCuenta()
    //public static void main(String[] args)
    {
        int id = pedirId();
        String pin = pedirPin();
        int monto = pedirMonto();
        int numero = ControladorUsuario.insertarCuenta(pin, monto, id);
        
        String texto4 = "Se ha creado una nueva cuenta en el sistema, los datos de la cuenta son: ";
        System.out.println(texto4);
        String texto5 = ControladorUsuario.imprimirCuenta(numero);
        System.out.println(texto5);
        System.out.println("---");
        String texto6 = ControladorUsuario.imprimirPersona(id);
        System.out.println(texto6); 
    }
    
    public static void main(String[] args)
    {
        /*ArrayList<Persona> listaPersonas = new ArrayList<>();
        Persona p1 = new Persona("Arias", "2apellido", "nombre", 890);
        Persona p2 = new Persona("Martínez", "2apellido2", "nombre2", 891);
        Persona p3 = new Persona("Cedeño", "2apellido3", "nombre3", 892);
        listaPersonas.add(p1);
        listaPersonas.add(p2);
        listaPersonas.add(p3);*/
        
        ArrayList<Persona> listaPersonas = PersonaDAO.getPersonasBD();
        
        listaPersonas.sort((Persona persona1, Persona persona2)-> persona1.getPrimerApellido().compareTo(persona2.getPrimerApellido()));
        listaPersonas.forEach((es)->System.out.println(es));
        //Persona[] listaPersonas = ControladorUsuario.ordenarPersonas();
        /*System.out.print("Primer apellido" + "\t  Segundo apellido" + "\t  Nombre" + "\t  Identificacion\n");
        for (Persona persona : listaPersonas) {
            System.out.println("    " + persona.getPrimerApellido() + "\t          " 
                               + persona.getSegundoApellido() + "\t      "
                               + persona.getNombre() + "\t      " + 
                               persona.getId());
        }*/
    }
    
    public static int pedirId()
    {
        Scanner sc = new Scanner (System.in);
        String texto = "Digite su identificacion: ";
        System.out.println(texto);
        String strid = sc.next();
        boolean esCorrecto = ControladorUsuario.auxIdP1(strid);
        
        while (esCorrecto == false)
        {
            String texto1 = "Digite su identificacion: ";
            System.out.println(texto1);
            strid = sc.next();
            esCorrecto = ControladorUsuario.auxIdP1(strid);
        }
        int id = Integer.parseInt(strid);
        return id;
    }
    
    public static String pedirPin()
    {
        Scanner sc = new Scanner (System.in);
        String texto2 = "Digite el pin que será asignado a la cuenta: ";
        System.out.println(texto2);
        String pin = sc.next();
        boolean esPin = ExpresionesRegulares.validarPin(pin);
        
        while (esPin == false)
        {
            String texto3 = "Digite el pin que será asignado a la cuenta: ";
            System.out.println(texto3);
            pin = sc.next();
            esPin = ExpresionesRegulares.validarPin(pin);
        }
        return pin;
    }
    
    public static int pedirMonto()
    {
        Scanner sc = new Scanner (System.in);
        String texto3 = "Digite el monto de depósito inicial: ";
        System.out.println(texto3);
        String strmonto = sc.next();
        boolean esNum = ExpresionesRegulares.esNumero(strmonto);
        
        while (esNum == false)
        {
            String texto4 = "Digite el monto de depósito inicial: ";
            System.out.println(texto4);
            strmonto = sc.next();
            esNum = ExpresionesRegulares.esNumero(strmonto);
        }
        
        int monto = Integer.parseInt(strmonto);
        return monto;
    }
    
}
