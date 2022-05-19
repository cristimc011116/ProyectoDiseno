/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package webService;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Paola López
 */
public class ExtraerXML {
  private String xml;
  private Element raizElement;
  
  public ExtraerXML(String pDatos) throws SAXException, IOException, ParserConfigurationException{
      
    pDatos =  reemplazarCaracteres(pDatos);
    this.xml = pDatos;
    
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.parse(new InputSource(new StringReader(this.xml)));
    this.raizElement = document.getDocumentElement();
  }
  
  public String getValores(String tag){
    try {
      NodeList listaElementos = this.raizElement.getElementsByTagName(tag);
      if (listaElementos != null && listaElementos.getLength() > 0) {
          NodeList subLista = listaElementos.item(0).getChildNodes();
          if (subLista != null && subLista.getLength() > 0) {
              return subLista.item(0).getNodeValue();
          }
      }
    } catch (Exception e) {
      return "0";
    }
    return "0";
  }
  
  private String reemplazarCaracteres(String cadena){
    cadena = cadena.replace("&lt;", "<");
    cadena = cadena.replace("&gt;", ">");
    return cadena;
  }
}
