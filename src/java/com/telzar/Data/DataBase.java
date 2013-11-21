/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telzar.Data;

import java.sql.CallableStatement;
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

    
    public  Connection getConnection() throws NamingException, SQLException{
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
    
    public  Connection getSwitchConnection() throws NamingException, SQLException{
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
    
    private  Connection openConnection(String sql_server, String User , String Pass) {
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
    
    public  void closeStatmentandRS(ResultSet rs, Statement ps){
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
    
    public void saveBidding(String gridVars){
        System.out.println("inside saveBidding: " + gridVars);
        PreparedStatement ps = null;
        try{
            String sb = "insert into tmp_tmon_bidding"
                    + "( bid_date, bid_id, bid_vendor_id, bid_quality, country_code , "
                    +   "prefix, vendor_id, billing, currency ,buy_rate, sell_rate, rate_date ,quality, priority, weight, status ) "
                    + "select ?, ?, v2.id, ? , c.id, ?, v3.id, ?, ?, ?, ?, ?, ?, ?, ?, ?  from countries c , "
                    + "(select v.id from vendor v where v.vendor_name = ?) v2 , "
                    + "(select v.id from vendor v where v.vendor_name = ?) v3 "
                    + "where c.country = ?";

            Connection conn = getConnection();

            ps = conn.prepareStatement(sb.toString());
            java.sql.Timestamp dataTimeNow = new java.sql.Timestamp(new Date().getTime());
//            int lastBidId = getLastBidId();
            String[] columnSplit;
            String[] rowSplit = gridVars.split(";");
            for (int i=0; i< rowSplit.length; i++){
                ps.setTimestamp(1, dataTimeNow);
                columnSplit = rowSplit[i].split(",");
                for (int j=0; j<columnSplit.length; j++){ 
                    if(columnSplit[j].equals("1\\1")) // java built-in
                        columnSplit[j] = "1/1";
                    ps.setString(j+2, columnSplit[j]);
//                    System.out.println(j + ":" + columnSplit[j]);
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            closeStatmentandRS(null, ps);
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
            closeStatmentandRS(rs, stmt);
        }
        return 999; //not found
    }
    
    public void startBidDbActions(){
        System.out.println("inside startBidDbActions");
        CallableStatement callStmt = null;
        
        try{
            Connection conn = getConnection();
            callStmt = conn.prepareCall("begin PKG_TMON.processTmonBidding(?);end;");
            callStmt.setString(1, "stam"); //
            callStmt.execute();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            closeStatmentandRS(null, callStmt);
        }
    }
    
    public void addToRates(int bidId){
        System.out.println("inside addToRates");
        CallableStatement callStmt = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            Connection con = getConnection();
            callStmt = con.prepareCall("begin add_telzar_rate(?,?,?,?,?,?,?,?,?,?,?);end;");
            
            String query = "select rcx.quality, t.bid_vendor_id,0, 'IN', c.country , r.operator_desc, t.prefix, t.sell_rate, t.currency, trunc(sysdate), 'yyyy-mm-dd'\n" +
                            " from tmon_bidding t \n" +
                            " join countries c on c.id = t.country_code\n" +
                            " join rate_config_xls rcx on t.vendor_id = rcx.vendor_id and t.quality = rcx.rate_desc\n" +
                            " join rates r on r.vendor_id = t.vendor_id and r.country_code = t.country_code and r.prefix = t.prefix and r.rate = t.buy_rate and r.quality = rcx.quality and r.direction = rcx.direction\n" +
                            " where t.bid_id = ? ";
            ps = con.prepareStatement(query);
            ps.setInt(1, bidId);
            rs = ps.executeQuery();

            int i=1;
            while(rs.next()){
               for (int j=1; j<=rs.getMetaData().getColumnCount(); j++){ 
                    if(j == 10){
                        callStmt.setString(j, rs.getString(j).substring(0,10));
                    }
                    else{
                        callStmt.setString(j, rs.getString(j));
                    }    
               }
               for(int j=rs.getMetaData().getColumnCount() + 1;j<= rs.getMetaData().getColumnCount() + 5;j++){
                   callStmt.registerOutParameter(j, oracle.jdbc.OracleTypes.VARCHAR);
               }
               
                i++;
            }
            
            callStmt.execute();
            
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            closeStatmentandRS( rs, ps);
        }
    }
    
    
}
