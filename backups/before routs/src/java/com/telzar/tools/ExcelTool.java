package com.telzar.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author omri
 */
public class ExcelTool {
    public String createExel(String path,String gridVars){
        System.out.println("inside CreatePDF");
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +".xls";
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sample sheet");
        HSSFCellStyle boldStyle = workbook.createCellStyle();
        HSSFFont my_font=workbook.createFont();
        my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        boldStyle.setFont(my_font);
        
        Row row = null;
        Cell cell = null;
        row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("TMon Bidding");
        cell.setCellStyle(boldStyle);
        
        
        int rownum =3, columnsInGrid = 0;
        String[] columnSplit;
        String[] rowSplit = gridVars.split(";");
        for (int i=0; i< rowSplit.length; i++){
            columnSplit = rowSplit[i].split(",");
            row = sheet.createRow(rownum++);
            columnsInGrid = columnSplit.length; 
            int cellnum = 1;
            for (int j=0; j<columnSplit.length; j++){ 
                cell = row.createCell(cellnum++);
                cell.setCellValue((String)columnSplit[j]);
                if(i==0)
                    cell.setCellStyle(boldStyle);
            }
        }
        
        for (int i = 0; i <columnsInGrid; i++){
            sheet.autoSizeColumn(i);
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
