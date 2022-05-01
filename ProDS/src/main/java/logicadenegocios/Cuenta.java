/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logicadenegocios;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.math.RoundingMode;
/**
 *
 * @author Cristi Martínez
 */
public class Cuenta {
    private int numero;
    private String pin;
    private LocalDate fechaCreacion;
    private double saldo;
    private String estatus;
    private Persona dueno;
    private ArrayList<Operacion> operaciones;
    
    public Cuenta(int pNumero, String pPin, LocalDate pFechaCreacion, double pSaldo, String pEstatus)
    {
        this.numero = pNumero;
        this.pin = pPin;
        this.fechaCreacion = pFechaCreacion;
        this.saldo = pSaldo;
        this.estatus = pEstatus;
        this.dueno = null;
        this.operaciones = new ArrayList<>();
    }

    public Cuenta()
    {
        this.numero = 0;
        this.pin = null;
        this.fechaCreacion = null;
        this.saldo = 0;
        this.estatus = null;
        this.dueno = null;
        this.operaciones = new ArrayList<>();
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
    
    public String toString()
    {
        String mensaje = "";
        mensaje = "Número de cuenta= " + this.numero + "\n" + "Estatus de la cuenta= " 
            + this.estatus + "\n" + "Saldo actual= " + this.saldo + "\n";
        return mensaje;
    }
    
    public static int generarNumCuenta()
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
        return (Integer.parseInt(numCuenta));
    }

    public void setFechaCreacion2(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    
    
    
    
    public static LocalDate setFechaCreacion()
    //public static void main(String[] args)
    {
        LocalDate fechaActual = LocalDate.now();
        String formattedDate = fechaActual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return fechaActual;
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
    
}

