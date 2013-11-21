/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbinteg.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;
import javax.naming.NamingException;

    
public class DataBase {
    private static final String SWICH_SERVER = "jdbc:oracle:thin:@10.10.200.2:1521:EMSDB01";
    private static final String USER_SWICH = "ipverse";
    private static final String PASS_SWICH = "ipverse";
    //------------------------------------------------------------------------//
    private static final String USER_BLNG = "blng";
    private static final String PASS_BLNG = "blng";
    private static final String BLNG_SERVER = "jdbc:oracle:thin:@10.20.190.2:1521:DBIDB";

    
    public static Connection getConnection() throws NamingException, SQLException{
        Connection connBlng = openConnection(BLNG_SERVER, USER_BLNG, PASS_BLNG);
        try {
            if (connBlng != null)
                connBlng.setAutoCommit(true);
        }
        catch (SQLException e) {
            System.out.println( "Got Exception in StartBlngConnection BLNG_SERVER: " + e);
        }
        return connBlng;
    }
    
    public static Connection getSwitchConnection() throws NamingException, SQLException{
        Connection connSwitch = openConnection(SWICH_SERVER, USER_SWICH, PASS_SWICH);
        try {
            if (connSwitch != null)
                connSwitch.setAutoCommit(true);
        }
        catch (SQLException e) {
            System.out.println( "Got Exception in StartSwitchConnection : " + e);
        }
        return connSwitch;
    }
    
    private static Connection openConnection(String sql_server, String User , String Pass) {
//       System.out.println( requestWrapper.getNpgRequestId() +requestWrapper.getNpgRequestId() +"Opening connection for: " + sql_server);
        Properties properties = new Properties();
        properties.put("user", User);
        properties.put("password", Pass);
        properties.put("characterEncoding", "ISO-8859-1");
        properties.put("useUnicode", "true");
        Connection c = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            c = DriverManager.getConnection(sql_server, properties);
            } catch (SQLException e) {
               System.out.println( "Got SQLException in openConnection : " + e);
            } catch (InstantiationException e) {
               System.out.println( "Got InstantiationException in openConnection : " + e);
            } catch (IllegalAccessException e) {
               System.out.println( "Got IllegalAccessException in openConnection : " + e);
            } catch (ClassNotFoundException e) {
               System.out.println( "Got ClassNotFoundException in openConnection : " + e);
            }
            return c;
    }
    
    public static void closeStatmentandRS(ResultSet rs, Statement ps){
        if (rs!=null)
        {
            try
            {
                rs.close();
            }
            catch(SQLException e)
            {
                System.out.println("Got Exception in closeRSandStatment-rs:" + e);
            }
        }
        if (ps != null)
        {
            try
            {
                ps.close();
            } catch (SQLException e)
            {
                System.out.println("Got Exception in closeRSandStatment-ps:" + e);
            }
        }
    }
    
    public void saveBidding(String biddingOperatorPrefix, String biddingCountryCode, String gridVars){
        System.out.println("inside saveBidding");
        PreparedStatement ps = null;
        try{
            String sb = "insert into tmon_bidding " +
                       "(bid_id, bid_date, bid_operator_country_code , bid_operator_prefix, country_code, prefix, vendor,  currency , sell_rate, quality, approved) " +
                       "values  " +
                       "(?, ?, ?, ?, ?, ?, ? ,?, ?, ?, 'NO')";

            Connection conn = DataBase.getConnection();

            ps = conn.prepareStatement(sb.toString());
            java.sql.Timestamp dataTimeNow = new java.sql.Timestamp(new Date().getTime());
            String[] columnSplit;
            String[] rowSplit = gridVars.split(";");
            for (int i=0; i< rowSplit.length; i++){
                ps.setInt(1, getLastBidId() + 1);
                ps.setTimestamp(2, dataTimeNow);
                ps.setString(3, biddingCountryCode);
                ps.setString(4, biddingOperatorPrefix);
                columnSplit = rowSplit[i].split(",");
                for (int j=0; j<columnSplit.length; j++){ 
                    ps.setString(j+5, columnSplit[j]);
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            DataBase.closeStatmentandRS(null, ps);
        }
    }
    
    public int getLastBidId(){
        System.out.println("inside getLastBidId");
        Statement stmt = null;
        ResultSet rs = null;
        
        try{
            Connection conn = getConnection();
            stmt = conn.createStatement(); 
            rs = stmt.executeQuery("select max(bid_id) from tmon_bidding");
            if(rs.next())
                return rs.getInt(1);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            DataBase.closeStatmentandRS(rs, stmt);
        }
        return 999; //not found
    }
    

}
