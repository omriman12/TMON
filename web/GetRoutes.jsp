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
    System.out.println("inside GetRoutes.jsp: ");
    
            
    String routhQuality = request.getParameter("route_vendor_quality");
    String routhCountryCode = request.getParameter("route_country");
    String routhVendorID = request.getParameter("route_vendor");
    
    System.out.println(routhQuality + "," + routhCountryCode + "," + routhVendorID);
    
    StringBuilder query = new StringBuilder("select t.id, c.country, v.vendor_name buying_vendor, rc.rate_desc buyying_quality, t.route_id from tmon_routes_vendors t "
            + "join countries c on t.country_code = c.id "
            + "join vendor v on v.id = t.buying_vendor_id "
            + "join rate_config_xls rc on t.buying_vendor_id = rc.vendor_id and to_char(t.buying_vendor_quality) = rc.quality and rc.direction ='OUT'");
    if (!routhQuality.equals("") || !routhCountryCode.equals("") || !routhVendorID.equals("")) {
        query.append(" where ");
        if (!routhQuality.equals("")) {
            query.append("rc.rate_desc = ?");
        }
        if (!routhCountryCode.equals("")) {
            if (!routhQuality.equals("")) {
                query.append(" AND ");
            }
            query.append("c.country = ?");
        }
        if (!routhVendorID.equals("")) {
            if (!routhQuality.equals("") || !routhCountryCode.equals("")) {
                query.append(" AND ");
            }
            query.append("v.vendor_name = ?");
        }
    }
    
    DataBase db = new DataBase();
    Connection conn = db.getConnection();
    PreparedStatement s = conn.prepareStatement(query.toString());
    if (!routhQuality.equals("") || !routhCountryCode.equals("") || !routhVendorID.equals("")) {
        int n = 1;
        if (!routhQuality.equals("")) {
            s.setString(n++, routhQuality);
        }
        if (!routhCountryCode.equals("")) {
            s.setString(n++, routhCountryCode);
        }
        if (!routhVendorID.equals("")) {
            s.setString(n, routhVendorID);
        }
    }
    
    System.out.println(query);
    
%>{
    data: [
<%
    com.telzar.tools.JsonTool.GenerateJSON(s, out, "\t\t");
%>
    ]
}