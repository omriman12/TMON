<%@page import="java.util.ArrayList"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@page contentType="text/json" pageEncoding="UTF-8"%>
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
            "c.country country_name,\n" +
            "s1.country_code country_code,\n" +
            "s1.operator_prefix operator_prefix,\n" +
            "s1.call_duration call_duration_h,\n" +
            "m.call_duration call_duration_m,\n" +
            "12 priority,\n" + 
            "s1.acd acd_h,\n" +
            "m.acd acd_m,\n" +
            "s1.asr asr_h,\n" +
            "m.asr asr_m,\n" +
            "s1.call_count calls_h,\n" +
            "m.call_count calls_m\n" +
     "from\n" +
     "(select \n" +
           "s.start_time,\n" +
           "s.country,\n" +
           "s.country_code,\n" +
           "s.operator_prefix,\n" +
           "round(s.call_duration,2) call_duration,\n" +
           "round(s.call_duration/decode(s.call_count_s,0,1,s.call_count_s),2) acd,\n" +
           "round(s.call_count_s/decode(s.call_count_s + s.call_count_u,0,1, s.call_count_s + s.call_count_u)*100,2) asr,\n" +
           "s.call_count_s call_count\n" +
     "from STAT_STREAM_OUT_NO_TRUNK s \n" +
     "where to_char(s.start_time, 'yyyy/mm/dd hh24') = to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP)-1/24, 'yyyy/mm/dd hh24')) s1 \n" +
     "join \n" +
     "(select \n" +
           "a.country_code,\n" +
           "a.operator_prefix,\n" +
           "round(a.call_duration,2) call_duration,\n" +
           "round(a.call_duration/decode(a.call_count_s,0,1,a.call_count_s),2) acd,\n" +
           "round(a.call_count_s/decode(a.call_count_s + a.call_count_u,0,1, a.call_count_s + a.call_count_u)*100,2) asr,\n" +
           "a.call_count_s call_count\n" +
      "from\n" +
         "(select\n" +
             "s.country_code,\n" +
             "s.operator_prefix,\n" +
             "round(avg(s.call_duration),2) call_duration,\n" +
             "round(avg(s.call_count_s),2) call_count_s,\n" +
             "round(avg(s.call_count_u),2) call_count_u\n" +
          "from STAT_STREAM_OUT_NO_TRUNK s \n" +
          "where to_char(s.start_time,'hh24') = to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP) - (1/24),'hh24')\n" +
          "group by s.country_code, s.operator_prefix) a\n" +
     ") m \n" +
     "on s1.country_code = m.country_code and s1.operator_prefix = m.operator_prefix  \n" +
          "join countries c on c.id = s1.country_code\n" +
     "order by s1.call_duration desc" ;

    
    
    
    /*
    String query = 
        "select\n" +
        "  s.country         country_name,\n" + 
        "  s.country_code    country_code,\n" + 
        "  s.operator_desc   country_desc,\n" + 
        "  s.call_duration   call_duration_h,\n" + 
        "  m.call_duration   call_duration_m,\n" + 
        "  s.call_count_s+s.call_count_u+m.calls_m  priority,\n" + 
        "  s.acd           acd_h,\n" + 
        "  m.acd           acd_m,\n" + 
        "  s.asr           asr_h,\n" + 
        "  m.asr           asr_m,\n" + 
        "  s.call_count_s+s.call_count_u calls_h,\n" + 
        "  m.calls_m\n" + 
        "from\n" + 
        "stat_stream_out_no_trunk s  join (select\n" + 
        "   country,country_code,operator_desc,round(avg(call_duration),2) call_duration,\n" + 
        "   round(avg(acd),2) acd,\n" + 
        "   round(avg(asr),2) asr,\n" + 
        "   round(avg(call_count_s+call_count_u)) calls_m\n" + 
        "from\n" + 
        "   stat_stream_out_no_trunk\n" + 
        "   where to_char(start_time,'hh24') = to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP) - (1/24),'hh24')\n" + 
        "   group by country,country_code,operator_desc) m on s.country = m.country and s.operator_desc = m.operator_desc\n" + 
        "   where to_char(s.start_time,'yyyy/mm/dd HH24') = to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP) - 1 - (1/24), 'yyyy/mm/dd HH24') and  s.call_duration > 0 and m.call_duration > 0\n"+
        "   order by s.call_duration desc";*/
    
    Connection conn = DataBase.getConnection();
//    Context ctx = new InitialContext();
//    //DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/TMon");
//    DataSource ds = (DataSource) ctx.lookup("jdbc/RBS");
//    Connection conn = ds.getConnection();
    Statement s = conn.createStatement();
    ResultSet rs = s.executeQuery( query );
    
    String lineSeparator = "\t\t";
    
%>
{
    data: [

<%
    ArrayList<com.dbinteg.Data.OutGoingStats> OutGoingStats = new ArrayList<com.dbinteg.Data.OutGoingStats>();
    ArrayList<String> CountryOrder = new ArrayList<String>();
    
    while(rs.next()){
        OutGoingStats.add(new com.dbinteg.Data.OutGoingStats(rs));
        if(!CountryOrder.contains(rs.getString("country_name")))
              CountryOrder.add(rs.getString("country_name"));
    }
    
    ArrayList<com.dbinteg.Data.OutGoingStats> OrderedOutGoingStats = new ArrayList<com.dbinteg.Data.OutGoingStats>();
    for(int i=0;i<CountryOrder.size();i++){
        for(int j=0;j<OutGoingStats.size();j++){
            if(CountryOrder.get(i).equals(OutGoingStats.get(j).getCountryName()))
                OrderedOutGoingStats.add(OutGoingStats.get(j));
        }
    }
    
    for (int i=0;i<OrderedOutGoingStats.size();i++){
        
        out.print(String.format(
            "%s{ \"country_name\":\"%s\"," + 
            "\"country_code\":\"%s\"," + 
            //"\"country_desc\":\"%s\"," +
            "\"operator_prefix\":\"%s\"," + 
            "\"call_duration_h\":\"%s\"," + 
            "\"call_duration_m\":\"%s\"," + 
            "\"priority\":\"%s\"," + 
            "\"acd_h\":\"%s\"," + 
            "\"acd_m\":\"%s\"," + 
            "\"asr_h\":\"%s\"," + 
            "\"asr_m\":\"%s\"," + 
            "\"calls_h\":\"%s\"," + 
            "\"calls_m\":\"%s\" }",
            lineSeparator,
            OrderedOutGoingStats.get(i).getCountryName(),
            OrderedOutGoingStats.get(i).getCountryCode(),
            //OrderedOutGoingStats.get(i).getCountryDescription(),
            OrderedOutGoingStats.get(i).getOperatorPrefix(),
            OrderedOutGoingStats.get(i).getCallDurationHour(),
            OrderedOutGoingStats.get(i).getCallDurationMinute(),
            OrderedOutGoingStats.get(i).getPriority(),
            OrderedOutGoingStats.get(i).getAcdHour(),
            OrderedOutGoingStats.get(i).getAcdMinute(),
            OrderedOutGoingStats.get(i).getAsrHour(),
            OrderedOutGoingStats.get(i).getAsrMinute(),
            OrderedOutGoingStats.get(i).getCallsHour(),
            OrderedOutGoingStats.get(i).getCallsMinute()
        ));
        out.flush();
        if ( lineSeparator.equals("\t\t") ) lineSeparator = ",\n\t\t";
    }
%>    
    ]
}
<%
    rs.close();
    s.close();
    conn.close();
%>