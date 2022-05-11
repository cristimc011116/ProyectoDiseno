/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logicadenegocios;
import dao.CuentaDAO;
import dao.OperacionDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import util.Encriptacion;
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

    

//------------------------------------------METODOS DE CLASE----------------------------------------    
    
    public boolean realizarDeposito(String pCuenta, String pCantColones){
      String pCuentaDesencriptada = Encriptacion.desencriptar(pCuenta);
      boolean resultado = false;
      if (validacion.ExpresionesRegulares.validarCuenta(Integer.parseInt(pCuentaDesencriptada)) && 
        validacion.ExpresionesRegulares.esNumero(String.valueOf(pCantColones))){
        resultado = depositar( pCuenta, pCantColones);
      }
      return resultado;
    }
      
    public boolean realizarDepositoDolares(String pCuenta, String pPinAcceso, 
        double pCantDolares){
      String pCuentaDesencriptada = Encriptacion.desencriptar(pCuenta);
      String pPinAccesoDesencriptada = Encriptacion.desencriptar(pPinAcceso);
      String dinero;
      boolean resultado = false;
      double tipoCambioCompra = 0;/*ConsultaCompraDolar();*/ //Se debe igualar a la función ConsultaCompraDolar().
      if (validacion.ExpresionesRegulares.validarCuenta(Integer.parseInt(pCuentaDesencriptada)) && 
          validacion.ExpresionesRegulares.esNumero(String.valueOf(pCantDolares))){
          dinero = String.valueOf(pCantDolares*tipoCambioCompra);
          resultado = depositar(pCuentaDesencriptada,dinero);
      }
      return resultado;
    }
    
    public boolean depositar(String pCuenta, String pCantColones){
      ArrayList<Cuenta> listaCuentas = CuentaDAO.getCuentasBD();
      for(int i=0;i<listaCuentas.size();i++){
        cuenta=listaCuentas.get(i);
        String dinero = cuenta.getSaldo();
        if(cuenta.getNumero()== pCuenta){
          cuenta.setSaldo(dinero+pCantColones);
          return true;
        }
      }
      return false;
    }
    
    public double consultarGananciaCuentaBanco(String pCuenta){
      String pCuentaDesencriptada = Encriptacion.desencriptar(pCuenta);
      ArrayList<Operacion> listaOperaciones = OperacionDAO.getOperacionesCuenta(pCuentaDesencriptada);
      double gananciaBancoPorCuenta=0;
      for(int i=0 ; i < listaOperaciones.size();i++){
        Operacion operacion = listaOperaciones.get(i);
        gananciaBancoPorCuenta = gananciaBancoPorCuenta + operacion.getMontoComision();
      }
      return gananciaBancoPorCuenta;
    }
    
    public double consultarGananciaBancoTotal(){
      ArrayList<Cuenta> listaCuentasTotales = CuentaDAO.getCuentasBD();
      double gananciaBancoTotal=0;
      for(int i=0; i < listaCuentasTotales.size() ; i++){
        ArrayList<Operacion> listaOperaciones = OperacionDAO.getOperacionesCuenta(listaCuentasTotales.get(i).getNumero());
        for(int e=0 ; e < listaOperaciones.size();e++){
          Operacion operacion = listaOperaciones.get(e);
          gananciaBancoTotal=gananciaBancoTotal+operacion.getMontoComision();
        }
      }
      return gananciaBancoTotal;
    } 
    
//-------------------------------------METODOS ACCESORES--------------------------------------------------
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

