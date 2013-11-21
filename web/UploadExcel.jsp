<%@ page import="java.io.*,java.util.*, javax.servlet.*" %>
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
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.fileupload.disk.*" %>
<%@ page import="org.apache.commons.fileupload.servlet.*" %>
<%
    System.out.println("inside Upload Excel");
    
    byte tmpbuffer[] = new byte[8192];
    
    ServletInputStream inputStream =  request.getInputStream();//.readLine(tmpbuffer, 0, tmpbuffer.length);
    
    File file ;
   int maxFileSize = 5000 * 1024;
   int maxMemSize = 5000 * 1024;
//   ServletContext context = pageContext.getServletContext();
   String filePath = "C:\\omri\\" ;//context.getInitParameter("file-upload");
   
   
   // Verify the content type
   String contentType = request.getContentType();
   if ((contentType.indexOf("multipart/form-data") >= 0)) {

      DiskFileItemFactory factory = new DiskFileItemFactory();
      // maximum size that will be stored in memory
      factory.setSizeThreshold(maxMemSize);
      // Location to save data that is larger than maxMemSize.
      factory.setRepository(new File("C:\\app"));

      // Create a new file upload handler
      ServletFileUpload upload = new ServletFileUpload(factory);
      // maximum file size to be uploaded.
      upload.setSizeMax( maxFileSize );
      try{ 
         // Parse the request to get file items.
         List fileItems = upload.parseRequest(request);

         // Process the uploaded file items
         Iterator i = fileItems.iterator();

//         out.println("<html>");
//         out.println("<head>");
//         out.println("<title>JSP File upload</title>");  
//         out.println("</head>");
//         out.println("<body>");
         while ( i.hasNext () ) 
         {
            FileItem fi = (FileItem)i.next();
            if ( !fi.isFormField () )	
            {
            // Get the uploaded file parameters
            String fieldName = fi.getFieldName();
            String fileName = fi.getName();
            boolean isInMemory = fi.isInMemory();
            long sizeInBytes = fi.getSize();
            // Write the file
            if( fileName.lastIndexOf("\\") >= 0 ){
                file = new File( filePath + fileName.substring(fileName.lastIndexOf("\\")));
            }else{
                file = new File( filePath + fileName.substring(fileName.lastIndexOf("\\")+1));
            }
            fi.write(file);
//            out.println("Uploaded Filename: " + filePath + 
//            fileName + "<br>");
            }
         }
         
//         out.println("</body>");
//         out.println("</html>");
      }catch(Exception ex) {
         System.out.println(ex);
      }
   }else{
//      out.println("<html>");
//      out.println("<head>");
//      out.println("<title>Servlet upload</title>");  
//      out.println("</head>");
//      out.println("<body>");
//      out.println("<p>No file uploaded</p>"); 
//      out.println("</body>");
//      out.println("</html>");
       
   }
    String responseMessage = "{\"success\": true}"; //, \"file\": \"filename\"
%>{
    data: [
<%
      PrintWriter o = new PrintWriter( new BufferedWriter(out) ,true);
      System.out.println(responseMessage);
      o.write(responseMessage);
      o.flush();
//    o.write("{ \"success\": true, \"file\": \"filename\" }");
%>
    ]
}