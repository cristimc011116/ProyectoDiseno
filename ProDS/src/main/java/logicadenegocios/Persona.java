/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logicadenegocios;
import dao.PersonaDAO;
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
    private int id;
    private LocalDate fechaNacimiento;
    private int numero;
    private String correo;
    private String rol;
    private ArrayList<Cuenta> misCuentas;

//--------------------------------------------CONSTRUCTORES-------------------------------------------    
    public Persona(String pPrimerApellido, String pSegundoApellido, String pNombre, int pId, LocalDate pFechaNacimiento,
                   int pNumero, String pCorreo, String pRol)
    {
        /*setCodigo();
        this.primerApellido = pPrimerApellido;
        this.segundoApellido = pSegundoApellido;
        this.nombre = pNombre;
        this.id = pId;
        this.fechaNacimiento = pFechaNacimiento;
        this.numero = pNumero;
        this.correo = pCorreo;
        this.rol = pRol;
        this.misCuentas = new ArrayList<>();*/
        setPrimerApellido(pPrimerApellido);
        setSegundoApellido(pSegundoApellido);
        setNombre(pNombre);
        setId(pId);
        setFechaNacimiento(pFechaNacimiento);
        setNumero(pNumero);
        setCorreo(pCorreo);
        setRol(pRol);
        setCodigo();
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
    
    public Persona(String pPrimerApellido, String pSegundoApellido, String pNombre, int pId)
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

//---------------------------------METODOS DE CLASE-------------------------------------------------  
    
    public void asignarCuenta(Cuenta pCuenta){
        this.misCuentas.add(pCuenta);
    }
    
    public static int contarClientes(){
      ArrayList<Persona> listaClientes = PersonaDAO.getPersonasBD();
       int contadorClientes = listaClientes.size();
       return contadorClientes;
    }
    
    public boolean menorQue(Comparable objeto){
      System.out.println(objeto);
      return (getPrimerApellido().compareTo(((Persona)objeto).getPrimerApellido())<0);
    }
    
//-----------------------------------------METODOS ACCESORES--------------------------------------------    
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo() {
        int contadorClientes = contarClientes();
        String cod ="CIF_";
        String pCodigo = cod + String.valueOf(contadorClientes);
        this.codigo = pCodigo;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String pPrimerApellido) {
        this.primerApellido = pPrimerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String pSegundoApellido) {
        this.segundoApellido = pSegundoApellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String pNombre) {
        this.nombre = pNombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int pId) {
        this.id = pId;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate pFechaNacimiento) {
        this.fechaNacimiento = pFechaNacimiento;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int pNumero) {
        this.numero = pNumero;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String pCorreo) {
        this.correo = pCorreo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String pRol) {
        this.rol = pRol;
    }  
    
    public String toString(){
        String mensaje = "";
        mensaje = "Nombre del dueño de la cuenta= " + this.nombre + " " + this.primerApellido + " " + this.segundoApellido + "\n" 
                + "Identificación= " + this.id + "\n" ;
        return mensaje;
    }  
}

