/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logicadenegocios;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
/**
 *
 * @author Cristi Martínez
 */

//------------------------------------------------PASARLO A PAQUETE UTIL------------------------------
public class Mensaje {
    public static final String ACCOUNT_SID = "AC99990d2f368c631048e2be792a0653cd";
    public static final String AUTH_TOKEN = "cccc1b8fa67be226e4c6fdbbc5b31e27";
    /**
     * @param args the command line arguments
     */
    public static void enviarMensaje(int pNumero, String pMensaje) {
        // TODO code application logic here
        String strNumero = Integer.toString(pNumero);
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(new PhoneNumber(strNumero),
            new PhoneNumber("+17473026735"), 
            pMensaje).create();

        System.out.println(message.getSid());
    }
}
