import com.mysql.cj.protocol.Resultset;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class TableCommunicator extends DatabaseCommunicator{
    
    public void displayDbToTable(JTable table, String tableName, String[] attributes){
        String sql = "SELECT "; 
        
        int obs = 0;
        
        for(String attribute : attributes){
            
            if(obs != attributes.length - 1)
                sql += attribute + ", ";
            else
                sql += attribute;
            
            obs++;
        }
        
        sql += " FROM " + tableName;
        
        connectToDb();
        
        try{
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            DefaultTableModel model = (DefaultTableModel)table.getModel();
            model.setRowCount(0);
            while(rs.next()){
                String[] results = new String[attributes.length];
                
                int obs1 = 0;
                for(String attribute: attributes){   
                    results[obs1] = rs.getString(obs1 + 1);
                    obs1 ++;
                }
                
                model.addRow(results);
            }
        }catch(SQLException ex){
            System.out.println(ex);
        }
    }
    
    public void displayDbToTable(JTable table, String[] tableAttributes,ResultSet result){   
        connectToDb();
        
        try{
            
            DefaultTableModel model = (DefaultTableModel)table.getModel();
            model.setRowCount(0);
            
            while(result.next()){
                String[] results = new String[tableAttributes.length];
                
                int obs1 = 0;
                for(String attribute: tableAttributes){   
                    results[obs1] = result.getString(obs1 + 1);
                    obs1 ++;
                }
                
                model.addRow(results);
            }
        }catch(SQLException ex){
            System.out.println(ex);
        }
    }
    
    public static String getSelectedRowId(JTable table){
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        int row = table.getSelectedRow();
        String searchId = model.getValueAt(row, 0).toString();
        return searchId;
    }
}
