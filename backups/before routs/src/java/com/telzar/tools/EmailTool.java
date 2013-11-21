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
    
    public static void sendEmail(String emailTo, String attachedFilePath, String attachedFileName){
      System.out.println(LOG_TAG +"inside sendEmail");

      Properties props = System.getProperties();
      props.setProperty("mail.smtps.host", "mail.telzar-019.co.il");
      props.setProperty("mail.smtp.port", "25");
      props.setProperty("mail.smtps.auth", "true");

      Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("invoice@telzar-019.co.il", "1q2w3e4r");
                }
            });

      try{
         MimeMessage message = new MimeMessage(session);
         message.setFrom(new InternetAddress("invoice@telzar-019.co.il"));
         message.addRecipient(Message.RecipientType.TO,
                                  new InternetAddress(emailTo));
         message.setSubject("TMon Bidding");
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

        System.out.println("Sent message successfully....");
      }catch (Exception mex) {
          System.out.println("Got Exexption in Email sending");
         mex.printStackTrace();
      }
    }
    
    public static void sendEmail(String emailTo ,String Bids ,String biddingCountryCode ,String biddingOperatorPrefix){
      System.out.println(LOG_TAG +"inside sendEmail");

      Properties props = System.getProperties();
      props.setProperty("mail.smtps.host", "mail.telzar-019.co.il");
      props.setProperty("mail.smtp.port", "25");
      props.setProperty("mail.smtps.auth", "true");
      
      Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("invoice@telzar-019.co.il", "1q2w3e4r");
                }
            });

      try{
         MimeMessage message = new MimeMessage(session);
         message.setFrom(new InternetAddress("invoice@telzar-019.co.il"));
         message.addRecipient(Message.RecipientType.TO,
                                  new InternetAddress(emailTo));
         message.setSubject("TMon Biddings Aprroved For Operator: " + biddingCountryCode + ", " + biddingOperatorPrefix);
         message.setContent("<h1>Bid Ids Approved: " + Bids + " </h1>","text/html" );
         
         Transport.send(message);

         System.out.println("Sent message successfully..");
      }catch (Exception mex) {
         mex.printStackTrace();
      }
    }
}
