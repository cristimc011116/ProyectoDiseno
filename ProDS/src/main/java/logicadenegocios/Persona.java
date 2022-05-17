/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logicadenegocios;
import dao.CuentaDAO;
import dao.OperacionDAO;
import dao.PersonaDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import util.Encriptacion;
import webService.ConsultaMoneda;

/**
 *
 * @author Cristi Martínez
 */
public class Persona{
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
    public Persona(String pCodigo, String pPrimerApellido, String pSegundoApellido, String pNombre, int pId, LocalDate pFechaNacimiento,
                   int pNumero, String pCorreo, String pRol)
    {
        setPrimerApellido(pPrimerApellido);
        setSegundoApellido(pSegundoApellido);
        setNombre(pNombre);
        setId(pId);
        setFechaNacimiento(pFechaNacimiento);
        setNumero(pNumero);
        setCorreo(pCorreo);
        setRol(pRol);
        setCodigo(pCodigo);
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
   
    
    public static Persona insertarCliente(String apellido1,String apellido2, String nombre,
            int idCliente, LocalDate fechaNacimiento,int telefonoCliente, String correo)
    {
      int codigo = PersonaDAO.contadorPersonasBD();
      String strCodigo = Integer.toString(codigo);
      Persona cliente = new Persona("CIF_" + strCodigo, apellido1,apellido2,nombre,idCliente,fechaNacimiento,telefonoCliente,correo, "usuario");
      PersonaDAO.insertarCliente(cliente);
      return cliente;
    }
    
    public static String consultarSaldo(String pNumCenta)
    {
      Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCenta);
      String saldo = cuenta.getSaldo();
      return saldo;
    }
    
    public static String consultarStatus(String pNumCenta)
    {
      Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCenta);
      String status = cuenta.getEstatus();
      return status;
    }
    
    public static String consultarEstadoCuenta(String pNumCuenta, String moneda){
        ConsultaMoneda consulta = new ConsultaMoneda();
        String strSaldo = "";
        String strPin = "";
        String resultado = "";
        int contador = 0;
        String oper = "";
        Cuenta cuentaBase = CuentaDAO.obtenerCuenta(pNumCuenta);
        int idDueno = CuentaDAO.obtenerPersonaCuenta(pNumCuenta);
        Persona persona = PersonaDAO.obtenerPersona(idDueno);
        String nombreDueno = persona.getNombre() + " " + persona.getPrimerApellido() + " " + persona.getSegundoApellido();
        ArrayList<Operacion> operaciones = OperacionDAO.getOperacionesCuenta(pNumCuenta);
        for(Operacion operacion: operaciones)
        {
            if("colones".equals(moneda))
            {
                strSaldo = cuentaBase.getSaldo();
                strPin = cuentaBase.getPin();
                double saldo = Double.parseDouble(strSaldo);
                double monto = (operacion.getMontoComision()/0.02);
                LocalDate fecha = operacion.getFechaOperacion();
                String tipo = operacion.getTipo();
                double montoComision = operacion.getMontoComision();
                contador++;
                oper += "Operacion #" + contador + "\nFecha: " + fecha + "\nTipo: " + tipo + "\nComisión: " + montoComision + "\n\n";
            }
            else
            {
                double venta = consulta.consultaCambioVenta();
                String strSaldoColones = cuentaBase.getSaldo();
                double saldoColones = Double.parseDouble(strSaldoColones);
                double saldoDolares = saldoColones/venta;
                strSaldo = Double.toString(saldoDolares);
                strPin = cuentaBase.getPin();
                double monto = (operacion.getMontoComision()/0.02);
                double comisionDolares = (operacion.getMontoComision()/venta);
                double montoDolares = monto/venta;
                LocalDate fecha = operacion.getFechaOperacion();
                String tipo = operacion.getTipo();
                contador++;
                oper += "Operacion #" + contador + "\nFecha: " + fecha + "\nTipo: " + tipo + "\nComisión: " + comisionDolares + "\n\n";
            }
        }
        resultado += "Información de la cuenta\n\n" + "Número de cuenta: " + pNumCuenta + "\nPin encriptado de la cuenta: " + strPin
                        + "\nNombre del dueño: " + nombreDueno + "\nIdentificación del dueño: " + idDueno + "\nSaldo de la cuenta: " 
                        + strSaldo + "\n\n\nOperaciones de la cuenta: " + "\n\n" + oper;
        return resultado;
    }
    
//-----------------------------------------METODOS ACCESORES--------------------------------------------    
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String pCodigo) {
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
    
    public String toStringCompleto()
    {
        String mensaje = "";
        mensaje = "Nombre= " + this.nombre + " " + this.primerApellido + " " + this.segundoApellido + "\n" 
                + "Identificación= " + this.id + "\n" + "Código= " + this.codigo + "\n" + "Fecha de nacimiento= " + this.fechaNacimiento + "\n"
                + "Número de teléfono= " + this.numero + "\n" + "Correo= " + this.correo + "\n" + "Rol= " + this.rol + "\n";
        return mensaje;
    }
    
}

