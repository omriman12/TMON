///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
package com.telzar.Data;
//
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
 
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
public class FileDownloadServlet extends javax.servlet.http.HttpServlet implements
        javax.servlet.Servlet {
    private Boolean Debug = true;
    private static String path, logoPath;
    static final long serialVersionUID = 1L;
    private static final int BUFSIZE = 4096;
//    private String filePath;
    
    public void init() {
        System.out.println("isnide FileDownloadServlet : init " );
        
        if (Debug){
            path = "C:\\app\\";
            logoPath = "c:\\app\\019.png";
        }
        else{
            path = "/tmp/";
            logoPath = "/var/tmon/019.png";
        }
        
    }
    
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        System.out.println("isnide FileDownloadServlet Get :" + request.getParameter("hh"));
        ServletOutputStream outStream = null;
        DataInputStream in = null;
        
        
        
        com.telzar.tools.ExcelTool excelTool = new com.telzar.tools.ExcelTool();
        String bidExcel = excelTool.createExelFromTmp(path, logoPath); 
        String filePath = path + bidExcel;
        
        try {
                File file = new File(filePath);
                int length = 0;
                outStream = response.getOutputStream();
                ServletContext context  = getServletConfig().getServletContext();
                String mimetype = context.getMimeType(filePath);

                // sets response content type
                if (mimetype == null) {
                    mimetype = "application/octet-stream";
                }
                response.setContentType(mimetype);
                response.setContentLength((int)file.length());
                String fileName = (new File(filePath)).getName();

                // sets HTTP header
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

                byte[] byteBuffer = new byte[BUFSIZE];
                in = new DataInputStream(new FileInputStream(file));

                // reads the file's bytes and writes them to the response stream
                while ((in != null) && ((length = in.read(byteBuffer)) != -1))
                {
                    outStream.write(byteBuffer,0,length);
                }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if (in != null)
                in.close();
            if(outStream !=null)
                outStream.close();
        }
    }
}

