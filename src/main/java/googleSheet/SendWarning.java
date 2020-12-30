package googleSheet;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class SendWarning {

    SBMT_Sheet sbmt_sheet = new SBMT_Sheet();
    String startDate = sbmt_sheet.getStartDate();
    double totalMileage = sbmt_sheet.getTotalMileage();

    public SendWarning() throws IOException, GeneralSecurityException {}

    public void writeNotification() throws IOException {
        File file = new File( "src/main/resources/txt/mileageWarning.txt" );
        FileWriter writer = new FileWriter(file);
        writer.write( "\nYou've added 250+ miles! Time for a belt change." );
        writer.write( "\n\nCurrent mileage since " + startDate + " : " + totalMileage + " mi" );
        writer.write( "\n\nTeam SB MileageTrack" );
        writer.close();
    }

    public void sendNotificationAsEmail(String userEmail) throws MessagingException, IOException {
        writeNotification();
        Multipart emailContent = new MimeMultipart();
        MimeBodyPart textBodyPart = new MimeBodyPart();
        MimeMessage msg = new MimeMessage(establishEmailSession());
        String companyEmail = "sb_mileagetrack@yahoo.com";
        msg.setFrom(new InternetAddress(companyEmail));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
        msg.setSubject("Mileage Warning!");
        String fileName = "src/main/resources/txt/mileageWarning.txt";
        DataSource source = new FileDataSource(fileName);
        textBodyPart.setDataHandler(new DataHandler(source));
        emailContent.addBodyPart(textBodyPart);
        msg.setContent(emailContent);
        Transport.send(msg);
    }

    public void sendNotificationAsTextMSG(String phoneNumber, String userCarrier) throws MessagingException,
            IOException {
        writeNotification();
        switch (userCarrier) {
            case "AT&T" -> sendNotificationAsEmail(phoneNumber + "@mms.att.net");
            case "Sprint" -> sendNotificationAsEmail(phoneNumber + "@pm.sprint.com");
            case "Verizon" -> sendNotificationAsEmail(phoneNumber + "@vzwpix.com");
            case "T-Mobile" -> sendNotificationAsEmail(phoneNumber + "@tmomail.net");
        }
    }

    public static Properties setEmailProperties() {
        Properties props = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.mail.yahoo.com");
        props.put("mail.smtp.port", "587");
        return props;
    }

    private static Session establishEmailSession() {
        return Session.getInstance(setEmailProperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication("sb_mileagetrack","lgcc sxje zhio ahbh");
            }
        });
    }
}
