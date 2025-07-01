package com.hotelJB.hotelJB_API.services;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    public void sendMail(String to, String subject, String htmlBody) {
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");

            String jsonBody = String.format("""
                {
                  "sender": {
                    "email": "%s",
                    "name": "%s"
                  },
                  "to": [
                    {
                      "email": "%s"
                    }
                  ],
                  "subject": "%s",
                  "htmlContent": "%s"
                }
                """, senderEmail, senderName, to, subject, htmlBody.replace("\"", "\\\""));

            RequestBody body = RequestBody.create(mediaType, jsonBody);

            Request request = new Request.Builder()
                    .url("https://api.brevo.com/v3/smtp/email")
                    .post(body)
                    .addHeader("api-key", apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.out.println("❌ Error al enviar correo Brevo: " + response.body().string());
                    throw new RuntimeException("Brevo API error: " + response);
                }
                System.out.println("✅ Correo enviado correctamente vía Brevo a: " + to);
            }
        } catch (Exception e) {
            System.out.println("❌ Excepción enviando correo Brevo:");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
