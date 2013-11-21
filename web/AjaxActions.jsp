<%@page trimDirectiveWhitespaces="true"%>
<%@page contentType="text/json" pageEncoding="UTF-8"%>
<%@page import="java.sql.PreparedStatement" %>
<%@page import="java.sql.CallableStatement" %>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.util.HashMap"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.Context"%>
<%@page import="com.telzar.Data.DataBase"%>
<%@page import="com.telzar.tools.ExcelTool"%>
<%@page import="com.telzar.tools.EmailTool"%>
<%
    
    System.out.println("inside AjaxActions");
    String action = request.getParameter("action");
    System.out.println("action: " + action);
    
    DataBase db = new DataBase();
    Connection conn = null; 
    PreparedStatement ps = null;
    Statement stmt = null;
    EmailTool emailTool ;
    ExcelTool excelTool;
    
    if(action.equals("add_to_bid")){
        System.out.println("Ajax recieved: add_to_bid ");
        
        
        try{
            String gridVars = request.getParameter("grid_vars");
            String sb = "insert into tmp_tmon " +
                       "(RATE_DATE_STRING, vendor, country_code, operator_prefix, currency, buy_rate, quality, quality_direction, billing) " +
                       "values  " +
                       "(?, ?, ?, ?, ? ,?, ?, ?, ?)";
            
            conn = db.getConnection();
            ps = conn.prepareStatement(sb.toString());
            String[] columnSplit;
            String[] rowSplit = gridVars.split(";");
            for (int i=0; i< rowSplit.length; i++){
                columnSplit = rowSplit[i].split(",");
                for (int j=0; j<columnSplit.length; j++){ 
                    ps.setString(j+1, columnSplit[j]);
                }
                ps.setString(9, "1/1");
                ps.addBatch();
            }
            ps.executeBatch();
            
            stmt = conn.createStatement();
            stmt.execute("update tmp_tmon t set t.country_name = (SELECT c.country FROM countries c WHERE t.country_code = c.id)");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            db.closeStatmentandRS(null, ps);
            db.closeStatmentandRS(null, stmt);
        }
    }
    
    if(action.equals("add_bid_from_history")){
        System.out.println("Ajax recieved: add_bid_from_history ");
        try{
            String bidId = request.getParameter("id");
            String sb = "insert into tmp_tmon (RATE_DATE_STRING, vendor, country_code, country_name, operator_prefix, currency, buy_rate, sell_rate, quality, billing, priority, weight, status ) "  
                      + "select t.rate_date, v2.vendor_name , t.country_code, c.country, t.prefix, t.currency, t.buy_rate, t.sell_rate, t.quality, t.billing, t.priority, t.weight, 'NO CHANGE' "
                      + "from TMON_BIDDING t " 
                      + "join vendor v2 on t.vendor_id = v2.id " 
                      + "join countries c on t.country_code = c.id " 
                      +        "where t.bid_id = ? "
                      +        "and t.status != ? ";

            conn = db.getConnection();
            ps = conn.prepareStatement(sb.toString());
            ps.setString(1, bidId);
            ps.setString(2, "CLOSE");
            ps.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            db.closeStatmentandRS(null, ps);
            db.closeStatmentandRS(null, stmt);
        }
    }
    
    else if (action.equals("reset_temp_bid")){ 
        System.out.println("Ajax recieved: reset_temp_bid ");
        try{
            conn = db.getConnection();
            stmt = conn.createStatement(); 
            stmt.executeQuery("delete from tmp_tmon");

        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            db.closeStatmentandRS(null, stmt);
        }
     } 
    
             
     else if (action.equals("create_and_send_first_excel")){ 
////        String bidId = request.getParameter("bid_id");
////        String buyingVendor = request.getParameter("buying_vendor");
////        String buyingVendorQuality = request.getParameter("bid_id");
////        String gridVarsFor = request.getParameter("grid_vars");
//        System.out.println("Ajax recieved: create_and_send_pdf: ");// + gridVarsFor);
////        System.out.println("bidId:" + bidId);
//                
////        DataBase db = new DataBase();
////        db.saveBidding(gridVarsFor);
////        
////        db.startBidDbActions(bidId);
//        
//        String path,logoPath, bidEmailTo = "",routeEmailTo = "";
//        Boolean Debug = false;
//        
//        if (Debug){
//            path = "C:\\app\\";
//            logoPath = "c:\\app\\019.png";
//        }
//        else{
//            path = "/tmp/";
//            logoPath = "/var/tmon/019.png";
//            bidEmailTo = "shir@telzar.co.il"; //shir
//            routeEmailTo = "shir@telzar.co.il"; //cs
////            db.addToRates(bidId);
//        }
//        
//        excelTool = new ExcelTool();
//        String bidExcel = excelTool.createExelFromTmp(path, logoPath); //, bidId
////        String routeExcel = excelTool.createRouteExel(path, bidId);
//      
////        if (!bidExcel.equals("fail") && !Debug){
////            String title = "TMon Bidding";
////            emailTool = new EmailTool();
////            emailTool.sendEmail(bidEmailTo ,"invoice@telzar-019.co.il", path , bidExcel, title);
////        }
////        
////        if (!routeExcel.equals("fail") && !Debug){
////            String title = "Tmon - Route Changes For Vendor:" + buyingVendor + ", Quality:" + buyingVendorQuality;
////            emailTool = new EmailTool();
////            emailTool.sendEmail(routeEmailTo, "shir@telzar.co.il", path, routeExcel , title);
////        }
     }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//    else if (action.equals("multiple_update_sell_rate")){ 
//        String gridVars = request.getParameter("grid_vars");
//        String sellRate = request.getParameter("sell_rate");
//        String currency = request.getParameter("currency");
//        String priority = request.getParameter("priority");
//        String weight = request.getParameter("weight");
//        String status = request.getParameter("status");
//        String billing = request.getParameter("billing");
//        String rate_date = request.getParameter("rate_date");
//        
//        System.out.println("Ajax recieved: multiple_update_sell_rate - " + sellRate + ", " + gridVars);
//
//        CallableStatement callStmt = null;
//
//        try{
//            String sb = "update tmp_tmon set sell_rate = ?, currency = ?, priority = ?, weight = ?, status = ?, rate_date_string = ?, billing = ? where id = ?";
//
//            Connection connBlng = DataBase.getConnection();
////            Connection connSwitch =  DataBase.getSwitchConnection();
//
//            ps = connBlng.prepareStatement(sb.toString());
//            ps.setFloat(1, Float.parseFloat(sellRate));
//            ps.setString(2, currency);
//            ps.setString(3, priority);
//            ps.setString(4, weight);
//            ps.setString(5, status);
//            ps.setString(6, rate_date);
//            ps.setString(7, billing);
//            
//            String[] rowSplit = gridVars.split(";");
//            for (int i=0; i< rowSplit.length; i++){
//                ps.setString(8, rowSplit[i]);
//                ps.addBatch();
//            }
//            ps.executeBatch();
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//        finally{
//            DataBase.closeStatmentandRS(null, ps);
//        }
//     }
//    
//    else if (action.equals("delete_bid_row")){ 
//        try{
//            System.out.println("Ajax recieved: delete_bid_row");
//            String id = request.getParameter("id");
//            
//            String sb = "delete from tmp_tmon t where t.id = ?";
//
//            Connection connBlng = DataBase.getConnection();
//            ps = connBlng.prepareStatement(sb.toString());
//            ps.setInt(1, Integer.parseInt(id));
//            ps.executeUpdate();
//            
//        
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        finally{
//            DataBase.closeStatmentandRS(null, ps);
//        }
//    }
//     
//     
//    
//    else if (action.equals("update_bid")){ 
//        String gridVars = request.getParameter("grid_vars");
//        System.out.println("Ajax recieved: update_bid - " + gridVars);
//        
//        CallableStatement callStmt = null;
//        
//        try{
//            String sb = "update TMON_BIDDING set route = ? where bid_id = ?";
//
//            Connection connBlng = DataBase.getConnection();
////            Connection connSwitch =  DataBase.getSwitchConnection();
//            
//            ps = connBlng.prepareStatement(sb.toString());
//            String[] columnSplit;
//            String[] rowSplit = gridVars.split(";");
//            for (int i=0; i< rowSplit.length; i++){
//                columnSplit = rowSplit[i].split(",");
//                for (int j=0; j<columnSplit.length; j++){ 
//                    ps.setString(j+1, columnSplit[j]);
//                    System.out.println(Integer.parseInt(columnSplit[j]));
////                    if (j==0) {
////                        try{
////                            callStmt = connSwitch.prepareCall("begin api_re_policy_rule.SetNodeAdminState(null,null,?,null,?);end;");
////                            callStmt.setInt(1, Integer.parseInt(columnSplit[j]));
////                            callStmt.setString(2, "ACTIVATED");
////                            callStmt.execute();
////                        }
////                        catch(Exception e1){
////                            e1.printStackTrace();
////                        }
////                    }
//                }
//                ps.addBatch();
//            }
//            ps.executeBatch();
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//        finally{
//            DataBase.closeStatmentandRS(null, ps);
//        }
//    }
    
//    else if (action.equals("add_route_vendor")){ 
//        try{
//            System.out.println("Ajax recieved: add_route_vendor");
//            String routeVendorQuality = request.getParameter("route_quality");
//            String routhCountryCode = request.getParameter("route_country_code");
//            String routhVendor = request.getParameter("route_vendor");
//
//            System.out.println(routeVendorQuality + "," + routhCountryCode + "," + routhVendor);
//            String sb = "insert into tmon_routes_vendors(buying_vendor_id, country_code, buying_vendor_quality, BUYING_QUALITY_DIRECTION) "
//                    + "select v2.id, c.id, rc2.quality, rc2.direction from countries c , "
//                    + "(select v.id from vendor v where v.vendor_name = ?) v2 , "
//                    + "(select rc.quality, rc.direction from rate_config_xls rc join vendor v on v.id = rc.vendor_id where rc.rate_desc = ? and v.vendor_name = ?) rc2 "
//                    + "where c.country = ? and rownum <=1";
//
//            Connection connBlng = DataBase.getConnection();
//            ps = connBlng.prepareStatement(sb.toString());
//            ps.setString(1, routhVendor);
//            ps.setString(2, routeVendorQuality);
//            ps.setString(3, routhVendor);
//            ps.setString(4, routhCountryCode);
//            ps.executeUpdate();
//        
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        finally{
//            DataBase.closeStatmentandRS(null, ps);
//        }
//    }
//    
//    else if (action.equals("remove_route")){ 
//        try{
//            System.out.println("Ajax recieved: remove_route");
//            String id = request.getParameter("id");
//            
//            String sb = "delete from tmon_routes_vendors "
//                    + "where id = ?";
//
//            Connection connBlng = DataBase.getConnection();
//            ps = connBlng.prepareStatement(sb.toString());
//            ps.setInt(1, Integer.parseInt(id));
//            ps.executeUpdate();
//            
//            //secound delete
//            String sb2 = "delete from tmon_routes "
//                    + "where id_routes_vendor = ?";
//
//            ps = connBlng.prepareStatement(sb2.toString());
//            ps.setInt(1, Integer.parseInt(id));
//            ps.executeUpdate();
//        
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        finally{
//            DataBase.closeStatmentandRS(null, ps);
//        }
//    }
//    
//    else if (action.equals("save_route")){ 
//        try{
//            System.out.println("Ajax recieved: save_route");
//            String id = request.getParameter("id");
//            String routeId = request.getParameter("route_id");
//            Connection connBlng = DataBase.getConnection();
//
////            System.out.println(routeCountry + "," + routeBuyingVendor + "," + routhBuyingVendorQuality + "," + routeId);
//            
//            String sb = "update tmon_routes_vendors t set t.route_id = ? "
//                    + "where t.id = ?";
//            
//            ps = connBlng.prepareStatement(sb.toString());
//            ps.setInt(1, Integer.parseInt(routeId));
//            ps.setInt(2, Integer.parseInt(id));
//            ps.executeUpdate();
//            
//            String sb2 = "update tmon_routes t set t.route_id = ? "
//                    + "where id_routes_vendor = ?";
//            ps = connBlng.prepareStatement(sb2.toString());
//            ps.setInt(1, Integer.parseInt(routeId));
//            ps.setInt(2, Integer.parseInt(id));
//            ps.executeUpdate();
//        
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        finally{
//            DataBase.closeStatmentandRS(null, ps);
//        }
//    }
//    
//    else if (action.equals("save_inner_route")){ 
//        try{
//            String gridVars = request.getParameter("grid_vars");
//            System.out.println("Ajax recieved: save_inner_route: " + gridVars);
//            String idRoutesVendor = request.getParameter("id_routes_vendor");
//            Connection connBlng = DataBase.getConnection();
//
//            
//            String sb = "delete from tmon_routes "
//                    + "where ID_ROUTES_VENDOR = ?";
//            
//            ps = connBlng.prepareStatement(sb.toString());
//            ps.setInt(1, Integer.parseInt(idRoutesVendor));
//            ps.executeUpdate();
//            //
//            String sb2 = "insert into tmon_routes " +
//                       "(ID_ROUTES_VENDOR, buying_vendor_id, buying_vendor_quality, country_code, prefix, selling_vendor_id, priority, weight, selling_vendor_quality, route_id, BUYING_QUALITY_DIRECTION, selling_QUALITY_DIRECTION) " +
//                       "select ?, v2.id, rc2.quality, c.id, ?, v3.id, ?, ?, rc3.id ,?, rc.direction, trv3.selling_quality_direction from countries c , " +
//                       "(select v.id from vendor v where v.vendor_name = ?) v2 , " +
//                       "(select rc.quality, rc.direction from rate_config_xls rc join vendor v on v.id = rc.vendor_id where rc.rate_desc = ? and v.vendor_name = ?) rc2, " +
//                       "(select rc.quality, rc.direction from rate_config_xls rc join vendor v on v.id = rc.vendor_id where rc.rate_desc = ? and v.vendor_name = ?) rc3, " +
//                       "(select v.id from vendor v where v.vendor_name = ?) v3, " +
//                       "where c.country = ? and rownum <=1";
//            System.out.println(sb2.toString());
//            
//            ps = connBlng.prepareStatement(sb2.toString());
//            
//            String[] columnSplit;
//            String[] rowSplit = gridVars.split(";");
//           
//             if (!gridVars.equals("")){
//                for (int i=0; i< rowSplit.length; i++){
//                    columnSplit = rowSplit[i].split(",");
//                    for (int j=0; j<columnSplit.length; j++){ 
//                        ps.setString(j+1, columnSplit[j]);
//                    }
//                    ps.addBatch();
//                }
//                ps.executeBatch();
//             }
//        
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        finally{
//            DataBase.closeStatmentandRS(null, ps);
//        }
//    }
//    
//    else if (action.equals("send_route")){ 
//        String gridVars = request.getParameter("grid_vars");
//        String email_grid_vars = "Prefix,Selling Vendor,Priority,Weight,Quality;" + gridVars; 
//        String routhCountryCode = request.getParameter("route_country_code");
//        String routhVendorID = request.getParameter("route_vendor_id");
//        String routhBuyingVendorQuality = request.getParameter("route_buying_vendor_quality");
//        System.out.println("Ajax recieved: send_route " + email_grid_vars);
//        
//        emailTool = new EmailTool();
//        String emailTo = "shir@telzar.co.il";
//        String title = "Tmon - Route Changes For Vendor:" + routhVendorID + ", Country Code:" + routhCountryCode + ", Quality:" + routhBuyingVendorQuality;
//        
//        emailTool.sendRouteEmail(emailTo, title, email_grid_vars);
//     }
    
     
    
    
%>{
    data: [
<%
    
%>
    ]
}