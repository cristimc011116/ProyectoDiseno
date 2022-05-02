/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.ArrayList;
import logicadenegocios.Persona;

/**
 *
 * @author Cristi Martínez
 */
public class Ordenamiento {
    public static void ordenar(Comparable [] arreglo) { 
        for (int indice = 1; indice < arreglo.length; indice ++) { 
            Comparable comparar = arreglo [indice]; 
            int contador = indice - 1; 
            while (contador >= 0 && ! arreglo [contador].menorQue(comparar)) { 
                arreglo [contador+1] = arreglo [contador]; 
                contador --; 
            } 
            arreglo [contador+1] = comparar; 
        }
    }
}
