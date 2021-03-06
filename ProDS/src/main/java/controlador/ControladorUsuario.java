/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import CLI.CLI;
import static CLI.CLI.montoValidoDeposito;
import GUI.CambiarPIN;
import GUI.ConsultaGananciaBancoCuenta;
import GUI.ConsultaGananciaBancoTotal;
import GUI.ConsultarEstadoCuenta;
import GUI.ConsultarEstadoCuentaP2;
import GUI.ConsultarSaldo;
import GUI.ConsultarStatus;
import GUI.CrearCuenta;
import GUI.CrearCliente;
import GUI.Menu;
import GUI.ListarPersonas;
import validacion.ExpresionesRegulares;
import GUI.Palabra;
import GUI.ConsultarCambioDolar;
import GUI.ListarCuentas;
import GUI.RealizarDeposito;
import GUI.RealizarRetiro;
import GUI.RealizarTransferencia;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import dao.CuentaDAO;
import webService.ConsultaMoneda;
import dao.OperacionDAO;
import dao.PersonaDAO;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import logicadenegocios.Cuenta;
//import util.Mensaje;
import logicadenegocios.Persona;
import logicadenegocios.Operacion;
import util.CorreoElectronico;
import util.Encriptacion;
import util.Mensaje;
//import util.Mensaje;
import validacion.ExpresionesRegulares;
import webService.ConsultaMoneda;

/**
 *
 * @author Cristi Martínez
 */
public class ControladorUsuario implements ActionListener{
    
    public Menu menu;
    Cuenta cuenta= new Cuenta();
    Operacion operacion = new Operacion();
    public CrearCuenta vista1;
    public ListarPersonas vista2;
    public RealizarRetiro vista3;
    public ConsultarEstadoCuenta vista4;
    public ConsultarEstadoCuentaP2 vista5;
    public CambiarPIN vista6; 
    public RealizarDeposito vista7;
    public ConsultarSaldo vista8;
    public ConsultaGananciaBancoCuenta vista9;
    public ConsultaGananciaBancoTotal vista10;
    public Palabra palabra;
    public CrearCliente vista11;
    public ConsultarCambioDolar vista12;
    public RealizarTransferencia vista13;
    public ConsultarStatus vista14;
    private ArrayList<Persona> personasSistema;
    private ArrayList<Cuenta> cuentasSistema;
    private ListarPersonas latabla;
    private ListarCuentas latabla2;
    
    public ControladorUsuario(Menu pMenu)
    {
        this.menu = pMenu;
        this.latabla = null;
        this.latabla2 = null;
        this.vista1 = null;
        this.personasSistema = new ArrayList<>();
        this.cuentasSistema = new ArrayList<>();
        this.menu.btnListar.addActionListener(this);
        this.menu.btnCrearCuenta.addActionListener(this);
        this.menu.btnRealizarRetiro.addActionListener(this);
        this.menu.btnEstadoCuenta.addActionListener(this);
        this.menu.btnDepositar.addActionListener(this);
        this.menu.btnConsultaSaldoCuenta.addActionListener(this);
        this.menu.btnGananciaBancoPorCuenta.addActionListener(this);
        this.menu.btnCrearCliente.addActionListener(this);
        this.menu.btnCambioDolar.addActionListener(this);
        this.menu.btnCambiarPIN.addActionListener(this);
        this.menu.btnTransferencia.addActionListener(this);
        this.menu.btnGananciaBancoTotalizado.addActionListener(this);
        this.menu.btnConsultaStatus.addActionListener(this);
        this.menu.btnListarCuentas.addActionListener(this);
        //cargarDatosPersonas();
        personasSistema = Persona.ordenarClientes();
        //cargarDatosCuentas();
        cuentasSistema = Cuenta.ordenarCuentas();
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        switch(e.getActionCommand()){
            case "Listar personas":
                listarPersonas();
                break;
            case "Listar cuentas":
                listarCuentas();
                break;
            case "Crear cuenta":
                abrirVista1();
                break;
            case "Realizar retiro":
                abrirVista3();
                break;
            case "TransferenciaMenu":
                abrirVista13();
                break;
            case "Consultar estado de cuenta":
                abrirVista4();
                break;
            case "Continuar":
                crearCuenta();
                break;
            case "Enviar palabra clave":
                enviarPalabraTransferencia();
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
            case "Continuar Pin":
                cambiarPIN();
                break;
            case "Realizar deposito":
                abrirVista7();
                break;
            case "Consultar Saldo Cuenta":
                abrirVista8();
                break;
            case "Consulta ganancia del banco por cuenta":
                abrirVista9();
                break;
            case "Consulta ganancia del banco TOTALIZADO":
                abrirVista10();
                LlenarTablaConsultaBancoTotalizado();
                break;
            case "Crear Cliente":
                abrirVista11();
                break;
            case "CambioDolarMenu":
                abrirVista12();
                ConsultarCambioVentaCompra();
                break;
            case "ContinuarDeposito":
                depositar();
                break;
            case "ContinuarSaldo":
                consultarSaldoCuenta();
                break;
            case "Registrar Cliente":
                crearCliente();
                break;
            case "Transferir":
                transferir();
                break;
            case "ContinuarStatus": 
                consultarStatus();
                break;
            case "ConsultarComCuenta":
                consultarGananciaBancoCuenta();
                break;
            case "ConsultarGananciaCuenta":
                consultarGananciaBancoCuenta();
                break;
            case "Consultar status":
                abrirVista14();
                break;
            case "Consultar cuenta en específico":
                consultarInfoCuenta();
                break;
            case "Enviar palabra retiro":
                enviarPalabra();
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
    
    public void abrirVista11()
    {
        this.vista11 = new CrearCliente();
        this.vista11.btnRegistrar.addActionListener(this);
        this.vista11.setVisible(true);
        this.menu.setVisible(false);
    }
    
        public void abrirVista12()
    {
        this.vista12 = new ConsultarCambioDolar();
        this.vista12.setVisible(true);
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
    
        public void abrirVista13()
    {
        this.vista13 = new RealizarTransferencia();
        this.vista13.btnRegresar.addActionListener(this);
        this.vista13.btnLimpiar.addActionListener(this);
        this.vista13.btnEnviarPalabra.addActionListener(this);
        this.vista13.btnTransferir.addActionListener(this);
        this.vista13.txtPalabra.setEnabled(false);
        this.vista13.txtMonto.setEnabled(false);
        this.vista13.btnTransferir.setEnabled(false);
        this.vista13.setVisible(true);
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
    
        public void abrirVista14()
    {
        this.vista14 = new ConsultarStatus();
        this.vista14.btnContinuar.addActionListener(this);
        this.vista14.btnLimpiar.addActionListener(this);
        this.vista14.btnRegrasar.addActionListener(this);
        this.vista14.setVisible(true);
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
    
    public void abrirVista7()
    {
      this.vista7 = new RealizarDeposito();
      this.vista7.btnContinuar.addActionListener(this);
      this.vista7.btnAtras.addActionListener(this);
      this.vista7.cmbTipoMoneda.addActionListener(this);
      this.vista7.setVisible(true);
      this.menu.setVisible(false);
    }
    public void abrirVista8()
    {
      this.vista8 = new ConsultarSaldo();
      this.vista8.btnContinuar.addActionListener(this);
      this.vista8.btnRegrasar.addActionListener(this);
      this.vista8.cmbTipoMoneda.addActionListener(this);
      this.vista8.setVisible(true);
      this.menu.setVisible(false);
    }
    
    public void abrirVista9()
    {
      this.vista9 = new ConsultaGananciaBancoCuenta();
      this.vista9.btnConsultar.addActionListener(this);
      this.vista9.setVisible(true);
      this.menu.setVisible(false);
    }
    public void abrirVista10()
    {
      this.vista10 = new ConsultaGananciaBancoTotal();
      this.vista10.setVisible(true);
      this.menu.setVisible(false);
    }

    //FUNCIONALIDADES----------------------------------------------------------------------------------------------------------------------------------
    public void cambiarPIN()
    {
      int insertar = 0;
      int contador =0;
      
      String numCuenta = this.vista6.txtNumeroCuenta.getText();
      Cuenta cuentaBase = CuentaDAO.obtenerCuenta(numCuenta);
      if(!"inactiva".equals(cuentaBase.getEstatus())){
        String pinActual = this.vista6.txtPinActual.getText();
        String pinNuevo = this.vista6.txtPinNuevo.getText();
        contador += validarIngreso(numCuenta, "cuenta");
        contador += validarIngreso(pinActual, "pin actual");
        contador += validarIngreso(pinNuevo, "pin nuevo");
        if(contador == 0)
        {
          insertar += validarCuentaPinCambio(numCuenta,  pinActual, pinNuevo);
          if (insertar == 0){
            operacion.cambiarPIN(numCuenta, pinNuevo);

            this.vista6.txtNumeroCuenta.setText("");
            this.vista6.txtPinActual.setText("");
            this.vista6.txtPinNuevo.setText("");

            JOptionPane.showMessageDialog(null, "Estimado usuario, se ha cambiado satisfactoriamente el PIN de su cuenta: "+numCuenta);
          }
          else
          {
            JOptionPane.showMessageDialog(null, "Verifique sus datos");
          }
        }
      }
      else
      {
        JOptionPane.showMessageDialog(null, "Su cuenta se encuentra desactivada");
      } 
    }
    
    public void depositar()   
    {
      String cuenta = this.vista7.txtNumCuenta.getText();
      Cuenta cuentaBase = CuentaDAO.obtenerCuenta(cuenta);
      int insertar = 0;
      int contador = 0;
      String resultado = "";
      if(!"inactiva".equals(cuentaBase.getEstatus())){
        String strMonto = this.vista7.txtMontoDeposito.getText();
        String moneda = this.vista7.cmbTipoMoneda.getSelectedItem().toString();
        contador += validarIngreso(cuenta, "cuenta");
        contador += validarIngreso(strMonto, "monto");
        contador += validarEntrMonto(strMonto);
        if(contador == 0)
        {
          double monto = Double.parseDouble(strMonto);
          insertar += validarEntrCuenta(cuenta);
          if (insertar == 0)
          {
            Operacion.realizarDeposito(monto, moneda, cuenta);  
            double montoCorrecto = montoValidoDeposito(monto, cuenta, moneda);
            double comision = ControladorUsuario.aplicaComisionRetiro(cuenta, montoCorrecto, 4);
            resultado = imprimirResultadoDeposito( moneda,comision,montoCorrecto,cuenta);
            JOptionPane.showMessageDialog(null, resultado);
            this.vista7.txtMontoDeposito.setText("");
            this.vista7.txtNumCuenta.setText("");
            this.vista7.cmbTipoMoneda.setSelectedIndex(0);
          }
        }
      }
      else
      {
        JOptionPane.showMessageDialog(null, "Su cuenta se encuentra desactivada");
      }   
    }
    
    public void consultarSaldoCuenta()   
    {
      String cuenta = this.vista8.txtNumCuenta.getText();
      Cuenta cuentaBase = CuentaDAO.obtenerCuenta(cuenta);
      int insertar = 0;
      int contador = 0;
      String resultado = "";
      if(!"inactiva".equals(cuentaBase.getEstatus())){
        String pin = this.vista8.txtPIN.getText();
        String moneda = this.vista8.cmbTipoMoneda.getSelectedItem().toString();
        contador += validarIngreso(cuenta, "cuenta");
        contador += validarIngreso(pin, "pin");
        if(contador == 0)
        {
          insertar += validarCuentaPinSaldo(cuenta, pin);
          if (insertar == 0)
          {
            String saldo = Operacion.consultarSaldo(cuenta);
            resultado = imprimirResultadoConsultaSaldo(moneda, saldo);
            
            JOptionPane.showMessageDialog(null, resultado);
            this.vista8.txtPIN.setText("");
            this.vista8.txtNumCuenta.setText("");
            this.vista8.cmbTipoMoneda.setSelectedIndex(0);
          }
        }
      }
      else
      {
        JOptionPane.showMessageDialog(null, "Su cuenta se encuentra desactivada");
      }   
    }
    
    public void consultarStatus()   
    {
      String cuenta = this.vista14.txtNumCuenta.getText();
      Cuenta cuentaBase = CuentaDAO.obtenerCuenta(cuenta);
      int insertar = 0;
      int contador = 0;
      String resultado = "";
      contador += validarIngreso(cuenta, "cuenta");
      if(contador == 0)
      {
        insertar += validarEntrCuenta(cuenta);
        if (insertar == 0)
        {
          String status = Operacion.consultarStatus(cuenta);
          resultado = imprimirResultadoConsultaStatus(status);
          JOptionPane.showMessageDialog(null, resultado);
          this.vista14.txtNumCuenta.setText("");
        }
      } 
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
          mensaje = Operacion.enviarMensaje(cuenta);
          this.palabra.lbPalabra.setText(mensaje);
          JOptionPane.showMessageDialog(null, "Estimado usuario se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes"
                  + " y procesa a digitar la palabra enviada");
        }
        else
        {
          JOptionPane.showMessageDialog(null, "La cuenta o el pin esta incorrecto");
        }
      }
    }   
    
        public void enviarPalabraTransferencia()
    {
      this.palabra = new Palabra();
      int insertar = 0;
      int contador = 0;
      String mensaje = "";
      String cuenta = this.vista13.txtCuentaOrigen.getText();
      String pin = this.vista13.txtPin.getText();
      contador += validarIngreso(cuenta, "cuenta");
      contador += validarIngreso(pin, "pin");
      if(contador == 0)
      {
        insertar += validarCuentaPinTransferencia(cuenta, pin);
        if (insertar == 0)
        {
          this.vista13.txtPalabra.setEnabled(true);
          this.vista13.txtMonto.setEnabled(true);
          this.vista13.btnTransferir.setEnabled(true);
          mensaje = Operacion.enviarMensaje(cuenta);
          this.palabra.lbPalabra.setText(mensaje);
          JOptionPane.showMessageDialog(null, "Estimado usuario se ha enviado una palabra por mensaje de texto, por favor revise sus mensajes"
                  + " y procesa a digitar la palabra enviada");
        }
        else
        {
          JOptionPane.showMessageDialog(null, "La cuenta o el pin está incorrecto");
        }
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
            
            double montoCorrecto = CLI.montoValido(monto, cuenta, moneda);
            Operacion.realizarRetiro(monto, moneda, cuenta);
            double comision = ControladorUsuario.aplicaComisionRetiro(cuenta, montoCorrecto, 4);
            resultado = imprimirResultado(moneda, comision, montoCorrecto);
            JOptionPane.showMessageDialog(null, resultado);
            this.vista3.txtCuenta.setText("");
            this.vista3.txtPin.setText("");
            this.vista3.txtPalabra.setText("");
            this.vista3.txtMonto.setText("");
            this.vista3.txtPalabra.setEnabled(false);
            this.vista3.txtMonto.setEnabled(false);
            this.vista3.btnRetirar.setEnabled(false);
          }
        }
      }
      else
      {
        JOptionPane.showMessageDialog(null, "Su cuenta se encuentra desactivada");
      }   
    }
    
    public void transferir()   
    {
      String cuentaDestino = this.vista13.txtCuentaDestino .getText();
      Cuenta cuentaBaseDestino = CuentaDAO.obtenerCuenta(cuentaDestino);
      String cuentaOrigen = this.vista13.txtCuentaOrigen .getText();
      Cuenta cuentaBaseOrigen = CuentaDAO.obtenerCuenta(cuentaOrigen);
      int insertar = 0;
      int contador = 0;
      String mensaje;
      String resultado = "";
      if(!"inactiva".equals(cuentaBaseOrigen.getEstatus())){
        String pin = this.vista13.txtPin.getText();
        String palabra = this.vista13.txtPalabra.getText();
        String strMonto = this.vista13.txtMonto.getText();
        String palabraClave = this.palabra.lbPalabra.getText();
        contador += validarIngreso(cuentaOrigen, "cuenta");
        contador += validarIngreso(pin, "pin");
        contador += validarIngreso(palabra, "palabra clave");
        contador += validarIngreso(strMonto, "monto");
        contador += validarEntrMonto(strMonto);

        if(contador == 0)
        {
          insertar += validarEntrCuenta(cuentaDestino);
          insertar += validarCuentaPinTransferencia(cuentaOrigen, pin);
          double monto = Double.parseDouble(strMonto);
          insertar += validarMonto(monto, cuentaOrigen, "colones");
          insertar += validarPalabraTransferencia(palabraClave, cuentaOrigen);
          if (insertar == 0)
          {                              
            Operacion.realizarTransferencia(cuentaOrigen,cuentaDestino, monto);
            resultado = imprimirResultadoTransf(monto);
            JOptionPane.showMessageDialog(null, resultado);
            this.vista13.txtCuentaDestino.setText("");
            this.vista13.txtCuentaOrigen.setText("");
            this.vista13.txtPin.setText("");
            this.vista13.txtPalabra.setText("");
            this.vista13.txtMonto.setText("");
            this.vista13.txtPalabra.setEnabled(false);
            this.vista13.txtMonto.setEnabled(false);
            this.vista13.btnTransferir.setEnabled(false);
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
    //-------------
    
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
          String numero = Cuenta.insertarCuenta(pin, strMonto, id);

          String mensaje = "Se ha creado una nueva cuenta en el sistema, los datos de la cuenta son: \n";
          mensaje += ControladorUsuario.imprimirCuenta(numero);
          mensaje += "\n---\n";
          mensaje += ControladorUsuario.imprimirPersona(id);
          JOptionPane.showMessageDialog(null, mensaje, "Consulta de usuario", JOptionPane.INFORMATION_MESSAGE);
        }
      }
      else
      {
        JOptionPane.showMessageDialog(null, "Complete todos sus datos");
      }
    }
    
    
    public void crearCliente()
    {
      int insertar = 0;
      int contador = 0;
      String apellido1 = this.vista11.tfApellido1.getText();
      String apellido2 = this.vista11.tfApellido2.getText();
      String nombre = this.vista11.tfNombre.getText();
      String identificacion = this.vista11.tfIdCliente.getText();
      LocalDate fechaNacimiento = obtenerFecha();
      String fechaParaValidar = fechaNacimiento.format(DateTimeFormatter.ofPattern("dd-MMM-yy"));

      String telefono = this.vista11.tfTelefono.getText();
      String correo = this.vista11.tfCorreo.getText();
      
      contador += validarIngreso(apellido1, "primer apellido");
      contador += validarIngreso(apellido2, "segundo apellido");
      contador += validarIngreso(nombre, "nombre");
      contador += validarIngreso(identificacion, "identificacion");
      contador += validarIngreso(fechaParaValidar, "fecha nacimiento");
      contador += validarIngreso(correo, "correo");
      contador += validarIngreso(telefono, "telefono");
      
      if(contador == 0)
      {
        insertar += validarId(identificacion);
        insertar += validarEntrCorreo(correo);
        insertar += validarEntrTelefono(telefono);

        if (insertar == 0)
        {
          int idCliente = Integer.parseInt(identificacion);
          int telefonoCliente = Integer.parseInt(telefono);
          Persona NuevoCliente = Persona.insertarCliente(apellido1,apellido2,nombre,idCliente,fechaNacimiento,telefonoCliente,correo);

          String mensaje = "Se ha creado un nuevo cliente en el sistema, los datos del cliente son: \n";
          mensaje += NuevoCliente.toStringCompleto();
          JOptionPane.showMessageDialog(null, mensaje, "Consulta de usuario", JOptionPane.INFORMATION_MESSAGE);
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
    
    public void listarCuentas(){
      Persona consulta = new Persona();
      this.latabla2 = new ListarCuentas();
      this.latabla2.btnConsultar.addActionListener(this);
      String[] titulos = {
        "Número",
        "Estatus",
        "Saldo",
        "Identificación de dueño",
        "Nombre de dueño"};
      this.latabla2.modelo = new DefaultTableModel(null, titulos);
      this.latabla2.tablaCuentas.setModel(this.latabla2.modelo);
      for(Cuenta cuenta: cuentasSistema)
      {
        String numero = Encriptacion.desencriptar(cuenta.getNumero());
        String saldo = Encriptacion.desencriptar(cuenta.getSaldo());
        int infoId = CuentaDAO.obtenerPersonaCuenta(numero);
        consulta = PersonaDAO.obtenerPersona(infoId);
        String mensaje = consulta.getNombre() + " " + consulta.getPrimerApellido() + " " + consulta.getSegundoApellido();
        Object[] info = {numero, cuenta.getEstatus(), saldo, consulta.getId(), mensaje};
        this.latabla2.modelo.addRow(info);
      }
      this.latabla2.setVisible(true);
      this.menu.setVisible(false);
    }
    
    public void consultarEstadoCuentaP1(){
      int insertar = 0;
      int contador = 0;
      String mensaje;
      String cuenta = this.vista4.txtCuenta.getText();
      String pin = this.vista4.txtPin.getText();
      String moneda = this.vista4.cbMoneda.getSelectedItem().toString();
      contador += validarIngreso(cuenta, "cuenta");
      contador += validarIngreso(pin, "pin");
      if(contador == 0)
      {
        insertar += validarCuentaPin2(cuenta, pin);
        if (insertar==0)
        {

          Cuenta cuentaBase = CuentaDAO.obtenerCuenta(cuenta);
          if("activo".equals(cuentaBase.getEstatus())){
            consultarEstadoCuentaP2(cuenta, moneda);
            this.vista4.setVisible(false);
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
    
    public void consultarEstadoCuentaP2(String cuenta, String moneda){
      ConsultaMoneda consulta = new ConsultaMoneda();
      this.vista5 = new ConsultarEstadoCuentaP2();
      //String cuenta = this.vista4.txtCuenta.getText();
      Cuenta cuentaBase = CuentaDAO.obtenerCuenta(cuenta);
      this.vista5.lbCuenta.setText(cuenta);
      String pin = cuentaBase.getPin();
      this.vista5.lbPin.setText(pin);
      int idDueno = CuentaDAO.obtenerPersonaCuenta(cuenta);
      String strIdDueno = Integer.toString(idDueno);
      this.vista5.lbIdDueno.setText(strIdDueno);
      String strSaldoColones = cuentaBase.getSaldo();
      double saldoColones = Double.parseDouble(strSaldoColones);
      double montoCorrec = montoMoneda(saldoColones, moneda);
      String strMontoCorrec = Double.toString(montoCorrec);
      this.vista5.lbSaldo.setText(strMontoCorrec);
      Persona persona = PersonaDAO.obtenerPersona(idDueno);
      String nombreDueno = persona.getNombre() + " " + persona.getPrimerApellido() + " " + persona.getSegundoApellido();
      this.vista5.lbNombreDueno.setText(nombreDueno);
      ArrayList<Operacion> operaciones = OperacionDAO.getOperacionesCuenta(cuenta);
      String[] titulos = {
        "Fecha",
        "Tipo",
        "Comisión"};
      this.vista5.modelo = new DefaultTableModel(null, titulos);
      this.vista5.tablaEstado.setModel(this.vista5.modelo);
      for(Operacion operacion: operaciones)
      {
        if("colones".equals(moneda))
        {
          
          Object[] info = {operacion.getFechaOperacion(), operacion.getTipo(), operacion.getMontoComision()};
          this.vista5.modelo.addRow(info);
        }
        else
        {
          double venta = consulta.consultaCambioVenta();
          double comisionDolares = (operacion.getMontoComision()/venta);
          Object[] info = {operacion.getFechaOperacion(), operacion.getTipo(), comisionDolares};
          this.vista5.modelo.addRow(info);
        }
      }
      this.vista5.setVisible(true);
      
    }
    
    public void consultarGananciaBancoCuenta()
    {
      double sumaRetiros =0;
      double sumaDepositos =0;
      String cuentaText = this.vista9.txtNumCuenta.getText();
      Cuenta cuentaBase = CuentaDAO.obtenerCuenta(cuentaText);
      int contador = 0;
      if(!"inactiva".equals(cuentaBase.getEstatus())){
        contador += validarIngreso(cuentaText, "cuenta");
        if (contador == 0)
        {
          LlenarTablaConsultaBancoPorCuenta(cuentaText);
          sumaRetiros = Operacion.sumarComisionesRetiros(cuentaText);
          sumaDepositos = Operacion.sumarComisionesdepositos(cuentaText);
          this.vista9.txtGananciaBancoRetiros.setText(String.valueOf(sumaRetiros));
          this.vista9.txtGananciaBancoDeposito.setText(String.valueOf(sumaDepositos));
        }
        else
        {
          JOptionPane.showMessageDialog(null, "Complete los datos requeridos");
        }
      }
      else
      {
        JOptionPane.showMessageDialog(null, "Su cuenta se encuentra desactivada");
      }
    }
    
    public void LlenarTablaConsultaBancoPorCuenta(String cuenta)
    {
      double sumaComisiones = 0;

      String[] titulos = {
      "Detalle de la operación",
      "Fecha de la operación",
      "Comisión del Banco"};
      ArrayList<Operacion> operaciones = OperacionDAO.getOperacionesCuenta(cuenta);
      this.vista9.modelo = new DefaultTableModel(null, titulos);
      this.vista9.tblGananciaBancoComicionesCuenta.setModel(this.vista9.modelo);
      for(Operacion operacion: operaciones)
      {
        if (operacion.getTipo().equals("retiro") || operacion.getTipo().equals("deposito"))
        {
        Object[] info = {operacion.getTipo(),operacion.getFechaOperacion(), operacion.getMontoComision()};
        sumaComisiones = sumaComisiones + operacion.getMontoComision();
        this.vista9.modelo.addRow(info);
        this.vista9.txtGananciasBanco.setText(String.valueOf(sumaComisiones));
        }
      }
    }
    
    public void consultarGananciaBancoTotalizado()
    {
      double sumaComisiones = 0;
      double sumaRetiros =0;
      double sumaDepositos =0;
      ArrayList<Cuenta> listaCuentas = CuentaDAO.getCuentasBD();
      Cuenta cuenta=new Cuenta();
      for(int i=0;i<listaCuentas.size();i++){
        cuenta = listaCuentas.get(i);
        String numCuenta = Encriptacion.desencriptar(cuenta.getNumero());
        sumaRetiros += Operacion.sumarComisionesRetiros(numCuenta);
        sumaDepositos += Operacion.sumarComisionesdepositos(numCuenta);
        sumaComisiones = sumaComisiones + LlenarTablaConsultaBancoTotalizado();
      }
      
      this.vista10.txtGananciaTotal.setText(String.valueOf(sumaComisiones));
    }
    
    
    
    public double LlenarTablaConsultaBancoTotalizado()
    {
      double sumaComi=0;
      String[] titulos = {
      "Detalle de la operación",
      "Fecha de la operación",
      "Comisión del Banco"};
      ArrayList<Operacion> operaciones = OperacionDAO.getOperacionesBD();
      this.vista10.modelo = new DefaultTableModel(null, titulos);
      this.vista10.tblGananciasBancoTotalizado.setModel(this.vista10.modelo);
      for(Operacion operacion: operaciones)
      {
        if (operacion.getTipo().equals("retiro") || operacion.getTipo().equals("deposito"))
        {
          Object[] info = {operacion.getTipo(),operacion.getFechaOperacion(), operacion.getMontoComision()};
          this.vista10.modelo.addRow(info);
          sumaComi=sumaComi+operacion.getMontoComision();
        }
      }
      double sumaR = Operacion.sumarComisionesTotalesRetiros();
      double sumaD = Operacion.sumarComisionesTotalesdepositos();
      this.vista10.txtComisionPorRetiros.setText(String.valueOf(sumaR));
      this.vista10.txtGananciaPorDepositos.setText(String.valueOf(sumaD));
      this.vista10.txtGananciaTotal.setText(String.valueOf(sumaComi));
      return sumaComi;
    }
    
    public void ConsultarCambioVentaCompra()
    {
      Double cambioCompra = 0.0;
      Double cambioVenta = 0.0;
      cambioCompra = Operacion.consultarCambioDolar("compra");
      cambioVenta = Operacion.consultarCambioDolar("venta");
  
      
      this.vista12.txtCompra.setText(String.valueOf(cambioCompra));
      this.vista12.txtVenta.setText(String.valueOf(cambioVenta));
      
    }
    
    public void consultarInfoCuenta(){
        String numeroCuenta = this.latabla2.txtCuenta.getText();
        consultarEstadoCuentaP2(numeroCuenta,"colones");
        this.latabla2.setVisible(false);
    }
    
    
    
    //VALIDACIONES ENTRADAS DE TEXTO-------------------------------------------------------------------------------------------------------------------------------------
   
    public int validarIngreso(String pEntrada, String opcion)
    {
      if(pEntrada.length() == 0)
      {
        JOptionPane.showMessageDialog(null, "Complete el dato: " + opcion);
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
      else{
          JOptionPane.showMessageDialog(null, "Verifique la palabra clave digitada");
      }
      return 1;
    }
    
    public int validarPalabraTransferencia(String pPalabra, String pNumCuenta)
    {
      if(pedirPalabraTransferencia(pPalabra, pNumCuenta))
      {
        return 0;
      }else{
          JOptionPane.showMessageDialog(null, "Verifique la palabra clave digitada");
      }
      return 1;
    }
    
    public int validarMonto(double pMonto, String pNumCuenta, String moneda)
    {
      if(montoValido(pMonto, pNumCuenta, moneda))
      {
        return 0;
      }
      else{
          JOptionPane.showMessageDialog(null, "Verifique el monto");
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
          Cuenta.inactivarCuenta(pNumCuenta);
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
    
    public boolean esPinCuentaTransferencia(String pNumCuenta, String pin)
    {
      Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
      String cont;
      int contador;
      String pinDesencriptado = Encriptacion.desencriptar(cuenta.getPin());
      if (!pin.equals(pinDesencriptado))
      {
        cont = this.vista13.txtIntPin.getText();
        contador = Integer.parseInt(cont);
        contador++;
        cont = Integer.toString(contador);

        if(contador >= 2)
        {
          this.vista13.txtIntPin.setText("2");
          Cuenta.inactivarCuenta(pNumCuenta);
          JOptionPane.showMessageDialog(null, "Se ha desactivado la cuenta por el ingreso del pin incorrecto");
        }
        else
        {
          this.vista13.txtIntPin.setText(cont);
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
          Cuenta.inactivarCuenta(pNumCuenta);
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
    
    public boolean esPinCuentaConsultaSaldo(String pNumCuenta, String pin)
    {
      Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
      String cont;
      int contador;
      String pinDesencriptado = Encriptacion.desencriptar(cuenta.getPin());
      if (!pin.equals(pinDesencriptado))
      {
        cont = this.vista8.lblFallosPin.getText();
        contador = Integer.parseInt(cont);
        contador++;
        cont = Integer.toString(contador);

        if(contador >= 2)
        {
          this.vista8.lblFallosPin.setText("2");
          Cuenta.inactivarCuenta(pNumCuenta);
          JOptionPane.showMessageDialog(null, "Se ha desactivado la cuenta por el ingreso del pin incorrecto");
        }
        else
        {
          this.vista8.lblFallosPin.setText(cont);
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
          Cuenta.inactivarCuenta(pNumCuenta);
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
    
    public static double montoMoneda(double pMonto, String moneda)
    {
      ConsultaMoneda consulta = new ConsultaMoneda();
      double venta = consulta.consultaCambioVenta();
      double monto = pMonto;
      if("dolares".equals(moneda))
      {
        monto = pMonto / venta;
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
    
    public int validarPinTransferencia(String pNumCuenta, String pPin)
    {
      if(esPinCuentaTransferencia(pNumCuenta, pPin))
      {
        return 0;
      }
      return 1;
    }
    
    public int validarPinCambio(String pNumCuenta, String pPin, String pinNuevo)
    {
      if(esPinCuentaCambioPin(pNumCuenta, pPin) & ExpresionesRegulares.validarPin(pinNuevo))
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
    
    public int validarPinSaldo(String pNumCuenta, String pPin)
    {
      if(esPinCuentaConsultaSaldo(pNumCuenta, pPin))
      {
        return 0;
      }
      else{
          JOptionPane.showMessageDialog(null, "Verifique el pin");
      }
      return 1;
    }
    
    public int validarEntrCuenta(String numCuenta)
    {
        if(auxNumCuentaP1(numCuenta))
        {
            return 0;
        }
        else{
            JOptionPane.showMessageDialog(null, "Verifique la cuenta");
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
    
    public int validarEntrCorreo(String strCorreo)
    {
      boolean esCorreo = ExpresionesRegulares.validarEmail(strCorreo);
      if (esCorreo==false)
      {
        JOptionPane.showMessageDialog(null, "Correcto electrónico no válido, verifique el formato");
        return 1;
      }
      return 0;
    }
    
        public int validarEntrTelefono(String strTelefono)
    {
      boolean esTelefono = ExpresionesRegulares.validarTelefono(strTelefono);
      if (esTelefono==false)
      {
        JOptionPane.showMessageDialog(null, "Número de teléfono inválido, revise nuevamente");
        return 1;
      }
      return 0;
    }
        
    public int validarId(String id){
        boolean num = ExpresionesRegulares.esNumero(id);
        if(num==false){
            JOptionPane.showMessageDialog(null, "Número de id inválido, revise nuevamente");
            return 1;
        }
        else{
            int ident = Integer.parseInt(id);
            if(ExpresionesRegulares.validarIdExiste(ident)){
                JOptionPane.showMessageDialog(null, "Número de id inválido, ya está registrado");
                return 1;
            }
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
      JOptionPane.showMessageDialog(null, "Verifique la cuenta o el pin digitado");
      return 1;
    }
    
    public int validarCuentaPinTransferencia(String numCuenta, String pin)
    {
      int insertar = 0;
      insertar += validarEntrCuenta(numCuenta);
      if(insertar==0)
      {
        insertar += validarPinTransferencia(numCuenta, pin);
        if(insertar==0)
        {
           return 0;
        }
      }
      else{
          JOptionPane.showMessageDialog(null, "Verifique la cuenta o el pin digitado");
      }
      return 1;
    }
    
    public int validarCuentaPinSaldo(String numCuenta, String pin)
    {
      int insertar = 0;
      insertar += validarEntrCuenta(numCuenta);
      if(insertar==0)
      {
        insertar += validarPinSaldo(numCuenta, pin);
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
    
    public int validarCuentaPinCambio(String numCuenta, String pin, String pinNuevo)
    {
      int insertar = 0;
      insertar += validarEntrCuenta(numCuenta);
      if(insertar==0)
      {
        insertar += validarPinCambio(numCuenta, pin, pinNuevo);
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
          Cuenta.inactivarCuenta(pNumCuenta);
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
    
    public boolean pedirPalabraTransferencia(String pPalabra, String pNumCuenta)
    {
      Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
      String cont;
      int contador;
      String palabra = this.vista13.txtPalabra.getText();
      if (!pPalabra.equals(palabra))
      {
        cont = this.vista13.txtIntPalabra.getText();
        contador = Integer.parseInt(cont);
        contador++;
        cont = Integer.toString(contador);
        this.vista13.txtIntPalabra.setText(cont);
        if(contador >= 2)
        {
          Cuenta.inactivarCuenta(pNumCuenta);
          JOptionPane.showMessageDialog(null, "Se ha desactivado la cuenta por el ingreso incorrecto de la palabra clave");
        }
        else
        {
          //JOptionPane.showMessageDialog(null, "Para otro intento, presione en el botón 'Enviar palabra'");
          //this.palabra.lbPalabra.setText("");

          enviarPalabraTransferencia();
        }
        return false;
      }
      return true;
    }
    
    
    //OTROS--------------------------------------------------------------------------------------------------------------------------------------------
    public static String imprimirResultado(String moneda, double comision, double monto)
    {
      String resultado = "";
      DecimalFormat df = new DecimalFormat("#.00");
      if("colones".equals(moneda))
      {
        resultado += "Estimado usuario, el monto de este retiro es: " + (df.format(monto));
        resultado += "\n[El monto cobrado por concepto de comisión fue de " + (df.format(comision)) + " colones, que fueron rebajados "
            + "automáticamente de su saldo actual]";
      }
      else
      {
        ConsultaMoneda consulta = new ConsultaMoneda();
        double ventaDolar = consulta.consultaCambioVenta();
        double montoDolares = monto/ventaDolar;
        resultado += "Estimado usuario, el monto de este retiro es: " + (df.format(montoDolares)) + " dólares";
        resultado += "\n\n[Según el BCCR, el tipo de cambio de venta del dólar hoy es: " + (df.format(ventaDolar)) +"]";
        resultado += "\n[El monto equivalente de su retiro es: " + (df.format(monto)) + "colones]";
        resultado += "\n[El monto cobrado por concepto de comisión fue de " + (df.format(comision)) + " colones, que fueron rebajados "
            + "automáticamente de su saldo actual]";
      }
      return resultado;
    }  
    
    public static String imprimirResultadoTransf(double monto)
    {
      DecimalFormat df = new DecimalFormat("#.00");
      String resultado = "";
      resultado += "Estimado usuario, la transferencia de fondos se ejecutó satisfactoriamente.\nEl monto retirado de la cuenta origen y depositado en la cuenta destino es " + (df.format(monto)) + "0";
      resultado += "\n[El monto cobrado por concepto de comisión a la cuenta origen fue de 0.00 colones, que fueron rebajados automáticamente de su saldo actual]";
     
      return resultado;
    }  
    
    public static String imprimirResultadoDeposito(String moneda, double comision, double monto, String cuenta)
    {
      DecimalFormat df = new DecimalFormat("#.00");
      String resultado = "";
      double depositoReal;
      if("colones".equals(moneda))
      {
        depositoReal=monto-comision;
        //operacion.depositar(cuenta,String.valueOf(depositoReal));
        resultado += "Estimado usuario, se han depositado correctamente: " + (df.format(monto))+" colones";
        resultado += "\n[El monto real depositado a su cuenta "+cuenta+" es de "+(df.format(depositoReal))+" colones]";
        resultado += "\n[El monto cobrado por concepto de comisión fue de "+(df.format(comision))+" colones, que\n" +
                        "fueron rebajados automáticamente de su saldo actual]";
      }
      else
      {
        ConsultaMoneda consulta = new ConsultaMoneda();
        double ventaDolar = consulta.consultaCambioVenta();
        double montoDolares = monto/ventaDolar;
        depositoReal=monto-comision;
        //operacion.depositar(cuenta,String.valueOf(depositoReal));
        resultado += "Estimado usuario, se han recibido correctamente: " + (df.format(montoDolares)) + " dólares";
        resultado += "\n\n[Según el BCCR, el tipo de cambio de venta del dólar hoy es: " + (df.format(ventaDolar)) +"]";
        resultado += "\n[El monto equivalente de su deposito es: " + (df.format(monto)) + " colones]";
        resultado += "\n[El monto real depositado a su cuenta " + cuenta + " es de "+(df.format(depositoReal))+" colones]";
        resultado += "\n[El monto cobrado por concepto de comisión fue de " + (df.format(comision)) + " colones, que fueron rebajados "
            + "automáticamente de su saldo actual]";
      }
      return resultado;
    }  
    
    public static String imprimirResultadoConsultaSaldo(String moneda, String saldo)
    {
      double saldoFinal = Double.parseDouble(saldo);
      DecimalFormat df = new DecimalFormat("#.00");
      String resultado = "";
      if("colones".equals(moneda))
      {

        resultado += "Estimado usuario el saldo actual de su cuenta es: " + (df.format(saldoFinal))+" colones";

      }
      else
      {
        ConsultaMoneda consulta = new ConsultaMoneda();
        double ventaDolar = consulta.consultaCambioVenta();
        double montoDolares = saldoFinal/ventaDolar;
        resultado += "Estimado usuario el saldo actual de su cuenta es: " + (df.format(montoDolares))+" dólares";
        resultado += "\n\nPara esta conversión se utilizó el tipo de cambio del dólar, precio de compra.";
        resultado += "\n[Según el BCCR, el tipo de cambio de compra del dólar hoy es: " + (df.format(ventaDolar)) +"]";

      }
      return resultado;
    }
    
    public static String imprimirResultadoConsultaStatus(String status)
    {

      String resultado = "";
      resultado += "Estimado usuario el status actual de su cuenta es: " + status +"";
      return resultado;
    }
    
    public void cargarDatosPersonas(){
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
    
    public void cargarDatosCuentas(){
      cuentasSistema = CuentaDAO.getCuentasBD();
    }
    
    private void ordenarClientes(){
      personasSistema.sort(Comparator.comparing(Persona::getPrimerApellido));
    }
    
    public void ordenarCuentas(){
        Collections.sort(cuentasSistema);
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
      String mensaje = cuenta.toString1();
      String total = "\nInformación de la cuenta solicitada:\n" + mensaje;
      return total;
    }
    
    public LocalDate obtenerFecha(){
        Date date = this.vista11.tfFecha.getDate();
        long dateTime = date.getTime();
        java.sql.Date fecha = new java.sql.Date(dateTime);
        LocalDate fechaLocal = fecha.toLocalDate();
        
        return fechaLocal; 
    }
    
    public static double aplicaComision(String numCuenta, double monto)
   {
       int contador = CuentaDAO.contadorOperacionesCuenta(numCuenta);
       if (contador > 4)
       {
           return (monto*0.02);
       }
       else
       {
           return 0.00;
       }
   }   
   
   public static double aplicaComisionRetiro(String numCuenta, double monto, int numero)
   {
       int contador = CuentaDAO.contadorOperacionesCuenta(numCuenta);
       if (contador > numero)
       {
           return (monto*0.02);
       }
       else
       {
           return 0.00;
       }
   } 
    
    public static String imprimirCuenta(String pNum){
      Cuenta cuenta = CuentaDAO.obtenerCuenta(pNum);
      DecimalFormat df = new DecimalFormat("#.00");
      String strSaldo = cuenta.getSaldo();
      double saldo = Double.parseDouble(strSaldo);
      String mensaje = "Número de cuenta: " + pNum + "\nEstatus de la cuenta: " + cuenta.getEstatus() + "\nSaldo actual: " + (df.format(saldo));
      return mensaje;
    }
    
    public static String imprimirPersona(int id){
      Persona persona = PersonaDAO.obtenerPersona(id);
      String mensaje = "Nombre del dueño de la cuenta: " + persona.getNombre() + " " + persona.getPrimerApellido() + " " + 
          persona.getSegundoApellido() + "\nNúmero de teléfono 'asociado' a la cuenta: " + 
          persona.getNumero() + "\nDirección de correo electrónico 'asociada' a la cuenta: " + persona.getCorreo();
      return mensaje;
    } 
}
