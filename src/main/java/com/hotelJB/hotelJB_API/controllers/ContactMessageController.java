package com.hotelJB.hotelJB_API.controllers;

import com.hotelJB.hotelJB_API.models.dtos.ContactMessageDTO;
import com.hotelJB.hotelJB_API.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact-message")
@CrossOrigin
public class ContactMessageController {

    @Autowired
    private EmailSenderService emailSenderService;

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
              background-color: #f6f6f6;
              padding: 30px;
              color: #333;
            }
            .container {
              background-color: #ffffff;
              padding: 30px;
              border-radius: 10px;
              max-width: 700px;
              margin: auto;
              box-shadow: 0 0 12px rgba(0,0,0,0.05);
            }
            .logo {
              text-align: center;
              margin-bottom: 30px;
            }
            .logo img {
              height: 90px;
            }
            h2 {
              color: #4A8B2C;
              font-size: 1.6rem;
              margin-bottom: 20px;
              text-align: center;
            }
            .section-title {
              font-weight: 600;
              font-size: 1rem;
              color: #6A4A3C;
              margin-top: 25px;
              margin-bottom: 8px;
            }
            .info {
              background-color: #fefefe;
              border: 1px solid #eee;
              padding: 15px 20px;
              border-radius: 8px;
              font-size: 0.95rem;
            }
            .info p {
              margin: 8px 0;
            }
            .highlight {
              color: #4A8B2C;
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
            <div class="logo">
              <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQLo8t9NH1j1eo_tGo70lM2OcYKY4mhwhntvA&s" alt="Hotel Jardines de las Marías" />
            </div>

            <h2>Nueva solicitud de cotización</h2>

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

        emailSenderService.sendMail(
                "pruebajardin@jardindelasmarias.com",
                "Nueva cotización recibida - Jardines de las Marías",
                html
        );
    }
}
