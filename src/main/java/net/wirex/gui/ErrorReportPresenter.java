package net.wirex.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JDialog;
import javax.swing.JPanel;
import net.wirex.AppEngine;
import net.wirex.Invoker;
import net.wirex.annotations.Access;
import net.wirex.annotations.Dispose;
import net.wirex.annotations.Fire;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;

/**
 *
 * @author Ritchie Borja
 */
public class ErrorReportPresenter extends Presenter {

    @Access
    private DetailModel details;

    public ErrorReportPresenter(Model model, JPanel panel) {
        super(model, panel);
    }

    public void send() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.visp.net");
        props.put("mail.smtp.port", "587");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("rborja@visp.net", "YQetyZ4zFy");
                    }
                });
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("test@bug.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("rborja@visp.net"));
            message.setSubject("Error Reporting");
            Multipart multipart = new MimeMultipart();
            BodyPart messageBodyPart = new MimeBodyPart();
            String myMessage = "Reporter: " + details.getReporter() + "\n"
                    + "Organization: " + details.getOrganization() + "\n"
                    + "Comments: " + details.getComment() + "\n\n"
                    + AppEngine.getErrorMessage();
            messageBodyPart.setText(myMessage);
            multipart.addBodyPart(messageBodyPart);
            addAttachment(multipart, new MimeBodyPart());

            message.setContent(multipart);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(ErrorReportPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }

    private static void addAttachment(Multipart multipart, BodyPart messageBodyPart) throws MessagingException, IOException {
        BufferedImage image = AppEngine.getScreenshot();
        File file = new File("screenshot.png");
        ImageIO.write(image, "png", file);
        DataSource source = new FileDataSource(file);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName("screenshot.png");
        multipart.addBodyPart(messageBodyPart);
    }

    @Dispose
    public void close() {

    }

    @Fire(view = DetailPanel.class, type = JDialog.class)
    public void details() {

    }

    @Override
    public void run(ConcurrentHashMap<String, Invoker> methods) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
