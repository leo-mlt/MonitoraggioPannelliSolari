
import com.thoughtworks.xstream.*;
import java.io.*;
import java.nio.file.*;



/**
 *
 * @author Leo
 */
public class ParametriDiConfigurazione {
    public int numeroAnni;
    public int[] listaAnni;
    public String coloreGraficoProduzione;
    public String coloreGraficoConsumi;
    public String coloreGraficoImmissioni;
    public int numeroRigheTabella;
    public int numeroUltimeMisurazioni;
    public String IPClient;
    public String IPLog;
    public int portaLog;
    public String IPDB;
    public int portaDB;
    
    public void deserializzaParametri(){
        ParametriDiConfigurazione pc;
        String x;
        XStream xs = new XStream();
        xs.addImplicitCollection(ParametriDiConfigurazione.class,"listaAnni", Integer.class);
        try{
            x = new String(Files.readAllBytes(Paths.get("configurazione.xml")));
            
            if(ValidazioneXML.valida("configurazione.xml", "configurazione.xsd", false)){
                pc = (ParametriDiConfigurazione) xs.fromXML(x);
                this.numeroAnni = pc.numeroAnni;
                this.listaAnni = pc.listaAnni;
                this.coloreGraficoProduzione = pc.coloreGraficoProduzione;
                this.coloreGraficoConsumi = pc.coloreGraficoConsumi;
                this.coloreGraficoImmissioni = pc.coloreGraficoImmissioni;
                this.numeroRigheTabella = pc.numeroRigheTabella;
                this.numeroUltimeMisurazioni = pc.numeroUltimeMisurazioni;
                this.IPClient = pc.IPClient;
                this.IPLog = pc.IPLog;
                this.portaDB = pc.portaDB;
                this.IPDB = pc.IPDB;
                this.portaLog = pc.portaLog;
            }
        }catch(IOException e){
            System.out.println("Errore deserializzazione del file di configurazione");
        }
    }
    
    public void serializzaParametri(){
        XStream xs = new XStream();
        xs.addImplicitCollection(ParametriDiConfigurazione.class, "listaAnni");
        xs.alias("listaAnni", String.class, Integer.class);
       
        String x ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
         x += xs.toXML(this);
        
        
        try{
            if(ValidazioneXML.valida(x, "configurazione.xsd", true))
                Files.write(Paths.get("configurazione.xml"), x.getBytes());
            else
                System.out.println("Validazione non riuscita");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

