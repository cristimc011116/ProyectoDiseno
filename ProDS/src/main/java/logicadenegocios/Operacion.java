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

  public Operacion() {
  
  }
    

//------------------------------------------METODOS DE CLASE----------------------------------------    
    public boolean cambiarPIN(String pCuenta, String pPinNuevo)
    {
      ArrayList<Cuenta> listaCuentas = CuentaDAO.getCuentasBD();
      for(int i=0;i<listaCuentas.size();i++){
        cuenta=listaCuentas.get(i);
        String pCuentaDesencriptada = Encriptacion.desencriptar(cuenta.getNumero());
        if(pCuentaDesencriptada.equals(pCuenta)){
          pPinNuevo = Encriptacion.encriptar(pPinNuevo);
          cuenta.setPin(pPinNuevo);
          CuentaDAO.cambiarPinCuenta( pCuenta,  pPinNuevo);
          return true;
        }
      }
      return false;
    }
    
    public void depositar(String pCuenta, String pCantColones){
      ArrayList<Cuenta> listaCuentas = CuentaDAO.getCuentasBD();
      for(int i=0;i<listaCuentas.size();i++){
        cuenta=listaCuentas.get(i);
        String dinero = cuenta.getSaldo();
        if(cuenta.getNumero().equals(pCuenta)){
          cuenta.setSaldo(dinero+pCantColones);
        }
      }
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

