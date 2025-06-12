package com.hotelJB.hotelJB_API.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;

 // Asegúrate de mover esta clase a un paquete config

import com.hotelJB.hotelJB_API.models.entities.GmailProperties;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

@Service
public class GmailApiSenderService {

    @Autowired
    private GmailProperties gmailProperties;

    public void sendMail(String to, String subject, String htmlBody) {
        try {
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setClientSecrets(gmailProperties.getClientId(), gmailProperties.getClientSecret())
                    .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                    .setJsonFactory(JacksonFactory.getDefaultInstance())
                    .build()
                    .setRefreshToken(gmailProperties.getRefreshToken());

            credential.refreshToken();

            Gmail service = new Gmail.Builder(
                    credential.getTransport(),
                    credential.getJsonFactory(),
                    credential
            ).setApplicationName("HotelJardinesMailer").build();

            // Crear el correo con Jakarta Mail
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            MimeMessage email = new MimeMessage(session);
            email.setFrom(new InternetAddress(gmailProperties.getEmailFrom()));
            email.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            email.setSubject(subject);
            email.setContent(htmlBody, "text/html; charset=utf-8");

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            email.writeTo(buffer);

            byte[] rawMessageBytes = buffer.toByteArray();
            String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);

            // Usar FQN para evitar conflicto con jakarta.mail.Message
            com.google.api.services.gmail.model.Message gmailMessage = new com.google.api.services.gmail.model.Message();
            gmailMessage.setRaw(encodedEmail);

            service.users().messages().send("me", gmailMessage).execute();

            System.out.println("✅ Correo enviado exitosamente a " + to);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error al enviar correo con Gmail API");
        }
    }
}
