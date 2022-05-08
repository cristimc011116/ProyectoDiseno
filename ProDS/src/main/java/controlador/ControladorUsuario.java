/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import static CLI.CLI.enviarMensaje;
import static CLI.CLI.esPinCuenta;
import static CLI.CLI.imprimirResultado;
import static CLI.CLI.inactivarCuenta;
import static CLI.CLI.montoValido;
import static CLI.CLI.nuevoMonto;
import static CLI.CLI.pedirInfoUsuario;
import static CLI.CLI.pedirMonto;
import static CLI.CLI.pedirNumCuenta;
import static CLI.CLI.pedirPalabra;
import GUI.ConsultarEstadoCuenta;
import GUI.ConsultarEstadoCuentaP2;
import GUI.CrearCuenta;
import GUI.Menu;
import GUI.ListarPersonas;
import GUI.Palabra;
import GUI.RealizarRetiro;
import dao.CuentaDAO;
import dao.OperacionDAO;
import dao.PersonaDAO;
import static dao.PersonaDAO.getPersonasBD;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import logicadenegocios.Cuenta;
import logicadenegocios.Operacion;
import logicadenegocios.Persona;
import util.Ordenamiento;
import validacion.ExpresionesRegulares;
import webService.ConsultaMoneda;

/**
 *
 * @author Cristi Martínez
 */
public class ControladorUsuario implements ActionListener{
    
    public Menu menu;
    public CrearCuenta vista1;
    public ListarPersonas vista2;
    public RealizarRetiro vista3;
    public ConsultarEstadoCuenta vista4;
    public ConsultarEstadoCuentaP2 vista5;
    public Palabra palabra;
    private ArrayList<Persona> personasSistema;
    private ListarPersonas latabla;
    
    public ControladorUsuario(Menu pMenu)
    {
        this.menu = pMenu;
        this.latabla = null;
        this.vista1 = null;
        this.personasSistema = new ArrayList<>();
        this.menu.btnListar.addActionListener(this);
        this.menu.btnCrearCuenta.addActionListener(this);
        this.menu.btnRealizarRetiro.addActionListener(this);
        this.menu.btnEstadoCuenta.addActionListener(this);
        cargarDatosPersonas();
        ordenarClientes();
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        switch(e.getActionCommand()){
            case "Listar personas":
                listarPersonas();
                break;
            case "Crear cuenta":
                abrirVista1();
                break;
            case "Realizar retiro":
                abrirVista3();
                break;
            case "Consultar estado de cuenta":
                abrirVista4();
                break;
            case "Continuar":
                crearCuenta();
                break;
            case "Enviar palabra clave":
                enviarPalabra();
                break;
            case "Retirar":
                retirar();
                break;
            case "Consultar":
                consultarEstadoCuentaP1();
                break;
            default:
                break;
        }
    }
    
    public void abrirVista1()
    {
        this.vista1 = new CrearCuenta();
        this.vista1.btnContinuar.addActionListener(this);
        this.vista1.setVisible(true);
        this.menu.setVisible(false);
    }
    
    public void abrirVista3()
    {
        this.vista3 = new RealizarRetiro();
        this.vista3.btnRegresar.addActionListener(this);
        this.vista3.btnLimpiar.addActionListener(this);
        this.vista3.btnEnviarPalabra.addActionListener(this);
        this.vista3.btnRetirar.addActionListener(this);
        this.vista3.txtPalabra.setEnabled(false);
        this.vista3.txtMonto.setEnabled(false);
        this.vista3.btnRetirar.setEnabled(false);
        this.vista3.setVisible(true);
        this.menu.setVisible(false);
    }
    
    public void abrirVista4()
    {
        this.vista4 = new ConsultarEstadoCuenta();
        this.vista4.btnConsultar.addActionListener(this);
        this.vista4.btnLimpiar.addActionListener(this);
        this.vista4.btnRegresar.addActionListener(this);
        this.vista4.setVisible(true);
        this.menu.setVisible(false);
    }
    
    public void enviarPalabra()
    {
        this.palabra = new Palabra();
        int insertar = 0;
        int contador = 0;
        String mensaje = "";
        String cuenta = this.vista3.txtCuenta.getText();
        String pin = this.vista3.txtPin.getText();
        //String palabra = this.vista3.txtPalabra.getText();
        //String strMonto = this.vista3.txtMonto.getText();
        contador += validarIngreso(cuenta, "cuenta");
        contador += validarIngreso(pin, "pin");
        //contador += validarIngreso(palabra, "palabra clave");
        //contador += validarIngreso(strMonto, "monto");
        if(contador == 0)
        {
            insertar += validarCuentaPin(cuenta, pin);
            //insertar += validarEntrMonto(strMonto);
            if (insertar == 0)
            {
                this.vista3.txtPalabra.setEnabled(true);
                this.vista3.txtMonto.setEnabled(true);
                this.vista3.btnRetirar.setEnabled(true);
                mensaje = enviarMensaje(cuenta);
                this.palabra.lbPalabra.setText(mensaje);
                JOptionPane.showMessageDialog(null, "Estimado usuario se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes"
                        + " y procesa a digitar la palabra enviada");
                this.vista3.btnEnviarPalabra.setEnabled(false);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "La cuenta o el pin esta incorrecto");
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Complete todos sus datos");
        }
        
    }    
        
        
        
        
    public void retirar()   
    {
        String cuenta = this.vista3.txtCuenta.getText();
        Cuenta cuentaBase = CuentaDAO.obtenerCuenta(cuenta);
        int insertar = 0;
        int contador = 0;
        String mensaje;
        String resultado = "";
        if(!"inactiva".equals(cuentaBase.getEstatus())){
            String pin = this.vista3.txtPin.getText();
            String palabra = this.vista3.txtPalabra.getText();
            String strMonto = this.vista3.txtMonto.getText();
            String moneda = this.vista3.cbMoneda.getSelectedItem().toString();
            String palabraClave = this.palabra.lbPalabra.getText();
            contador += validarIngreso(cuenta, "cuenta");
            contador += validarIngreso(pin, "pin");
            contador += validarIngreso(palabra, "palabra clave");
            contador += validarIngreso(strMonto, "monto");
            double monto = Double.parseDouble(strMonto);
            contador += validarMonto(monto, cuenta, moneda);
            if(contador == 0)
            {
                insertar += validarCuentaPin(cuenta, pin);
                insertar += validarEntrMonto(strMonto);
                insertar += validarPalabra(palabraClave, cuenta);
                if (insertar == 0)
                {
                    double comision;
                    double nuevoMonto;
                    boolean aplicaCom = Cuenta.aplicaComision(cuenta);
                    monto = montoCorrecto(monto, moneda);
                    comision = monto * 0.02;
                    nuevoMonto = nuevoMonto(comision, aplicaCom, monto);
                    String strSaldoViejo = cuentaBase.getSaldo();
                    double saldoViejo = Double.parseDouble(strSaldoViejo);
                    double nuevoSaldo = saldoViejo - nuevoMonto;
                    String strNuevoSaldo = Double.toString(nuevoSaldo);
                    cuentaBase.setSaldo(strNuevoSaldo);
                    CuentaDAO.actualizarSaldo(cuenta, strNuevoSaldo);
                    ControladorUsuario.insertarOperacion("retiro", (comision>0) , comision, cuenta);
                    resultado = imprimirResultado(moneda, comision, monto);
                    JOptionPane.showMessageDialog(null, resultado);
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Verifique sus datos");
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Complete todos sus datos");
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Su cuenta se encuentra desactivada");
        }
        this.vista3.txtCuenta.setText("");
        this.vista3.txtPin.setText("");
        this.vista3.txtPalabra.setText("");
        this.vista3.txtMonto.setText("");
        this.vista3.txtPalabra.setEnabled(false);
        this.vista3.txtMonto.setEnabled(false);
        this.vista3.btnRetirar.setEnabled(false);
    }
    
    public void crearCuenta()
    {
        int insertar = 0;
        int contador = 0;
        String strId = this.vista1.tfId.getText();
        String pin = this.vista1.tfPin.getText();
        String strMonto = this.vista1.tfMonto.getText();
        contador += validarIngreso(strId, "identificacion");
        contador += validarIngreso(pin, "pin");
        contador += validarIngreso(strMonto, "monto");
        if(contador == 0)
        {
            insertar += validarEntrId(strId);
            insertar += validarEntrPin(pin);
            insertar += validarEntrMonto(strMonto);
            if (insertar == 0)
            {
                int id = Integer.parseInt(strId);
                String numero = ControladorUsuario.insertarCuenta(pin, strMonto, id);

                String mensaje = "Se ha creado una nueva cuenta en el sistema, los datos de la cuenta son: \n";
                mensaje += ControladorUsuario.imprimirCuenta(numero);
                mensaje += "\n---\n";
                mensaje += ControladorUsuario.imprimirPersona(id);
                JOptionPane.showMessageDialog(null, mensaje, "Consulta de usuario", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Verifique sus datos");
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Complete todos sus datos");
        }
        
    }
    public int validarIngreso(String pEntrada, String opcion)
    {
        if(pEntrada.length() == 0)
        {
            JOptionPane.showMessageDialog(null, "Error en dato: " + opcion);
            return 1;
        }
        return 0;
    }
    
    public boolean pedirPalabra(String pPalabra, String pNumCuenta)
    {
        Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
        String cont;
        int contador;
        String palabra = this.vista3.txtPalabra.getText();
        if (!pPalabra.equals(palabra))
        {
            cont = this.vista3.txtIntPalabra.getText();
            contador = Integer.parseInt(cont);
            contador++;
            cont = Integer.toString(contador);
            this.vista3.txtIntPalabra.setText(cont);
            if(contador >= 2)
            {
                inactivarCuenta(pNumCuenta);
                JOptionPane.showMessageDialog(null, "Se ha desactivado la cuenta");
            }
            return false;
        }
        return true;
    }
    
    public int validarPalabra(String pPalabra, String pNumCuenta)
    {
        if(pedirPalabra(pPalabra, pNumCuenta))
        {
            return 0;
        }
        return 1;
    }
    
    public int validarMonto(double pMonto, String pNumCuenta, String moneda)
    {
        if(montoValido(pMonto, pNumCuenta, moneda))
        {
            return 0;
        }
        return 1;
        
    }
    
    public boolean esPinCuenta(String pNumCuenta, String pin)
    {
        Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
        String cont;
        int contador;
        String pinDesencriptado = Cuenta.desencriptar(cuenta.getPin());
        if (!pin.equals(pinDesencriptado))
        {
            cont = this.vista3.txtIntPin.getText();
            contador = Integer.parseInt(cont);
            contador++;
            cont = Integer.toString(contador);
            this.vista3.txtIntPin.setText(cont);
            if(contador >= 2)
            {
                inactivarCuenta(pNumCuenta);
                JOptionPane.showMessageDialog(null, "Se ha desactivado la cuenta");
            }
            return false;
        }
        return true;
    }
    
    public boolean esPinCuenta2(String pNumCuenta, String pin)
    {
        Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
        String cont;
        int contador;
        String pinDesencriptado = Cuenta.desencriptar(cuenta.getPin());
        if (!pin.equals(pinDesencriptado))
        {
            cont = this.vista4.lbintentos.getText();
            contador = Integer.parseInt(cont);
            contador++;
            cont = Integer.toString(contador);
            this.vista4.lbintentos.setText(cont);
            if(contador >= 2)
            {
                inactivarCuenta(pNumCuenta);
                JOptionPane.showMessageDialog(null, "Se ha desactivado la cuenta");
            }
            return false;
        }
        return true;
    }
    
    public static boolean montoValido(double pMonto, String pNumCuenta, String moneda)
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
            if (monto<pMonto)
            {
                return false;
            }
        }
        else
        {
            if (monto<pMonto)
            {
                return false;
            }
        }
        
        return true;
    }
    
    
    public static double montoCorrecto(double pMonto, String moneda)
    {
        ConsultaMoneda consulta = new ConsultaMoneda();
        double venta = consulta.consultaCambioVenta();
        double monto = pMonto;
        if("dolares".equals(moneda))
        {
            monto = pMonto * venta;
        }
        return monto;
    }
    
    public int validarPin(String pNumCuenta, String pPin)
    {
        if(esPinCuenta(pNumCuenta, pPin))
        {
            return 0;
        }
        return 1;
    }
    
    
    public int validarPin2(String pNumCuenta, String pPin)
    {
        if(esPinCuenta2(pNumCuenta, pPin))
        {
            return 0;
        }
        return 1;
    }
    
    
    public int validarEntrCuenta(String numCuenta)
    {
        if(auxNumCuentaP1(numCuenta))
        {
            return 0;
        }
        return 1;
    }
    
    public int validarEntrId(String strId)
    {
        boolean esId = ControladorUsuario.auxIdP1(strId);
        if (esId==false)
        {
            JOptionPane.showMessageDialog(null, "Verifique su identificación");
            return 1;
        }
        return 0;
    }
    
    public int validarEntrPin(String pin)
    {
        boolean esPin = ExpresionesRegulares.validarPin(pin);
        if (esPin == false)
        {
            JOptionPane.showMessageDialog(null, "Verifique su pin");
            return 1;
        }
        return 0;
    }
    
    public int validarEntrMonto(String strMonto)
    {
        boolean esNum = ExpresionesRegulares.esNumero(strMonto);
        if (esNum == false)
        {
            JOptionPane.showMessageDialog(null, "Verifique el monto digitado");
            return 1;
        }
        return 0;
    }
    
    
    private void cargarDatosPersonas(){
        ResultSet datos = PersonaDAO.recuperarTodosLosUsuariosBD();
        try{
            while (datos.next()){
                Persona usuarioCargar = new Persona(datos.getString("codigo"), datos.getString("primerApellido"),
                        datos.getString("segundoApellido"),
                        datos.getString("nombre"),
                        Integer.parseInt(datos.getString("id")),
                        LocalDate.parse(datos.getString("fechaNacimiento"), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        Integer.parseInt(datos.getString("numero")),
                        datos.getString("correo"),
                        datos.getString("rol"));
                personasSistema.add(usuarioCargar);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
    }
    
    public void listarPersonas(){
        this.latabla = new ListarPersonas();
        String[] titulos = {
            "Nombre",
            "Primer apellido",
            "Segundo apellido",
            "Identificación"};
        this.latabla.modelo = new DefaultTableModel(null, titulos);
        this.latabla.tabla2.setModel(this.latabla.modelo);
        for(Persona persona: personasSistema)
        {
           Object[] info = {persona.getNombre(), persona.getPrimerApellido(), persona.getSegundoApellido(), persona.getId()};
           this.latabla.modelo.addRow(info);
        }
        this.latabla.setVisible(true);
        this.menu.setVisible(false);
    }
    
    public int validarCuentaPin(String numCuenta, String pin)
    {
        int insertar = 0;
        insertar += validarEntrCuenta(numCuenta);
        if(insertar==0)
        {
           insertar += validarPin(numCuenta, pin);
           if(insertar==0)
           {
               return 0;
           }
        }
        return 1;
        
    }
    
    
    public int validarCuentaPin2(String numCuenta, String pin)
    {
        int insertar = 0;
        insertar += validarEntrCuenta(numCuenta);
        if(insertar==0)
        {
           insertar += validarPin2(numCuenta, pin);
           if(insertar==0)
           {
               return 0;
           }
        }
        return 1;
        
    }
    
    public void consultarEstadoCuentaP1(){
        int insertar = 0;
        int contador = 0;
        String mensaje;
        String cuenta = this.vista4.txtCuenta.getText();
        String pin = this.vista4.txtPin.getText();
        contador += validarIngreso(cuenta, "cuenta");
        contador += validarIngreso(pin, "pin");
        if(contador == 0)
        {
            insertar += validarCuentaPin2(cuenta, pin);
            if (insertar==0)
            {
                Cuenta cuentaBase = CuentaDAO.obtenerCuenta(cuenta);
                if("inactiva".equals(cuentaBase.getEstatus())){
                    consultarEstadoCuentaP2();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "La cuenta está inactiva");
                }
                
            }
            else
            {
                JOptionPane.showMessageDialog(null, "La cuenta o pin está incorrecto");
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Complete todos sus datos");
        }
    }
    
    
    public void consultarEstadoCuentaP2(){
        ConsultaMoneda consulta = new ConsultaMoneda();
        this.vista5 = new ConsultarEstadoCuentaP2();
        String cuenta = this.vista4.txtCuenta.getText();
        Cuenta cuentaBase = CuentaDAO.obtenerCuenta(cuenta);
        if(cuentaBase.getEstatus()=="inactiva"){
            this.vista5.lbCuenta.setText(cuenta);
            int idDueno = CuentaDAO.obtenerPersonaCuenta(cuenta);
            String strIdDueno = Integer.toString(idDueno);
            this.vista5.lbIdDueno.setText(strIdDueno);
            Persona persona = PersonaDAO.obtenerPersona(idDueno);
            String nombreDueno = persona.getNombre() + persona.getPrimerApellido() + persona.getSegundoApellido();
            this.vista5.lbNombreDueno.setText(nombreDueno);
            String moneda = this.vista4.cbMoneda.getSelectedItem().toString();
            String numCuenta = this.vista4.txtCuenta.getText();
            ArrayList<Operacion> operaciones = OperacionDAO.getOperacionesCuenta(numCuenta);
            String[] titulos = {
                "Fecha",
                "Tipo",
                "Monto",
                "Comisión"};
            this.vista5.modelo = new DefaultTableModel(null, titulos);
            this.vista5.tablaEstado.setModel(this.vista5.modelo);
            for(Operacion operacion: operaciones)
            {
               if(moneda=="colones")
               {
                    this.vista5.lbSaldo.setText(cuentaBase.getSaldo());
                    double monto = (operacion.getMontoComision()/0.02);
                    Object[] info = {operacion.getFechaOperacion(), operacion.getTipo(), monto, operacion.getMontoComision()};
                    this.vista5.modelo.addRow(info);
               }
               else
               {
                    double venta = consulta.consultaCambioVenta();
                    String strSaldoColones = cuentaBase.getSaldo();
                    double saldoColones = Double.parseDouble(strSaldoColones);
                    double saldoDolares = saldoColones/venta;
                    String strSaldoDolares = Double.toString(saldoDolares);
                    this.vista5.lbSaldo.setText(strSaldoDolares);
                    double monto = (operacion.getMontoComision()/0.02);
                    double comisionDolares = (operacion.getMontoComision()/venta);
                    double montoDolares = monto/venta;
                    Object[] info = {operacion.getFechaOperacion(), operacion.getTipo(), montoDolares, comisionDolares};
                    this.vista5.modelo.addRow(info);
               }
            }
            this.vista5.setVisible(true);
            this.vista4.setVisible(false);
        }
        else
        {
            JOptionPane.showMessageDialog(null, "La cuenta está inactiva");
        }
    }
    
        
    public static String consultarUsuario(String idUsuario)
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
    
    public static String recuperarUsuario(String idUsuario)
    {
        int id = Integer.parseInt(idUsuario);
        Persona persona = PersonaDAO.obtenerPersona(id);
        String mensaje = persona.toStringCompleto();
        String mensajeCuentas = CuentaDAO.obtenerCuentasPersona(id);
        String total = "Información del usuario:\n" + mensaje + "\n\nCuentas de este usuario:\n" + mensajeCuentas;
        return total;
    }
    
    //Punto 2
    public static String insertarCuenta(String pPin, String pMonto, int pId)
    {
        String numero = Cuenta.generarNumCuenta();
        LocalDate fecha = Cuenta.setFechaCreacion();
        Cuenta cuenta = new Cuenta(numero, pPin, fecha, pMonto, "activo");
        CuentaDAO.insertarCuenta(cuenta,fecha);
        CuentaDAO.asignarCuentaCliente(cuenta, pId);
        
        return numero;
    }
    
    public static void insertarOperacion(String pTipo, boolean pEsComision, double pMontoComision, String pNumCuenta)
    {
        int id = OperacionDAO.cantOperacionesBD();
        LocalDate fecha = Cuenta.setFechaCreacion();
        Operacion operacion = new Operacion(id, fecha, pTipo, pEsComision, pMontoComision);
        OperacionDAO.insertarOperacion(operacion,fecha);
        OperacionDAO.asignarOperacionCuenta(operacion, pNumCuenta);
    }
    
    private void ordenarClientes(){
        personasSistema.sort(Comparator.comparing(Persona::getPrimerApellido));
    }
    
    public static String imprimirCuenta(String pNum){
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
    
    public static boolean auxNumCuentaP1(String strNumCuenta){
        boolean esCorrecto = false;
        boolean esNum = ExpresionesRegulares.esNumero(strNumCuenta);
        if(esNum == true)
        {
            //int numCuenta = Integer.parseInt(strNumCuenta);
            esCorrecto = auxNumCuentaP2(strNumCuenta);
        }
        return esCorrecto;
    }
    
    public static boolean auxNumCuentaP2(String pNumCuenta){
        boolean esNum = false;
        Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
        String numCuenta = cuenta.getNumero();
        if(!"".equals(numCuenta)){
            esNum = true;
        }
        
        return esNum;
    }
    
    //Punto 3
    public static Persona[] ordenarPersonas(){
        //int cont = 0;
        //Persona[] listaPersona = new Persona[100];
        //ArrayList<Persona> listaPersonas1 = PersonaDAO.getPersonasBD();
        Persona[] listaPersonas = new Persona[10];
        Persona p1 = new Persona("1apellido", "2apellido", "nombre", 890);
        Persona p2 = new Persona("1apellido2", "2apellido2", "nombre2", 891);
        Persona p3 = new Persona("1apellido3", "2apellido3", "nombre3", 892);
        listaPersonas[0] = p1;
        listaPersonas[1] = p2;
        listaPersonas[2] = p3;
        for(Persona persona: listaPersonas)
        {
            String primerApellido = persona.getPrimerApellido();
            String segundoApellido = persona.getSegundoApellido();
            String nombre = persona.getNombre();
            int id = persona.getId();
            Persona usuario = new Persona(primerApellido, segundoApellido, nombre, id);
            //listaPersona[cont] = usuario;
            //cont++;
        }
        Ordenamiento.ordenar(listaPersonas);
        return listaPersonas;
    }
    
}
