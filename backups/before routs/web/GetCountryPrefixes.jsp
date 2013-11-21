<%@page import="java.sql.PreparedStatement"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@page import="com.telzar.tools.JsonTool"%>
<%@page import="com.dbinteg.Data.DataBase"%>
<%@page import="java.sql.Connection"%>

<%@page contentType="text/json" pageEncoding="UTF-8"%>
<%
    Connection conn = DataBase.getConnection();
    String query = 
            "select \n" +
                    "s.price_date,\n" +
                    "s.country_code,\n" +
                    "s.prefix,\n" +
                    "s.vendor_name,\n" +
                    "s.rate,\n" +
                    "s.currency,\n" +
                    "s.quality, \n" +
                    "s.duration,\n" +
                    "s.acd,\n" +
                    "s.call_count\n" +
             "from stat_price_report s \n" + 
             "where s.prefix = ?";
            
            /*
        "select distinct                                                            \n" +
        "  s.stat_data,                                                             \n" + 
        "  r.country_code,r.prefix,r.operator_desc,v.vendor_name,r.rate,r.currency, \n" + 
        "   s.minutes,                                                              \n" + 
        "   s.acd,                                                                  \n" + 
        "  round((100/(s.calls+s.cnt2))*s.calls,2) asr                              \n" + 
        "from\n" + 
        "  rates r left join stat_price_report s on r.country_code = s.country_code and r.prefix = s.prefix and r.operator_desc = s.operator_desc\n" + 
        "  inner join vendor v on v.id = r.vendor_id                                \n" + 
        "where\n" + 
        "  sysdate between r.effective_date_from and nvl(r.effective_date_to,sysdate)\n" + 
        "  and sysdate between v.effective_date_from and nvl(v.effective_date_to,sysdate)\n" + 
        "  and direction = 'OUT'\n" + 
        "  and r.prefix like ?\n" + 
        "  order by 1 desc";*/
    
    
    PreparedStatement s = conn.prepareStatement(query);
    s.setString(1, request.getParameter("prefix"));
%>{
    data: [
<%
    JsonTool.GenerateJSON(s, out, "\t\t");
    s.close();
    conn.close();
%>
    ]
}