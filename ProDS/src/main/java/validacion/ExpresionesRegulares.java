/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validacion;

import dao.CuentaDAO;
import dao.PersonaDAO;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import logicadenegocios.Cuenta;
import logicadenegocios.Persona;

/**
 *
 * @author Cristi Martínez
 */
public class ExpresionesRegulares {
    
    public static boolean validarPin(String pinCuenta)
    {
        if (pinCuenta.length() != 6)
        {
            return false;
        }
        char[] abc = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        char[] esp = {'|','!','"', '#','$','%','&','/',')','(','=','?','¡','¿','´','@','¨','+','*','¬','~','}',']','`','{','[','^','<','>',',',';','.',':','-','_'};
        char[] num = {'0','1','2','3','4','5','6','7','8','9'};
        int cont1 = 0;
        int cont2 = 0;
        int cont3 = 0;
        char array[] = pinCuenta.toCharArray();
        //Dividir esto en partes porque la CC va a ser mayor que 3
        for (int i=0; i<array.length; i++)
        {
            for(int a=0; a<abc.length; a++){
                if(array[i] == abc[a]){
                    cont1++;
                }
            }
            for(int e=0; e<esp.length; e++){
                if(array[i] == esp[e]){
                    cont2++;
                }
            }
            for(int n=0; n<num.length; n++){
                if(array[i] == num[n]){
                    cont3++;
                }
            }   
        }
        if(cont1>0 & cont2>0 & cont3>0){
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static boolean esNumero(String pMonto) 
    {
        //Verficar si tambien valida con decimales
        boolean isNumeric =  pMonto.matches("[+-]?\\d*(\\.\\d+)?");
        return isNumeric;
    }
    
    
    public static boolean validarEmail(String email){
        String regx = "^(.+)@(.+)$";
        //Compile regular expression to get the pattern  
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    public static boolean validarTelefono(String s)
    {
        Pattern p = Pattern.compile("^\\d{8}$");
        Matcher m = p.matcher(s);
        return (m.matches());
    } 
    
    public static boolean validarCuenta(int cuenta)
    {
      ArrayList<Cuenta> listaCuentas = CuentaDAO.getCuentasBD();
      int cuentaLista;
      for(int i=0; i < listaCuentas.size();i++){
        cuentaLista = Integer.parseInt(listaCuentas.get(i).toString());
        if( cuentaLista == cuenta){
          return true;
        }
      }
      return false;
    } 
    
    public static boolean validarIdExiste(int id){
        Persona persona = PersonaDAO.obtenerPersona(id);
        String nombre = persona.getNombre();
        if(nombre!=null){
            return true;
        }
        return false;
    }
}
