/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logicadenegocios;
import java.time.LocalDate;
import java.util.ArrayList;
import util.Comparable;

/**
 *
 * @author Cristi Martínez
 */
public class Persona implements Comparable{
    private String codigo;
    private String primerApellido;
    private String segundoApellido;
    private String nombre;
    private double id;
    private LocalDate fechaNacimiento;
    private int numero;
    private String correo;
    private String rol;
    private ArrayList<Cuenta> misCuentas;

    public Persona(String pCodigo, String pPrimerApellido, String pSegundoApellido, String pNombre, double pId, LocalDate pFechaNacimiento,
                   int pNumero, String pCorreo, String pRol)
    {
        this.codigo = pCodigo;
        this.primerApellido = pPrimerApellido;
        this.segundoApellido = pSegundoApellido;
        this.nombre = pNombre;
        this.id = pId;
        this.fechaNacimiento = pFechaNacimiento;
        this.numero = pNumero;
        this.correo = pCorreo;
        this.rol = pRol;
        this.misCuentas = new ArrayList<>();
    }

    public Persona()
    {
        this.codigo = null;
        this.primerApellido = null;
        this.segundoApellido = null;
        this.nombre = null;
        this.id = 0;
        this.fechaNacimiento = null;
        this.numero = 0;
        this.correo = null;
        this.rol = null;
        this.misCuentas = new ArrayList<>();
    }
    
    public Persona(String pPrimerApellido, String pSegundoApellido, String pNombre, double pId)
    {
        this.codigo = null;
        this.primerApellido = pPrimerApellido;
        this.segundoApellido = pSegundoApellido;
        this.nombre = pNombre;
        this.id = pId;
        this.fechaNacimiento = null;
        this.numero = 0;
        this.correo = null;
        this.rol = null;
        this.misCuentas = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public boolean menorQue(Comparable objeto){
        return (getPrimerApellido().compareTo(((Persona)objeto).getPrimerApellido())<0);
    }
    
    public String toString(){
        String mensaje = "";
        mensaje = "Nombre del dueño de la cuenta= " + this.nombre + " " + this.primerApellido + " " + this.segundoApellido + "\n" 
                + "Número de teléfono 'asociado' a la cuenta= " + this.numero + "\n" + "Dirección de correo electrónico 'asociado' a la cuenta= " 
                + this.correo + "\n";
        return mensaje;
    }
    
    public void asignarCuenta(Cuenta pCuenta){
        this.misCuentas.add(pCuenta);
    }
    
    
}

