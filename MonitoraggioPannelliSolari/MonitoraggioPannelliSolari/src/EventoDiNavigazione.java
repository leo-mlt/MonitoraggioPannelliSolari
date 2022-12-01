
import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.converters.basic.*;
import java.io.*;

import java.net.*;
import java.text.*;
import java.util.*;


/**
 *
 * @author Leo
 */
public class EventoDiNavigazione {
    private final String nomeApplicazione = "Monitoraggio Pannelli Solari";
    private  String IpClient;
    private  String Data;
    private  String Evento;
    
    public void setLog(String event, String ipServ, int portaServ){
        Evento = event;
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss E");
        Data = format.format(now);
        try{
            IpClient = InetAddress.getLocalHost().toString();
        }catch(Exception e){
            System.out.println("Indirizzo IP non memorizzato");
        }
        String serializzato = serializzaXML();
        
        try(Socket s = new Socket(ipServ, portaServ);
            DataOutputStream dout = new DataOutputStream(s.getOutputStream()) 
           )
           {
               dout.writeUTF(serializzato);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
      
    }
    
    private String serializzaXML(){
        XStream serializza = new XStream();
        
        return serializza.toXML(this);
    }
}
