<%@page trimDirectiveWhitespaces="true"%>
<%@page contentType="text/json" pageEncoding="UTF-8"%>
<%@page import="java.sql.PreparedStatement" %>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.Context"%>
<%@page import="com.telzar.Data.DataBase"%>
<%
    System.out.println("inside GetBid.jsp, bid_id=" + request.getParameter("bid_id"));
    
    
    String query = 
            "select \n" +
                    "t.rate_date,\n" +
                    "c.country,\n" +
                    "t.country_code,\n" +
                    "t.prefix  operator_prefix,\n" +
                    "v.vendor_name vendor,\n" +
                    "t.currency,\n" +
                    "t.buy_rate,\n" +
                    "t.sell_rate,\n" +
                    "t.quality, \n" +
                    "t.billing, \n" +
                    "t.priority, \n" +
                    "t.weight, \n" +
                    "t.status \n" +
             "from tmon_bidding t \n" + 
             "join vendor v on t.VENDOR_ID = v.id \n"
            + "join countries c on t.country_code = c.id \n" +
             "where bid_id = ?";
      
    DataBase db = new DataBase();
    Connection conn = db.getConnection();
    PreparedStatement s = conn.prepareStatement(query);
    s.setString(1, request.getParameter("bid_id"));
    
%>{
    data: [
<%
    com.telzar.tools.JsonTool.GenerateJSON(s, out, "\t\t");
%>
    ]
}