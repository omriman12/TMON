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
    System.out.println("inside GetBiddingRows.jsp");
    
    String query = "Select rate_date, country_code, operator_prefix , vendor vendor_name, currency, buy_rate, quality from tmp_tmon";
            
    Connection conn = DataBase.getConnection();
    PreparedStatement s = conn.prepareStatement(query);
    
%>{
    data: [
<%
    com.telzar.tools.JsonTool.GenerateJSON(s, out, "\t\t");
//    String Json= com.dbinteg.Data.JsonTool.createJsonFromArray(new String[][]{{ "stat_data", "stat_data"},{ "vendor_name", request.getParameter("vendors")},{ "operator_desc", "operator_desc"},{ "currency", "currency"},{ "rate", "12"}});
//    out.write(Json);
%>
    ]
}