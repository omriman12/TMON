package com.telzar.tools;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailTool {
    private final static String LOG_TAG = "EmailTool: ";
    
    public void sendEmail(String emailTo, String mailFrom, String attachedFilePath, String attachedFileName, String title){
      System.out.println(LOG_TAG +"inside sendEmail");

      Properties props = System.getProperties();
      props.setProperty("mail.smtp.host", "mail.telzar-019.co.il"); //smtps
      props.setProperty("mail.smtp.port", "25");
      props.setProperty("mail.smtp.auth", "true"); //smtps

      Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("invoice@telzar-019.co.il", "1q2w3e4r@");
                }
            });

      try{
         MimeMessage message = new MimeMessage(session);
         message.setFrom(new InternetAddress(mailFrom));
         message.addRecipient(Message.RecipientType.TO,
                                  new InternetAddress(emailTo));
         message.setSubject(title); //
         //message.setText("Message Body");
         
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        Multipart multipart = new MimeMultipart();

        String file = attachedFilePath + attachedFileName;
        String fileName = attachedFileName;
        DataSource source = new FileDataSource(file);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileName);
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);

        Transport.send(message);

        System.out.println("Sent message successfully to: " + emailTo);
      }catch (Exception mex) {
          System.out.println("Got Exexption in Email sending");
         mex.printStackTrace();
      }
    }
    
    public void sendRouteEmail(String emailTo ,String title ,String body){
      System.out.println(LOG_TAG +"inside sendRouteEmail");

      Properties props = System.getProperties();
      props.setProperty("mail.smtp.host", "mail.telzar-019.co.il");
      props.setProperty("mail.smtp.port", "25");
      props.setProperty("mail.smtp.auth", "true");
      
      Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("invoice@telzar-019.co.il", "1q2w3e4r@");
                }
            });

      try{
         MimeMessage message = new MimeMessage(session);
         message.setFrom(new InternetAddress("invoice@telzar-019.co.il"));
         message.addRecipient(Message.RecipientType.TO,
                                  new InternetAddress(emailTo));
         message.setSubject(title);
         
         String newBody = "";
        
         String[] columnSplit;
         String[] rowSplit = body.split(";");
         String[] firstRowColumns = rowSplit[0].split(",");
         for (int i=1; i<rowSplit.length; i++){
            columnSplit = rowSplit[i].split(",");
            for (int j=0; j<columnSplit.length; j++){ 
                newBody += firstRowColumns[j] + " = " + columnSplit[j];
                if (j+1 != columnSplit.length)
                    newBody += ",   ";
            }
            newBody += ";";
         }
         System.out.println(newBody);
         
         message.setText(newBody.replace(";", "\n\r"), "utf-8");
         
         Transport.send(message);

         System.out.println("Sent message successfully..");
      }catch (Exception mex) {
         mex.printStackTrace();
      }
    }
    
    public void sendBidRouteEmail(String emailTo ,String title ,String body){
      System.out.println(LOG_TAG +"inside sendBidRouteEmail: " + body);

      Properties props = System.getProperties();
      props.setProperty("mail.smtp.host", "mail.telzar-019.co.il");
      props.setProperty("mail.smtp.port", "25");
      props.setProperty("mail.smtp.auth", "true");
      
      Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("invoice@telzar-019.co.il", "1q2w3e4r@");
                }
            });

      try{
         MimeMessage message = new MimeMessage(session);
         message.setFrom(new InternetAddress("invoice@telzar-019.co.il"));
         message.addRecipient(Message.RecipientType.TO,
                                  new InternetAddress(emailTo));
         message.setSubject(title);
         
         String newBody = "";
        
         String[] columnSplit;
         String[] rowSplit = body.split(";");
         String[] firstRowColumns = rowSplit[0].split(",");
         for (int i=1; i<rowSplit.length; i++){
            columnSplit = rowSplit[i].split(",");
            for (int j=0; j<columnSplit.length; j++){ 
                newBody += firstRowColumns[j] + " = " + columnSplit[j];
                if (j+1 != columnSplit.length)
                    newBody += ",   ";
            }
            newBody += ";";
         }
         System.out.println(newBody);
         
         message.setText(body.replace(";", "\n\r"), "utf-8");
         
         Transport.send(message);

         System.out.println("Sent message successfully..");
      }catch (Exception mex) {
         mex.printStackTrace();
      }
    }
}
