/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package webServiceBCCR;
import java.text.SimpleDateFormat;
import java.util.Date;
import webServiceBCCR.ConexionBCCR;

/**
 *
 * @author User
 */
public class ConsultarCambioMoneda {
    private String fechaActual;
    private int indicadorRequerido = 0;
    private final String nombre = "Paola Lopez Mendez";
    private final String subNiveles = "N";
    private final String HOST = "https://gee.bccr.fi.cr/Indicadores/Suscripciones/WS/wsindicadoreseconomicos.asmx/ObtenerIndicadoresEconomicosXML";
    private String url;
    private final String VALUE_TAG = "NUM_VALOR";

//-------------------------------------------CONSTRUCTOR------------------------------------
    public ConsultarCambioMoneda(){
      setFecha();
    }

//---------------------------------------METODOS ACCESORES-----------------------------------
    
    
    public double  ConsultarCompraDolar(){
        setCompra();
        double valor = Double.parseDouble(getValores());
        return valor; 
    }

    public double ConsultarVentaDolar(){
        setVenta();
        double valor = Double.parseDouble(getValores());
        return valor;
    }
    
    private void setCompra(){
        this.indicadorRequerido = 317; //CODIGO DE COMPRA
    }

    private void setVenta(){
        this.indicadorRequerido = 318;//CODIGO DE VENTA
    }
    
    private void setUrl(){
        String params = "Indicador="+indicadorRequerido+"&FechaInicio="+fechaActual+"&FechaFinal="+fechaActual+"&Nombre="+nombre+"&SubNiveles="+subNiveles+"&CorreoElectronico="+"farolayn@gmail.com"+"&Token="+"155OLEOAAG";
        this.url = HOST+"?"+params;
    }
    private String getValores(){
        try {
          setUrl(); 

          //Obtiene y Parsea el XML
          String data = ConexionBCCR.getHTML(url);
          ExtraerXML xml = new ExtraerXML(data);

          //Retorna el valor del tag
          return xml.getCambio(VALUE_TAG);
        } 
        catch (Exception e) {
          System.out.println("ERROR, NO SE PUDO CONSULTAR EL VALOR DE TIPO CAMBIO");
          return "0";
        }
    }



    private void setFecha(){
        Date date = new Date();
        SimpleDateFormat fechaAuxiliar = new SimpleDateFormat("dd/MM/yyyy");

        this.fechaActual = fechaAuxiliar.format(date);
        this.fechaActual = fechaActual;
    }
}
