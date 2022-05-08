/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author Cristi Martínez
 */
public class Encriptacion {
    public static String encriptar(String mensaje)
    {
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
        char arrayD[] = mensaje.toCharArray();
        
        for(int i=0; i<arrayD.length; i++)
        {
            arrayD[i] = (char)(arrayD[i]-(char)3);
        }
        String desencriptado = String.valueOf(arrayD);
        return desencriptado;
    }
}
