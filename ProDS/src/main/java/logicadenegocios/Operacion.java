/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logicadenegocios;
import java.time.LocalDate;
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
    
    public Operacion(int pId, LocalDate pFechaOperacion, String pTipo, boolean pComision, double pMontoComision)
    {
        this.id = pId;
        this.fechaOperacion = pFechaOperacion;
        this.tipo = pTipo;
        this.comision = pComision;
        this.montoComision = pMontoComision;
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
    
    //Falta el método valiarCuenta en el paquete de validaciones.
    public boolean cambiaPIN(int pNumCuenta, String pPinAccesoAnterior, 
        String pPinAccesoNuevo){
        
        if (/*validacion.ExpresionesRegulares.validarCuenta(pNumCuenta) && */
            validacion.ExpresionesRegulares.validarPin(pPinAccesoAnterior) &&
            validacion.ExpresionesRegulares.validarPin(pPinAccesoNuevo)){
            
            cuenta.setPin(pPinAccesoNuevo);
            
            System.out.print("Estimado usuario, se ha cambiado "
                + "satisfactoriamente el PIN de su cuenta "+pNumCuenta);
            
            return true;
            
        }else{
            return false;
        }
    }
    
    public boolean realizarDeposito(int pCuenta, String pPinAcceso, 
        double pCantColones){
        double dinero = cuenta.getSaldo();
        if (/*validacion.ExpresionesRegulares.validarCuenta(pNumCuenta) && */
            validacion.ExpresionesRegulares.esNumero(String.valueOf(pCantColones))){
                
            cuenta.setSaldo(dinero+pCantColones);
            return true;
            
        }else{
            return false;
        }
    }
    
    
}

