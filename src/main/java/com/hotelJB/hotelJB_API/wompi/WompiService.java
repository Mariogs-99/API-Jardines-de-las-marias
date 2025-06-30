package com.hotelJB.hotelJB_API.wompi;

import com.hotelJB.hotelJB_API.models.responses.ReservationResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WompiService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String crearEnlacePago(ReservationResponse reservation) {
        String url = "https://api.wompi.sv/EnlacePago";

        Map<String, Object> payload = new HashMap<>();
        payload.put("idAplicativo", "2a0a70a7-9842-4c3a-baee-267a8b2946b2");
        payload.put("identificadorEnlaceComercio", reservation.getReservationCode());
        payload.put("monto", (int) (reservation.getPayment() * 100)); // en centavos
        payload.put("nombreProducto", "Reserva habitación");

        Map<String, Object> formaPago = new HashMap<>();
        formaPago.put("permitirTarjetaCreditoDebido", true);
        formaPago.put("permitirPagoConPuntoAgricola", true);
        formaPago.put("permitirPagoEnCuotasAgricola", false);
        formaPago.put("permitirPagoEnBitcoin", false);
        formaPago.put("permitePagoQuickPay", false);
        payload.put("formaPago", formaPago);

        Map<String, Object> infoProducto = new HashMap<>();
        infoProducto.put("descripcionProducto", "Reserva del " + reservation.getInitDate() + " al " + reservation.getFinishDate());
        infoProducto.put("urlImagenProducto", "https://tuhotel.com/images/habitacion.jpg");
        payload.put("infoProducto", infoProducto);

        Map<String, Object> configuracion = new HashMap<>();
        configuracion.put("urlRedirect", "https://tuhotel.com/pago-exitoso");
        configuracion.put("urlWebhook", "https://hoteljardin.loca.lt/webhook-wompi");
        configuracion.put("esMontoEditable", false);
        configuracion.put("esCantidadEditable", false);
        configuracion.put("cantidadPorDefecto", 1);
        configuracion.put("duracionInterfazIntentoMinutos", 60);
        configuracion.put("notificarTransaccionCliente", true);
        payload.put("configuracion", configuracion);

        Map<String, Object> vigencia = new HashMap<>();
        vigencia.put("fechaInicio", reservation.getInitDate().atStartOfDay().toString());
        vigencia.put("fechaFin", reservation.getFinishDate().atTime(23, 59).toString());
        payload.put("vigencia", vigencia);

        Map<String, Object> limites = new HashMap<>();
        limites.put("cantidadMaximaPagosExitosos", 0);
        limites.put("cantidadMaximaPagosFallidos", 0);
        payload.put("limitesDeUso", limites);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        Map<String, Object> responseBody = response.getBody();

        if (responseBody != null && responseBody.containsKey("urlEnlace")) {
            return (String) responseBody.get("urlEnlace");
        } else {
            return null;
        }
    }
}
