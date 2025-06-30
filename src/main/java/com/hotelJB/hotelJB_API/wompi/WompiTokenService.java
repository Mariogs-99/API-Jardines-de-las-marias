package com.hotelJB.hotelJB_API.wompi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WompiTokenService {

    @Value("${wompi.token.url}")
    private String tokenUrl;

    @Value("${wompi.client.id}")
    private String clientId;

    @Value("${wompi.client.secret}")
    private String clientSecret;

    @Value("${wompi.token.audience}")
    private String audience;

    private final RestTemplate restTemplate = new RestTemplate();

    private String cachedToken;
    private long expirationTimestamp = 0;

    public String getAccessToken() {
        long now = System.currentTimeMillis() / 1000;
        if (cachedToken != null && now < expirationTimestamp - 60) {
            return cachedToken;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("audience", audience);
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                request,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();

        if (responseBody == null || !responseBody.containsKey("access_token")) {
            throw new RuntimeException("No se pudo obtener token de Wompi");
        }

        String token = (String) responseBody.get("access_token");
        Integer expiresIn = (Integer) responseBody.get("expires_in");
        expirationTimestamp = now + expiresIn;

        this.cachedToken = token;

        return token;
    }
}
