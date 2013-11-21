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
    
    String query =
        "select \n" +
            "s1.start_time call_date,\n" +
            "s1.vendor_name trunk_owner,\n" +
            "s1.trunk_out trunk_out,\n" +
            "s1.acd acd_h,\n" +
            "m.acd acd_m,\n" +
            "s1.asr asr_h,\n" +
            "m.asr asr_m,\n" +
            "s1.call_duration mins_h,\n" +
            "m.call_duration mins_m\n" +
         "from\n" +
          "(select \n" +
                "start_time,\n" +
                "s.vendor_name,\n" +
                "s.trunk_out,\n" +
                "round(s.call_duration,2) call_duration,\n" +
                "round(s.call_duration/decode(s.call_count_s,0,1,s.call_count_s),2) acd,\n" +
                "round(s.call_count_s/decode(s.call_count_s + s.call_count_u,0,1, s.call_count_s + s.call_count_u)*100,2) asr,\n" +
                "s.call_count_s + s.call_count_u call_count\n" +
           "from stat_stream_out_trunk s\n" +
                "where to_char(s.start_time, 'yyyy/mm/dd hh24') > to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP) -1 -(1/24), 'yyyy/mm/dd hh24')\n" +
                "and s.country_code = ? and s.operator_prefix = ?) s1\n" +
         "join \n" +
         "(select \n" +
             "a.start_time,\n" +
             "a.vendor_name,\n" +
             "a.trunk_out,\n" +
             "round(a.call_duration,2) call_duration,\n" +
             "round(a.call_duration/decode(a.call_count_s,0,1,a.call_count_s),2) acd,\n" +
             "round(a.call_count_s/decode(a.call_count_s + a.call_count_u,0,1, a.call_count_s + a.call_count_u)*100,2) asr,\n" +
             "a.call_count call_count\n" +
          "from\n" +
             "(select\n" +
                 "to_char(s.start_time, 'hh24') start_time,\n" +
                 "s.vendor_name,\n" +
                 "s.trunk_out,\n" +
                 "round(avg(s.call_duration),2) call_duration,\n" +
                 "round(avg(s.call_count_s),2) call_count_s,\n" +
                 "round(avg(s.call_count_u),2) call_count_u,\n" +
                 "round(avg(s.call_count_s + s.call_count_u),2) call_count\n" +
              "from stat_stream_out_trunk s \n" +
                   "where to_char(s.start_time, 'yyyy/mm/dd hh24') <= to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP)-(1/24), 'yyyy/mm/dd hh24')\n" +
                   "and s.country_code = ? and s.operator_prefix = ?\n" +
                   "group by to_char(s.start_time, 'hh24'),s.vendor_name, s.trunk_out) a\n" +
         ") m\n" +
         "on to_char(s1.start_time, 'hh24') = m.start_time and s1.vendor_name = m.vendor_name and s1.trunk_out = m.trunk_out  \n" +
         "order by 1 desc" ;   
            
    /*        
    "select \n" +
    "   s2.stat_time       call_date,\n" +
    "   s2.vendor_name  trunk_owner,\n" +
    "   s2.trunk_out       trunk_out,\n" +
    "   s2.acd            acd_h ,\n" +
    "   m.acd            acd_m , \n" +
    "   s2.asr            asr_h,\n" +
    "   m.asr            asr_m,\n" +
    "   s2.call_count   MINS_H,\n" +
    "   m.call_count   MINS_M\n" +
    "from "
    + "(select stat_time       ,\n" +
             "v1.vendor_name  ,\n" +
             "trunk_out       ,\n" +
             "round(avg(acd),2) acd,\n" +
             "round(avg(asr),2) asr,\n" +
             "round(avg(call_count)) call_count\n" +   
      "from stat_acd_asr_hourly_trunk s join vendor v1 on s.owner_in_id = v1.id\n" +
           "join vendor v2 on s.owner_out_id = v2.id\n" +
           "join countries c on s.country_code = c.id\n" +
      "where  c.country = ? and operator_desc = ? and\n" +
            "stat_time > SYS_EXTRACT_UTC(SYSTIMESTAMP) -1 - (1/24)\n" +
            "group by stat_time, v1.vendor_name,trunk_out order by 1,2,3,4,5) s2\n" +
      "left join\n" +
       "(select \n" +
             "--stat_time,\n" +
             "v1.vendor_name  vendor_name,\n" +
             "trunk_out,\n" +
             "round(avg(acd),2) acd, \n" +
             "round(avg(asr),2) asr, \n" +
             "round(avg(call_count),2) call_count \n" + 
      "from stat_acd_asr_hourly_trunk s join vendor v1 on s.owner_in_id = v1.id \n" +
           "join vendor v2 on s.owner_out_id = v2.id \n" +
           "join countries c on s.country_code = c.id \n" +
      "where  c.country = ? and operator_desc = ? and \n" +
            "to_char(stat_time,'hh24') = to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP) - (1/24),'hh24') \n" +
            "group by v1.vendor_name,trunk_out order by 1,2,3,4,5) m on m.vendor_name = s2.vendor_name and s2.trunk_out = m.trunk_out order by 1,2,3 \n" ;
    */
   
    System.out.println(query);
    System.out.println(request.getParameter("country"));
    System.out.println(request.getParameter("operator_prefix"));
    
    Connection conn = DataBase.getConnection();
    PreparedStatement s = conn.prepareStatement(query);

    s.setString(1, request.getParameter("country_code"));
    s.setString(2, request.getParameter("operator_prefix"));
    s.setString(3, request.getParameter("country_code"));
    s.setString(4, request.getParameter("operator_prefix"));
  
%>{
    data: [
<%
    com.telzar.tools.JsonTool.GenerateJSON(s, out, "\t\t");
%>
    ]
}