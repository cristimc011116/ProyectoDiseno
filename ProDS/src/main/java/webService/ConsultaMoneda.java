/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package webService;

import java.text.SimpleDateFormat;
import java.util.Date;
import webService.ConexionBCCR;

/**
 *
 * @author ranbe
 */
public class ConsultaMoneda {
    private String fechaActual;
    private int indicadorRequerido = 0;
    private final String nombre = "proyectoDS";
    private final String subNiveles = "N";
    private final String HOST = "https://gee.bccr.fi.cr/Indicadores/Suscripciones/WS/wsindicadoreseconomicos.asmx/ObtenerIndicadoresEconomicosXML";
    private String url;
    private final String VALUE_TAG = "NUM_VALOR";

    public ConsultaMoneda(){
      setFecha();
    }
    
    private void setCompra(){
        this.indicadorRequerido = 317;
    }

    private void setVenta(){
        this.indicadorRequerido = 318;
    }
    
    public double consultaCambioCompra(){
        setCompra();

        double valor = Double.parseDouble(getValores());
        return valor;
    }

    public double consultaCambioVenta(){
        setVenta();

        double valor = Double.parseDouble(getValores());
        return valor;
    }
    
    private String getValores(){
        try {
          setUrl(); //Set del Url con los Parámetros

          //Obtiene y Parsea el XML
          String data = ConexionBCCR.getHTML(url);
          ExtraerXML xml = new ExtraerXML(data);

          //Retorna el valor del tag
          return xml.getValores(VALUE_TAG);
        } 
        catch (Exception e) {
          System.out.println("ERROR, EL VALOR DE CAMBIO NO SE PUDO OBTENER.");
          return "0";
        }
    }

    private void setUrl(){
        String params = "Indicador="+indicadorRequerido+"&FechaInicio="+fechaActual+"&FechaFinal="+fechaActual+"&Nombre="+nombre+"&SubNiveles="+subNiveles+"&CorreoElectronico="+"cristimc011116@gmail.com"+"&Token="+"CP6151SC2E";
        this.url = HOST+"?"+params;
    }

    private void setFecha(){
        Date date = new Date();
        SimpleDateFormat fechaAuxiliar = new SimpleDateFormat("dd/MM/yyyy");
        this.fechaActual = fechaAuxiliar.format(date);
    }
}
