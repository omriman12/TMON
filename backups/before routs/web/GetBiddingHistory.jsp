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
    System.out.println("inside GetBiddingHistory.jsp");
    
    String query = "select bid_id, MIN(bid_date) bid_date, min(approved) bid_approved, min(route) route from tmon_bidding t where bid_operator_country_code = ? and bid_operator_prefix = ? group by bid_id order by bid_id desc";
            
    Connection conn = DataBase.getConnection();
    PreparedStatement s = conn.prepareStatement(query);
    s.setString(1, request.getParameter("bid_operator_country_code"));
    s.setString(2, request.getParameter("bid_operator_prefix"));
    
%>{
    data: [
<%
    com.telzar.tools.JsonTool.GenerateJSON(s, out, "\t\t");
%>
    ]
}