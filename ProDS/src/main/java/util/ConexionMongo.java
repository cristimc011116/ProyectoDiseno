/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Cristi Martínez
 */
public class ConexionMongo {
    public static DB db;
    public static DBCollection persona;
    public static DBCollection cuenta;
    public static DBCollection operacion;
    public static DBCollection cuentaOperacion;
    public static DBCollection personaCuenta;
    public DBCursor cursor = null;
    public static BasicDBObject document = new BasicDBObject();
    
    public static void conexionMD()
    {
        try
        {
            Mongo mongo = new Mongo("localhost", 27017);
            db = mongo.getDB("gestorCuentas");
            persona = db.getCollection("Persona");
            cuenta = db.getCollection("Cuenta");
            operacion = db.getCollection("Operacion");
            cuentaOperacion = db.getCollection("CuentaOperacion");
            personaCuenta = db.getCollection("PersonaCuenta");
            System.out.println("Conexión exitosa");
        } catch (Exception e)
        {
            System.out.println("ERROR AL CONECTAR: " + e);
        }
    }
    
}
