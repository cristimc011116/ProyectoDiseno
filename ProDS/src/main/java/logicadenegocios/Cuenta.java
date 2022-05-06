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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*; 
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
        /*this.numero = pNumero;
        this.pin = pPin;
        this.fechaCreacion = pFechaCreacion;
        this.saldo = pSaldo;
        this.estatus = pEstatus;
        this.dueno = null;
        this.operaciones = new ArrayList<>();
        */
        setNumero(pNumero);
        setPin(pPin);
        setFechaCreacion();
        setSaldo(pSaldo);
        setEstatus(pEstatus);
        this.operaciones = new ArrayList<>();   
    }
    
    public Cuenta(String pNumero, String pEstatus, String pSaldo)
    {
        /*this.numero = pNumero;
        this.saldo = pSaldo;
        this.estatus = pEstatus;
        this.operaciones = new ArrayList<>();*/
        setNumero(pNumero);
        setSaldo(pSaldo);
        setEstatus(pEstatus);
        this.operaciones = new ArrayList<>();
    }

    public Cuenta()
    {
        /*this.numero = 0;
        this.pin = null;
        this.fechaCreacion = null;
        this.saldo = 0;
        this.estatus = null;
        this.dueno = null;
        this.operaciones = new ArrayList<>();*/
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
        
        //Nueva clase para encriptar y desencriptar
    public static String encriptar(String mensaje)
    {
        //pasar saldo y numCuenta a string
        char array[] = mensaje.toCharArray();
        for (int i=0; i<array.length; i++)
        {
            array[i] = (char)(array[i]+(char)3);
        }
        String encriptado = String.valueOf(array);
        return encriptado;
    }
    
    public static String desencriptar(String mensaje)
    {
        //pasar saldo y numCuenta a string
        char arrayD[] = mensaje.toCharArray();
        
        for(int i=0; i<arrayD.length; i++)
        {
            arrayD[i] = (char)(arrayD[i]-(char)3);
        }
        String desencriptado = String.valueOf(arrayD);
        //Pasar saldo y numCuenta a double o int
        return desencriptado;
    }
    
    public double aDecimal(int pMonto)
    {
        //Falta un decimal 0
        String strNum = Integer.toString(pMonto);
        DecimalFormat df = new DecimalFormat("0.00");
        String resultado = df.format(Double.parseDouble(strNum));
        resultado = resultado.replace(',','.');
        double numDec = Double.parseDouble(resultado);
        return numDec;
    }

    public static void listarCuentas()
     //public static void main(String[] args)
    {
        ArrayList<Cuenta> listaCuentas = CuentaDAO.getCuentasBD();
        listaCuentas.sort((Cuenta cuenta1, Cuenta cuenta2)-> desencriptar(cuenta2.getSaldo()).compareTo(desencriptar(cuenta1.getSaldo())));
        listaCuentas.forEach((es)->System.out.println(es));
    }
    
    public static boolean consultarStatusCuenta(Cuenta cuentaConsulta){
     if(cuentaConsulta.estatus == "activa"){
       return true;
     } else
       return false;
    }
    

       
   public static boolean aplicaComision(int numCuenta)
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
        String num = desencriptar(this.numero);
        String saldo = desencriptar(this.saldo);
        mensaje = "Número de cuenta= " + num + "\n" + "Estatus de la cuenta= " 
            + this.estatus + "\n" + "Saldo actual= " + saldo + "\n";
        return mensaje;
    }
    

    public void setFechaCreacion2(LocalDate pFechaCreacion) {
        this.fechaCreacion = pFechaCreacion;
    }

        
    public static LocalDate setFechaCreacion()
    //public static void main(String[] args)
    {
        LocalDate fechaActual = LocalDate.now();
        String formattedDate = fechaActual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return fechaActual;
    }
        
}

