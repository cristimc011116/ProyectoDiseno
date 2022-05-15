/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logicadenegocios;
import dao.CuentaDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import util.Encriptacion;
/**
 *
 * @author Cristi Martínez
 */
public class Cuenta implements Comparable<Cuenta>{
    private String numero;
    private String pin;
    private LocalDate fechaCreacion;
    private String saldo;
    private String estatus;
    private Persona dueno;
    private ArrayList<Operacion> operaciones;
  
    
//---------------------------------------------CONSTRUCTORES-------------------------------------    
    public Cuenta(String pNumero, String pPin, LocalDate pFechaCreacion, String pSaldo, String pEstatus)
    {
        setNumero(pNumero);
        setPin(pPin);
        setFechaCreacion();
        setSaldo(pSaldo);
        setEstatus(pEstatus);
        this.operaciones = new ArrayList<>();   
    }
    
    public Cuenta(String pNumero, String pEstatus, String pSaldo)
    {
        setNumero(pNumero);
        setSaldo(pSaldo);
        setEstatus(pEstatus);
        this.operaciones = new ArrayList<>();
    }

    public Cuenta()
    {
        setNumero("");
        setSaldo("");
        this.operaciones = new ArrayList<>();
    }
    
    //--------------------------------------METODOS DE CLASE--------------------------------------
    
    
    public static String generarNumCuenta()
    {
        int min = 0;
        int max = 9;
        String numCuenta = "";
        Random random = new Random();
        
        for(int i=0; i<9; i++){
           int valor = random.nextInt(max+min)+min; 
           String strValor = Integer.toString(valor);
           numCuenta += valor;
        }
        return numCuenta;
    }
    
    public double aDecimal(int pMonto)
    {
        String strNum = Integer.toString(pMonto);
        DecimalFormat df = new DecimalFormat("0.00");
        String resultado = df.format(Double.parseDouble(strNum));
        resultado = resultado.replace(',','.');
        double numDec = Double.parseDouble(resultado);
        return numDec;
    }
    
    @Override
    public int compareTo(Cuenta e){
        String strSaldoEncrip1 = e.getSaldo();
        String strSaldo1 = Encriptacion.desencriptar(strSaldoEncrip1);
        double saldo1 = Double.parseDouble(strSaldo1);
        
        String strSaldoEncrip2 = saldo;
        String strSaldo2 = Encriptacion.desencriptar(strSaldoEncrip2);
        double saldo2 = Double.parseDouble(strSaldo2);
        if(saldo1>saldo2){
            return -1;
        }
        else if(saldo1==saldo2){
            return 0;
            
        }
        else{
            return 1;
        }
    }


    
    public static Double convertirSaldo(Cuenta pCuenta){
        String saldoS = Encriptacion.desencriptar(pCuenta.getSaldo());
        Double saldoDouble = Double.parseDouble(saldoS);
        return saldoDouble;   
    }
    
   public static double aplicaComision(String numCuenta, double monto)
   {
       int contador = CuentaDAO.contadorOperacionesCuenta(numCuenta);
       if (contador > 3)
       {
           return (monto*0.02);
       }
       else
       {
           return 0.00;
       }
   }   
   
   public static double aplicaComisionRetiro(String numCuenta, double monto)
   {
       int contador = CuentaDAO.contadorOperacionesCuenta(numCuenta);
       if (contador >= 10)
       {
           return (monto*0.02);
       }
       else
       {
           return 0.00;
       }
   } 
//-------------------------------------METODOS ACCESORES------------------------------------------
    public String getNumero() {
        return numero;
    }

    public void setNumero(String pNumero) {
        this.numero = pNumero;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pPin) {
        this.pin = pPin;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String pSaldo) {
        this.saldo = pSaldo;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String pEstatus) {
        this.estatus = pEstatus.toLowerCase();
    }
    
    public String toString()
    {
        String mensaje = "";
        String num = Encriptacion.desencriptar(this.numero);
        String saldo = Encriptacion.desencriptar(this.saldo);
        mensaje = "Número de cuenta= " + num + "\n" + "Estatus de la cuenta= " 
            + this.estatus + "\n" + "Saldo actual= " + saldo + "\n";
        return mensaje;
    }
    
    public String toString1()
    {
        String mensaje = "";
        mensaje = "Número de cuenta= " + this.numero + "\n" + "Estatus de la cuenta= " 
            + this.estatus + "\n" + "Saldo actual= " + this.saldo + "\n";
        return mensaje;
    }
    
    public void setFechaCreacion2(LocalDate pFechaCreacion) {
        this.fechaCreacion = pFechaCreacion;
    }

    public static LocalDate setFechaCreacion()
    {
        LocalDate fechaActual = LocalDate.now();
        String formattedDate = fechaActual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return fechaActual;
    }
        
}

