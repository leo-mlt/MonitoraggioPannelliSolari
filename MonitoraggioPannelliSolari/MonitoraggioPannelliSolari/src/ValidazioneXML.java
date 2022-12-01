
import java.io.*;
import javax.xml.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import org.w3c.dom.*;
import org.xml.sax.*;


/**
 *
 * @author Leo
 */
public class ValidazioneXML {
    public static boolean valida(String filexml, String filexsd, boolean singolaRiga){
        try{
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Document d = null;
            if(!singolaRiga)
                d = db.parse(new File(filexml));
            else
                d = db.parse(new InputSource(new StringReader(filexml)));
            Schema s = sf.newSchema(new StreamSource(new File(filexsd)));
            s.newValidator().validate(new DOMSource(d));
        }catch(Exception e){
            if( e instanceof SAXException)
                System.out.println("Errore di validazione: "+e.getMessage());
            else
                System.out.println(e.getMessage());
            
            return false;
        }
        return true;
    }
}
