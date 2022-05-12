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
import util.Encriptacion;
/**
 *
 * @author Cristi Martínez
 */
public class Cuenta {
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

    public static ArrayList listarCuentas()
    {
        ArrayList<Cuenta> listaCuentas = CuentaDAO.getCuentasBD(); 
        listaCuentas.sort((Cuenta cuenta1, Cuenta cuenta2)-> cuenta1.convertirSaldo(cuenta2).compareTo(cuenta1.convertirSaldo(cuenta1)));
        return listaCuentas;
    }
    
    public static Double convertirSaldo(Cuenta pCuenta){
        String saldoS = Encriptacion.desencriptar(pCuenta.getSaldo());
        Double saldoDouble = Double.parseDouble(saldoS);
        return saldoDouble;   
    }
    
   public static boolean aplicaComision(String numCuenta)
   {
       int contador = CuentaDAO.contadorOperacionesCuenta(numCuenta);
       if (contador > 3)
       {
           return true;
       }
       else
       {
           return false;
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

