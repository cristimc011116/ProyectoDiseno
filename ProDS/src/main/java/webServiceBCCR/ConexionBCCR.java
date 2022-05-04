/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package webServiceBCCR;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author User
 */
public class ConexionBCCR {
  protected static String getHTML(String urlToRead) throws Exception {
    StringBuilder cadenaCambiable = new StringBuilder();
    URL link = new URL(urlToRead);
    HttpURLConnection conexion = (HttpURLConnection) link.openConnection();
    conexion.setRequestMethod("GET");
    BufferedReader data = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
    String line;
    
    while ((line = data.readLine()) != null) {
      cadenaCambiable.append(line);
    }
    data.close();
    return cadenaCambiable.toString();
    }
    
}
