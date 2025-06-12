package com.hotelJB.hotelJB_API.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.hotelJB.hotelJB_API.models.entities.GmailProperties;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class GmailSenderService {

    private final GmailProperties gmailProperties;

    @Autowired
    public GmailSenderService(GmailProperties gmailProperties) {
        this.gmailProperties = gmailProperties;
    }

    public void sendMail(String to, String subject, String body) {
        try {
            Credential credential = new GoogleCredential.Builder()
                    .setClientSecrets(gmailProperties.getClientId(), gmailProperties.getClientSecret())
                    .setJsonFactory(JacksonFactory.getDefaultInstance())
                    .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                    .build()
                    .setRefreshToken(gmailProperties.getRefreshToken());

            credential.refreshToken();
            String accessToken = credential.getAccessToken();

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(gmailProperties.getEmailFrom()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");

            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", gmailProperties.getEmailFrom(), accessToken);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            System.out.println("✅ Correo enviado a: " + to);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error al enviar el correo");
        }
    }
}
