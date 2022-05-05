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

//-----------------------------------------------CONSTRUCTOR-----------------------------------------    
    public Operacion(int pId, LocalDate pFechaOperacion, String pTipo, boolean pComision, double pMontoComision)
    {
       /* this.id = pId;
        this.fechaOperacion = pFechaOperacion;
        this.tipo = pTipo;
        this.comision = pComision;
        this.montoComision = pMontoComision;*/
        setId(pId);
        setFechaOperacion(pFechaOperacion);
        setTipo(pTipo);
        setComision(pComision);
        setMontoComision(pMontoComision);
        
    }

    

//------------------------------------------METODOS DE CLASE----------------------------------------    
    //Falta el método valiarCuenta en el paquete de validaciones.
    public boolean cambiaPIN(String pCuenta, String pPinAccesoAnterior, 
      String pPinAccesoNuevo){
      String pCuentaDesencriptada = Cuenta.desencriptar(pCuenta); 
      String pPinAccesoDesencriptada = Cuenta.desencriptar(pPinAccesoAnterior); 
      if (validacion.ExpresionesRegulares.validarCuenta(Integer.parseInt
        (pCuentaDesencriptada)) && 
        validacion.ExpresionesRegulares.validarPin(pPinAccesoDesencriptada) &&
        validacion.ExpresionesRegulares.validarPin(pPinAccesoNuevo)){

        cuenta.setPin(pPinAccesoNuevo);

        System.out.print("Estimado usuario, se ha cambiado "
          + "satisfactoriamente el PIN de su cuenta "+pCuenta);

        return true;

      }else{
          return false;
      }
    }
    
    public boolean realizarDeposito(String pCuenta, String pCantColones){
      String pCuentaDesencriptada = Cuenta.desencriptar(pCuenta);

      String dinero = cuenta.getSaldo();
      if (validacion.ExpresionesRegulares.validarCuenta(Integer.parseInt(pCuentaDesencriptada)) && 
        validacion.ExpresionesRegulares.esNumero(String.valueOf(pCantColones))){

        cuenta.setSaldo(dinero+pCantColones);
        return true;

      }else{
        return false;
      }
    }    
    
    
    public boolean realizarDepositoDolares(String pCuenta, String pPinAcceso, 
        double pCantDolares){
        String dinero = cuenta.getSaldo();
        String pCuentaDesencriptada = Cuenta.desencriptar(pCuenta);
        String pPinAccesoDesencriptada = Cuenta.desencriptar(pPinAcceso);
        
        double tipoCambioCompra = 0;/* ConsultaCompraDolar(); */ //Se debe igualar a la función ConsultaCompraDolar().
        if (validacion.ExpresionesRegulares.validarCuenta(Integer.parseInt(pCuentaDesencriptada)) && 
            validacion.ExpresionesRegulares.esNumero(String.valueOf(pCantDolares))){
            dinero = String.valueOf(pCantDolares*tipoCambioCompra);
            cuenta.setSaldo(dinero);
            return true;
            
        }else{
            return false;
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

