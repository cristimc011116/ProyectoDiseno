/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import controlador.ControladorUsuario;

/**
 *
 * @author Cristi Martínez
 */
public class MainGUI {
    public static void main(String[] args)
    {
      Menu menu = new Menu();
      ControladorUsuario controlador = new ControladorUsuario(menu);
      controlador.menu.setVisible(true);
    }
    
}
