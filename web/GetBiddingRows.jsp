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
    System.out.println("inside GetBiddingRows.jsp");
    
    String query = "select "
                        + "t.id, "
                        + "nvl(tb.bid_id, 0)+1 bid_id, "
                        + "t.rate_date_string rate_date, "
                        + "c.country country_name, "
                        + "t.country_code, "
                        + "t.operator_prefix , "
                        + "t.vendor vendor_name, "
                        + "t.currency, "
                        + "t.buy_rate, "
                        + "t.sell_rate, "
                        + "t.quality, "
                        + "t.quality_direction direction, "
                        + "t.priority, "
                        + "t.weight, "
                        + "t.status, "
                        + "t.billing "
                  + "from tmp_tmon t "
                  + "join countries c on t.country_code = c.id,"
                  + "(select max(bid_id) bid_id from tmon_bidding) tb";
            
    DataBase db = new DataBase();
    Connection conn = db.getConnection();
    PreparedStatement s = conn.prepareStatement(query);
    
%>{
    data: [
<%
    com.telzar.tools.JsonTool.GenerateJSON(s, out, "\t\t");
//    String Json= com.telzar.Data.JsonTool.createJsonFromArray(new String[][]{{ "stat_data", "stat_data"},{ "vendor_name", request.getParameter("vendors")},{ "operator_desc", "operator_desc"},{ "currency", "currency"},{ "rate", "12"}});
//    out.write(Json);
%>
    ]
}