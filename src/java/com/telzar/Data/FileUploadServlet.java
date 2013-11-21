package com.telzar.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import java.sql.*;
 
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
import com.google.gson.JsonObject;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.util.Date;
import oracle.net.nt.ConnOption;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
 
public class FileUploadServlet extends HttpServlet {
 
    private Boolean Debug = true;
    private static String path, logoPath, bidEmailTo, routeEmailTo;
    private static final long serialVersionUID = 1L;
    private File tmpDir;
//    private static final String DESTINATION_DIR_PATH ="C:\\omri\\";
    private File destinationDir;
 
    public FileUploadServlet() {
        super();
    }
 
    public void init(ServletConfig config) throws ServletException {
 
        super.init(config);
        
        if (Debug){
            path = "C:\\app\\";
            logoPath = "c:\\app\\019.png";
        }
        else{
            path = "/tmp/";
            logoPath = "/var/tmon/019.png";
            bidEmailTo = "shir@telzar.co.il"; //shir
            routeEmailTo = "shir@telzar.co.il"; //cs
        }
        
        tmpDir = new File(path);
        if(!tmpDir.isDirectory()) {
            throw new ServletException(path + " is not a directory");
        }
 
        destinationDir = new File(path);
        if(!destinationDir.isDirectory()) {
            throw new ServletException(path+" is not a directory");
        }
 
    }
 
 
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("inside FileUploadServlet post");
        
        String buyingVendor = request.getParameter("buying_vendor");
        String buyingVendorQuality = request.getParameter("buying_vendor_quality");
        
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
 
        DiskFileItemFactory  fileItemFactory = new DiskFileItemFactory ();
 
        fileItemFactory.setSizeThreshold(1*1024*1024); //1 MB
 
        //Set the temporary directory to store the uploaded files of size above threshold.
        fileItemFactory.setRepository(tmpDir);
 
        ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
        JsonObject myObj = new JsonObject();
 
        String fileName = null;
        String fullName = null;
        File file = null;
 
        try {
 
            //Parse the request
            List items = uploadHandler.parseRequest(request);
            Iterator iterator = items.iterator();
            while(iterator.hasNext()) {
                FileItem item = (FileItem) iterator.next();
 
                //Handle Form Fields
                if(item.isFormField()) {
                    System.out.println("Field Name = " + item.getFieldName() + ", Value = " + item.getString());
                    if(item.getFieldName().trim().equalsIgnoreCase("filename")){
                        fileName = item.getString().trim();
                    }
                } 
 
                //Handle Uploaded files.
                else {
                    fileName = item.getName();
                    System.out.println("Field Name = " + item.getFieldName()+
                            ", File Name = "+ item.getName()+
                            ", Content type = "+item.getContentType()+
                            ", File Size = "+item.getSize());
                    fullName = item.getName().trim();
 
                    //Write file to the ultimate location.
                    file = new File(destinationDir,item.getName());
                    item.write(file);
                }
            }
 
//            int count = 0;
            String extension = FilenameUtils.getExtension(fullName);
            if(extension.trim().equalsIgnoreCase("xlsx")){
                System.out.println("FileDownloadServlet: recieved xlsx");
                processXlsxExcelFile(file);
            }
            else if(extension.trim().equalsIgnoreCase("xls")){
                System.out.println("FileDownloadServlet: recieved xls");
                processXlsExcelFile(path + fileName , buyingVendor , buyingVendorQuality );
            }
            if(extension.trim().equalsIgnoreCase("csv")){
                System.out.println("FileDownloadServlet: recieved csv");
            }
 
            myObj.addProperty("success", true);
//            myObj.addProperty("message", count + " item(s) were processed for file " + fileName);
            out.println(myObj.toString());
 
        }
        catch(FileUploadException ex) {
            log("Error encountered while parsing the request",ex);
            myObj.addProperty("success", false);
            out.println(myObj.toString());
        } catch(Exception ex) {
            log("Error encountered while uploading file",ex);
            myObj.addProperty("success", false);
            out.println(myObj.toString());
        }
 
        out.close();
 
    }
 
    private void processXlsxExcelFile(File file){
        System.out.println("inside processXlsxExcelFile");
 
//        int count = 0;
 
        try{
            // Creating Input Stream 
            FileInputStream myInput = new FileInputStream(file);
 
            // Create a workbook using the File System 
            XSSFWorkbook myWorkBook = new XSSFWorkbook(myInput);
 
            // Get the first sheet from workbook 
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
 
            /** We now need something to iterate through the cells.**/
            Iterator<Row> rowIter = mySheet.rowIterator();
            while(rowIter.hasNext()){
 
                XSSFRow myRow = (XSSFRow) rowIter.next();
                Iterator<Cell> cellIter = myRow.cellIterator();
                while(cellIter.hasNext()){
 
                    XSSFCell myCell = (XSSFCell) cellIter.next();
                    //get cell index
                    System.out.println("Cell column index: " + myCell.getColumnIndex());
                    //get cell type
                    System.out.println("Cell Type: " + myCell.getCellType());
 
                    //get cell value
                    switch (myCell.getCellType()) {
                    case XSSFCell.CELL_TYPE_NUMERIC :
                        System.out.println("Cell Value: " + myCell.getNumericCellValue());
                        break;
                    case XSSFCell.CELL_TYPE_STRING:   
                        System.out.println("Cell Value: " + myCell.getStringCellValue());
                        break;
                    default:   
                        System.out.println("Cell Value: " + myCell.getRawValue());
                        break;   
                    }
                    System.out.println("---");
 
                    if(myCell.getColumnIndex() == 0 && 
                            !myCell.getStringCellValue().trim().equals("") &&
                            !myCell.getStringCellValue().trim().equals("Item Number")){
//                        count++;
                    }
 
                }
 
            }
        }
        catch (Exception e){
            e.printStackTrace(); 
        }
 
//        return count;
 
    }
    
    public void processXlsExcelFile(String inputFile, String buyingVendor ,String buyingVendorQuality) {
         System.out.println("inside processXlsExcelFile: " + inputFile);
         com.telzar.Data.DataBase db = new com.telzar.Data.DataBase();
         Connection con = null;
         try{
            con = db.getConnection(); 
            int bidId = db.getLastBidId() + 1;
            parseExcelToDB(inputFile, bidId, buyingVendor ,buyingVendorQuality, con);
         
            db.startBidDbActions();
            
            createAndSendBidAndRoute(bidId, buyingVendor , buyingVendorQuality);
            
//            db.addToRates(bidId);
         }
         catch(Exception e){
             e.printStackTrace();
         }
         finally{
             try {
                 if (con != null)
                     con.close();
             } catch (SQLException ex) {
                 ex.printStackTrace();
             }
         }
         
    }
    
    private void parseExcelToDB(String inputFile, int bidId, String buyingVendor ,String buyingVendorQuality, Connection con){
        try
         {
            InputStream myxls = new FileInputStream(inputFile);
            HSSFWorkbook wb     = new HSSFWorkbook(myxls);

            HSSFSheet sheet = wb.getSheetAt(0);       // first sheet
            
            Iterator<Row> rowIterator = sheet.iterator();
            
            for(int i=1;i<=2;i++){
                rowIterator.next();
            }

            String query = "insert into tmp_tmon_bidding t\n" +
                            "( bid_id, bid_date, bid_vendor_id, bid_quality, country_code , prefix, vendor_id, quality , "
                            + "billing, buy_rate, sell_rate , currency , rate_date, priority, weight, status ) \n" +
                            "select ?, ?, v2.id, ?, ?, ?, v3.id, ?, ?, ?, ?, ?, ?, ?, ?, ? from  \n" +
                            "(select v.id from vendor v where v.vendor_name = ?) v2,\n" +
                            "(select v.id from vendor v where v.vendor_name = ?) v3 " ;
            
            
            PreparedStatement ps =con.prepareCall(query);
            
            
            String value = "";
            Cell cell = null;
            while (rowIterator.hasNext()) 
            {
//                System.out.println("\n" + i +": " );
                ps.setInt(1, bidId);
                ps.setTimestamp(2, new java.sql.Timestamp(new Date().getTime()));
                ps.setString(15, buyingVendor);
                ps.setString(3, buyingVendorQuality);
                
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                
                secoundWhileLoop:
                while (cellIterator.hasNext()) 
                {
                    cell = cellIterator.next();
                    switch (cell.getCellType()) 
                    {
                        case Cell.CELL_TYPE_NUMERIC:
                            value = new Double(cell.getNumericCellValue()).toString();
                            break;
                        case Cell.CELL_TYPE_STRING:
                            value = cell.getStringCellValue() ;
                            break;
                        default: 
                            break secoundWhileLoop;
                    }
                    
                    switch (cell.getColumnIndex()) 
                    {
                        case 0: //Country Code
                            ps.setString(4, value);
                            break;
                        case 1: //  prefix
                            ps.setString(5, value);
                            break;
                        case 2://vendor
                            ps.setString(16, value);
                            break;
                        case 3://quality
                            ps.setString(6, value);
                            break;
                        case 4://billing
                            ps.setString(7, value);
                            break;
                        case 5://buy_rate
                            ps.setString(8, value);
                            break;
                        case 6://sell rate
                            ps.setString(9, value);
                            break;
                        case 7://currency
                            ps.setString(10, value);
                            break; 
                        case 8://effective date
                            ps.setString(11, value);
                            break;
                        case 9:// priority
                            ps.setString(12, value);
                            break;
                        case 10://weight
                            ps.setString(13, value);
                        case 11: //status
                            ps.setString(14, value);
                            break; 
                     }
//                     System.out.println("");
                }
                if(cell !=null )
                    if(cell.getColumnIndex() == 11){ // in our format
                        ps.addBatch();
                    }
            }
            ps.executeBatch();
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    private void createAndSendBidAndRoute(int bidId, String buyingVendor ,String buyingVendorQuality){
        System.out.println("inside createAndSendBidAndRoute");
        
        
        com.telzar.Data.DataBase db = new com.telzar.Data.DataBase();
        com.telzar.tools.EmailTool emailTool = new com.telzar.tools.EmailTool();
        com.telzar.tools.ExcelTool excelTool = new com.telzar.tools.ExcelTool();
        String bidExcel = excelTool.createBidExel(path, bidId, logoPath ); //, bidId
        String routeExcel = excelTool.createRouteExel(path, bidId);
      
        
        if (!bidExcel.equals("fail") && !Debug){
            String title = "TMon Bidding";
            emailTool.sendEmail(bidEmailTo ,"invoice@telzar-019.co.il", path , bidExcel, title);
        }
        
        if (!routeExcel.equals("fail") && !Debug){
            String title = "Tmon - Route Changes For Vendor:" + buyingVendor + ", Quality:" + buyingVendorQuality;
            emailTool.sendEmail(routeEmailTo, "shir@telzar.co.il", path, routeExcel , title);
        }
    }
    
}