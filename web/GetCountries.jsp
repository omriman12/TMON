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
    System.out.println("inside GetCountries.jsp");
    
    String query = "select c.country from countries c";
            
    DataBase db = new DataBase();
    Connection conn = db.getConnection();
    PreparedStatement s = conn.prepareStatement(query);
    
%>{
    data: [
<%
    com.telzar.tools.JsonTool.GenerateJSON(s, out, "\t\t");
%>
    ]
}