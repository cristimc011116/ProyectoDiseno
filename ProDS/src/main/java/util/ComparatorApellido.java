/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.Comparator;
import logicadenegocios.Persona;

/**
 *
 * @author Cristi Martínez
 */
public class ComparatorApellido implements Comparator<Persona>{
    @Override
    public int compare(Persona a, Persona b){
        return b.getNombre().compareToIgnoreCase(a.getNombre());
    }
}

