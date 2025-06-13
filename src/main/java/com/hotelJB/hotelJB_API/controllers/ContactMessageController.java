package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.ContactMessageDTO;
import com.hotelJB.hotelJB_API.services.GmailApiSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact-message")
@CrossOrigin
public class ContactMessageController {

    @Autowired
    private GmailApiSenderService gmailApiSenderService;

    @PostMapping("/send")
    public void sendContactMessage(@RequestBody ContactMessageDTO data) {
        String html = String.format("""
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="UTF-8">
          <style>
            body {
              font-family: 'Segoe UI', sans-serif;
              background-color: #f5f5f5;
              padding: 30px;
              color: #333;
            }
            .container {
              background-color: #ffffff;
              padding: 30px;
              border-radius: 10px;
              max-width: 700px;
              margin: auto;
              box-shadow: 0 0 12px rgba(0,0,0,0.1);
            }
            h2 {
              color: #8B5A2B;
              margin-bottom: 20px;
              font-size: 1.5rem;
            }
            .section-title {
              font-weight: 600;
              margin-top: 20px;
              font-size: 1rem;
              color: #444;
            }
            .info {
              background-color: #f9f9f9;
              padding: 15px 20px;
              border-radius: 8px;
              margin-top: 10px;
              font-size: 0.95rem;
            }
            .info p {
              margin: 6px 0;
            }
            .highlight {
              color: #8B5A2B;
              font-weight: 600;
            }
            .footer {
              margin-top: 30px;
              font-size: 0.85rem;
              color: #777;
              text-align: center;
            }
          </style>
        </head>
        <body>
          <div class="container">
            <h2>📩 Nueva solicitud de cotización</h2>

            <div class="section-title">Datos del cliente:</div>
            <div class="info">
              <p><span class="highlight">Nombre:</span> %s</p>
              <p><span class="highlight">Teléfono:</span> %s</p>
              <p><span class="highlight">Correo:</span> %s</p>
              <p><span class="highlight">Marca:</span> %s</p>
            </div>

            <div class="section-title">Detalles de la solicitud:</div>
            <div class="info">
              <p><span class="highlight">Fechas posibles:</span> %s</p>
              <p><span class="highlight">Presupuesto estimado:</span> %s</p>
              <p><span class="highlight">Mensaje:</span></p>
              <p style="margin-left: 10px;">%s</p>
            </div>

            <div class="footer">
              Este correo fue generado automáticamente desde el formulario de contacto de <strong>Hotel Jardines de las Marías</strong>.
            </div>
          </div>
        </body>
        </html>
        """,
                data.getName(),
                data.getPhone(),
                data.getEmail() != null ? data.getEmail() : "(no especificado)",
                data.getBrand() != null ? data.getBrand() : "(no especificado)",
                data.getDates(),
                data.getBudget() != null ? data.getBudget() : "(no especificado)",
                data.getMessage() != null ? data.getMessage() : "(sin mensaje)"
        );

        gmailApiSenderService.sendMail(
                "escobar.mario@globalsolutionslt.com",  // correo destino
                "📋 Nueva cotización recibida - Jardines de las Marías",
                html
        );
    }
}
