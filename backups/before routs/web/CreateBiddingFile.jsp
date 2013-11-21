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
<%@page import="com.telzar.tools.PDFTool"%>
<%@page import="com.telzar.tools.EmailTool"%>
<%
    
    System.out.println("inside CreateBiddingFile");
    String var = request.getParameter("var");
    System.out.println("inside CreateBiddingFile: " + var);
    var = "Operator, Vendor, Currency, Rate;" + var;
    
    String path = "/tmp/";
    PDFTool pt = new PDFTool();
    String fileName = pt.CreatePDF(path,var);

    if (!fileName.equals("fail")){
        EmailTool et = new EmailTool();
        String emailTo = "omri@telzar.co.il";
        et.sendEmail(emailTo ,path , fileName);
    }
    
%>{
    data: [
<%
    
%>
    ]
}