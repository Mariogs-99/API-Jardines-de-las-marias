package com.hotelJB.hotelJB_API.wompi;

import com.hotelJB.hotelJB_API.models.dtos.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Service
public class WompiService {

    @Value("${wompi.api.url}")
    private String wompiApiUrl;

    @Value("${wompi.api.app-id}")
    private String wompiAppId;

    @Value("${wompi.api.redirect-url}")
    private String redirectUrl;

    @Value("${wompi.api.webhook-url}")
    private String webhookUrl;

    @Value("${wompi.api.bearer-token:}") // opcional
    private String bearerToken;

    @Autowired
    private final WompiTokenService wompiTokenService;

    private final RestTemplate restTemplate = new RestTemplate();

    public WompiService(WompiTokenService wompiTokenService) {
        this.wompiTokenService = wompiTokenService;
    }

    public String crearEnlacePago(ReservationDTO dto, String tempReference, String reservationCode) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("idAplicativo", wompiAppId);
        payload.put("identificadorEnlaceComercio", tempReference);

        // Deja el monto en dólares con centavos
        double monto = Math.round(dto.getPayment() * 100.0) / 100.0;
        payload.put("monto", monto);

        String nombreProducto = "Reserva habitación para " + dto.getName();
        payload.put("nombreProducto", nombreProducto);

        Map<String, Object> formaPago = new HashMap<>();
        formaPago.put("permitirTarjetaCreditoDebido", true);
        formaPago.put("permitirPagoConPuntoAgricola", true);
        formaPago.put("permitirPagoEnCuotasAgricola", false);
        formaPago.put("permitirPagoEnBitcoin", false);
        formaPago.put("permitePagoQuickPay", false);
        payload.put("formaPago", formaPago);

        Map<String, Object> infoProducto = new HashMap<>();
        infoProducto.put("descripcionProducto", "Reserva del " + dto.getInitDate() + " al " + dto.getFinishDate());
        infoProducto.put("urlImagenProducto", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQLo8t9NH1j1eo_tGo70lM2OcYKY4mhwhntvA&s");
        payload.put("infoProducto", infoProducto);

        String redirectUrlWithParam =
                redirectUrl
                        + "?tempReference=" + tempReference
                        + "&reservationCode=" + reservationCode;

        Map<String, Object> configuracion = new HashMap<>();
        configuracion.put("urlRedirect", redirectUrlWithParam);
        configuracion.put("urlWebhook", webhookUrl);
        configuracion.put("esMontoEditable", false);
        configuracion.put("esCantidadEditable", false);
        configuracion.put("cantidadPorDefecto", 1);
        configuracion.put("duracionInterfazIntentoMinutos", 60);
        configuracion.put("notificarTransaccionCliente", true);
        payload.put("configuracion", configuracion);

        Map<String, Object> limites = new HashMap<>();
        limites.put("cantidadMaximaPagosExitosos", 1);
        limites.put("cantidadMaximaPagosFallidos", 0);
        payload.put("limitesDeUso", limites);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String token = wompiTokenService.getAccessToken();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(wompiApiUrl, entity, Map.class);
        Map<String, Object> responseBody = response.getBody();

        if (responseBody != null && responseBody.containsKey("urlEnlace")) {
            return (String) responseBody.get("urlEnlace");
        } else {
            return null;
        }
    }




}
