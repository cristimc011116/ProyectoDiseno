/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logicadenegocios;
import controlador.ControladorUsuario;
import static controlador.ControladorUsuario.montoCorrecto;
import static controlador.ControladorUsuario.montoMoneda;
import dao.CuentaDAO;
import dao.OperacionDAO;
import dao.PersonaDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import util.Encriptacion;
import util.Mensaje;
import webService.ConsultaMoneda;
/**
 *
 * @author Cristi Martínez
 */
public class Operacion {
    private int id;
    private LocalDate fechaOperacion;
    private String tipo;
    private boolean comision;
    private double montoComision;
    Cuenta cuenta = new Cuenta();

//-----------------------------------------------CONSTRUCTOR-----------------------------------------    
    public Operacion(int pId, LocalDate pFechaOperacion, String pTipo, boolean pComision, double pMontoComision)
    {
        setId(pId);
        setFechaOperacion(pFechaOperacion);
        setTipo(pTipo);
        setComision(pComision);
        setMontoComision(pMontoComision);
        
    }

  public Operacion() {
  
  }
    

//------------------------------------------METODOS DE CLASE----------------------------------------    
    public static void cambiarPIN(String pCuenta, String pPinNuevo)
    {
      CuentaDAO.actualizarPin(pCuenta, pPinNuevo);
    }
    
    public static void realizarDeposito(double monto, String moneda, String cuenta){
        double comision;
        double nuevoMonto;
        Cuenta cuentaBase = CuentaDAO.obtenerCuenta(cuenta);
        monto = montoCorrecto(monto, moneda);
        comision = ControladorUsuario.aplicaComisionRetiro(cuenta, monto, 3);
        nuevoMonto = monto + comision;
        String strSaldoViejo = cuentaBase.getSaldo();
        double saldoViejo = Double.parseDouble(strSaldoViejo);
        double nuevoSaldo = saldoViejo + monto - comision;
        String strNuevoSaldo = Double.toString(nuevoSaldo);
        cuentaBase.setSaldo(strNuevoSaldo);
        CuentaDAO.actualizarSaldo(cuenta, strNuevoSaldo);
        insertarOperacion("deposito", (comision>0.00) , comision, cuenta);
    }
    
    public static void realizarRetiro(double monto, String moneda, String cuenta){
        double comision;
        double nuevoMonto;
        Cuenta cuentaBase = CuentaDAO.obtenerCuenta(cuenta);
        monto = montoCorrecto(monto, moneda);
        comision = ControladorUsuario.aplicaComisionRetiro(cuenta, monto, 3);
        nuevoMonto = monto + comision;
        String strSaldoViejo = cuentaBase.getSaldo();
        double saldoViejo = Double.parseDouble(strSaldoViejo);
        double nuevoSaldo = saldoViejo - nuevoMonto;
        String strNuevoSaldo = Double.toString(nuevoSaldo);
        cuentaBase.setSaldo(strNuevoSaldo);
        CuentaDAO.actualizarSaldo(cuenta, strNuevoSaldo);
        insertarOperacion("retiro", (comision>0.00) , comision, cuenta);
    }
    
    public static void realizarTransferencia(String cuentaOrigen, String cuentaDestino, double monto){
        //rebajar saldo    
        Cuenta cuentaBaseOrigen = CuentaDAO.obtenerCuenta(cuentaOrigen);
        Cuenta cuentaBaseDestino = CuentaDAO.obtenerCuenta(cuentaDestino);
        double comision;
        double nuevoMonto;
        boolean aplicaCom = false;
        comision = 0.00;
        String strSaldoViejo = cuentaBaseOrigen.getSaldo();
        double saldoViejo = Double.parseDouble(strSaldoViejo);
        double nuevoSaldo = saldoViejo - monto;
        String strNuevoSaldo = Double.toString(nuevoSaldo);
        cuentaBaseOrigen.setSaldo(strNuevoSaldo);
        CuentaDAO.actualizarSaldo(cuentaOrigen, strNuevoSaldo);
        insertarOperacion("transferencia", false , comision, cuentaOrigen);

        //hacer transferencia

        String strSaldoViejoDestino = cuentaBaseDestino.getSaldo();
        double saldoViejoDestino = Double.parseDouble(strSaldoViejoDestino);
        double nuevoSaldoDestino = saldoViejoDestino + monto;
        String strNuevoSaldoDestino = Double.toString(nuevoSaldoDestino);
        cuentaBaseDestino.setSaldo(strNuevoSaldoDestino);
        CuentaDAO.actualizarSaldo(cuentaDestino, strNuevoSaldoDestino);
    }
    
    public static Double consultarCambioDolar(String opcion){
      Double resultado= 0.0;
      ConsultaMoneda consulta = new ConsultaMoneda();
      if (opcion == "compra"){
          resultado = consulta.consultaCambioCompra();
      }
      else if (opcion == "venta"){
          resultado = consulta.consultaCambioVenta();
      }
    return resultado;
    }
    
    public static String enviarMensaje(String numCuenta)
    {
      int id = CuentaDAO.obtenerPersonaCuenta(numCuenta);
      Persona persona = PersonaDAO.obtenerPersona(id);
      int numero = persona.getNumero();
      String mensaje = Mensaje.crearPalabra();
      Mensaje.enviarMensaje(numero, mensaje);
      return mensaje;
    }
    
    public static String consultarSaldo(String pNumCenta)
    {
      Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCenta);
      String saldo = cuenta.getSaldo();
      return saldo;
    }
    
    public static double consultarSaldoDolares(String pNumCenta)
    {
      ConsultaMoneda consulta = new ConsultaMoneda();
      double cambio = consulta.consultaCambioVenta();
      Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCenta);
      String strSaldo = cuenta.getSaldo();
      double saldo = Double.parseDouble(strSaldo);
      double dolares = saldo/cambio;
      return dolares;
    }
    
    public static String consultarStatus(String pNumCenta)
    {
      Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCenta);
      String status = cuenta.getEstatus();
      return status;
    }
    
    public static String consultarEstadoCuenta(String pNumCuenta, String moneda){
        ConsultaMoneda consulta = new ConsultaMoneda();
        String strSaldo = "";
        String strPin = "";
        String resultado = "";
        int contador = 0;
        String oper = "";
        Cuenta cuentaBase = CuentaDAO.obtenerCuenta(pNumCuenta);
        int idDueno = CuentaDAO.obtenerPersonaCuenta(pNumCuenta);
        String strSaldoColones = cuentaBase.getSaldo();
        double saldoColones = Double.parseDouble(strSaldoColones);
        double montoCorrec = montoMoneda(saldoColones, moneda);
        strSaldo = Double.toString(montoCorrec);
        Persona persona = PersonaDAO.obtenerPersona(idDueno);
        String nombreDueno = persona.getNombre() + " " + persona.getPrimerApellido() + " " + persona.getSegundoApellido();
        ArrayList<Operacion> operaciones = OperacionDAO.getOperacionesCuenta(pNumCuenta);
        for(Operacion operacion: operaciones)
        {
            if("colones".equals(moneda))
            {
                strPin = cuentaBase.getPin();
                LocalDate fecha = operacion.getFechaOperacion();
                String tipo = operacion.getTipo();
                double montoComision = operacion.getMontoComision();
                contador++;
                oper += "Operacion #" + contador + "\nFecha: " + fecha + "\nTipo: " + tipo + "\nComisión: " + montoComision + "\n\n";
            }
            else
            {
                double venta = consulta.consultaCambioVenta();
                strPin = cuentaBase.getPin();
                double comisionDolares = (operacion.getMontoComision()/venta);
                LocalDate fecha = operacion.getFechaOperacion();
                String tipo = operacion.getTipo();
                contador++;
                oper += "Operacion #" + contador + "\nFecha: " + fecha + "\nTipo: " + tipo + "\nComisión: " + comisionDolares + "\n\n";
            }
        }
        resultado += "Información de la cuenta\n\n" + "Número de cuenta: " + pNumCuenta + "\nPin encriptado de la cuenta: " + strPin
                        + "\nNombre del dueño: " + nombreDueno + "\nIdentificación del dueño: " + idDueno + "\nSaldo de la cuenta: " 
                        + strSaldo + "\n\n\nOperaciones de la cuenta: " + "\n\n" + oper;
        return resultado;
    }
    
    public static void insertarOperacion(String pTipo, boolean pEsComision, double pMontoComision, String pNumCuenta)
    {
      int id = OperacionDAO.cantOperacionesBD();
      LocalDate fecha = Cuenta.setFechaCreacion();
      Operacion operacion = new Operacion(id, fecha, pTipo, pEsComision, pMontoComision);
      OperacionDAO.insertarOperacion(operacion,fecha);
      OperacionDAO.asignarOperacionCuenta(operacion, pNumCuenta);
    }
    
    public static double consultarGananciaCuentaBanco(String numCuenta){
        ArrayList<Operacion> operaciones = OperacionDAO.getOperacionesCuenta(numCuenta);
        double comisionTotal = 0.00;
        for(Operacion operacion: operaciones){
            comisionTotal += operacion.getMontoComision();
        }
        return comisionTotal;
    }
    
    public static double consultarGananciaBancoTotal(){
        ArrayList<Operacion> operaciones = OperacionDAO.getOperacionesBD();
        double comisionTotal = 0.00;
        for(Operacion operacion: operaciones){
            comisionTotal += operacion.getMontoComision();
        }
        return comisionTotal;
    }
    
    public void consultarOperacionesRealizadas(String pNumCuenta){
        OperacionDAO.getOperacionesCuenta(pNumCuenta);
    }
    
    public void asignarOperacionACuenta(Cuenta cuenta){
        this.setCuenta(cuenta);
    }

    public static double sumarComisionesRetiros(String pnumCuenta)
    {
      double suma = CuentaDAO.comisionesPorRetiro(pnumCuenta);
      return suma;
    }
    
    public static double sumarComisionesdepositos(String pnumCuenta)
    {
      double suma = CuentaDAO.comisionesPorDeposito(pnumCuenta);
      return suma;
    }
    
    public static double sumarComisionesTotalesRetiros()
    {
      ArrayList<Cuenta> listaCuentas = CuentaDAO.getCuentasBD();
      double sumaRetiros = 0.00;
      for(Cuenta cuenta: listaCuentas){
          String numCuenta = Encriptacion.desencriptar(cuenta.getNumero());
          sumaRetiros += sumarComisionesRetiros(numCuenta);
      }
      return sumaRetiros;
    }
    
    public static double sumarComisionesTotalesdepositos()
    {
      ArrayList<Cuenta> listaCuentas = CuentaDAO.getCuentasBD();
      double sumaDepositos = 0.00;
      for(Cuenta cuenta: listaCuentas){
          String numCuenta = Encriptacion.desencriptar(cuenta.getNumero());
          sumaDepositos += sumarComisionesdepositos(numCuenta);
      }
      return sumaDepositos;
    }
    
//-------------------------------------METODOS ACCESORES--------------------------------------------------
    
    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(LocalDate fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isComision() {
        return comision;
    }

    public void setComision(boolean comision) {
        this.comision = comision;
    }

    public double getMontoComision() {
        return montoComision;
    }

    public void setMontoComision(double montoComision) {
        this.montoComision = montoComision;
    }

}

