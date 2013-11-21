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
    System.out.println("inside GetIncomeStats");
    
    String query = 
          "select\n" +  
                "s.vendor_name trunk_owner,\n" +
                "c.country country_name,\n" + 
                "s.country_code country_code,\n" + 
                "s.operator_prefix operator_prefix,\n" + 
                "round(s.call_duration,2) call_duration,\n" + 
                "round(s.call_duration/decode(s.call_count_s,0,1,s.call_count_s),2) acd,\n" + 
                "round(s.call_count_s/decode(s.call_count_s + s.call_count_u,0,1, s.call_count_s + s.call_count_u)*100,2) asr,\n" + 
                "s.call_count_s calls\n" + 
          "from STAT_STREAM_IN_NO_TRUNK s \n" + 
               "join countries c on c.id = s.country_code\n" + 
          "where to_char(s.start_time, 'yyyy/mm/dd hh24') = to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP)-1/24, 'yyyy/mm/dd hh24')\n"+
            "order by 1,2";
            
            /*
        "select\n" +
        "  s.vender_name trunk_owner,\n" + 
        "  c.country   country_name,\n" + 
        "  s.operator_desc country_desc,\n" + 
        "  round(avg(s.minutes),2) call_duration,\n" + 
        "  round(avg(s.acd),2) acd,\n" + 
        "  round(avg(s.asr),2) asr,\n" + 
        "  round(avg(s.cnt)) calls\n" + 
        "from\n" + 
        "  stat_acd_asr_hourly_no_trunk s join countries c on c.id = s.country_code where stat_time > SYS_EXTRACT_UTC(SYSTIMESTAMP) - 1 - (1/24)\n" + 
        "  group by vender_name,country,operator_desc order by s.vender_name asc";
*/
            

    Connection conn = DataBase.getConnection();
    Statement s = conn.createStatement();
    ResultSet rs = s.executeQuery( query );
    
    String lineSeparator = "\t\t";
    
%>{
    data: [
<%
    com.telzar.tools.JsonTool.GenerateJSON(rs, out, "\t\t");
//    com.dbinteg.Data.JsonTool.GenerateJSON(rs, out, "\n\n");
    
    /*
    ArrayList<com.dbinteg.Data.IncomeStats> IncomeStats = new ArrayList<com.dbinteg.Data.IncomeStats>();
    
    while(rs.next()){
        IncomeStats.add(new com.dbinteg.Data.IncomeStats(rs));
    }
    
    
    String line;
    for(int i=0;i<IncomeStats.size();i++){
       line = String.format(
            "%s{ \"trunk_owner\":\"%s\"," + 
            "\"country_name\":\"%s\"," + 
            "\"country_desc\":\"%s\"," + 
            "\"call_duration\":\"%s\"," + 
            "\"acd\":\"%s\"," + 
            "\"asr\":\"%s\"," + 
            "\"calls\":\"%s\" }",
            lineSeparator,
            IncomeStats.get(i).getTrunkOwner(),
            IncomeStats.get(i).getCountryName(),
            IncomeStats.get(i).getCountryDescription(),
            IncomeStats.get(i).getCallDuration(),
            IncomeStats.get(i).getAcd(),
            IncomeStats.get(i).getAsr(),
            IncomeStats.get(i).getCalls()
        ); 
        out.print(line);
//        System.out.println("-------here:\n" + line);
        out.flush();
        if ( lineSeparator.equals("\t\t") ) lineSeparator = ",\n\t\t"; */
//    }
%>    
    ]
}
