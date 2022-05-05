/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package webService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author ranbe
 */
public class ConexionBCCR {
        protected static String getHTML(String urlToRead) throws Exception {
        StringBuilder sincronizar = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setRequestMethod("GET");
        BufferedReader datos = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        String line;
        
        while ((line = datos.readLine()) != null) {
           sincronizar.append(line);
        }
        datos.close();
        return sincronizar.toString();
    }
}
