
import java.io.*;
import java.net.*;


/**
 *
 * @author Leo
 */
public class LogDiNavigazione {
    public static void main(String[] args){
        try(ServerSocket serv = new ServerSocket(8081);
                FileOutputStream fouts = new FileOutputStream("ArchivioEventiLog.xml");
                DataOutputStream dout = new DataOutputStream(fouts))
                
        {
            System.out.println("Inizio");
            while(true){
                try(Socket s = serv.accept();
                        DataInputStream din = new DataInputStream(s.getInputStream());
                        )
                {
                  String stringaLog = din.readUTF();
                  if(ValidazioneXML.valida(stringaLog, "eventoDiNavigazione.xsd", true)){
                      dout.writeUTF(stringaLog);
                      System.out.println("Evento salvato");
                  }
                  else
                     System.out.println("Evento non salvato"); 
                }catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
