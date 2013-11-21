<%@page import="java.sql.PreparedStatement"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@page import="com.telzar.tools.JsonTool"%>
<%@page import="com.telzar.Data.DataBase"%>
<%@page import="java.sql.Connection"%>

<%@page contentType="text/json" pageEncoding="UTF-8"%>
<%
    DataBase db = new DataBase();
    Connection conn = db.getConnection();
    
    System.out.println("inside GetCountryPrefixes.jsp");
    String action = request.getParameter("action");
    String vendor = request.getParameter("vendor");
    String prefix = request.getParameter("prefix");
    String vendorQuality = request.getParameter("vendor_quality");
    String direction = request.getParameter("direction");
    
    System.out.println(action);
    String query = "";
    
    query =
            "select \n" +
                    "s.price_date,\n" +
                    "s.country_code,\n" +
                    "s.prefix,\n" +
                    "s.vendor_name,\n" +
                    "s.rate,\n" +
                    "s.currency,\n" +
                    "rc.rate_desc quality, \n" +
                    "? direction, \n" +
                    "s.duration,\n" +
                    "s.acd,\n" +
                    "s.call_count\n" +
             "from stat_price_report s \n" +
             "join rate_config_xls rc on s.vendor_name = rc.rate_title and to_char(s.quality) = rc.quality "
            + "where rc.direction = ? ";
    
    if (action.equals("prefixSearchExactId")){
             query += "and s.prefix = ? "; 
    }
    else if (action.equals("prefixSearchRangeId")){
        query += "and s.prefix like ? ";
    }
    
    if(!vendor.equals("")){
        query += "and s.vendor_name = ? ";
        if(!vendorQuality.equals(""))
            query +=  "and rc.rate_desc = ? ";
    }
        
    PreparedStatement s = conn.prepareStatement(query);
    s.setString(1, direction);
    s.setString(2, direction);
    
    
    if (action.equals("prefixSearchExactId")){
        s.setString(3, prefix);
    }
    else {
        s.setString(3, prefix + "%");
    }
    
    if(!vendor.equals("")){
        s.setString(4, vendor);
        if(!vendorQuality.equals(""))
            s.setString(5, vendorQuality);
    }
    
//    System.out.println("query:" + query);
    
%>{
    data: [
<%
    JsonTool.GenerateJSON(s, out, "\t\t");
    s.close();
    conn.close();
%>
    ]
}