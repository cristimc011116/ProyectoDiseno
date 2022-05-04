/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CLI;
import GUI.Menu;
import controlador.ControladorUsuario;
import java.util.Scanner;
import validacion.ExpresionesRegulares;
import dao.PersonaDAO;
import java.time.LocalDate;
import logicadenegocios.Cuenta;
import logicadenegocios.Persona;
import webServiceBCCR.*;
import dao.CuentaDAO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
/**
 *
 * @author Cristi Martínez
 */
public class CLI {
    //el main debe ser el menu, las funciones métodos
    public static void main(String[] args)
    {
        Scanner sc = new Scanner (System.in);
        System.out.println("Bienvenido al gestor de cuentas\nDigite la funcionalidad que desea realizar:\n1.Registrar un cliente"
                + "\n2.Crear cuenta\n3.Listar los clientes en orden ascendente\n4.Listar las cuentas en orden descendente de acuerdo al saldo"
                + "\n5.Cambiar PIN\n6.Realizar depósito en colones\n7.Realizar depósito en doláres\n8.Realizar retiro en colones"
                + "\n9.Realizar retiro en dólares\n10.Realizar transferencia en colones\n11.Consultar tipo de cambio de compra en dólares"
                + "\n12.Consultar tipo de cambio de ventar en dólares\n13.Consultar saldo actual\n14.Consultar saldo actual (dólares)"
                + "\n15.Consultar estado de cuenta\n16.Consultar estado de cuenta (dólares)\n16.Consultar estatus de la cuenta"
                + "\n17.Consultar ganancias del banco por comisiones\n18.Consultar ganancias del banco por comisiones en una cuenta específica"
                + "\nDigite su opción: ");
        String opcion = sc.next();
        crearCuenta(opcion);
        listarPersonas(opcion);
                /*System.out.println("El tipo de cambio de compra es");
//                System.out.println(ConsultarCompraDolar());
                System.out.println("El tipo de cambio de venta es");
//                System.out.println(ConsultarVentaDolar());*/
                
        
        
    }
 /*   
    public static void listarCuentas()
    //public static void main(String[] args)
    {
        ArrayList<Cuenta> listaCuentas = CuentaDAO.getCuentasBD();
        listaCuentas.sort((Cuenta cuenta1, Cuenta cuenta2)-> cuenta2.getSaldo().compareTo(cuenta1.getSaldo()));
        listaCuentas.forEach((es)->System.out.println(es));
    }*/
    
    public static void crearCuenta(String opcion)
    //public static void main(String[] args)
    {
        if ("2".equals(opcion))
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
        
    }
    
    public static void  listarPersonas(String opcion)
    //public static void main(String[] args)
    {
        Scanner sc = new Scanner (System.in);
        if("3".equals(opcion))
        {
            ArrayList<Persona> listaPersonas = PersonaDAO.getPersonasBD();
            listaPersonas.sort((Persona persona1, Persona persona2)-> persona1.getPrimerApellido().compareTo(persona2.getPrimerApellido()));
            listaPersonas.forEach((es)->System.out.println(es));
            System.out.println("Digite 1 si desea consultar a un usuario en específico, o 0 si desea volver al menú principal");
            String op = sc.next();
            if("1".equals(op))
            {
                System.out.println("Digite la identificacion del usuario que desea consultar");
                String usuario = sc.next();
                consultarUsuario(usuario);
            }
            else
            {
                main(null);
            }
        }   
    }
    //VALIDARRRR
    public static String consultarUsuario(String idUsuario)
    {
        int id = Integer.parseInt(idUsuario);
        Persona persona = PersonaDAO.obtenerPersona(id);
        String mensaje = persona.toStringCompleto();
        String mensajeCuentas = CuentaDAO.obtenerCuentasPersona(id);
        String total = "Información del usuario:\n" + mensaje + "\n\nCuentas de este usuario:\n" + mensajeCuentas;
        return total;
        
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
