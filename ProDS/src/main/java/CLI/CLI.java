/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CLI;
import webService.*;
import controlador.ControladorUsuario;
import static controlador.ControladorUsuario.imprimirResultadoConsultaSaldo;
import static controlador.ControladorUsuario.inactivarCuenta;
import static controlador.ControladorUsuario.recuperarUsuario;
import java.util.Scanner;
import validacion.ExpresionesRegulares;
import dao.PersonaDAO;
import logicadenegocios.Cuenta;
import logicadenegocios.Persona;
import dao.CuentaDAO;
import dao.OperacionDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import logicadenegocios.Operacion;
import util.Encriptacion;
/**
 *
 * @author Cristi Martínez
 */
public class CLI {
    Operacion operacion = new Operacion();
    //MENU----------------------------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args)
    {
        Scanner sc = new Scanner (System.in);
        System.out.println("Bienvenido al gestor de cuentas\nDigite la funcionalidad que desea realizar:\n1.Registrar un cliente"
                + "\n2.Crear cuenta\n3.Listar los clientes en orden ascendente\n4.Listar las cuentas en orden descendente de acuerdo al saldo"
                + "\n5.Cambiar PIN\n6.Realizar depósito en colones\n7.Realizar depósito en doláres\n8.Realizar retiro en colones"
                + "\n9.Realizar retiro en dólares\n10.Realizar transferencia en colones\n11.Consultar tipo de cambio de compra en dólares"
                + "\n12.Consultar tipo de cambio de ventar en dólares\n13.Consultar saldo actual\n14.Consultar saldo actual (dólares)"
                + "\n15.Consultar estado de cuenta\n16.Consultar estado de cuenta (dólares)\n17.Consultar estatus de la cuenta\n18.Consultar informacion de una cuenta"
                + "\n18.Consultar ganancias del banco por comisiones\n19.Consultar ganancias del banco por comisiones en una cuenta específica"
                + "\n20.Salir"
                + "\nDigite su opción: ");
        String opcion = sc.next();
        boolean esCorrecta = validarOpcion(opcion, 1,20);
        if(esCorrecta){
            crearCuenta(opcion);
            listarPersonas(opcion);
            seleccionarMonedaRetiro(opcion);
            salirPrograma(opcion);
            consultarStatus(opcion);
            seleccionarMonedaEstado(opcion);
            consultarUnaCuenta(opcion);
            listarCuentas(opcion);
            realizarTransferencia(opcion);
            
            
        }
        else
        {
            main(null);
        }
        
        
    }
    
    //FUNCIONALIDADES----------------------------------------------------------------------------------------------------------------------------------
    public void cambiarPIN(String opcion)
    {
      if ("5".equals(opcion))
      {
        String numCuenta = pedirNumCuenta();
        String pinActual = esPinCuenta(numCuenta);
        String pinNuevo = pedirPinNuevo();
        String numero; 
        boolean insertar;
        int contador =0;
        Cuenta cuentaBase = CuentaDAO.obtenerCuenta(numCuenta);
        
        if(!"inactiva".equals(cuentaBase.getEstatus())){
          
          String pin = esPinCuenta(numCuenta);
          if("Se ha desactivado la cuenta".equals(pin))
          {
            System.out.println("La cuenta ha sido desactivada por el ingreso del pin incorrecto");
            volverMenu();
          }
          else
          {
            operacion.cambiarPIN(numCuenta, pinNuevo);
            JOptionPane.showMessageDialog(null, "Estimado usuario, se ha cambiado satisfactoriamente el PIN de su cuenta:"+numCuenta);
          }
        }
        else
        {
          System.out.println("Su cuenta se encuentra desactivada");
        } 
      }
    }
    
    public void realizarDeposito(String opcion)
    {
      if ("6".equals(opcion))
        depositar("colones");
      if ("7".equals(opcion))
        depositar("dolares");
    }
    
    public static void depositar(String moneda)
    {
      String pNumCuenta = pedirNumCuenta();
      Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
      String resultado="";
      if(!"inactiva".equals(cuenta.getEstatus()))
      {
        String pin = esPinCuenta(pNumCuenta);
        if("Se ha desactivado la cuenta".equals(pin))
        {
            System.out.println("La cuenta ha sido desactivada por el ingreso del pin incorrecto");
            volverMenu();
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
          nuevoMonto = ControladorUsuario.nuevoMonto(comision, aplicaCom, montoCorrecto);
          String strSaldoViejo = cuenta.getSaldo();
          double saldoViejo = Double.parseDouble(strSaldoViejo);
          double nuevoSaldo = saldoViejo + nuevoMonto;
          String strNuevoSaldo = Double.toString(nuevoSaldo);
          cuenta.setSaldo(strNuevoSaldo);
          CuentaDAO.actualizarSaldo(pNumCuenta, strNuevoSaldo);
          ControladorUsuario.insertarOperacion("deposito", (comision>0) , comision, pNumCuenta);
          resultado = ControladorUsuario.imprimirResultadoDeposito(moneda, comision, montoCorrecto,pNumCuenta);
          System.out.println(resultado);
          volverMenu();
        }
      }
      else
      {
        System.out.println("Su cuenta se encuentra desactivada");
        volverMenu();
      }
    }
    
    public static void ConsultarSaldoActual(String opcion)
    //public static void main(String[] args)
    {
      if("13".equals(opcion))
      {
        consultaSaldo("colones");
      }
      if("14".equals(opcion))
      {
        consultaSaldo("dolares");
      }
    }
    
    public static void consultaSaldo(String moneda)
    {
      String pNumCuenta = pedirNumCuenta();
      Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
      String resultado="";
      if(!"inactiva".equals(cuenta.getEstatus()))
      {
        String pin = esPinCuenta(pNumCuenta);
        if("Se ha desactivado la cuenta".equals(pin))
        {
            System.out.println("La cuenta ha sido desactivada por el ingreso del pin incorrecto");
            volverMenu();
        }
        else
        {
          String saldo = cuenta.getSaldo();
          resultado = ControladorUsuario.imprimirResultadoConsultaSaldo( moneda,  saldo);
          System.out.println(resultado);
          volverMenu();
        }
      }
      else
      {
        System.out.println("Su cuenta se encuentra desactivada");
        volverMenu();
      }
    }
    
    public static String retirar(String moneda)
    {
        String pNumCuenta = pedirNumCuenta();
        Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
        String mensaje;
        String mensaje2;
        String resultado="";
        if(!"inactiva".equals(cuenta.getEstatus()))
        {
            String pin = esPinCuenta(pNumCuenta);
            if("Se ha desactivado la cuenta".equals(pin))
            {
                System.out.println("La cuenta ha sido desactivada por el ingreso del pin incorrecto");
                volverMenu();
                return pin;
            }
            else
            {
                mensaje = ControladorUsuario.enviarMensaje(pNumCuenta);
                System.out.println("Estimado usuario se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes"
                        + " y procesa a digitar la palabra enviada");
                mensaje2 = pedirPalabra(mensaje, pNumCuenta);
                if("Se ha desactivado la cuenta".equals(mensaje2))
                {
                    System.out.println("La cuenta ha sido desactivada por el ingreso de la palabra clave incorrecta");
                    volverMenu();
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
                    nuevoMonto = ControladorUsuario.nuevoMonto(comision, aplicaCom, montoCorrecto);
                    String strSaldoViejo = cuenta.getSaldo();
                    double saldoViejo = Double.parseDouble(strSaldoViejo);
                    double nuevoSaldo = saldoViejo - nuevoMonto;
                    String strNuevoSaldo = Double.toString(nuevoSaldo);
                    cuenta.setSaldo(strNuevoSaldo);
                    CuentaDAO.actualizarSaldo(pNumCuenta, strNuevoSaldo);
                    ControladorUsuario.insertarOperacion("retiro", (comision>0) , comision, pNumCuenta);
                    resultado = ControladorUsuario.imprimirResultado(moneda, comision, montoCorrecto);
                    System.out.println(resultado);
                    volverMenu();
                }
            }
            
        }
        else
        {
            System.out.println("Su cuenta se encuentra desactivada");
            volverMenu();
        }
    return resultado; 
    }
    
    
        public static String transferencia()
    {
        //ingresar cuenta
        System.out.println("¿Cual es la cuenta origen de la que desea tranferir?");
        String pNumCuenta = pedirNumCuenta();
        Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
        String mensaje;
        String mensaje2;
        String resultado="";
        if(!"inactiva".equals(cuenta.getEstatus()))
        {
            String pin = esPinCuenta(pNumCuenta);
            if("Se ha desactivado la cuenta".equals(pin))
            {
                System.out.println("La cuenta ha sido desactivada por el ingreso del pin incorrecto");
                volverMenu();
                return pin;
            }
            else
            {
                mensaje = ControladorUsuario.enviarMensaje(pNumCuenta);
                System.out.println("Estimado usuario se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes"
                        + " y procesa a digitar la palabra enviada");
                mensaje2 = pedirPalabra(mensaje, pNumCuenta);
                if("Se ha desactivado la cuenta".equals(mensaje2))
                {
                    System.out.println("La cuenta ha sido desactivada por el ingreso de la palabra clave incorrecta");
                    volverMenu();
                    return mensaje2;
                }
                else
                {
                    //pedir monto y validar
                    System.out.println("¿Cual es el monto que desea tranferir?");
                    String strMonto = pedirMonto();
                    double monto = Double.parseDouble(strMonto);
                    double montoCorrecto = montoValido(monto, pNumCuenta, "colones");
                    
                    //pedir cuenta destino
                    System.out.println("¿Cual es la cuenta destino a la que desea tranferir?");
                    String pNumCuentaDestino = pedirNumCuenta();
                    Cuenta cuentadestino = CuentaDAO.obtenerCuenta(pNumCuentaDestino);
                    
                    //rebajar saldo                    
                    double comision;
                    double nuevoMonto;
                    boolean aplicaCom = false;
                    comision = 0.00;
                    nuevoMonto = ControladorUsuario.nuevoMonto(comision, aplicaCom, montoCorrecto);
                    String strSaldoViejo = cuenta.getSaldo();
                    double saldoViejo = Double.parseDouble(strSaldoViejo);
                    double nuevoSaldo = saldoViejo - nuevoMonto;
                    String strNuevoSaldo = Double.toString(nuevoSaldo);
                    cuenta.setSaldo(strNuevoSaldo);
                    CuentaDAO.actualizarSaldo(pNumCuenta, strNuevoSaldo);
                    ControladorUsuario.insertarOperacion("transferencia", false , comision, pNumCuenta);
                    
                    //hacer transferencia
                    
                    String strSaldoViejoDestino = cuentadestino.getSaldo();
                    double saldoViejoDestino = Double.parseDouble(strSaldoViejoDestino);
                    double nuevoSaldoDestino = saldoViejoDestino + nuevoMonto;
                    String strNuevoSaldoDestino = Double.toString(nuevoSaldoDestino);
                    cuentadestino.setSaldo(strNuevoSaldoDestino);
                    CuentaDAO.actualizarSaldo(pNumCuentaDestino, strNuevoSaldoDestino);
                    resultado = ControladorUsuario.imprimirResultadoTransf(montoCorrecto);
                    System.out.println(resultado);
                    volverMenu();
                }
            }
            
        }
        else
        {
            System.out.println("Su cuenta se encuentra desactivada");
            volverMenu();
        }
    return resultado; 
    }
          
    
    public static String consultarEstado(String moneda)
    {
        ConsultaMoneda consulta = new ConsultaMoneda();
        String pNumCuenta = pedirNumCuenta();
        Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
        String mensaje;
        String mensaje2;
        String resultado="";
        String strSaldo = "";
        int contador = 0;
        String oper = "";
        String strPin = "";
        if(!"inactiva".equals(cuenta.getEstatus()))
        {
            String pin = esPinCuenta(pNumCuenta);
            if("Se ha desactivado la cuenta".equals(pin))
            {
                System.out.println("La cuenta ha sido desactivada por el ingreso del pin incorrecto");
                volverMenu();
                return pin;
            }
            else
            {
                Cuenta cuentaBase = CuentaDAO.obtenerCuenta(pNumCuenta);
                int idDueno = CuentaDAO.obtenerPersonaCuenta(pNumCuenta);
                Persona persona = PersonaDAO.obtenerPersona(idDueno);
                String nombreDueno = persona.getNombre() + " " + persona.getPrimerApellido() + " " + persona.getSegundoApellido();
                ArrayList<Operacion> operaciones = OperacionDAO.getOperacionesCuenta(pNumCuenta);
                for(Operacion operacion: operaciones)
                {
                   if("colones".equals(moneda))
                   {
                        strSaldo = cuentaBase.getSaldo();
                        strPin = cuentaBase.getPin();
                        double saldo = Double.parseDouble(strSaldo);
                        double monto = (operacion.getMontoComision()/0.02);
                        LocalDate fecha = operacion.getFechaOperacion();
                        String tipo = operacion.getTipo();
                        double montoComision = operacion.getMontoComision();
                        contador++;
                        oper += "Operacion #" + contador + "\nFecha: " + fecha + "\nTipo: " + tipo + "\nMonto: " + monto + "\nComisión: " + montoComision + "\n\n";
                   }
                   else
                   {
                        double venta = consulta.consultaCambioVenta();
                        String strSaldoColones = cuentaBase.getSaldo();
                        double saldoColones = Double.parseDouble(strSaldoColones);
                        double saldoDolares = saldoColones/venta;
                        strSaldo = Double.toString(saldoDolares);
                        strPin = cuentaBase.getPin();
                        double monto = (operacion.getMontoComision()/0.02);
                        double comisionDolares = (operacion.getMontoComision()/venta);
                        double montoDolares = monto/venta;
                        LocalDate fecha = operacion.getFechaOperacion();
                        String tipo = operacion.getTipo();
                        contador++;
                        oper += "Operacion #" + contador + "\nFecha: " + fecha + "\nTipo: " + tipo + "\nMonto: " + montoDolares + "\nComisión: " + comisionDolares + "\n\n";
                   }
                }
                
                resultado += "Información de la cuenta\n\n" + "Número de cuenta: " + pNumCuenta + "\nPin encriptado de la cuenta: " + strPin
                        + "\nNombre del dueño: " + nombreDueno + "\nIdentificación del dueño: " + idDueno + "\nSaldo de la cuenta: " 
                        + strSaldo + "\n\n\nOperaciones de la cuenta: " + "\n\n" + oper;
                System.out.println(resultado);
                volverMenu();
            }
        }
        else
        {
            System.out.println("Su cuenta se encuentra desactivada");
            volverMenu();
        }
    return resultado; 
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
    
    public static void  listarCuentas(String opcion)
    //public static void main(String[] args)
    {
        Scanner sc = new Scanner (System.in);
        if("4".equals(opcion))
        {
            listadoCuentas();

        }   
    }
    
    //NO PASA POR CONTROLADOS HAY QUE CAMBIARLO PERO ESTA EN CUENTAS
    public static void listadoCuentas()
    {
        ArrayList<Cuenta> listaCuentas = CuentaDAO.getCuentasBD(); 
        listaCuentas.sort((Cuenta cuenta1, Cuenta cuenta2)-> cuenta1.convertirSaldo(cuenta2).compareTo(cuenta1.convertirSaldo(cuenta1)));
        listaCuentas.forEach((es)->System.out.println(es));
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
    
    public static void seleccionarMonedaRetiro(String opcion)
    {
        if("8".equals(opcion))
        {
            retirar("colones");
        }
        if("9".equals(opcion))
        {
            retirar("dolares");
        }
    }
    
    
    public static void seleccionarMonedaEstado(String opcion)
    {
        if("15".equals(opcion))
        {
            consultarEstado("colones");
        }
        if("16".equals(opcion))
        {
            consultarEstado("dolares");
        }
    }
    
        public static void realizarTransferencia(String opcion)
    {
        if("10".equals(opcion))
        {
            transferencia();
        }
    }
    
    public static void salirPrograma(String opcion)
    {
        if("20".equals(opcion))
        {
            System.out.println("Cerrando programa");
        }
    }
    
    //VALIDACIONES-------------------------------------------------------------------------------------------------------------------------------------
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
    
    public static String esPinCuenta(String pNumCuenta)
    {
        Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
        Scanner sc = new Scanner (System.in);
        String texto2 = "Digite el pin de la cuenta: ";
        System.out.println(texto2);
        String pin = sc.next();
        int cont = 0;
        String pinDesencriptado = Encriptacion.desencriptar(cuenta.getPin());
        while (!pin.equals(pinDesencriptado))
        {
            cont++;
            if(cont >= 2)
            {
                ControladorUsuario.inactivarCuenta(pNumCuenta);
                return ("Se ha desactivado la cuenta");
            }
            String texto3 = "Digite el pin de la cuenta: ";
            System.out.println(texto3);
            pin = sc.next();
            
        }
        return pin;
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
    
    //PEDIDOS DE INFORMACIÓN---------------------------------------------------------------------------------------------------------------------------
    public static void pedirInfoUsuario()
    {
        Scanner sc = new Scanner (System.in);
        System.out.println("Digite la identificación del usuario que desea consultar");
        String usuario = sc.next();
        String mensaje = consultarCliente(usuario);
        System.out.println(mensaje);
    }
    
        public static void pedirInfoCuenta()
    {
        Scanner sc = new Scanner (System.in);
        System.out.println("Digite el numero de cuenta que desea consultar");
        String cuenta = sc.next();
        String mensaje = consultarCuenta(cuenta);
        System.out.println(mensaje);
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
    
    public static String pedirPinNuevo()
    {
        Scanner sc = new Scanner (System.in);
        String texto2 = "Digite el pin nuevo de la cuenta: ";
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
            String texto4 = "Digite un monto valido: ";
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
            String texto1 = "Digite un número de cuenta válido: ";
            System.out.println(texto1);
            strNumCuenta = sc.next();
            esCorrecto = ControladorUsuario.auxNumCuentaP1(strNumCuenta);
        }
        return strNumCuenta;
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
                ControladorUsuario.inactivarCuenta(pNumCuenta);
                return ("Se ha desactivado la cuenta");
            }
            else
            {
                pPalabra = ControladorUsuario.enviarMensaje(pNumCuenta);
                System.out.println("Estimado usuario se ha enviado una nueva palabra por mensaje de texto, por favor revise sus mensajes"
                        + " y procesa a digitar correctamente la palabra enviada");
            }
            String texto3 = "Digite la palabra clave: ";
            System.out.println(texto3);
            palabra = sc.next();
            
        }
        return palabra;
    }
    
    
    //OTROS--------------------------------------------------------------------------------------------------------------------------------------------
    public static void volverMenu(){
        Scanner sc = new Scanner (System.in);
        System.out.println("Desea volver al menú: 1.Sí 2.No");
        String op = sc.next();
        if("1".equals(op)){
            main(null);
        }
        if("2".equals(op)){
            System.out.println("Cerrando programa");
        }
        else
        {
            volverMenu();
        }
    }
    
    public static String consultarCliente(String idUsuario)
    {
        boolean esCorrecto = ControladorUsuario.auxIdP1(idUsuario);
        String total = "";
        if(esCorrecto)
        {
            total = recuperarUsuario(idUsuario);
        }
        else
        {
            pedirInfoUsuario();
        }
        return total;
    }
    
        public static String consultarCuenta(String pNumeroCuenta)
    {
        boolean esCorrecto = ControladorUsuario.auxNumCuentaP1(pNumeroCuenta);
        String total = "";
        if(esCorrecto)
        {
            total = ControladorUsuario.recuperarCuenta(pNumeroCuenta);
        }
        else
        {
            pedirNumCuenta();
        }
        return total;
    }
    
    public static String consultarUsuario(String idUsuario)
    {
        int id = Integer.parseInt(idUsuario);
        Persona persona = PersonaDAO.obtenerPersona(id);
        String mensaje = persona.toStringCompleto();
        String mensajeCuentas = CuentaDAO.obtenerCuentasPersona(id);
        String total = "Información del usuario:\n" + mensaje + "\n\nCuentas de este usuario:\n" + mensajeCuentas;
        return total;
        
    }
    
    public static void consultarStatus(String opcion)
    {
        if("17".equals(opcion)){
            String pNumCuenta = pedirNumCuenta();
            Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
            String estatus = cuenta.getEstatus();
            String total = "La cuenta número " + pNumCuenta+  " tiene estatus de " +estatus;
            System.out.println(total);
        }
    }
        public static void consultarUnaCuenta(String opcion)
    {
        if("18".equals(opcion)){
            String pNumCuenta = pedirNumCuenta();
            String cuenta = consultarCuenta(pNumCuenta);
            String total = "La cuenta número " + pNumCuenta+  " contiene la siguiente informacion  "
                    +cuenta;
            System.out.println(total);
        }
    }
}
