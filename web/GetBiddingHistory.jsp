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
    System.out.println("inside GetBiddingHistory.jsp: ");
    
    String query = "select bid_id, MIN(bid_date) bid_date from tmon_bidding t "
            + "join vendor v2 on t.BID_VENDOR_ID = v2.id " 
            + "where v2.vendor_name = ? "
            + "and bid_quality = ? "
            + "group by bid_id order by bid_id desc";
            
    DataBase db = new DataBase();
    Connection conn = db.getConnection();
    PreparedStatement s = conn.prepareStatement(query);
    s.setString(1, request.getParameter("bid_vendor"));
    s.setString(2, request.getParameter("bid_vendor_quality"));
    
%>{
    data: [
<%
    com.telzar.tools.JsonTool.GenerateJSON(s, out, "\t\t");
%>
    ]
}