

import java.time.*;
import javafx.beans.property.*;


/**
 *
 * @author Leo
 */
public class Misurazione{
    private final SimpleStringProperty idUtente;
    private final ObjectProperty<LocalDate> data;
    
    private final SimpleDoubleProperty letturaInverter;
    private final SimpleDoubleProperty produzione;
    private final SimpleDoubleProperty consumi;
    private final SimpleDoubleProperty immissioni;
    
    public Misurazione(String idUt, LocalDate d, double i, double prod, double cons, double imm){
        this.idUtente = new SimpleStringProperty(idUt);
        this.data = new SimpleObjectProperty(d);
        this.letturaInverter = new SimpleDoubleProperty(i);
        this.produzione = new SimpleDoubleProperty(prod);
        this.consumi = new SimpleDoubleProperty(cons);
        this.immissioni = new SimpleDoubleProperty(imm);
    }
    
    public String getIdUtente(){
        return idUtente.get();
    }
    
    public LocalDate getData(){
        return data.get();
    }
    
    public double getLetturaInverter(){
        return letturaInverter.get();
    }
    
    public double getProduzione(){
        return produzione.get();
    }
    
    public double getConsumi(){
        return consumi.get();
    }
    
    public double getImmissioni(){
        return immissioni.get();
    }
}
