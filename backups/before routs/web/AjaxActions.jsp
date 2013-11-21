<%@page trimDirectiveWhitespaces="true"%>
<%@page contentType="text/json" pageEncoding="UTF-8"%>
<%@page import="java.sql.PreparedStatement" %>
<%@page import="java.sql.CallableStatement" %>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.Context"%>
<%@page import="com.dbinteg.Data.DataBase"%>
<%@page import="com.telzar.tools.ExcelTool"%>
<%@page import="com.telzar.tools.EmailTool"%>
<%
    
    System.out.println("inside AjaxActions");
    String action = request.getParameter("action");
    String gridVars = request.getParameter("grid_vars");
    System.out.println("action: " + action);
    System.out.println("grid_vars " + request.getParameter("grid_vars"));
    
    PreparedStatement ps = null;
    Statement stmt = null;
    EmailTool emailTool;
    ExcelTool excelTool;
    
    
    if(action.equals("add_to_bid")){
        System.out.println("Ajax recieved: add_to_bid ");
        try{
            gridVars = request.getParameter("grid_vars");
            String sb = "insert into tmp_tmon " +
                       "(vendor, country_code, operator_prefix, currency, buy_rate, quality) " +
                       "values  " +
                       "(?, ?, ?, ? ,?, ?)";

            Connection conn = DataBase.getConnection();
            ps = conn.prepareStatement(sb.toString());
            String[] columnSplit;
            String[] rowSplit = gridVars.split(";");
            for (int i=0; i< rowSplit.length; i++){
                columnSplit = rowSplit[i].split(",");
                for (int j=0; j<columnSplit.length; j++){ 
                    ps.setString(j+1, columnSplit[j]);
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
    
    
    else if (action.equals("reset_temp_bid")){ 
        System.out.println("Ajax recieved: reset_temp_bid ");
        try{
            Connection conn = DataBase.getConnection();
            stmt = conn.createStatement(); 
            stmt.executeQuery("delete from tmp_tmon");

        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            DataBase.closeStatmentandRS(null, stmt);
        }
     } 
    
             
     else if (action.equals("create_and_send_excel")){ 
        gridVars = request.getParameter("grid_vars");
        System.out.println("Ajax recieved: create_and_send_pdf: " + gridVars);
        String excelGridVars = "Country Code, Prefix, Vendor, Currency, Rate, Quality;" + gridVars; 

        String path = "/tmp/";
//        String path = "C:\\app\\";
        excelTool = new ExcelTool();
        String fileName = excelTool.createExel(path,excelGridVars);

        if (!fileName.equals("fail")){
            emailTool = new EmailTool();
            String emailTo = "omri@telzar.co.il";
            emailTool.sendEmail(emailTo ,path , fileName);
        }
        
        DataBase db = new DataBase();
        db.saveBidding(request.getParameter("bidding_operator_prefix"), request.getParameter("bidding_country_code") ,gridVars);
            
     }
         
    else if (action.equals("delete_bid_row")){ // @TODO
        gridVars = request.getParameter("grid_vars");
        System.out.println("Ajax recieved: delete_bid_row - " + gridVars);
        try{
            String sb = "delete from tmp_tmon where vendor = ? and operator = ?";

            Connection conn = DataBase.getConnection();
            stmt = conn.createStatement(); 

            ps = conn.prepareStatement(sb.toString());
            String[] columnSplit;
            String[] rowSplit = gridVars.split(";");
            for (int i=0; i< rowSplit.length; i++){
                columnSplit = rowSplit[i].split(",");
                for (int j=0; j<columnSplit.length; j++){ 
                    ps.setString(j+1, columnSplit[j]);
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
    
    else if (action.equals("approve_bids")){ 
        gridVars = request.getParameter("grid_vars");
        String isApproved = request.getParameter("approved");
        System.out.println("Ajax recieved: approve_bids - " + gridVars);
        String Bids = "";
        String biddingCountryCode = request.getParameter("bidding_country_code");
        String biddingOperatorPrefix = request.getParameter("bidding_operator_prefix");
                
        try{
            String sb = "update TMON_BIDDING set approved = ? where bid_id = ?";
            Connection conn = DataBase.getConnection();
            ps = conn.prepareStatement(sb.toString());
            String[] columnSplit;
            String[] rowSplit = gridVars.split(";");
            for (int i=0; i< rowSplit.length; i++){
                ps.setString(1, isApproved);
                columnSplit = rowSplit[i].split(",");
                for (int j=0; j<columnSplit.length; j++){ 
                    ps.setString(j+2, columnSplit[j]);
                    Bids += columnSplit[j];
                }
                ps.addBatch();
            }
            ps.executeBatch();
            
            if (isApproved.equals("YES")){
                EmailTool et = new EmailTool();
                String emailTo = "omri@telzar.co.il";
                et.sendEmail(emailTo ,Bids ,biddingCountryCode ,biddingOperatorPrefix);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            DataBase.closeStatmentandRS(null, ps);
        }
    }
    
    else if (action.equals("update_bid")){ 
        gridVars = request.getParameter("grid_vars");
        System.out.println("Ajax recieved: update_bid - " + gridVars);
        
        CallableStatement callStmt = null;
        
        try{
            String sb = "update TMON_BIDDING set route = ? where bid_id = ?";

            Connection connBlng = DataBase.getConnection();
            Connection connSwitch =  DataBase.getSwitchConnection();
            
            ps = connBlng.prepareStatement(sb.toString());
            String[] columnSplit;
            String[] rowSplit = gridVars.split(";");
            for (int i=0; i< rowSplit.length; i++){
                columnSplit = rowSplit[i].split(",");
                for (int j=0; j<columnSplit.length; j++){ 
                    ps.setString(j+1, columnSplit[j]);
                    System.out.println(Integer.parseInt(columnSplit[j]));
//                    if (j==0) {
//                        try{
//                            callStmt = connSwitch.prepareCall("begin api_re_policy_rule.SetNodeAdminState(null,null,?,null,?);end;");
//                            callStmt.setInt(1, Integer.parseInt(columnSplit[j]));
//                            callStmt.setString(2, "ACTIVATED");
//                            callStmt.execute();
//                        }
//                        catch(Exception e1){
//                            e1.printStackTrace();
//                        }
//                    }
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
    
    
%>{
    data: [
<%
    
%>
    ]
}