package com.hotelJB.hotelJB_API.config;

public class EnvConfig {

    // Gmail
    public static String getGmailClientId() {
        return System.getenv("GOOGLE_OAUTH_CLIENT_ID");
    }

    public static String getGmailClientSecret() {
        return System.getenv("GOOGLE_OAUTH_CLIENT_SECRET");
    }

    public static String getGmailRefreshToken() {
        return System.getenv("GOOGLE_OAUTH_REFRESH_TOKEN");
    }

    public static String getGmailFromEmail() {
        return System.getenv("GMAIL_EMAIL_FROM");
    }

    // PayPal
    public static String getPaypalClientId() {
        return System.getenv("PAYPAL_CLIENT_ID");
    }

    public static String getPaypalSecret() {
        return System.getenv("PAYPAL_SECRET");
    }

    public static String getPaypalApiBase() {
        return System.getenv("PAYPAL_API_BASE");
    }
}
