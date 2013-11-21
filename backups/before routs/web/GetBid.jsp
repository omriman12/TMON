<%@page trimDirectiveWhitespaces="true"%>
<%@page contentType="text/json" pageEncoding="UTF-8"%>
<%@page import="java.sql.PreparedStatement" %>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.Context"%>
<%@page import="com.dbinteg.Data.DataBase"%>
<%
    System.out.println("inside GetBid.jsp, bid_id=" + request.getParameter("bid_id"));
    
    
    String query = 
            "select \n" +
                    "bid_date rate_date,\n" +
                    "country_code,\n" +
                    "prefix operator_prefix,\n" +
                    "vendor vendor_name,\n" +
                    "currency,\n" +
                    "sell_rate,\n" +
                    "quality \n" +
             "from tmon_bidding  \n" + 
             "where bid_id = ?";
            
    Connection conn = DataBase.getConnection();
    PreparedStatement s = conn.prepareStatement(query);
    s.setString(1, request.getParameter("bid_id"));
    
%>{
    data: [
<%
    com.telzar.tools.JsonTool.GenerateJSON(s, out, "\t\t");
%>
    ]
}