/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CLI;
import webService.*;
import GUI.Menu;
import controlador.ControladorUsuario;
import java.util.Scanner;
import validacion.ExpresionesRegulares;
import dao.PersonaDAO;
import java.time.LocalDate;
import logicadenegocios.Cuenta;
import logicadenegocios.Persona;
import dao.CuentaDAO;
import dao.OperacionDAO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import logicadenegocios.Mensaje;
/**
 *
 * @author Cristi Martínez
 */
public class CLI {
    //el main debe ser el menu, las funciones métodos
    public static void main(String[] args)
    {
        String resultado = retirarColones("colones");
        System.out.println(resultado);
        /*Scanner sc = new Scanner (System.in);
        System.out.println("Bienvenido al gestor de cuentas\nDigite la funcionalidad que desea realizar:\n1.Registrar un cliente"
                + "\n2.Crear cuenta\n3.Listar los clientes en orden ascendente\n4.Listar las cuentas en orden descendente de acuerdo al saldo"
                + "\n5.Cambiar PIN\n6.Realizar depósito en colones\n7.Realizar depósito en doláres\n8.Realizar retiro en colones"
                + "\n9.Realizar retiro en dólares\n10.Realizar transferencia en colones\n11.Consultar tipo de cambio de compra en dólares"
                + "\n12.Consultar tipo de cambio de ventar en dólares\n13.Consultar saldo actual\n14.Consultar saldo actual (dólares)"
                + "\n15.Consultar estado de cuenta\n16.Consultar estado de cuenta (dólares)\n16.Consultar estatus de la cuenta"
                + "\n17.Consultar ganancias del banco por comisiones\n18.Consultar ganancias del banco por comisiones en una cuenta específica"
                + "\n19.Salir"
                + "\nDigite su opción: ");
        String opcion = sc.next();
        boolean esCorrecta = validarOpcion(opcion, 1,19);
        if(esCorrecta){
            crearCuenta(opcion);
            listarPersonas(opcion);
        }
        else
        {
            main(null);
        }*/
              //  System.out.println("El tipo de cambio de compra es");
//                System.out.println(ConsultarCompraDolar());
                //System.out.println("El tipo de cambio de venta es");
//                System.out.println(ConsultarVentaDolar());*/
        
              /*  ConsultaMoneda consulta = new ConsultaMoneda();
                System.out.println("El tipo de cambio de compra es");
                System.out.println(consulta.consultaCambioCompra());
                System.out.println("El tipo de cambio de venta es");
                System.out.println(consulta.consultaCambioVenta());*/
                
        
        
    }
    
    public static boolean validarOpcion(String opcion, int numMenor, int numMayor)
    {
        if(ExpresionesRegulares.esNumero(opcion))
        {
            int op = Integer.parseInt(opcion);
            return (numMenor <= op && op <= numMayor);
        }
        else
        {
            return false;
        }
    }
        
    public static void crearCuenta(String opcion)
    //public static void main(String[] args)
    {
        if ("2".equals(opcion))
        {
            int id = pedirId();
            String pin = pedirPin();
            String monto = pedirMonto();
            String numero = ControladorUsuario.insertarCuenta(pin, monto, id);

            String texto4 = "Se ha creado una nueva cuenta en el sistema, los datos de la cuenta son: ";
            System.out.println(texto4);
            String texto5 = ControladorUsuario.imprimirCuenta(numero);
            System.out.println(texto5);
            System.out.println("---");
            String texto6 = ControladorUsuario.imprimirPersona(id);
            System.out.println(texto6); 
            volverMenu();
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
            seleccionarPersona();
        }   
    }
    
    public static void seleccionarPersona()
    {
        Scanner sc = new Scanner (System.in);
        System.out.println("Digite 1 si desea consultar a un usuario en específico, o 0 si desea volver al menú principal");
        String op = sc.next();
        boolean esCorrecto = validarOpcion(op, 0, 1);
        if(esCorrecto)
        {
            if("1".equals(op))
            {
                pedirInfoUsuario();
                volverMenu();
            }
            else
            {
                main(null);
            }
        } 
        else
        {
            seleccionarPersona();
        }
        
    }
    
    public static void volverMenu(){
        Scanner sc = new Scanner (System.in);
        System.out.println("Desea volver al menú: 1.Sí 2.No");
        String op = sc.next();
        if("1".equals(op)){
            main(null);
        }
        else
        {
            //Salir del programa
        }
    }
    
    public static void pedirInfoUsuario()
    {
        Scanner sc = new Scanner (System.in);
        System.out.println("Digite la identificación del usuario que desea consultar");
        String usuario = sc.next();
        String mensaje = ControladorUsuario.consultarUsuario(usuario);
        System.out.println(mensaje);
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
        String texto2 = "Digite el pin de la cuenta: ";
        System.out.println(texto2);
        String pin = sc.next();
        boolean esPin = ExpresionesRegulares.validarPin(pin);
        
        while (esPin == false)
        {
            String texto3 = "Digite el pin de la cuenta: ";
            System.out.println(texto3);
            pin = sc.next();
            esPin = ExpresionesRegulares.validarPin(pin);
        }
        return pin;
    }
    
    public static String pedirMonto()
    {
        Scanner sc = new Scanner (System.in);
        String texto3 = "Digite el monto: ";
        System.out.println(texto3);
        String strmonto = sc.next();
        boolean esNum = ExpresionesRegulares.esNumero(strmonto);
        
        while (esNum == false)
        {
            String texto4 = "Digite el monto: ";
            System.out.println(texto4);
            strmonto = sc.next();
            esNum = ExpresionesRegulares.esNumero(strmonto);
        }
        
        return strmonto;
    }
    
    public static String pedirNumCuenta()
    {
        Scanner sc = new Scanner (System.in);
        String texto = "Digite el número de cuenta: ";
        System.out.println(texto);
        String strNumCuenta = sc.next();
        boolean esCorrecto = ControladorUsuario.auxNumCuentaP1(strNumCuenta);
        
        while (esCorrecto == false)
        {
            String texto1 = "Digite el número de cuenta: ";
            System.out.println(texto1);
            strNumCuenta = sc.next();
            esCorrecto = ControladorUsuario.auxNumCuentaP1(strNumCuenta);
        }
        return strNumCuenta;
    }
    
    public static void inactivarCuenta(String pNumCuenta)
    {
        Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
        cuenta.setEstatus("inactiva");
        CuentaDAO.inactivarCuentaBase(pNumCuenta);
    }
    
    public static String esPinCuenta(String pNumCuenta)
    {
        Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
        Scanner sc = new Scanner (System.in);
        String texto2 = "Digite el pin de la cuenta: ";
        System.out.println(texto2);
        String pin = sc.next();
        int cont = 0;
        String pinDesencriptado = Cuenta.desencriptar(cuenta.getPin());
        while (!pin.equals(pinDesencriptado))
        {
            cont++;
            if(cont >= 2)
            {
                inactivarCuenta(pNumCuenta);
                return ("Se ha desactivado la cuenta");
            }
            String texto3 = "Digite el pin de la cuenta: ";
            System.out.println(texto3);
            pin = sc.next();
            
        }
        return pin;
    }
    
    public static String pedirPalabra(String pPalabra, String pNumCuenta)
    {
        Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
        Scanner sc = new Scanner (System.in);
        String texto2 = "Digite la palabra clave: ";
        System.out.println(texto2);
        String palabra = sc.next();
        int cont = 0;
        while (!pPalabra.equals(palabra))
        {
            cont++;
            if(cont >= 2)
            {
                inactivarCuenta(pNumCuenta);
                return ("Se ha desactivado la cuenta");
            }
            String texto3 = "Digite la palabra clave: ";
            System.out.println(texto3);
            palabra = sc.next();
            
        }
        return palabra;
    }
    
    public static String crearPalabra()
    {
        char[] abc = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        char[] num = {'0','1','2','3','4','5','6','7','8','9'};
        Random rand = new Random();
        int a = rand.nextInt(26);
        int n = rand.nextInt(10);
        int c = rand.nextInt(2);
        int contador = 0;
        String palabra = "";
        while(contador < 6)
        {
            a = rand.nextInt(26);
            n = rand.nextInt(10);
            c = rand.nextInt(2);
            if(c==1)
            {
                palabra += abc[a];
            }
            else
            {
                palabra += num[n];
            }
            contador++;
        }
        return palabra;
    }
    
    public static String enviarMensaje(String numCuenta)
    {
        int id = CuentaDAO.obtenerPersonaCuenta(numCuenta);
        Persona persona = PersonaDAO.obtenerPersona(id);
        int numero = persona.getNumero();
        String mensaje = crearPalabra();
        Mensaje.enviarMensaje(83211510, mensaje);
        return mensaje;
    }
    
    public static double montoValido(double pMonto, String pNumCuenta, String moneda)
    {
        ConsultaMoneda consulta = new ConsultaMoneda();
        Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
        String montoEncrip = cuenta.getSaldo();
        //String strMonto = Cuenta.desencriptar(montoEncrip);
        //String strMonto1 = strMonto.replace("+-","");
        double monto = Double.parseDouble(montoEncrip);
        double venta = consulta.consultaCambioVenta();
        double pMontoDolares = pMonto * venta;
        if("dolares".equals(moneda))
        {
            pMontoDolares = pMonto * venta;
            pMonto = pMontoDolares;
            while (monto<pMonto)
            {
                String strSaldo = pedirMonto();
                pMonto = Double.parseDouble(strSaldo);
                pMontoDolares = pMonto * venta;
                pMonto = pMontoDolares;
            }
        }
        else
        {
            while (monto<pMonto)
            {
                String strSaldo = pedirMonto();
                pMonto = Double.parseDouble(strSaldo);
            }
        }
        
        return pMonto;
    }
    
    public static double nuevoMonto(double comision, boolean aplicaCom, double montoCorrecto)
    {
        double nuevoMonto = montoCorrecto;
        if(aplicaCom)
        {
            comision = montoCorrecto * 0.02;
            nuevoMonto += comision;
        }
        return nuevoMonto;
    }
    
    public static String imprimirResultado(String moneda, double comision, double monto)
    {
        String resultado = "";
        if("colones".equals(moneda))
        {
            resultado += "Estimado usuario, el monto de este retiro es: " + monto;
            resultado += "\n[El monto cobrado por concepto de comisión fue de " + comision + " colones, que fueron rebajados "
                + "automáticamente de su saldo actual]";
        }
        else
        {
            ConsultaMoneda consulta = new ConsultaMoneda();
            double ventaDolar = consulta.consultaCambioVenta();
            double montoDolares = monto/ventaDolar;
            resultado += "Estimado usuario, el monto de este retiro es: " + montoDolares + " dólares";
            resultado += "\n\n[Según el BCCR, el tipo de cambio de venta del dólar hoy es: " + ventaDolar +"]";
            resultado += "\n[El monto equivalente de su retiro es: " + monto + "colones]";
            resultado += "\n[El monto cobrado por concepto de comisión fue de " + comision + " colones, que fueron rebajados "
                + "automáticamente de su saldo actual]";
        }
        return resultado;
    }
    
    public static String retirarColones(String moneda)
    {
        String pNumCuenta = pedirNumCuenta();
        Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
        String mensaje = "";
        String mensaje2 = "";
        String resultado = "";
        if(!"inactiva".equals(cuenta.getEstatus()))
        {
            String pin = esPinCuenta(pNumCuenta);
            if("Se ha desactivado la cuenta".equals(pin))
            {
                return pin;
            }
            else
            {
                mensaje = enviarMensaje(pNumCuenta);
                System.out.println("Estimado usuario se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes"
                        + " y procesa a digitar la palabra enviada");
                mensaje2 = pedirPalabra(mensaje, pNumCuenta);
                if("Se ha desactivado la cuenta".equals(mensaje2))
                {
                    return mensaje2;
                }
                else
                {
                    String strMonto = pedirMonto();
                    double monto = Double.parseDouble(strMonto);
                    double montoCorrecto = montoValido(monto, pNumCuenta, moneda);
                    double comision;
                    double nuevoMonto;
                    boolean aplicaCom = Cuenta.aplicaComision(pNumCuenta);
                    comision = montoCorrecto * 0.02;
                    nuevoMonto = nuevoMonto(comision, aplicaCom, montoCorrecto);
                    String strSaldoViejo = cuenta.getSaldo();
                    double saldoViejo = Double.parseDouble(strSaldoViejo);
                    double nuevoSaldo = saldoViejo - nuevoMonto;
                    String strNuevoSaldo = Double.toString(nuevoSaldo);
                    cuenta.setSaldo(strNuevoSaldo);
                    CuentaDAO.actualizarSaldo(pNumCuenta, strNuevoSaldo);
                    ControladorUsuario.insertarOperacion("retiro", (comision>0) , comision, pNumCuenta);
                    resultado = imprimirResultado(moneda, comision, montoCorrecto);
                }
            }
            
        }
        else
        {
            System.out.println("Su cuenta se encuentra desactivada");
        }
    return resultado; 
    }
}
