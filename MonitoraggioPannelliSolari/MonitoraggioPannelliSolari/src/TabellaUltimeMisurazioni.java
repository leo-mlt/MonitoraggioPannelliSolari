
import java.time.LocalDate;
import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;


/**
 *
 * @author Leo
 */
public class TabellaUltimeMisurazioni extends TableView{
    private final ObservableList<Misurazione> listaOsservabileMisurazioni;
    
    public TabellaUltimeMisurazioni(){
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        
        TableColumn colonnaIdUtente = new TableColumn("IDUTENTE");
        TableColumn colonnaData = new TableColumn("DATA");
        TableColumn colonnaInverter = new TableColumn("INVERTER");
        TableColumn colonnaProduzione = new TableColumn("PRODUZIONE");
        TableColumn colonnaConsumi = new TableColumn("CONSUMI");
        TableColumn colonnaImmissioni = new TableColumn("IMMISSIONI");
        
        colonnaIdUtente.setCellValueFactory(new PropertyValueFactory<>("idUtente"));
        colonnaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colonnaInverter.setCellValueFactory(new PropertyValueFactory<>("letturaInverter"));
        colonnaProduzione.setCellValueFactory(new PropertyValueFactory<>("produzione"));
        colonnaConsumi.setCellValueFactory(new PropertyValueFactory<>("consumi"));
        colonnaImmissioni.setCellValueFactory(new PropertyValueFactory<>("immissioni"));
        
        
        listaOsservabileMisurazioni = FXCollections.observableArrayList();
        setItems(listaOsservabileMisurazioni);
        getColumns().addAll(colonnaIdUtente, colonnaData, colonnaInverter, colonnaProduzione, colonnaConsumi, colonnaImmissioni);
   
    }
    
    public void aggiornaListaMisurazioni(List<Misurazione> misurazioni){
        listaOsservabileMisurazioni.clear();
        listaOsservabileMisurazioni.addAll(misurazioni);
        
    }
    
    public void rimuoviMisurazione(Misurazione selez){
        listaOsservabileMisurazioni.remove(selez);
    }
    
    
    
}
