
import java.io.*;

import java.time.LocalDate;



/**
 *
 * @author Leo
 */
public class CacheUltimaMisurazione implements Serializable{
    public String vecchioIdUtente;
    public String vecchioAnno;
    public LocalDate vecchiaData;
    public double vecchioInverter;
    public double vecchioProduzioneF1;
    public double vecchioProduzioneF2;
    public double vecchioProduzioneF3;
    public double vecchioConsumiF1;
    public double vecchioConsumiF2;
    public double vecchioConsumiF3;
    public double vecchioImmissioniF1;
    public double vecchioImmissioniF2;
    public double vecchioImmissioniF3;
    public int rigaTabella;
    
    public void salvaMisurazioneBin(MonitoraggioPannelliSolari mps){
        if(mps.idUtenteField.getText().equals("") && mps.annoField.getText().equals("") && mps.produzioneF1Field.getText().equals("") && mps.produzioneF2Field.getText().equals("")
            && mps.produzioneF3Field.getText().equals("") && mps.consumiF1Field.getText().equals("") && mps.consumiF2Field.getText().equals("")
            && mps.consumiF3Field.getText().equals("") && mps.immissioniF1Field.getText().equals("") && mps.immissioniF2Field.getText().equals("")
            && mps.immissioniF3Field.getText().equals("") && mps.dataField.getValue() == null){
            
            System.out.println("Campi Vuoti. Elementi non salvati");
            return;
        }
        vecchioIdUtente = mps.idUtenteField.getText();
        vecchioAnno = mps.annoField.getText();
        vecchiaData = mps.dataField.getValue();
        vecchioInverter = Double.parseDouble(verificaPresenzaValore(mps.letturaInverterField.getText()));
        
        vecchioProduzioneF1 = Double.parseDouble(verificaPresenzaValore(mps.produzioneF1Field.getText()));
        vecchioProduzioneF2 = Double.parseDouble(verificaPresenzaValore(mps.produzioneF2Field.getText()));
        vecchioProduzioneF3 = Double.parseDouble(verificaPresenzaValore(mps.produzioneF3Field.getText()));
        
        vecchioConsumiF1 = Double.parseDouble(verificaPresenzaValore(mps.consumiF1Field.getText()));
        vecchioConsumiF2 = Double.parseDouble(verificaPresenzaValore(mps.consumiF2Field.getText()));
        vecchioConsumiF3 = Double.parseDouble(verificaPresenzaValore(mps.consumiF3Field.getText()));
        
        vecchioImmissioniF1 = Double.parseDouble(verificaPresenzaValore(mps.immissioniF1Field.getText()));
        vecchioImmissioniF2 = Double.parseDouble(verificaPresenzaValore(mps.immissioniF2Field.getText()));
        vecchioImmissioniF3 = Double.parseDouble(verificaPresenzaValore(mps.immissioniF3Field.getText()));
        
        rigaTabella = (mps.tabellaMisurazioni.getFocusModel().getFocusedIndex() <  0)? -1 : mps.tabellaMisurazioni.getFocusModel().getFocusedIndex();
        
        try(FileOutputStream fout = new FileOutputStream("UltimaMisurazione.bin");
               ObjectOutputStream oout = new ObjectOutputStream(fout) )
        {
            oout.writeObject(this);
        }catch(NotSerializableException ex){
            System.out.println("Non serializzato");
        }catch(Exception e){
            System.out.println("Impossibile salvare misurazione in cache");
        }
                
    }
    
    private String verificaPresenzaValore(String valore){
        if(valore.equals(""))
            return "-1";
        else
            return valore;
    }
    
    private String verificaValoreCaricato(double valore){
        if(valore == -1)
            return "";
        else
            return ""+valore+"";
    }
    
    public void caricaMisurazioneBin(MonitoraggioPannelliSolari mps){
        try(FileInputStream fin = new FileInputStream("UltimaMisurazione.bin");
              ObjectInputStream oin = new ObjectInputStream(fin);)
        {
            CacheUltimaMisurazione um = (CacheUltimaMisurazione)oin.readObject();
            mps.idUtenteField.setText(um.vecchioIdUtente);
            mps.annoField.setText(um.vecchioAnno);
            mps.dataField.setValue(um.vecchiaData);
            mps.letturaInverterField.setText(verificaValoreCaricato(um.vecchioInverter));
            mps.produzioneF1Field.setText(verificaValoreCaricato(um.vecchioProduzioneF1));
            mps.produzioneF2Field.setText(verificaValoreCaricato(um.vecchioProduzioneF2));
            mps.produzioneF3Field.setText(verificaValoreCaricato(um.vecchioProduzioneF3));
            
            mps.consumiF1Field.setText(verificaValoreCaricato(um.vecchioConsumiF1));
            mps.consumiF2Field.setText(verificaValoreCaricato(um.vecchioConsumiF2));
            mps.consumiF3Field.setText(verificaValoreCaricato(um.vecchioConsumiF3));
            
            mps.immissioniF1Field.setText(verificaValoreCaricato(um.vecchioImmissioniF1));
            mps.immissioniF2Field.setText(verificaValoreCaricato(um.vecchioImmissioniF2));
            mps.immissioniF3Field.setText(verificaValoreCaricato(um.vecchioImmissioniF3));
            
            if(!mps.idUtenteField.getText().equals("") && !mps.annoField.getText().equals(""))
                mps.importaDatiUtenteAnno();
            
            int riga = um.rigaTabella;
            if(riga >= 0)
                 mps.tabellaMisurazioni.getSelectionModel().select(riga);
        }catch(Exception e){
            System.out.println("Impossibile caricare misurazione dalla cache/Primo avvio del programma");
        }
       
    }
     
}
