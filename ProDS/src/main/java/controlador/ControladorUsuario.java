/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import static CLI.CLI.pedirInfoUsuario;
import GUI.CrearCuenta;
import GUI.Menu;
import GUI.ListarPersonas;
import dao.CuentaDAO;
import dao.PersonaDAO;
import static dao.PersonaDAO.getPersonasBD;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import logicadenegocios.Cuenta;
import logicadenegocios.Persona;
import util.Ordenamiento;
import validacion.ExpresionesRegulares;

/**
 *
 * @author Cristi Martínez
 */
public class ControladorUsuario implements ActionListener{
    
    public Menu menu;
    public CrearCuenta vista1;
    public ListarPersonas vista2;
    private ArrayList<Persona> personasSistema;
    private ListarPersonas latabla;
    
    public ControladorUsuario(Menu pMenu)
    {
        this.menu = pMenu;
        this.latabla = null;
        this.vista1 = null;
        this.personasSistema = new ArrayList<>();
        this.menu.btnListar.addActionListener(this);
        this.menu.btnCrearCuenta.addActionListener(this);
        
        cargarDatosPersonas();
        ordenarClientes();
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        switch(e.getActionCommand()){
            case "Listar personas":
                listarPersonas();
                break;
            case "Crear cuenta":
                abrirVista1();
            case "Continuar":
                crearCuenta();
            default:
                break;
        }
    }
    
    public void abrirVista1()
    {
        this.vista1 = new CrearCuenta();
        this.vista1.btnContinuar.addActionListener(this);
        this.vista1.setVisible(true);
        this.menu.setVisible(false);
    }
    
    public void crearCuenta()
    {
        int insertar = 0;
        int contador = 0;
        String strId = this.vista1.tfId.getText();
        String pin = this.vista1.tfPin.getText();
        String strMonto = this.vista1.tfMonto.getText();
        contador += validarIngreso(strId, "identificacion");
        contador += validarIngreso(pin, "pin");
        contador += validarIngreso(strMonto, "monto");
        if(contador == 0)
        {
            insertar += validarEntrId(strId);
            insertar += validarEntrPin(pin);
            insertar += validarEntrMonto(strMonto);
            if (insertar == 0)
            {
                int id = Integer.parseInt(strId);
                String numero = ControladorUsuario.insertarCuenta(pin, strMonto, id);

                String mensaje = "Se ha creado una nueva cuenta en el sistema, los datos de la cuenta son: \n";
                mensaje += ControladorUsuario.imprimirCuenta(numero);
                mensaje += "\n---\n";
                mensaje += ControladorUsuario.imprimirPersona(id);
                JOptionPane.showMessageDialog(null, mensaje, "Consulta de usuario", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
    }
    public int validarIngreso(String pEntrada, String opcion)
    {
        if(pEntrada.length() == 0)
        {
            JOptionPane.showMessageDialog(null, "Error en dato: " + opcion);
            return 1;
        }
        return 0;
    }
    
    public int validarEntrId(String strId)
    {
        boolean esId = ControladorUsuario.auxIdP1(strId);
        if (esId==false)
        {
            JOptionPane.showMessageDialog(null, "Verifique su identificación");
            return 1;
        }
        return 0;
    }
    
    public int validarEntrPin(String pin)
    {
        boolean esPin = ExpresionesRegulares.validarPin(pin);
        if (esPin == false)
        {
            JOptionPane.showMessageDialog(null, "Verifique su pin");
            return 1;
        }
        return 0;
    }
    
    public int validarEntrMonto(String strMonto)
    {
        boolean esNum = ExpresionesRegulares.esNumero(strMonto);
        if (esNum == false)
        {
            JOptionPane.showMessageDialog(null, "Verifique el monto digitado");
            return 1;
        }
        return 0;
    }
    
    
    private void cargarDatosPersonas(){
        ResultSet datos = PersonaDAO.recuperarTodosLosUsuariosBD();
        try{
            while (datos.next()){
                Persona usuarioCargar = new Persona(datos.getString("codigo"), datos.getString("primerApellido"),
                        datos.getString("segundoApellido"),
                        datos.getString("nombre"),
                        Integer.parseInt(datos.getString("id")),
                        LocalDate.parse(datos.getString("fechaNacimiento"), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        Integer.parseInt(datos.getString("numero")),
                        datos.getString("correo"),
                        datos.getString("rol"));
                personasSistema.add(usuarioCargar);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
    }
    
    public void listarPersonas(){
        this.latabla = new ListarPersonas();
        String[] titulos = {
            "Nombre",
            "Primer apellido",
            "Segundo apellido",
            "Identificación"};
        this.latabla.modelo = new DefaultTableModel(null, titulos);
        this.latabla.tabla2.setModel(this.latabla.modelo);
        for(Persona persona: personasSistema)
        {
           Object[] info = {persona.getNombre(), persona.getPrimerApellido(), persona.getSegundoApellido(), persona.getId()};
           System.out.println(persona);
           this.latabla.modelo.addRow(info);
        }
        this.latabla.setVisible(true);
        this.menu.setVisible(false);
    }
    
        
    public static String consultarUsuario(String idUsuario)
    {
        boolean esCorrecto = ControladorUsuario.auxIdP1(idUsuario);
        String total = "";
        if(esCorrecto)
        {
            total = recuperarUsuario(idUsuario);
        }
        else
        {
            pedirInfoUsuario();
        }
        return total;
    }
    
    public static String recuperarUsuario(String idUsuario)
    {
        int id = Integer.parseInt(idUsuario);
        Persona persona = PersonaDAO.obtenerPersona(id);
        String mensaje = persona.toStringCompleto();
        String mensajeCuentas = CuentaDAO.obtenerCuentasPersona(id);
        String total = "Información del usuario:\n" + mensaje + "\n\nCuentas de este usuario:\n" + mensajeCuentas;
        return total;
    }
    
    //Punto 2
    public static String insertarCuenta(String pPin, String pMonto, int pId)
    {
        String numero = Cuenta.generarNumCuenta();
        LocalDate fecha = Cuenta.setFechaCreacion();
        Cuenta cuenta = new Cuenta(numero, pPin, fecha, pMonto, "activo");
        CuentaDAO.insertarCuenta(cuenta,fecha);
        CuentaDAO.asignarCuentaCliente(cuenta, pId);
        
        return numero;
    }
    
    private void ordenarClientes(){
        personasSistema.sort(Comparator.comparing(Persona::getPrimerApellido));
    }
    
    public static String imprimirCuenta(String pNum){
        Cuenta cuenta = CuentaDAO.obtenerCuenta(pNum);
        String mensaje = "Número de cuenta: " + pNum + "\nEstatus de la cuenta: " + cuenta.getEstatus() + "\nSaldo actual: " + cuenta.getSaldo();
        return mensaje;
    }
    
    public static String imprimirPersona(int id){
        Persona persona = PersonaDAO.obtenerPersona(id);
        String mensaje = "Nombre del dueño de la cuenta: " + persona.getNombre() + " " + persona.getPrimerApellido() + " " + 
                persona.getSegundoApellido() + "\nNúmero de teléfono 'asociado' a la cuenta: " + 
                persona.getNumero() + "\nDirección de correo elctrónico 'asociada' a la cuenta: " + persona.getCorreo();
        return mensaje;
    }
    
    public static boolean auxIdP1(String strid){
        boolean esCorrecto = false;
        boolean esNum = ExpresionesRegulares.esNumero(strid);
        if(esNum == true)
        {
            int id = Integer.parseInt(strid);
            esCorrecto = auxIdP2(id);
        }
        return esCorrecto;
    }
    
    public static boolean auxIdP2(int id){
        boolean esNum = false;
        Persona persona = PersonaDAO.obtenerPersona(id);
        String nombre = persona.getNombre();
        if(null != nombre){
            esNum = true;
        }
        
        return esNum;
    }
    
    public static boolean auxNumCuentaP1(String strNumCuenta){
        boolean esCorrecto = false;
        boolean esNum = ExpresionesRegulares.esNumero(strNumCuenta);
        if(esNum == true)
        {
            //int numCuenta = Integer.parseInt(strNumCuenta);
            esCorrecto = auxNumCuentaP2(strNumCuenta);
        }
        return esCorrecto;
    }
    
    public static boolean auxNumCuentaP2(String pNumCuenta){
        boolean esNum = false;
        Cuenta cuenta = CuentaDAO.obtenerCuenta(pNumCuenta);
        String numCuenta = cuenta.getNumero();
        if(null != numCuenta){
            esNum = true;
        }
        
        return esNum;
    }
    
    //Punto 3
    public static Persona[] ordenarPersonas(){
        //int cont = 0;
        //Persona[] listaPersona = new Persona[100];
        //ArrayList<Persona> listaPersonas1 = PersonaDAO.getPersonasBD();
        Persona[] listaPersonas = new Persona[10];
        Persona p1 = new Persona("1apellido", "2apellido", "nombre", 890);
        Persona p2 = new Persona("1apellido2", "2apellido2", "nombre2", 891);
        Persona p3 = new Persona("1apellido3", "2apellido3", "nombre3", 892);
        listaPersonas[0] = p1;
        listaPersonas[1] = p2;
        listaPersonas[2] = p3;
        for(Persona persona: listaPersonas)
        {
            String primerApellido = persona.getPrimerApellido();
            String segundoApellido = persona.getSegundoApellido();
            String nombre = persona.getNombre();
            int id = persona.getId();
            Persona usuario = new Persona(primerApellido, segundoApellido, nombre, id);
            //listaPersona[cont] = usuario;
            //cont++;
        }
        Ordenamiento.ordenar(listaPersonas);
        return listaPersonas;
    }
    
}
