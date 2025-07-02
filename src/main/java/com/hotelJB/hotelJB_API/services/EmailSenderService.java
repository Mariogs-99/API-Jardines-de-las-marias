package com.hotelJB.hotelJB_API.services;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class EmailSenderService {

    @Value("${mailjet.api.key}")
    private String apiKey;

    @Value("${mailjet.api.secret}")
    private String apiSecret;

    @Value("${mailjet.sender.email}")
    private String senderEmail;

    @Value("${mailjet.sender.name}")
    private String senderName;

    public void sendMail(String to, String subject, String htmlBody) {
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");

            // ✅ Escapar saltos de línea y comillas
            String escapedHtmlBody = htmlBody
                    .replace("\"", "\\\"")
                    .replace("\n", "")
                    .replace("\r", "");

            String jsonBody = String.format("""
            {
              "Messages": [
                {
                  "From": {
                    "Email": "%s",
                    "Name": "%s"
                  },
                  "To": [
                    {
                      "Email": "%s",
                      "Name": "%s"
                    }
                  ],
                  "Subject": "%s",
                  "TextPart": "%s",
                  "HTMLPart": "%s"
                }
              ]
            }
            """,
                    senderEmail,
                    senderName,
                    to,
                    "Destinatario",
                    subject,
                    "Este es un mensaje alternativo en texto plano.",
                    escapedHtmlBody
            );

            RequestBody body = RequestBody.create(mediaType, jsonBody);

            String credentials = apiKey + ":" + apiSecret;
            String basicAuth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());

            Request request = new Request.Builder()
                    .url("https://api.mailjet.com/v3.1/send")
                    .post(body)
                    .addHeader("Authorization", basicAuth)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body().string();

                if (!response.isSuccessful()) {
                    System.out.println("❌ Error al enviar correo Mailjet:");
                    System.out.println("HTTP Status: " + response.code());
                    System.out.println("Response Body: " + responseBody);
                    throw new RuntimeException("Mailjet API error: " + responseBody);
                }

                System.out.println("✅ Correo enviado correctamente vía Mailjet a: " + to);
                System.out.println("Response Body: " + responseBody);
            }
        } catch (Exception e) {
            System.out.println("❌ Excepción enviando correo Mailjet:");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
