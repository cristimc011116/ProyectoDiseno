/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import GUI.CrearCuenta;
import GUI.Menu;
import GUI.tabla;
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
    private ArrayList<Persona> personasSistema;
    private tabla latabla;
    
    public ControladorUsuario(Menu pMenu)
    {
        this.menu = pMenu;
        this.latabla = null;
        this.personasSistema = new ArrayList<>();
        this.menu.btnListar.addActionListener(this);
        cargarDatosPersonas();
        ordenarClientes();
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        switch(e.getActionCommand()){
            case "listar":
                listarPersonas();
                break;
            default:
                break;
        }
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
        this.latabla = new tabla();
        String[] titulos = {
            "Nombre",
            "Primer apellido",
            "Segundo apellido",
            "Identificación"};
        //this.latabla = new tabla();
        this.latabla.modelo = new DefaultTableModel(null, titulos);
        //JTable table = new JTable();
        //table.setModel(this.latabla.modelo);
        //this.latabla.tabla2 = table;
        this.latabla.tabla2.setModel(this.latabla.modelo);
        
        //this.latabla.panelSecundario.setViewportView(this.latabla.tabla2);
        for(Persona persona: personasSistema)
        {
           Object[] info = {persona.getNombre(), persona.getPrimerApellido(), persona.getSegundoApellido(), persona.getId()};
           System.out.println(persona);
           this.latabla.modelo.addRow(info);
        }
        this.latabla.setVisible(true);
        this.menu.setVisible(false);
    }
    
        
        
    
    //Punto 2
    public static int insertarCuenta(String pPin, int pMonto, int pId)
    {
        int numero = Cuenta.generarNumCuenta();
        LocalDate fecha = Cuenta.setFechaCreacion();
        Cuenta cuenta = new Cuenta(numero, pPin, fecha, pMonto, "activo");
        CuentaDAO.insertarCuenta(cuenta,fecha);
        CuentaDAO.asignarCuentaCliente(cuenta, pId);
        
        return numero;
    }
    
    private void ordenarClientes(){
        personasSistema.sort(Comparator.comparing(Persona::getPrimerApellido));
    }
    
    public static String imprimirCuenta(int pNum){
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
