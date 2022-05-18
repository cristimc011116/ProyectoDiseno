/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CLI;
import webService.*;
import controlador.ControladorUsuario;
import static controlador.ControladorUsuario.imprimirResultadoConsultaSaldo;
import static controlador.ControladorUsuario.recuperarUsuario;
import java.util.Scanner;
import validacion.ExpresionesRegulares;
import dao.PersonaDAO;
import logicadenegocios.Cuenta;
import logicadenegocios.Persona;
import dao.CuentaDAO;
import dao.OperacionDAO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import logicadenegocios.Operacion;
import util.Encriptacion;
/**
 *
 * @author Cristi Martínez
 */
public class CLI {
    //Operacion operacion = new Operacion();
    //MENU----------------------------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args)
    {
        Scanner sc = new Scanner (System.in);
        System.out.println("Bienvenido al gestor de cuentas\nDigite la funcionalidad que desea realizar:\n1.Registrar un cliente"
                + "\n2.Crear cuenta\n3.Listar los clientes en orden ascendente\n4.Listar las cuentas en orden descendente de acuerdo al saldo"
                + "\n5.Cambiar PIN\n6.Realizar depósito en colones\n7.Realizar depósito en doláres\n8.Realizar retiro en colones"
                + "\n9.Realizar retiro en dólares\n10.Realizar transferencia en colones\n11.Consultar tipo de cambio de compra en dólares"
                + "\n12.Consultar tipo de cambio de venta en dólares\n13.Consultar saldo actual\n14.Consultar saldo actual (dólares)"
                + "\n15.Consultar estado de cuenta\n16.Consultar estado de cuenta (dólares)\n17.Consultar estatus de la cuenta"
                + "\n18.Consultar ganancias del banco por comisiones\n19.Consultar ganancias del banco por comisiones en una cuenta específica"
                + "\n20.Salir"
                + "\nDigite su opción: ");
        String opcion = sc.next();
        boolean esCorrecta = validarOpcion(opcion, 1,20);
        if(esCorrecta){
            crearCliente(opcion);
            crearCuenta(opcion);
            listarPersonas(opcion);
            MenulistarCuentas(opcion);
            cambiarPIN(opcion);
            realizarDeposito(opcion);
            seleccionarMonedaRetiro(opcion);
            realizarTransferencia(opcion);
            ConsultarTipoCambioOpcion(opcion);
            ConsultarSaldoActual(opcion);
            seleccionarMonedaEstado(opcion);
            consultarStatus(opcion);
            ConsultaGananciaBanco(opcion);
            salirPrograma(opcion);  
        }
        else
        {
            main(null);
        }
        
        //listarCuentas();
        
    }
    
    //FUNCIONALIDADES----------------------------------------------------------------------------------------------------------------------------------
    public static  void cambiarPIN(String opcion)
    {
      if ("5".equals(opcion))
      {
        String numCuenta = pedirNumCuenta();
        
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
            String pinNuevo = pedirPinNuevo();  
            Operacion.cambiarPIN(numCuenta, pinNuevo);
            System.out.println("Estimado usuario, se ha cambiado satisfactoriamente el PIN de su cuenta: "+numCuenta);
            volverMenu();
          }
        }
        else
        {
          System.out.println("Su cuenta se encuentra desactivada");
          volverMenu();
        } 
      }
    }
    
    public static void realizarDeposito(String opcion)
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
        
        String strMonto = pedirMonto();
        double monto = Double.parseDouble(strMonto);
        double montoCorrecto = montoValidoDeposito(monto, pNumCuenta, moneda);
        double comision = ControladorUsuario.aplicaComision(pNumCuenta, montoCorrecto);
        Operacion.realizarDeposito(monto, moneda, pNumCuenta);
        resultado = ControladorUsuario.imprimirResultadoDeposito(moneda, comision, montoCorrecto,pNumCuenta);
        System.out.println(resultado);
        volverMenu();
        
      }
      else
      {
        System.out.println("Su cuenta se encuentra desactivada");
        volverMenu();
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
    
    public static void ConsultaGananciaBanco(String opcion)
    {
      double sumaRetiros =0;
      double sumaDepositos =0;
      double sumaComisiones = 0;
      //totalizado
      if ("18".equals(opcion))
      {
        ArrayList<Cuenta> listaCuentas = CuentaDAO.getCuentasBD();
        Cuenta cuenta=new Cuenta();
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("|  Cuenta   |-|  Detalle de la operación|-|Fecha de la operación|-| Comisión del Banco|");
        for(int i=0;i<listaCuentas.size();i++){
          cuenta = listaCuentas.get(i);
          sumaRetiros = Operacion.sumarComisionesRetiros(cuenta.getNumero());
          sumaDepositos = Operacion.sumarComisionesdepositos(cuenta.getNumero());
          sumaComisiones = sumaComisiones + LlenarTablaConsultaBancoTotalizado(cuenta.getNumero());
        }
        double sumaR = Operacion.sumarComisionesTotalesRetiros();
        double sumaD = Operacion.sumarComisionesTotalesdepositos();
        double sumaT = sumaR + sumaD;
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("LA SUMA DE LA GANANCIA DEL BANCO POR DEPOSITOS ES DE:"+ sumaD);
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("LA SUMA DE LA GANANCIA DEL BANCO POR RETIROS ES DE:"+ sumaR);
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("LA SUMA TOTAL DE LA GANANCIA DEL BANCO ES DE:"+ sumaT);
        System.out.println("------------------------------------------------------------------------------------");
        volverMenu();
      }
      //Cuenta específica:
      if ("19".equals(opcion))
      {
        String pNumCuenta = pedirNumCuenta();
        sumaComisiones = LlenarTablaConsultaBancoPorCuenta(pNumCuenta);
        sumaRetiros = Operacion.sumarComisionesRetiros(pNumCuenta);
        sumaDepositos = Operacion.sumarComisionesdepositos(pNumCuenta);
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("LA SUMA DE LA GANANCIA DEL BANCO POR DEPOSITOS ES DE:"+ sumaDepositos);
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("LA SUMA DE LA GANANCIA DEL BANCO POR RETIROS ES DE:"+ sumaRetiros);
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("LA SUMA TOTAL DE LA GANANCIA DEL BANCO ES DE:"+ sumaComisiones);
        System.out.println("------------------------------------------------------------------------------------");
        volverMenu();
      }
        
    }
    
    public static double LlenarTablaConsultaBancoTotalizado(String cuenta)
    {
      double sumaComisiones = 0;
      
      String cuentaDesencrip = Encriptacion.desencriptar(cuenta);
      ArrayList<Operacion> operaciones = OperacionDAO.getOperacionesCuenta(cuentaDesencrip);
      for(Operacion operacion: operaciones)
      {
        if (operacion.getTipo().equals("retiro") || operacion.getTipo().equals("deposito"))
        {
          System.out.println("------------------------------------------------------------------------------------");
          System.out.println("|"+cuentaDesencrip+"|-|"+operacion.getTipo()+"|-|"+operacion.getFechaOperacion()+"|-|"+operacion.getMontoComision()+"|");
          sumaComisiones = sumaComisiones + operacion.getMontoComision();
        }
      }
      
      return sumaComisiones;
    }
    
    public static double LlenarTablaConsultaBancoPorCuenta(String cuenta)
    {
      double sumaComisiones = 0;
      System.out.println("------------------------------------------------------------------------------------");
      System.out.println("|Detalle de la operación |-| Fecha de la operación |-| Comisión del Banco|");
      ArrayList<Operacion> operaciones = OperacionDAO.getOperacionesCuenta(cuenta);
      for(Operacion operacion: operaciones)
      {
        if (operacion.getTipo().equals("retiro") || operacion.getTipo().equals("deposito"))
        {
          double monto = (operacion.getMontoComision()/0.02);
          System.out.println("------------------------------------------------------------------------------------");
          System.out.println("|"+operacion.getTipo()+"|-|"+operacion.getFechaOperacion()+"|-|"+ monto+"|-|"+operacion.getMontoComision()+"|");
          sumaComisiones = sumaComisiones + operacion.getMontoComision();
        }
      }
      return sumaComisiones;
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
                mensaje = Operacion.enviarMensaje(pNumCuenta);
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
                    Operacion.realizarRetiro(monto, moneda, pNumCuenta);
                    double comision = ControladorUsuario.aplicaComisionRetiro(pNumCuenta, montoCorrecto, 4);
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
                mensaje = Operacion.enviarMensaje(pNumCuenta);
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
                    
                    Operacion.realizarTransferencia(pNumCuenta, pNumCuentaDestino, monto);
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
                return pin;
            }
            else
            {
                resultado = Persona.consultarEstadoCuenta(pNumCuenta, moneda);
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
            String numero = Cuenta.insertarCuenta(pin, monto, id);

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
    
        public static void crearCliente(String opcion)
    {
        if ("1".equals(opcion))
        {
            int id = validarId();
            int validar = 0;
            Scanner sc = new Scanner (System.in);
            String apellido1 = validarNombre("primer apellido");
            String apellido2 = validarNombre("segundo apellido");
            String nombre = validarNombre("nombre");
            
            if (validar == 0 ){
            
                //hacer funcion pedir fecha 
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String mensaje = "Digite su fecha de nacimiento (FORMATO AAAA-MM-DD): ";
                System.out.println(mensaje);
                String fechaNacimiento = sc.next();
                LocalDate localDate = LocalDate.parse(fechaNacimiento, formatter);
                int telefono = pedirTelefono();
                String correo = pedirCorreo();


                Persona numero = Persona.insertarCliente(apellido1,apellido2,nombre,id,localDate,telefono,correo);

                String texto4 = "Se ha creado un nuevo cliente en el sistema, los datos del cliente son: ";
                System.out.println(texto4);
                String texto5 = numero.toStringCompleto();
                System.out.println(texto5);
                volverMenu();
            }else{
                System.out.println("Datos incorrectos, revise que su nombre y apellidos no contengan letras");  
                crearCliente("1");
             }
        }
    
    }

        
    public static void listarCuentas(){
        Persona consulta = new Persona();
        ArrayList<Cuenta> cuentas = CuentaDAO.getCuentasBD();
        Collections.sort(cuentas);
        int contador = 1;
        for(Cuenta cuenta: cuentas){
            String cuentaDes = Encriptacion.desencriptar(cuenta.getNumero());
            int infoId = CuentaDAO.obtenerPersonaCuenta(cuentaDes);
            consulta = PersonaDAO.obtenerPersona(infoId);
            System.out.println("Cuenta número: " + contador);
            System.out.println(cuenta);
            System.out.println(consulta.toString());
            System.out.println("\n");
            contador++;
        }
        seleccionarCuenta();
    }

    
    public static void  listarPersonas(String opcion)
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
    
    //Opciones de menú ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    
    public static void ConsultarSaldoActual(String opcion)
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
    
    public static void ConsultarTipoCambioOpcion(String opcion)
    {
      if("11".equals(opcion))
      {
        ConsultaCambioResultado("compra");
      }
      if("12".equals(opcion))
      {
        ConsultaCambioResultado("venta");
      }
    }
    
    public static void ConsultaCambioResultado(String opcion){
      Double cambio = 0.0;
      if("compra".equals(opcion))
      {
        cambio = Operacion.consultarCambioDolar("compra");
      }
      if("venta".equals(opcion))
      {
        cambio = Operacion.consultarCambioDolar("venta");
      }
      
      String resultado = "El tipo de cambio de dolar en" + opcion + " es de: " + cambio + "";
      System.out.println(resultado);
    }
    
    public static void  MenulistarCuentas(String opcion)
    {
        Scanner sc = new Scanner (System.in);
        if("4".equals(opcion))
        {
            listarCuentas();

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
    
        public static void seleccionarCuenta()
    {
        Scanner sc = new Scanner (System.in);
        System.out.println("Digite 1 si desea consultar a una cuenta en específico, o 0 si desea volver al menú principal");
        String op = sc.next();
        boolean esCorrecto = validarOpcion(op, 0, 1);
        if(esCorrecto)
        {
            if("1".equals(op))
            {
                consultarUnaCuenta("18");
                volverMenu();
            }
            else
            {
                main(null);
            }
        } 
        else
        {
            seleccionarCuenta();
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
    
    //VALIDACIONES DE ENTRADAS DE TEXTO -------------------------------------------------------------------------------------------------------------------------------------
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
    
    
    
    public static int validarId()
    {
        Scanner sc = new Scanner (System.in);
        String texto = "Digite su identificacion: ";
        System.out.println(texto);
        String strid = sc.next();
        boolean esCorrecto = ExpresionesRegulares.esNumero(strid);
        
        while (esCorrecto == false)
        {
            String texto1 = "Digite su identificacion: ";
            System.out.println(texto1);
            strid = sc.next();
            esCorrecto = ExpresionesRegulares.esNumero(strid);
        }
        int id = Integer.parseInt(strid);
        if(ExpresionesRegulares.validarIdExiste(id)){
            validarId();
        }
        else{
            return id;
        }
        return id;
    }
    
    public static String validarNombre(String palabra)
    {
        Scanner sc = new Scanner (System.in);
        String texto = "Digite su " + palabra + ": ";
        System.out.println(texto);
        String strNombre = sc.next();
        
        while (strNombre=="")
        {
            String texto1 = "Digite su " + palabra + ": ";
            System.out.println(texto1);
            strNombre = sc.next();
        }
        return strNombre;
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
                Cuenta.inactivarCuenta(pNumCuenta);
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
    
    public static double montoValidoDeposito(double pMonto, String pNumCuenta, String moneda)
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
    
        public static int pedirTelefono()
    {
        Scanner sc = new Scanner (System.in);
        String texto = "Digite su número telefonico costarricense: ";
        System.out.println(texto);
        String strTelefono = sc.next();
        boolean esCorrecto = ExpresionesRegulares.validarTelefono(strTelefono);
        
        while (esCorrecto == false)
        {
            String texto1 = "Digite su número telefonico costarricense: ";
            System.out.println(texto1);
            strTelefono = sc.next();
            esCorrecto = ExpresionesRegulares.validarTelefono(strTelefono);
        }
        int telefono = Integer.parseInt(strTelefono);
        return telefono;
    }
    
    public static String pedirCorreo()
    {
        Scanner sc = new Scanner (System.in);
        String texto = "Digite su correo electrónico: ";
        System.out.println(texto);
        String strCorreo = sc.next();
        boolean esCorrecto = ExpresionesRegulares.validarEmail(strCorreo);
        
        while (esCorrecto == false)
        {
            String texto1 = "Digite su correo electrónico: ";
            System.out.println(texto1);
            strCorreo = sc.next();
            esCorrecto = ExpresionesRegulares.validarEmail(strCorreo);
        }
        return strCorreo;
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
            String texto3 = "Digite un pin válido para la cuenta: ";
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
                Cuenta.inactivarCuenta(pNumCuenta);
                return ("Se ha desactivado la cuenta");
            }
            else
            {
                pPalabra = Operacion.enviarMensaje(pNumCuenta);
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
            String total = Persona.consultarEstadoCuenta(pNumCuenta, "colones");
            System.out.println(total);
        }
    }
}
