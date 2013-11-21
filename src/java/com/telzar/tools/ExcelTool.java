package com.telzar.tools;

import com.telzar.Data.DataBase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.apache.pdfbox.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelTool {
    
    public String createExelFromTmp(String path, String logoPath){ //, String bidId
        System.out.println("inside createExelFromTmp");
        String fileName = "bid-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +".xls";
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Telzar Official Offer");
        Row row = null;
        Cell cell = null;
        
        //Styles
        HSSFFont boldFont=workbook.createFont();
        boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setFont(boldFont);
        
        HSSFCellStyle borderStyle = workbook.createCellStyle();
        borderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        borderStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        borderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        borderStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        
        HSSFCellStyle fullColorStyle = workbook.createCellStyle();
        fullColorStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        fullColorStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        fullColorStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        fullColorStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        fullColorStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        fullColorStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        fullColorStyle.setFont(boldFont);
        
        
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 2500);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 3000);
        sheet.setColumnWidth(5, 3000);
        sheet.setColumnWidth(6, 3000);
        
        //Logo Pic
        try{
            row = sheet.createRow(0);
            cell = row.createCell(0);
            InputStream my_banner_image = new FileInputStream(logoPath);
            byte[] bytes = IOUtils.toByteArray(my_banner_image);
            int my_picture_id = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            my_banner_image.close();

            HSSFPatriarch drawing = sheet.createDrawingPatriarch();
            ClientAnchor my_anchor = new HSSFClientAnchor();
            my_anchor.setCol1(0);
            my_anchor.setRow1(0);           
            HSSFPicture  my_picture = drawing.createPicture(my_anchor, my_picture_id);
            my_picture.resize();            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        
        //Starting line
        row = sheet.createRow(6);
        cell = row.createCell(0);
        cell.setCellValue("The following offer replaces all previous sent offers of the countries below.");
        cell.setCellStyle(boldStyle);
        
        //Grid
        int rownum =8;
        int cellnum = 0;
        
        row = sheet.createRow(rownum++);
        
        cell = row.createCell(cellnum++);
        cell.setCellValue("Country Code");
        cell.setCellStyle(fullColorStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Prefix");
        cell.setCellStyle(fullColorStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Vendor");
        cell.setCellStyle(fullColorStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Quality");
        cell.setCellStyle(fullColorStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Billing");
        cell.setCellStyle(fullColorStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Buy Rate"); 
        cell.setCellStyle(fullColorStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Sell Rate"); //Rate/Minute
        cell.setCellStyle(fullColorStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Currency");
        cell.setCellStyle(fullColorStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Effective Date");
        cell.setCellStyle(fullColorStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Priority");
        cell.setCellStyle(fullColorStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Weight");
        cell.setCellStyle(fullColorStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Status");
        cell.setCellStyle(fullColorStyle);
//        cell = row.createCell(cellnum++);
//        cell.setCellValue("Info");
//        cell.setCellStyle(fullColorStyle);
        
        try{
              DataBase db = new DataBase();
              PreparedStatement ps ;
//              String query = "select CONCAT(c.country || '-' ,min(r.operator_desc)), t.prefix, t.billing, t.sell_rate, t.currency, t.rate_date, t.rate_change from tmon_bidding t  --\n" +
//                             "join countries c on c.id = t.country_code \n" +
//                             "join rate_config_xls rcx on t.vendor_id = rcx.vendor_id and t.quality = rcx.rate_desc\n" +
//                             "join rates r on r.vendor_id = t.vendor_id and r.country_code = t.country_code and r.prefix = t.prefix and r.rate = t.buy_rate and r.quality = rcx.quality and r.direction = rcx.direction and r.effective_date_to is     null\n" +
//                             "where t.bid_id = ? \n" + 
//                             "group by c.country, t.prefix, t.billing, t.sell_rate, t.currency, t.rate_date , t.rate_change "
//                           + "order by CONCAT(c.country || '-' ,min(r.operator_desc)) desc";
              
//              String query = "select CONCAT(c.country || '-' ,min(r.operator_desc)), t.prefix, t.billing, t.sell_rate, t.currency, max(t.rate_date), min(t.rate_change) from tmon_bidding t  --\n" +
//                                "join countries c on c.id = t.country_code \n" +
//                                "join rate_config_xls rcx on t.vendor_id = rcx.vendor_id and t.quality = rcx.rate_desc\n" +
//                                "join rates r on r.vendor_id = t.vendor_id and r.country_code = t.country_code and r.prefix = t.prefix and r.rate = t.buy_rate and r.quality = rcx.quality and r.direction = rcx.direction and r.effective_date_to is null\n" +
//                             "where t.bid_id = ? \n" +
//                             "group by c.country, t.prefix, t.billing, t.sell_rate, t.currency \n" +
//                             "order by CONCAT(c.country || '-' ,min(r.operator_desc)) desc";
              
              //tmp-tmon excel
//              String query = "select CONCAT(c.country || '-' ,r.operator_desc), t.operator_prefix, t.billing, t.buy_rate, t.sell_rate, t.currency , t.rate_date_string, t.priority, t.weight, t.status from tmp_tmon t\n" +
//                            "join vendor v on v.vendor_name = t.vendor\n" +
//                            "join countries c on c.id = t.country_code\n" +
//                            "join rate_config_xls rcx on v.id = rcx.vendor_id and t.quality = rcx.rate_desc\n" +
//                            "join rates r on r.vendor_id = v.id \n" +
//                            "     and r.country_code = t.country_code \n" +
//                            "     and r.prefix = t.operator_prefix \n" +
//                            "     and r.rate = t.buy_rate \n" +
//                            "     and r.quality = rcx.quality \n" +
//                            "     and r.direction = rcx.direction\n" +
//                            "     and r.effective_date_to is null";
              
              String query = "select t.country_code, t.operator_prefix, t.vendor, t.quality, "
                              + "t.billing, t.buy_rate, t.sell_rate, t.currency , "
                              + "t.rate_date_string, t.priority, t.weight, t.status from tmp_tmon t";
                      
              Connection con = db.getConnection();  
              ps = con.prepareStatement(query);
//              ps.setString(1, bidId);
              ResultSet rs = ps.executeQuery();
              
              Date dateTimeNow = new Date();
              int i=0;
              while(rs.next()){
                   row = sheet.createRow(rownum++);
                   cellnum = 0;
                    for (int j=1; j<=12; j++){ 
                        cell = row.createCell(cellnum++);
                        cell.setCellStyle(borderStyle);
                        
                        if(j==9)
                            cell.setCellValue(new SimpleDateFormat("dd.MM.yy").format(dateTimeNow));
                        else
                            cell.setCellValue(rs.getString(j));
                    }
                  i++;
              }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        try {
            String dateTimeNow = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            FileOutputStream out = new FileOutputStream(new File(path + fileName));
            workbook.write(out);
            out.close();
            System.out.println("Excel written successfully..");
            return fileName;
        } catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }

    public String createBidExel(String path, int bidId, String logoPath){
        System.out.println("inside createBidExel");
        String fileName = "bid-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +".xls";
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Telzar Official Offer");
        Row row = null;
        Cell cell = null;
        
        //Styles
        HSSFFont boldFont=workbook.createFont();
        boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setFont(boldFont);
        
        HSSFCellStyle borderStyle = workbook.createCellStyle();
        borderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        borderStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        borderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        borderStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        
        HSSFCellStyle fullColorStyle = workbook.createCellStyle();
        fullColorStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        fullColorStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        fullColorStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        fullColorStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        fullColorStyle.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        fullColorStyle.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        fullColorStyle.setFont(boldFont);
        
        
        sheet.setColumnWidth(0, 8000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 3000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 3000);
        sheet.setColumnWidth(5, 5000);
        sheet.setColumnWidth(6, 5000);
        
        //Logo Pic
        try{
            row = sheet.createRow(0);
            cell = row.createCell(0);
            InputStream my_banner_image = new FileInputStream(logoPath);
            byte[] bytes = IOUtils.toByteArray(my_banner_image);
            int my_picture_id = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            my_banner_image.close();

            HSSFPatriarch drawing = sheet.createDrawingPatriarch();
            ClientAnchor my_anchor = new HSSFClientAnchor();
            my_anchor.setCol1(0);
            my_anchor.setRow1(0);           
            HSSFPicture  my_picture = drawing.createPicture(my_anchor, my_picture_id);
            my_picture.resize();            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        
        //Starting line
        row = sheet.createRow(6);
        cell = row.createCell(0);
        cell.setCellValue("The following offer replaces all previous sent offers of the countries below.");
        cell.setCellStyle(boldStyle);
        
        //Grid
        int rownum =8;
        int cellnum = 0;
        
        row = sheet.createRow(rownum++);
        
        cell = row.createCell(cellnum++);
        cell.setCellValue("Country");
        cell.setCellStyle(fullColorStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Codes");
        cell.setCellStyle(fullColorStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Billing");
        cell.setCellStyle(fullColorStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Rate/Minute");
        cell.setCellStyle(fullColorStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Currency");
        cell.setCellStyle(fullColorStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Effective Date");
        cell.setCellStyle(fullColorStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Info");
        cell.setCellStyle(fullColorStyle);
        
        try{
              DataBase db = new DataBase();
              PreparedStatement ps ;
//              String query = "select CONCAT(c.country || '-' ,min(r.operator_desc)), t.prefix, t.billing, t.sell_rate, t.currency, t.rate_date, t.rate_change from tmon_bidding t  --\n" +
//                             "join countries c on c.id = t.country_code \n" +
//                             "join rate_config_xls rcx on t.vendor_id = rcx.vendor_id and t.quality = rcx.rate_desc\n" +
//                             "join rates r on r.vendor_id = t.vendor_id and r.country_code = t.country_code and r.prefix = t.prefix and r.rate = t.buy_rate and r.quality = rcx.quality and r.direction = rcx.direction\n" +
//                             "where t.bid_id = ? \n" + 
//                             "group by c.country, t.prefix, t.billing, t.sell_rate, t.currency, t.rate_date , t.rate_change "
//                           + "order by CONCAT(c.country || '-' ,min(r.operator_desc)) desc";
              
              String query = "select CONCAT(c.country || '-' ,min(r.operator_desc)), t.prefix, t.billing, t.sell_rate, t.currency, max(t.rate_date), min(t.rate_change) from tmon_bidding t  --\n" +
                                "join countries c on c.id = t.country_code \n" +
                                "join rate_config_xls rcx on t.vendor_id = rcx.vendor_id and t.quality = rcx.rate_desc\n" +
                                "join rates r on r.vendor_id = t.vendor_id and r.country_code = t.country_code and r.prefix = t.prefix and r.rate = t.buy_rate and r.quality = rcx.quality and r.direction = rcx.direction\n" +
                             "where t.bid_id = ? \n" +
                             "group by c.country, t.prefix, t.billing, t.sell_rate, t.currency \n" +
                             "order by CONCAT(c.country || '-' ,min(r.operator_desc)) desc";
              Connection con = db.getConnection();  
              ps = con.prepareStatement(query);
              ps.setInt(1, bidId);
              ResultSet rs = ps.executeQuery();
              
              Date dateTimeNow = new Date();
              int i=0;
              while(rs.next()){
                   row = sheet.createRow(rownum++);
                   cellnum = 0;
                    for (int j=1; j<=7; j++){ 
                        cell = row.createCell(cellnum++);
                        cell.setCellStyle(borderStyle);
                        
                        if(j==6)
                            cell.setCellValue(new SimpleDateFormat("dd.MM.yy").format(dateTimeNow));
                        else
                            cell.setCellValue(rs.getString(j));
                    }
                  i++;
              }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        try {
            String dateTimeNow = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            FileOutputStream out = new FileOutputStream(new File(path + fileName));
            workbook.write(out);
            out.close();
            System.out.println("Excel written successfully..");
            return fileName;
        } catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }
    
    public String createRouteExel(String path, int bidId){
        System.out.println("inside createRouteExel");
        String fileName = "route-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +".xls";
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Telzar Official Offer");
        Row row = null;
        Cell cell = null;
        
        //Styles
        HSSFFont boldFont=workbook.createFont();
        boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setFont(boldFont);
        boldStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        boldStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        boldStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        boldStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        
        HSSFCellStyle borderStyle = workbook.createCellStyle();
        borderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        borderStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        borderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        borderStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        
        HSSFCellStyle fullColorStyle = workbook.createCellStyle();
        fullColorStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        fullColorStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        short[] colorArr = {HSSFColor.LIGHT_BLUE.index, HSSFColor.LEMON_CHIFFON.index, HSSFColor.LIGHT_GREEN.index,
                       HSSFColor.LIGHT_ORANGE.index, HSSFColor.LIGHT_TURQUOISE.index , HSSFColor.LIGHT_YELLOW.index,
                       HSSFColor.ORANGE.index, HSSFColor.SKY_BLUE.index , HSSFColor.LIME.index,
                       HSSFColor.DARK_GREEN.index, HSSFColor.DARK_RED.index , HSSFColor.DARK_RED.index,
                       HSSFColor.DARK_YELLOW.index, HSSFColor.DARK_TEAL.index , HSSFColor.ROSE.index,
                       
        };
        
        HSSFCellStyle[] colorStyleArr = new HSSFCellStyle[colorArr.length];
        for(int i =0;i<colorArr.length;i++){
           colorStyleArr[i] = workbook.createCellStyle();
           colorStyleArr[i].setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
           colorStyleArr[i].setFillForegroundColor(colorArr[i]);
        }
        
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 3000);
        sheet.setColumnWidth(3, 3500);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 5000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 3500);
        sheet.setColumnWidth(8, 3000);
        sheet.setColumnWidth(9, 3000);
        sheet.setColumnWidth(10, 4000);
        
        //Grid
        int rownum =8;
        int cellnum = 0;
        
        row = sheet.createRow(rownum++);
        
        cell = row.createCell(cellnum++);
        cell.setCellValue("Country");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Country Code");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Codes");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Rate/Minute");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Vendor");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Quality");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Effective Date");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Info");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Priority");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Weight");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(cellnum++);
        cell.setCellValue("Status");
        cell.setCellStyle(boldStyle);
        
        try{
              DataBase db = new DataBase();
              PreparedStatement ps ;
              String query = "select c.country, t.country_code, t.prefix, t.sell_rate, v.vendor_name, t.quality, t.rate_date, t.rate_change, t.priority, t.weight, t.status from tmon_bidding t "
                           + "join countries c on c.id = t.country_code "
                           + "join vendor v on v.id = t.vendor_id "
                           + "where t.bid_id = ? "
                      + "ORDER BY CASE WHEN t.status = 'NO CHANGE' THEN 3 ELSE 2 END , 1";
              Connection con = db.getConnection();  
              ps = con.prepareStatement(query);
              ps.setInt(1, bidId);
              ResultSet rs = ps.executeQuery();
              
              int i=0,k=0;
              String lastCountry = "";
              HSSFCellStyle currentStyle = colorStyleArr[0];
              HashMap<String, Integer> map = new HashMap<String, Integer>();
              
              while(rs.next()){
                   row = sheet.createRow(rownum++);
                   cellnum = 0;
                    for (int j=1; j<=rs.getMetaData().getColumnCount(); j++){ 
                        cell = row.createCell(cellnum++);
                        cell.setCellValue(rs.getString(j));
                        
                        //colorStyle
                        if(j==1 ){
                            if(map.keySet().contains(rs.getString(j))){
                                currentStyle = colorStyleArr[map.get(rs.getString(j))];
                            }
                            else{
//                                System.out.println("new country: i=" + i);
                                currentStyle = colorStyleArr[k];
                                map.put(rs.getString(j), k);
                                k++;
                            }
                        }
                            
                        cell.setCellStyle(currentStyle);
                    }
                  i++;
              }
              
//              int i=0,k=0;
//              String lastCountry = "";
//              HSSFCellStyle currentStyle = colorStyleArr[0];
//              ArrayList<String> countrysList = new ArrayList<String>();
//              while(rs.next()){
//                   row = sheet.createRow(rownum++);
//                   cellnum = 0;
//                    for (int j=1; j<=rs.getMetaData().getColumnCount(); j++){ 
//                        cell = row.createCell(cellnum++);
//                        cell.setCellValue(rs.getString(j));
//                        
//                        //s
//                        if(j==1 && !countrysList.contains(rs.getString(j))){
//                            lastCountry = rs.getString(j);
//                            currentStyle = colorStyleArr[k];
//                            if(k == colorStyleArr.length -1)
//                                k=0;
//                            else
//                                k++;
//                        }
//                            
//                        cell.setCellStyle(currentStyle);
//                    }
//                  i++;
//              }
              
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        try {
            String dateTimeNow = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            FileOutputStream out = new FileOutputStream(new File(path + fileName));
            workbook.write(out);
            out.close();
            System.out.println("Excel written successfully..");
            return fileName;
        } catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }
    
}
