
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.sql.Date;


/**
 *
 * @author Leo
 */
public class ArchivioMisurazioni {
    
    
    public static List<Misurazione> importaMisurazioni(String ip, int porta, String id, int anno, int limite){
        List<Misurazione> misurazioni = new ArrayList<>();
        try(Connection co = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porta+"/monitoraggiopannellisolari", "root", "");
            PreparedStatement ps = co.prepareStatement("SELECT * "
                    + "                                 FROM valorimonitoraggio "
                    + "                                 WHERE year(data) = ? and idUtente = ? OR (year(data)= (? - 1) and month(data) = 12 and idUtente = ?) "
                    + "                                 ORDER BY data DESC"
                    + "                                 LIMIT ?");
                )
        {
            ps.setInt(1, anno);
            ps.setString(2, id);
            ps.setInt(3, anno);
            ps.setString(4, id);
            ps.setInt(5, limite);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                
                misurazioni.add(new Misurazione(rs.getString("idUtente"),
                        LocalDate.parse(rs.getString("data"), DateTimeFormatter.ofPattern("yyyy-MM-dd")), rs.getDouble("letturaInverter"),
                        rs.getDouble("produzione"), rs.getDouble("consumi"), rs.getDouble("immissioni")));
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return misurazioni;
    }
    
    public static void aggiungiValoreInArchivio(String id, LocalDate d, double invert, double prod, double cons, double imm, ParametriDiConfigurazione parametri){
        Date data = Date.valueOf(d);
        try(Connection co = DriverManager.getConnection("jdbc:mysql://"+parametri.IPDB+":"+parametri.portaDB+"/monitoraggiopannellisolari", "root", "");
            PreparedStatement ps = co.prepareStatement("INSERT INTO valorimonitoraggio VALUES(?, ?, ?, ?, ?, ?)");
            ){
                ps.setString(1, id);
                ps.setDate(2, data);
                ps.setDouble(3, invert);
                ps.setDouble(4, prod);
                ps.setDouble(5, cons);
                ps.setDouble(6, imm);
                System.out.println("rows effect: "+ ps.executeUpdate());
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
    }
    
    public static void eliminaMisurazione(ParametriDiConfigurazione parametri, Misurazione eliminaMisurazione){
        try(Connection co = DriverManager.getConnection("jdbc:mysql://"+parametri.IPDB+":"+parametri.portaDB+"/monitoraggiopannelliSolari", "root", "");
                PreparedStatement ps = co.prepareStatement("DELETE FROM valorimonitoraggio WHERE idUtente = ? and Data = ?"))
        {
            ps.setString(1, eliminaMisurazione.getIdUtente());
            Date d = Date.valueOf(eliminaMisurazione.getData());
            ps.setDate(2, d);
            System.out.println("rows effect: "+ ps.executeUpdate());
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }
}
