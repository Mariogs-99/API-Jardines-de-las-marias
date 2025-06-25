package com.hotelJB.hotelJB_API.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {

    private static final Dotenv dotenv = Dotenv.load();

    // Gmail
    public static String getGmailClientId() {
        return dotenv.get("GOOGLE_OAUTH_CLIENT_ID");
    }

    public static String getGmailClientSecret() {
        return dotenv.get("GOOGLE_OAUTH_CLIENT_SECRET");
    }

    public static String getGmailRefreshToken() {
        return dotenv.get("GOOGLE_OAUTH_REFRESH_TOKEN");
    }

    public static String getGmailFromEmail() {
        return dotenv.get("GMAIL_EMAIL_FROM");
    }

    // PayPal
    public static String getPaypalClientId() {
        return dotenv.get("PAYPAL_CLIENT_ID");
    }

    public static String getPaypalSecret() {
        return dotenv.get("PAYPAL_SECRET");
    }

    public static String getPaypalApiBase() {
        return dotenv.get("PAYPAL_API_BASE");
    }
}
