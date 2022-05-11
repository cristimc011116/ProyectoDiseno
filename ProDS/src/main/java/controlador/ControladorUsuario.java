/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import GUI.CambiarPIN;
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
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import logicadenegocios.Cuenta;
//import util.Mensaje;
import logicadenegocios.Operacion;
import logicadenegocios.Persona;
import util.Encriptacion;
import validacion.ExpresionesRegulares;
import webService.ConsultaMoneda;

/**
 *
 * @author Cristi Martínez
 */
public class ControladorUsuario implements ActionListener{
    
    public Menu menu;
    Cuenta cuenta= new Cuenta();
    public CrearCuenta vista1;
    public ListarPersonas vista2;
    public RealizarRetiro vista3;
    public ConsultarEstadoCuenta vista4;
    public ConsultarEstadoCuentaP2 vista5;
    public CambiarPIN vista6; 
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
            case "Cambiar PIN":
                abrirVista6();
                break;
            default:
                break;
        }
    }
    
    //ABRIR VISTAS-------------------------------------------------------------------------------------------------------------------------------------
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
    
    public void abrirVista6()
    {
      this.vista6 = new CambiarPIN();
      this.vista6.btnCambiarPin.addActionListener(this);
      this.vista6.btnRegresar.addActionListener(this);
      this.vista6.setVisible(true);
      this.menu.setVisible(false);
    }
    
    //FUNCIONALIDADES----------------------------------------------------------------------------------------------------------------------------------
    public void cambiarPIN_p1()
    {
      boolean insertar;
      int contador =0;
      
      String numCuenta = this.vista6.txtNumeroCuenta.getText();
      Cuenta cuentaBase = CuentaDAO.obtenerCuenta(numCuenta);
      if(!"inactiva".equals(cuentaBase.getEstatus())){
        String pinActual = this.vista6.txtPinActual.getText();
        String pinNuevo = this.vista6.txtPinNuevo.getText();
        contador += validarIngreso(numCuenta, "cuenta");
        contador += validarIngreso(pinActual, "pin");
        contador += validarIngreso(pinNuevo, "pin");
        if(contador == 0)
        {
          insertar = esPinCuentaCambioPin (numCuenta,  pinActual);
          if (insertar == true)
          {
            cambiarPIN_p2( numCuenta, pinNuevo);
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
    }
    
    public void cambiarPIN_p2(String numCuenta, String pinNuevo)
    {
     
      ArrayList<Cuenta> listaCuentas = CuentaDAO.getCuentasBD();
      for(int i=0;i<listaCuentas.size();i++){
        cuenta=listaCuentas.get(i);
        String pCuentaDesencriptada = Encriptacion.desencriptar(cuenta.getNumero());
        if(pCuentaDesencriptada.equals(numCuenta)){
          pinNuevo = Encriptacion.encriptar(pinNuevo);
          cuenta.setPin(pinNuevo);
        }
      }

      //cargar los datos a la BD

      this.vista6.txtNumeroCuenta.setText("");
      this.vista6.txtPinActual.setText("");
      this.vista6.txtPinNuevo.setText("");
    }
    
    public void enviarPalabra()
    {
      this.palabra = new Palabra();
      int insertar = 0;
      int contador = 0;
      String mensaje = "";
      String cuenta = this.vista3.txtCuenta.getText();
      String pin = this.vista3.txtPin.getText();
      contador += validarIngreso(cuenta, "cuenta");
      contador += validarIngreso(pin, "pin");
      if(contador == 0)
      {
        insertar += validarCuentaPin(cuenta, pin);
        if (insertar == 0)
        {
          this.vista3.txtPalabra.setEnabled(true);
          this.vista3.txtMonto.setEnabled(true);
          this.vista3.btnRetirar.setEnabled(true);
          mensaje = enviarMensaje(cuenta);
          this.palabra.lbPalabra.setText(mensaje);
          JOptionPane.showMessageDialog(null, "Estimado usuario se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes"
                  + " y procesa a digitar la palabra enviada");
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
        contador += validarEntrMonto(strMonto);

        if(contador == 0)
        {
          insertar += validarCuentaPin(cuenta, pin);
          double monto = Double.parseDouble(strMonto);
          insertar += validarMonto(monto, cuenta, moneda);
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
            this.vista3.txtCuenta.setText("");
            this.vista3.txtPin.setText("");
            this.vista3.txtPalabra.setText("");
            this.vista3.txtMonto.setText("");
            this.vista3.txtPalabra.setEnabled(false);
            this.vista3.txtMonto.setEnabled(false);
            this.vista3.btnRetirar.setEnabled(false);
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
          if("activo".equals(cuentaBase.getEstatus())){
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
    }
    
    public void consultarEstadoCuentaP2(){
      ConsultaMoneda consulta = new ConsultaMoneda();
      this.vista5 = new ConsultarEstadoCuentaP2();
      String cuenta = this.vista4.txtCuenta.getText();
      Cuenta cuentaBase = CuentaDAO.obtenerCuenta(cuenta);
      this.vista5.lbCuenta.setText(cuenta);
      String pin = cuentaBase.getPin();
      this.vista5.lbPin.setText(pin);
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
        if("colones".equals(moneda))
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
    
    //VALIDACIONES-------------------------------------------------------------------------------------------------------------------------------------
    public int validarIngreso(String pEntrada, String opcion)
    {
      if(pEntrada.length() == 0)
      {
        JOptionPane.showMessageDialog(null, "Error en dato: " + opcion);
        return 1;
      }
      return 0;
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
      String pinDesencriptado = Encriptacion.desencriptar(cuenta.getPin());
      if (!pin.equals(pinDesencriptado))
      {
        cont = this.vista3.txtIntPin.getText();
        contador = Integer.parseInt(cont);
        contador++;
        cont = Integer.toString(contador);

        if(contador >= 2)
        {
          this.vista3.txtIntPin.setText("2");
          inactivarCuenta(pNumCuenta);
          JOptionPane.showMessageDialog(null, "Se ha desactivado la cuenta por el ingreso del pin incorrecto");
        }
        else
        {
          this.vista3.txtIntPin.setText(cont);
        }
        return false;
      }
      return true;
    }
    
    public boolean esPinCuentaCambioPin(String pNumCuenta, String pin)
    {
      Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
      String cont;
      int contador;
      String pinDesencriptado = Encriptacion.desencriptar(cuenta.getPin());
      if (!pin.equals(pinDesencriptado))
      {
        cont = this.vista6.lblCantIntentosFall.getText();
        contador = Integer.parseInt(cont);
        contador++;
        cont = Integer.toString(contador);

        if(contador >= 2)
        {
          this.vista6.lblCantIntentosFall.setText("2");
          inactivarCuenta(pNumCuenta);
          JOptionPane.showMessageDialog(null, "Se ha desactivado la cuenta por el ingreso del pin incorrecto");
        }
        else
        {
          this.vista6.lblCantIntentosFall.setText(cont);
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
      String pinDesencriptado = Encriptacion.desencriptar(cuenta.getPin());
      if (!pin.equals(pinDesencriptado))
      {
        cont = this.vista4.lbintentos.getText();
        contador = Integer.parseInt(cont);
        contador++;
        cont = Integer.toString(contador);

        if(contador >= 2)
        {
          this.vista4.lbintentos.setText("2");
          inactivarCuenta(pNumCuenta);
          JOptionPane.showMessageDialog(null, "Se ha desactivado la cuenta por el ingreso del pin incorrecto");
        }
        else
        {
          this.vista4.lbintentos.setText(cont);
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
    
    //PEDIDOS DE INFORMACIÓN---------------------------------------------------------------------------------------------------------------------------
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
          JOptionPane.showMessageDialog(null, "Se ha desactivado la cuenta por el ingreso incorrecto de la palabra clave");
        }
        else
        {
          //JOptionPane.showMessageDialog(null, "Para otro intento, presione en el botón 'Enviar palabra'");
          //this.palabra.lbPalabra.setText("");

          enviarPalabra();
        }
        return false;
      }
      return true;
    }
    
    //OTROS--------------------------------------------------------------------------------------------------------------------------------------------
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
    
    public static void inactivarCuenta(String pNumCuenta)
    {
      Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
      cuenta.setEstatus("inactiva");
      CuentaDAO.inactivarCuentaBase(pNumCuenta);
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
      //Mensaje.enviarMensaje(83211510, mensaje);
      return mensaje;
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
    
    public static String recuperarUsuario(String idUsuario)
    {
      int id = Integer.parseInt(idUsuario);
      Persona persona = PersonaDAO.obtenerPersona(id);
      String mensaje = persona.toStringCompleto();
      String mensajeCuentas = CuentaDAO.obtenerCuentasPersona(id);
      String total = "Información del usuario:\n" + mensaje + "\n\nCuentas de este usuario:\n" + mensajeCuentas;
      return total;
    }
    
        public static String recuperarCuenta(String pNumeroCuenta)
    {
      Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumeroCuenta);
      String mensaje = cuenta.toString();
      String total = "Información de la cuenta solicitada:\n" + mensaje;
      return total;
    }
    
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
}
