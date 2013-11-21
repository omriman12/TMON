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
    System.out.println("inside GetRoute.jsp:");
    
    String idRoutesVendor = request.getParameter("id_routes_vendor");
    
    System.out.println(idRoutesVendor);
    
    String query = 
            "select \n" +
                    "t.id_routes_vendor,\n" +
                    "v.vendor_name buying_vendor,\n" +
                    "rc.rate_desc buying_vendor_quality,\n" +
                    "c.country,\n" +
                    "t.prefix,\n" +
                    "v2.vendor_name selling_vendor,\n" +
                    "t.priority,\n" +
                    "t.weight,\n" +
                    "t.selling_vendor_quality, \n" +
                    "t.route_id \n" +
             "from tmon_routes t \n" 
            + "join countries c on t.country_code = c.id "
            + "join vendor v on v.id = t.buying_vendor_id "
            + "join rate_config_xls rc on t.buying_vendor_id = rc.vendor_id and to_char(t.buying_vendor_quality) = rc.quality and rc.direction ='OUT' "
            + "join vendor v2 on v2.id = t.selling_vendor_id "
            + "where t.id_routes_vendor=?";
    
//    System.out.println(query);
            
    DataBase db = new DataBase();
    Connection conn = db.getConnection();
    PreparedStatement s = conn.prepareStatement(query);
    s.setInt(1, Integer.parseInt(idRoutesVendor));
    
%>{
    data: [
<%
    com.telzar.tools.JsonTool.GenerateJSON(s, out, "\t\t");
%>
    ]
}