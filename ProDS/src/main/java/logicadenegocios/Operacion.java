/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logicadenegocios;
import controlador.ControladorUsuario;
import static controlador.ControladorUsuario.montoCorrecto;
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
        comision = ControladorUsuario.aplicaComision(cuenta, monto);
        nuevoMonto = monto + comision;
        String strSaldoViejo = cuentaBase.getSaldo();
        double saldoViejo = Double.parseDouble(strSaldoViejo);
        double nuevoSaldo = saldoViejo + monto;
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
        comision = ControladorUsuario.aplicaComisionRetiro(cuenta, monto);
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

