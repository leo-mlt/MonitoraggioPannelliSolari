
import java.util.*;
import javafx.scene.chart.*;


/**
 *
 * @author Leo
 */
public class GraficoMisurazioni {
    private static final String[] mesi = {"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"};
    BarChart<String, Number> istogramma;
    final CategoryAxis xAxis;
    NumberAxis yAxis;

    public GraficoMisurazioni(){
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis(0.0, 1000.0, 50.0);
        istogramma = new BarChart<String, Number>(xAxis, yAxis);
        istogramma.setTitle("Andamento Misurazioni");
        yAxis.setLabel("KW");
        xAxis.setLabel("Anno ");
        
        XYChart.Series series1 = new XYChart.Series();
       
        for(int i = 0; i < mesi.length; i++)
            series1.getData().add(new XYChart.Data(mesi[i], 0));
       
        istogramma.getData().addAll(series1);
        istogramma.setLegendVisible(false);
       
    }
    
    public void aggiornaGraficoMisurazioni(List<Misurazione> lista, TipoGrafico tg){
        XYChart.Series series1 = new XYChart.Series();
        Collections.sort(lista, (s1, s2)->s1.getData().compareTo(s2.getData())); //ordinamento della lista secondo la data dalla più vecchia all più recente

        int i = 0;
        int anno = 0;
        int mesePrecedente = 0;
        while(i < lista.size()){
            
            int valoreMese = lista.get(i).getData().getMonthValue();
            mesePrecedente++;
            if(mesePrecedente != valoreMese){
                series1.getData().add(new XYChart.Data(mesi[mesePrecedente-1], 0));
               
                continue;
            }
            if(i == 0)
                anno = lista.get(i).getData().getYear();
            double valore;
            if(tg == TipoGrafico.Produzione)
                valore = lista.get(i).getProduzione();
            else if(tg == TipoGrafico.Consumi)
                valore = lista.get(i).getConsumi();
            else
                valore = lista.get(i).getImmissioni();
            
            
            series1.getData().add(new XYChart.Data(mesi[valoreMese-1], valore));
            i++;
        }
        i = mesePrecedente;
        if(i != 12){
            
            for(; i < 12; i++){
                series1.getData().add(new XYChart.Data(mesi[i], 0));
            }
        }
        
        istogramma.getData().clear();
        if(tg == TipoGrafico.Produzione)
            istogramma.setTitle("Andamento Misurazioni - Produzione");
        else if(tg == TipoGrafico.Consumi)
            istogramma.setTitle("Andamento Misurazioni - Consumi");
        else
            istogramma.setTitle("Andamento Misurazioni - Immissioni");
        xAxis.setLabel("Anno "+anno);
        istogramma.getData().addAll(series1);
    }
}

/*
Fonti:
    https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/bar-chart.htm#CIHJFHDE //costruzione del grafico
    https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/css-styles.htm#CIHGIAGE modifica colori grafici
*/
