/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.util.Random;
/**
 *
 * @author Cristi Martínez
 */
public class Mensaje {
    public static final String ACCOUNT_SID = "AC99990d2f368c631048e2be792a0653cd";
    public static final String AUTH_TOKEN = "78f447d8f45f460527cf474d42203d58";
    /**
     * @param args the command line arguments
     */
    public static void enviarMensaje(int pNumero, String pMensaje) {
        // TODO code application logic here
        String strNumero = "+506" + Integer.toString(pNumero);
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(new PhoneNumber(strNumero),
            new PhoneNumber("+17473026735"), 
            pMensaje).create();

        //System.out.println(message.getSid());
    }





    public static String crearPalabra()
    {
      char[] abc = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
      char[] num = {'0','1','2','3','4','5','6','7','8','9'};
      Random rand = new Random();
      int a = rand.nextInt(26);
      int n = rand.nextInt(10);
      int c = rand.nextInt(2);
      int contador = 0;
      String palabra = "";
      while(contador < 6)
      {
        a = rand.nextInt(26);
        n = rand.nextInt(10);
        c = rand.nextInt(2);
        if(c==1)
        {
          palabra += abc[a];
        }
        else
        {
          palabra += num[n];
        }
        contador++;
      }
      return palabra;
    }
}












