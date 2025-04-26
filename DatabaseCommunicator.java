import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class DatabaseCommunicator {
    private static String url = "jdbc:mysql://localhost:3306/2b";
    private static String user = "root";
    private static String password = "";
    private static ResultSet results;
    protected static Connection conn;
    
    public static void insertToTable(String tableName,String[] attributes,String[] values){
        connectToDb();
        //statement generation
        String query = "INSERT INTO "+ tableName +"(";
        int obs = 0;
        
        for(String attribute : attributes){
            
            if(obs != attributes.length - 1)
                query += attribute + ", ";
            else
                query += attribute;
            
            obs++;
        }
        
        query += ") VALUES(";
        obs= 0;
        
        for(String attribute: attributes){
            if(obs != attributes.length - 1)
                query += "?, ";
            else
                query += "?)";
            
            obs++;
        }
                
        
        
                /*"INSERT INTO userstable(first_name, middle_name, surname, sex, birth_date, email, contact, address, "
                + "position, assumption_of_office, deduction, password) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";*/
        //replacement of placeholders
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            
            int i = 1;
            for(String value: values){
                if(i != 11)
                    statement.setString(i, value);
                else
                    statement.setInt(i, Integer.parseInt(value));
                
                i++;
            }
         //execution  
            statement.executeUpdate();
           
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void connectToDb(){
        
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection success");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    
    public static void deleteFromTable(String tableName, String identifier ,String searchId){
    try{
            connectToDb();
            
            String query = "DELETE FROM "+ tableName +" where " + identifier + " = ?";

            PreparedStatement prepS = conn.prepareStatement(query);
            prepS.setInt(1, Integer.parseInt(searchId));
            
            prepS.execute();
         
        }catch(SQLException ex){
            System.out.println(ex);
    }}
    
    public static void updateTable(String tableName, String[] attributes, String[] values, String identifier, int uid){
        connectToDb();
        
        String query = "UPDATE " + tableName + " SET ";
        
        int obs = 0;
        for(String attribute : attributes){
            
            if(obs != attributes.length - 1)
                query += attribute + " = ?, ";
            else
                query += attribute + " = ? ";
            
            obs++;
        }
        
        query += "WHERE " + identifier + " = ?";
        
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            
            for(int i = 0; i < attributes.length; i++){
                statement.setString( i + 1, values[i]);
            }
            
            statement.setInt(attributes.length +1, uid);
            
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static ResultSet getFromTable(String tableName, String[] attributes, String conditions){
        try {
            connectToDb();
            String query = "SELECT ";
            int obs = 0;
            
            if(attributes[0] == "*")
                query += "*";
            else if(attributes.length == 1)
                query += attributes[0];
            else
                for(String attribute : attributes){
                    if(obs != attributes.length -1)
                        query += attribute + ", ";
                    else
                        query += attribute;
                    
                    obs++;
                }
            
            query += " FROM " + tableName;
            
            if(conditions != ""){
                query += " WHERE " + conditions;
            }
            
            PreparedStatement statement = conn.prepareStatement(query);
            results = statement.executeQuery();
        } catch (SQLException ex) {  
            Logger.getLogger(DatabaseCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return results;
    }
    
    public static String createConditionForAttributes(String searchId, String[] attributes){
        String conditions = "";
        
        int obs = 0;
        for(String attribute : attributes){
            
            try{
                Integer.parseInt(searchId);
                
                if(obs != attributes.length -1)
                    
                        conditions += attribute + " = " + searchId + " OR ";
                    else
                        conditions += attribute + " = " + searchId;
                    
                    obs++;
            
            }catch(NumberFormatException e){
            
                    if(obs != attributes.length -1)
                        conditions += attribute + " = '" + searchId + "' OR ";
                    else
                        conditions += attribute + " = '" + searchId + "'";
                    
                    obs++;
                }
            }
        return conditions;
    }
}