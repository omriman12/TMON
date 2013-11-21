package com.telzar.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PDFTool {
    
    private final static String LOG_TAG = "PDFTool: ";
    
    public static String CreatePDF(String path, String vars){
        System.out.println(LOG_TAG +"inside CreatePDF");
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +".pdf";
        PDDocument doc = null;
        PDPage page = null;

        try{
           doc = new PDDocument();
           page = new PDPage();

           doc.addPage(page);
           PDFont font = PDType1Font.HELVETICA_BOLD;

           PDPageContentStream content = new PDPageContentStream(doc, page);
           content.beginText();
           content.setFont( font, 12 );
           
           content.moveTextPositionByAmount( 50, 725 );
           content.drawString(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

           content.setFont( font, 20 );
           content.moveTextPositionByAmount( 200, -75 );
           content.drawString("Price Inquery");
           content.endText();

           drawTable(page, content, 600, 100, vars);

           content.beginText();
           content.moveTextPositionByAmount( 100, 500 );
           content.drawString("This Is an inquery");
           content.endText();

           content.close();
           doc.save(path + fileName);
           doc.close();
           return fileName;
        } catch (Exception e){
            System.out.println(e);
            return "fail";
        }
        
    }
    
    public static void drawTable(PDPage page, PDPageContentStream contentStream, float y, float margin, String var)  {
        String[] rowSplit = var.split(";");
        String[] columnSplit = rowSplit[0].split(",");
        final int rows = rowSplit.length;
        final int cols = columnSplit.length;
        final float rowHeight = 20f;
        final float tableWidth = page.findMediaBox().getWidth() - margin - margin;
        final float tableHeight = rowHeight * rows;
        final float colWidth = tableWidth/(float)cols;
        final float cellMargin=5f;
        
        
        try{
            //draw the rows
            float nexty = y ;
            for (int i = 0; i <= rows; i++) {
                contentStream.drawLine(margin, nexty, margin+tableWidth, nexty);
                nexty-= rowHeight;
            }

            //draw the columns
            float nextx = margin;
            for (int i = 0; i <= cols; i++) {
                contentStream.drawLine(nextx, y, nextx, y-tableHeight);
                nextx += colWidth;
            }

            //now add the text        
            contentStream.setFont( PDType1Font.HELVETICA_BOLD , 12 );        

            float textx = margin+cellMargin;
            float texty = y-15;  

            
            for (int i=0; i< rowSplit.length; i++){
                columnSplit = rowSplit[i].split(",");
                for (int j=0; j<columnSplit.length; j++){
                    String text = columnSplit[j];
                    contentStream.beginText();
                    contentStream.moveTextPositionByAmount(textx,texty);
                    contentStream.drawString(text);
                    contentStream.endText();
                    textx += colWidth;
                }
                texty-=rowHeight;
                textx = margin+cellMargin;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
