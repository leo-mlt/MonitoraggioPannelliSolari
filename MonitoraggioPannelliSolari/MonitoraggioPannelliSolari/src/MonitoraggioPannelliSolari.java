

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;

import java.util.*;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;

import javafx.stage.*;



/**
 *
 * @author Leo
 */
public class MonitoraggioPannelliSolari extends Application{
    private Label title;
    private Label idUtenteLabel;
    private Label dataLabel;
    private Label annoLabel;
    private Label letturaInverterLabel;
    private Label contatoreProduzioneLabel;
    private Label produzioneF1Label;
    private Label produzioneF2Label;
    private Label produzioneF3Label;
    private Label contatoreConsumiLabel;
    private Label consumiF1Label;
    private Label consumiF2Label;
    private Label consumiF3Label;
    private Label contatoreImmissioniLabel;
    private Label immissioniF1Label;
    private Label immissioniF2Label;
    private Label immissioniF3Label;
    
    private Label ultimeMisurazioniLabel;
    private Button aggiungiButton;
    private Button annullaButton;
    private Button eliminaRigaButton;
    private Button produzioneButton;
    private Button consumiButton;
    private Button immissioniButton;
    private Button importaDatiButton;
    TabellaUltimeMisurazioni tabellaMisurazioni;
    public TextField idUtenteField;
    public DatePicker dataField;
    public MenuButton annoField;
    private List<MenuItem> opzioniAnni;
    private List<Misurazione> differenzaValori;
    public TextField letturaInverterField;
    public TextField produzioneF1Field;
    public TextField produzioneF2Field;
    public TextField produzioneF3Field;
    public TextField consumiF1Field;
    public TextField consumiF2Field;
    public TextField consumiF3Field;
    public TextField immissioniF1Field;
    public TextField immissioniF2Field;
    public TextField immissioniF3Field;
    public GraficoMisurazioni graficoMisurazioni;
    private ParametriDiConfigurazione parametri;
    private Scene scene;
    
    
   public void start(Stage stage){
       parametri = new ParametriDiConfigurazione();
       parametri.deserializzaParametri();
       EventoDiNavigazione navigazione = new EventoDiNavigazione();
       navigazione.setLog("AVVIA", parametri.IPLog, parametri.portaLog);
       inizializzaInterfacciaGrafica();
       inizializzaMenuAnni(parametri);
       graficoMisurazioni = new GraficoMisurazioni();
       inizializzaStyle();
       modificaCssIstogramma(parametri.coloreGraficoProduzione ,"istogrammaProduzione.css");
       modificaCssIstogramma(parametri.coloreGraficoConsumi, "istogrammaConsumi.css");
       modificaCssIstogramma(parametri.coloreGraficoImmissioni, "istogrammaImmissioni.css");
       righeVisualizzabiliTabella(parametri.numeroRigheTabella);
       
       CacheUltimaMisurazione cache = new CacheUltimaMisurazione();
       
      
       
       
       scene = new Scene(new Group(title, idUtenteLabel, idUtenteField, annoLabel, annoField, importaDatiButton,
                                dataLabel, dataField, letturaInverterLabel, letturaInverterField, contatoreProduzioneLabel,
                                produzioneF1Label, produzioneF1Field, produzioneF2Label, produzioneF2Field, produzioneF3Label,
                                produzioneF3Field, contatoreConsumiLabel, consumiF1Label, consumiF1Field, consumiF2Label, 
                                consumiF2Field, consumiF3Label, consumiF3Field, contatoreImmissioniLabel, immissioniF1Label,
                                immissioniF1Field, immissioniF2Label, immissioniF2Field, immissioniF3Label, immissioniF3Field,
                                aggiungiButton, annullaButton, eliminaRigaButton, 
                                produzioneButton, consumiButton, immissioniButton, ultimeMisurazioniLabel,
                                graficoMisurazioni.istogramma, tabellaMisurazioni), 950, 600);
       
       cache.caricaMisurazioneBin(this);
       
       
       importaDatiButton.setOnAction((ActionEvent ev)->{importaDatiUtenteAnno();
                                                        navigazione.setLog("IMPORTA DATI", parametri.IPLog, parametri.portaLog);
                                                        });
       produzioneButton.setOnAction((ActionEvent ev)->{caricaDatiGraficoProduzione();
                                                      navigazione.setLog("GRAFICO PRODUZIONE", parametri.IPLog, parametri.portaLog);
                                                        });
       consumiButton.setOnAction((ActionEvent ev)->{caricaDatiGraficoConsumi();
                                                    navigazione.setLog("GRAFICO CONSUMI", parametri.IPLog, parametri.portaLog);
                                                    });
       immissioniButton.setOnAction((ActionEvent ev)->{caricaDatiGraficoImmissioni();
                                                      navigazione.setLog("GRAFICO IMMISSIONI", parametri.IPLog, parametri.portaLog);
                                                        });
       aggiungiButton.setOnAction((ActionEvent ev)->{aggiungiValore();
                                                    navigazione.setLog("AGGIUNGI", parametri.IPLog, parametri.portaLog);
                                                    });
       eliminaRigaButton.setOnAction((ActionEvent ev)->{rimuoviMisurazioneTab();
                                                        navigazione.setLog("ELIMINA RIGA", parametri.IPLog, parametri.portaLog);
                                                        });
       annullaButton.setOnAction((ActionEvent ev)->{resettaValoriInserimento();
                                                    navigazione.setLog("ANNULLA", parametri.IPLog, parametri.portaLog);    
                                                    });
      tabellaMisurazioni.setOnMouseClicked((MouseEvent ev)->{navigazione.setLog("RIGA SELEZIONATA", parametri.IPLog, parametri.portaLog);});
       
       stage.setOnCloseRequest((WindowEvent e)->{registraValoriInCache(cache);
                                                navigazione.setLog("TERMINE", parametri.IPLog, parametri.portaLog);
                                                });
       
       stage.setTitle("Monitoraggio Pannelli Solari");
       stage.setScene(scene);
       scene.getStylesheets().add("istogrammaProduzione.css"); //carica all'avvio la produzione
       
       stage.show();
   }
   
   private void registraValoriInCache(CacheUltimaMisurazione cache){
       cache.salvaMisurazioneBin(this);
   }
   
   private void resettaValoriInserimento(){
       dataField.getEditor().clear();
       dataField.setValue(null);
       letturaInverterField.setText("");
       
       produzioneF1Field.setText("");
       produzioneF2Field.setText("");
       produzioneF3Field.setText("");
       
       consumiF1Field.setText("");
       consumiF2Field.setText("");
       consumiF3Field.setText("");
       
       immissioniF1Field.setText("");
       immissioniF2Field.setText("");
       immissioniF3Field.setText("");
       
       tabellaMisurazioni.getSelectionModel().select(-1);
   }
   
   private void rimuoviMisurazioneTab(){
       
       Misurazione selez = (Misurazione) tabellaMisurazioni.getSelectionModel().getSelectedItem();
       
       if(selez == null)
           return;
       tabellaMisurazioni.rimuoviMisurazione(selez);
       ArchivioMisurazioni.eliminaMisurazione(parametri, selez);
       
       importaDatiUtenteAnno();
   }
   
   private void aggiungiValore(){
      
       String id = idUtenteField.getText();
       LocalDate d = dataField.getValue();
       double invert = Double.parseDouble(letturaInverterField.getText());
       double produzioneF1 = Double.parseDouble(produzioneF1Field.getText());
       double produzioneF2 = Double.parseDouble(produzioneF2Field.getText());
       double produzioneF3 = Double.parseDouble(produzioneF3Field.getText());
       double consumiF1 = Double.parseDouble(consumiF1Field.getText());
       double consumiF2 = Double.parseDouble(consumiF2Field.getText());
       double consumiF3 = Double.parseDouble(consumiF3Field.getText());
       double immissioniF1 = Double.parseDouble(immissioniF1Field.getText());
       double immissioniF2 = Double.parseDouble(immissioniF2Field.getText());
       double immissioniF3 = Double.parseDouble(immissioniF3Field.getText());
       
       double produzioneTotale = produzioneF1 + produzioneF2 + produzioneF3;
       double consumiTotali = consumiF1 + consumiF2 + consumiF3;
       double immissioniTotali = immissioniF1 + immissioniF2 + immissioniF3;
       
       ArchivioMisurazioni.aggiungiValoreInArchivio(id, d, invert, produzioneTotale, consumiTotali, immissioniTotali, parametri);
       
       if(parametri.listaAnni[parametri.listaAnni.length - 1] < d.getYear()){
           int[] anni = new int[parametri.listaAnni.length+1];
           for(int i = 0; i < anni.length; i++){
               if(i == anni.length-1)
                   anni[i] = d.getYear();
               else
                   anni[i] = parametri.listaAnni[i];
           }
           parametri.listaAnni = anni;
           parametri.numeroAnni = parametri.numeroAnni + 1;
           parametri.serializzaParametri();
           parametri.deserializzaParametri();
           inizializzaMenuAnni(parametri);
       }
       
       importaDatiUtenteAnno();
   }
   
   private void caricaDatiGraficoProduzione(){
       graficoMisurazioni.aggiornaGraficoMisurazioni(differenzaValori, TipoGrafico.Produzione);
       scene.getStylesheets().clear();
       scene.getStylesheets().add("istogrammaProduzione.css");
   }
   
   private void caricaDatiGraficoConsumi(){
       graficoMisurazioni.aggiornaGraficoMisurazioni(differenzaValori, TipoGrafico.Consumi);
       scene.getStylesheets().clear();
       scene.getStylesheets().add("istogrammaConsumi.css");
   }
   
   private void caricaDatiGraficoImmissioni(){
       graficoMisurazioni.aggiornaGraficoMisurazioni(differenzaValori, TipoGrafico.Immissioni);
       scene.getStylesheets().clear();
       scene.getStylesheets().add("istogrammaImmissioni.css");
   }
   
   private void modificaCssIstogramma(String color, String file){
        String x="";
        for(int i = 0; i < 12; i++)
            x += ".data"+i+".chart-bar {-fx-background-color: "+color+"; }\n ";
        try{
            Files.write(Paths.get("./src/"+file), x.getBytes());
        }catch(IOException e){
            System.out.println("Errore settaggio colore grafico..");
        }
    }
   
   private void righeVisualizzabiliTabella(int numero){
       double numeroCalc = numero*25 +20;
       tabellaMisurazioni.setMaxHeight(numeroCalc);
   }
   
    void importaDatiUtenteAnno(){
       if(idUtenteField.getText().equals("") || annoField.getText().equals(""))
           return;
       
       String id = idUtenteField.getText();
       int anno = Integer.parseInt(annoField.getText());
       List<Misurazione> lista = ArchivioMisurazioni.importaMisurazioni(parametri.IPDB, parametri.portaDB, id, anno, parametri.numeroUltimeMisurazioni);
      
       if(lista.size() == 1){
           System.out.println("Hai inserito una sola misurazione!!! Ci vogliono almeno due misurazione per aggiornare il grafico e la tabella");
           System.out.println("Dati della prima misurazione:");
           System.out.println("ID UTENTE: "+lista.get(0).getIdUtente());
           System.out.println("DATA: "+lista.get(0).getData());
           System.out.println("INVERTER: "+lista.get(0).getLetturaInverter());
           System.out.println("PRODUZIONE: "+lista.get(0).getProduzione());
           System.out.println("CONSUMI: "+lista.get(0).getConsumi());
           System.out.println("IMMISSIONI: "+lista.get(0).getImmissioni());
           return;
       }
       differenzaValori = calcolaDifferenza(lista); 
       tabellaMisurazioni.aggiornaListaMisurazioni(differenzaValori);
       graficoMisurazioni.aggiornaGraficoMisurazioni(differenzaValori, TipoGrafico.Produzione);
       
       scene.getStylesheets().clear();
       scene.getStylesheets().add("istogrammaProduzione.css");
   }
   
   private List<Misurazione> calcolaDifferenza(List<Misurazione> lista){
       List<Misurazione> dif = new ArrayList<>();
       for(int i = 0; i <(lista.size()-1); i++){
           double deltaProduzione = lista.get(i).getProduzione() - lista.get(i+1).getProduzione();
           double deltaConsumi = lista.get(i).getConsumi() - lista.get(i+1).getConsumi();
           double deltaImmissioni = lista.get(i).getImmissioni() - lista.get(i+1).getImmissioni();
           dif.add(new Misurazione(lista.get(i).getIdUtente(), lista.get(i).getData(), lista.get(i).getLetturaInverter(), deltaProduzione, deltaConsumi, deltaImmissioni));
       }
       
       return dif;
   }
   
   private void inizializzaMenuAnni(ParametriDiConfigurazione parametri){
       opzioniAnni = new ArrayList<>();
       annoField.getItems().clear();
       for(int i = 0; i < parametri.numeroAnni; i++){
           opzioniAnni.add(new MenuItem(Integer.toString(parametri.listaAnni[i])));
           annoField.getItems().addAll(opzioniAnni.get(i));
           final String testo = opzioniAnni.get(i).getText();
           
           opzioniAnni.get(i).setOnAction((ActionEvent ev)->{annoField.setText(testo);}); //faccio mostrare la voce selezionata
           
       }
   }
   
   private void inizializzaLabel(){
       title = new Label("Monitoraggio Pannelli Solari");
       idUtenteLabel = new Label("ID Utente");
       dataLabel = new Label("Data Misurazione");
       annoLabel = new Label("Anno: ");
       letturaInverterLabel = new Label("Lettura Inverter Mensile(KW)");
       contatoreProduzioneLabel = new Label("Contatore Produzione (KW)");
       produzioneF1Label = new Label("F1");
       produzioneF2Label = new Label("F2");
       produzioneF3Label = new Label("F3");
       contatoreConsumiLabel = new Label("Contatore Consumi (KW)");
       consumiF1Label = new Label("F1");
       consumiF2Label = new Label("F2");
       consumiF3Label = new Label("F3");
       
       contatoreImmissioniLabel = new Label("Contatore Immissioni (KW)");
       immissioniF1Label = new Label("F1");
       immissioniF2Label = new Label("F2");
       immissioniF3Label = new Label("F3");
       ultimeMisurazioniLabel = new Label("Ultime Misurazioni:");
   }
   
   private void inizializzaButton(){
       aggiungiButton = new Button("Aggiungi");
       annullaButton = new Button("Annulla");
       eliminaRigaButton = new Button("Elimina Riga");
       importaDatiButton = new Button("Importa Dati");
       produzioneButton = new Button("Produzione");
       consumiButton = new Button("Consumi");
       immissioniButton = new Button("Immissioni");
   }
   
   private void inizializzaField(){
       idUtenteField = new TextField();
       dataField = new DatePicker();
       annoField = new MenuButton("");
       letturaInverterField = new TextField();
       produzioneF1Field = new TextField();
       produzioneF2Field = new TextField();
       produzioneF3Field = new TextField();
       consumiF1Field = new TextField();
       consumiF2Field = new TextField();
       consumiF3Field = new TextField();
       immissioniF1Field = new TextField();
       immissioniF2Field = new TextField();
       immissioniF3Field = new TextField();
   }
   
   private void inizializzaInterfacciaGrafica(){
       
       inizializzaLabel();
       
       inizializzaButton();
       
       inizializzaField();

       tabellaMisurazioni = new TabellaUltimeMisurazioni();
   }
   
   private void inizializzaStyleImportaDati(){
       title.setLayoutX(300);
       title.setLayoutY(10);
       title.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
       idUtenteLabel.setLayoutX(10);
       idUtenteLabel.setLayoutY(35);
       idUtenteLabel.setStyle("-fx-font-size: 16px;");
       idUtenteField.setMaxWidth(150);
       idUtenteField.setLayoutY(55);
       idUtenteField.setLayoutX(10);
       annoLabel.setStyle("-fx-font-size: 16px;");
       annoLabel.setLayoutY(55);
       annoLabel.setLayoutX(195);
       annoField.setLayoutY(55);
       annoField.setLayoutX(245);
       importaDatiButton.setLayoutX(320);
       importaDatiButton.setLayoutY(55);
       
   }
   
   private void inizializzaStyleData(){
       dataLabel.setStyle("-fx-font-size: 16px;");
       dataLabel.setLayoutX(10);
       dataLabel.setLayoutY(90);
       dataField.setLayoutY(110);
       dataField.setLayoutX(10);
       dataField.setMaxWidth(120);
       
   }
   
   private void inizializzaStyleLetturaInverter(){
       letturaInverterLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
       letturaInverterLabel.setLayoutX(30);
       letturaInverterLabel.setLayoutY(140);
        
       letturaInverterField.setLayoutX(30);
       letturaInverterField.setLayoutY(162);
   }
   
   private void inizializzaStyleContatoreProduzione(){
       contatoreProduzioneLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: green");
       contatoreProduzioneLabel.setLayoutY(200);
       contatoreProduzioneLabel.setLayoutX(30);
       
       produzioneF1Label.setStyle("-fx-font-size: 16px;");
       produzioneF1Label.setLayoutX(40);
       produzioneF1Label.setLayoutY(230);
       produzioneF1Field.setLayoutX(20);
       produzioneF1Field.setLayoutY(250);
       produzioneF1Field.setMaxWidth(62);
       produzioneF1Field.setStyle("-fx-border-width:2px; -fx-border-color: green; -fx-border-radius: 5px");
       
       produzioneF2Label.setStyle("-fx-font-size: 16px;");
       produzioneF2Label.setLayoutX(122);
       produzioneF2Label.setLayoutY(230);
       produzioneF2Field.setLayoutX(100);
       produzioneF2Field.setLayoutY(250);
       produzioneF2Field.setMaxWidth(62);
       produzioneF2Field.setStyle("-fx-border-width:2px; -fx-border-color: green; -fx-border-radius: 5px");
       
       produzioneF3Label.setStyle("-fx-font-size: 16px;");
       produzioneF3Label.setLayoutX(200);
       produzioneF3Label.setLayoutY(230);
       produzioneF3Field.setLayoutX(180);
       produzioneF3Field.setLayoutY(250);
       produzioneF3Field.setMaxWidth(62);
       produzioneF3Field.setStyle("-fx-border-width:2px; -fx-border-color: green; -fx-border-radius: 5px");
   }
   
   private void inizializzaStyleContatoreConsumi(){
       contatoreConsumiLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: red");
       contatoreConsumiLabel.setLayoutX(30);
       contatoreConsumiLabel.setLayoutY(290); 
       
       consumiF1Label.setStyle("-fx-font-size: 16px;");
       consumiF1Label.setLayoutX(40);
       consumiF1Label.setLayoutY(320);
       consumiF1Field.setLayoutX(20);
       consumiF1Field.setLayoutY(340);
       consumiF1Field.setMaxWidth(62);
       consumiF1Field.setStyle("-fx-border-width:2px; -fx-border-color: red; -fx-border-radius: 5px");
       
       consumiF2Label.setStyle("-fx-font-size: 16px;");
       consumiF2Label.setLayoutX(122);
       consumiF2Label.setLayoutY(320);
       consumiF2Field.setLayoutX(100);
       consumiF2Field.setLayoutY(340);
       consumiF2Field.setMaxWidth(62);
       consumiF2Field.setStyle("-fx-border-width:2px; -fx-border-color: red; -fx-border-radius: 5px");
       
       consumiF3Label.setStyle("-fx-font-size: 16px;");
       consumiF3Label.setLayoutX(200);
       consumiF3Label.setLayoutY(320);
       consumiF3Field.setLayoutX(180);
       consumiF3Field.setLayoutY(340);
       consumiF3Field.setMaxWidth(62);
       consumiF3Field.setStyle("-fx-border-width:2px; -fx-border-color: red; -fx-border-radius: 5px");
   }
   
   private void inizializzaStyleContatoreImmissioni(){
       contatoreImmissioniLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: blue");
       contatoreImmissioniLabel.setLayoutX(30);
       contatoreImmissioniLabel.setLayoutY(380);
       
       immissioniF1Label.setStyle("-fx-font-size: 16px;");
       immissioniF1Label.setLayoutX(40);
       immissioniF1Label.setLayoutY(410);
       immissioniF1Field.setLayoutX(20);
       immissioniF1Field.setStyle("-fx-border-width:2px; -fx-border-color: blue; -fx-border-radius: 5px");
       immissioniF1Field.setLayoutY(430);
       immissioniF1Field.setMaxWidth(62);
       
       immissioniF2Label.setStyle("-fx-font-size: 16px;");
       immissioniF2Label.setLayoutX(122);
       immissioniF2Label.setLayoutY(410);
       immissioniF2Field.setLayoutX(100);
       immissioniF2Field.setLayoutY(430);
       immissioniF2Field.setMaxWidth(62);
       immissioniF2Field.setStyle("-fx-border-width:2px; -fx-border-color: blue; -fx-border-radius: 5px");
       
       immissioniF3Label.setStyle("-fx-font-size: 16px;");
       immissioniF3Label.setLayoutX(200);
       immissioniF3Label.setLayoutY(410);
       immissioniF3Field.setLayoutX(180);
       immissioniF3Field.setLayoutY(430);
       immissioniF3Field.setMaxWidth(62);
       immissioniF3Field.setStyle("-fx-border-width:2px; -fx-border-color: blue; -fx-border-radius: 5px");
   }
   
   private void inizializzaStyleBottoni(){
       aggiungiButton.setLayoutX(30);
       aggiungiButton.setLayoutY(480);
       aggiungiButton.setMinWidth(150);
       aggiungiButton.setStyle("-fx-font-size: 18px; -fx-base: #f4cc80; -fx-fill-text: white");
       
       
       
       annullaButton.setLayoutX(30);
       annullaButton.setLayoutY(530);
       annullaButton.setStyle("-fx-font-size: 18px; -fx-base: #f4cc80;");
       annullaButton.setMinWidth(150);
       
       eliminaRigaButton.setStyle("-fx-font-size: 18px; -fx-base: #636369; -fx-fill-text: white");
       eliminaRigaButton.setLayoutY(505);
       eliminaRigaButton.setLayoutX(190);
       
       produzioneButton.setLayoutX(850);
       produzioneButton.setLayoutY(100);
       produzioneButton.setMinWidth(80);
       produzioneButton.setStyle("-fx-base: #9bff94");
       
       consumiButton.setLayoutX(850);
       consumiButton.setLayoutY(150);
       consumiButton.setMinWidth(80);
       consumiButton.setStyle("-fx-base: #ff9494");
       
       immissioniButton.setLayoutX(850);
       immissioniButton.setLayoutY(200);
       immissioniButton.setMinWidth(80);
       immissioniButton.setStyle("-fx-base: #94a6ff");
   }
   
   private void inizializzaStyle(){
       
       inizializzaStyleImportaDati();
       inizializzaStyleData();
       inizializzaStyleLetturaInverter();
       inizializzaStyleContatoreProduzione();
       inizializzaStyleContatoreConsumi();
       inizializzaStyleContatoreImmissioni();
       inizializzaStyleBottoni();

       graficoMisurazioni.istogramma.setLayoutX(350);
       graficoMisurazioni.istogramma.setLayoutY(50);
       
       ultimeMisurazioniLabel.setLayoutX(400);
       ultimeMisurazioniLabel.setLayoutY(430);
       ultimeMisurazioniLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
       
       tabellaMisurazioni.setLayoutX(400);
       tabellaMisurazioni.setLayoutY(460);
       tabellaMisurazioni.setMinWidth(520);
       for(int i = 0; i < 6; i++)
        tabellaMisurazioni.getVisibleLeafColumn(i).setStyle("-fx-alignment: center");
   }
   
   
}

/*
    Fonti utilizzate per implementazione Menu, datapicker, selezione riga tabella e modifica style bottoni
        https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/DatePicker.html //datapicker
        http://tutorials.jenkov.com/javafx/menubutton.html // menu
        https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/SelectionModel.html //riga tabella
        https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/FocusModel.html //riga tabella
        https://www.programcreek.com/java-api-examples/?class=javafx.scene.control.Button&method=setStyle // style bottoni
*/