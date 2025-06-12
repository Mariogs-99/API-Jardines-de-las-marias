package com.hotelJB.hotelJB_API.services;

import okhttp3.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
public class PayPalClient {

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.api-base}")
    private String baseUrl;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public String getAccessToken() throws IOException {
        String credential = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

        Request request = new Request.Builder()
                .url(baseUrl + "/v1/oauth2/token")
                .post(RequestBody.create("grant_type=client_credentials", MediaType.get("application/x-www-form-urlencoded")))
                .header("Authorization", "Basic " + credential)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error obteniendo token PayPal: " + response.code() + " -> " + response.message());
            }

            String responseBody = response.body().string();
            JsonNode jsonNode = mapper.readTree(responseBody);
            return jsonNode.get("access_token").asText();
        }
    }

    public String createOrder(double total) throws IOException {
        String accessToken = getAccessToken();

        String jsonBody = "{\n" +
                "  \"intent\": \"CAPTURE\",\n" +
                "  \"purchase_units\": [\n" +
                "    {\n" +
                "      \"amount\": {\n" +
                "        \"currency_code\": \"USD\",\n" +
                "        \"value\": \"" + total + "\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        Request request = new Request.Builder()
                .url(baseUrl + "/v2/checkout/orders")
                .post(RequestBody.create(jsonBody, MediaType.get("application/json")))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error creando orden PayPal: " + response.code() + " -> " + response.message());
            }

            return response.body().string();
        }
    }
}
