/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import controlador.ControladorUsuario;
import dao.CuentaDAO;
import dao.PersonaDAO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import logicadenegocios.Cuenta;
import logicadenegocios.Persona;

/**
 *
 * @author Cristi Martínez
 */
public class Listados {
    
    public static ArrayList<Persona> ordenarClientes(){
      ArrayList<Persona> personasSistema = PersonaDAO.getPersonasBD();
      personasSistema.sort(Comparator.comparing(Persona::getPrimerApellido));
      return personasSistema;
    }
    
    public static ArrayList<Cuenta> ordenarCuentas(){
        ArrayList<Cuenta> cuentasSistema = CuentaDAO.getCuentasBD();
        Collections.sort(cuentasSistema);
        return cuentasSistema;
    }
}
