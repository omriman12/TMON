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
     String query =
             "select \n" +
                    "start_time call_date,\n" +
                    "s.vendor_in owner_in,\n" +
                    "s.trunk_in,\n" +
                    "s.vendor_out owner_out,\n" +
                    "s.trunk_out,\n" +
                    "round(s.call_duration,2) call_duration,\n" +
                    "round(s.call_duration/decode(s.call_count_s,0,1,s.call_count_s),2) acd,\n" +
                    "round(s.call_count_s/decode(s.call_count_s + s.call_count_u,0,1, s.call_count_s + s.call_count_u)*100,2) asr,\n" +
                    "s.call_count_s calls\n" +
              "from stat_stream_in_trunk s\n" +
              "where to_char(s.start_time, 'yyyy/mm/dd hh24') > to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP) -1 -(1/24), 'yyyy/mm/dd hh24') \n" +
                    "and s.call_count_s != 0 and s.country_code = ? and s.operator_prefix = ? and s.vendor_in = ?\n" +
              "order by start_time desc";
     
     
             /*
        "select stat_time       call_date,\n" +
        "       v1.vendor_name  owner_in,\n" + 
        "       trunk_in        ,\n" + 
        "       v2.vendor_name  owner_out,\n" + 
        "       trunk_out       ,\n" + 
        "       acd             ,\n" + 
        "       asr             ,\n" + 
        "       call_count      calls \n" + 
        "from stat_acd_asr_hourly_trunk s join vendor v1 on s.owner_in_id = v1.id\n" +
        "     join vendor v2 on s.owner_out_id = v2.id\n" + 
        "     join countries c on s.country_code = c.id\n" + 
        "where  c.country = ? and operator_desc = ? and\n" + 
        "      stat_time > SYS_EXTRACT_UTC(SYSTIMESTAMP) - 1 - (1/24)\n" + 
        "      order by 1,2,3,4,5";
        */

            
     
     System.out.println("country_code" +request.getParameter("country_code") + ", desc" +request.getParameter("operator_prefix") +",owner_in:" + request.getParameter("owner_in"));
        
     DataBase db = new DataBase();
    Connection conn = db.getConnection();
    
     PreparedStatement s = conn.prepareStatement(query);
     s.setString(1, request.getParameter("country_code"));
     s.setString(2, request.getParameter("operator_prefix"));
     s.setString(3, request.getParameter("owner_in"));
     
  
%>{
    data: [
<%
    com.telzar.tools.JsonTool.GenerateJSON(s, out, "\t\t");
    s.close();
    conn.close();
%>
    ]
}