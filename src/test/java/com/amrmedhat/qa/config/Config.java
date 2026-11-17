package com.amrmedhat.qa.config;

import java.time.Duration;

/** Central configuration. Every value can be overridden with a JVM system property (-Dkey=value). */
public final class Config {

    private Config() {
    }

    public static String baseUrl() {
        return System.getProperty("baseUrl", "https://the-internet.herokuapp.com");
    }

    public static String browser() {
        return System.getProperty("browser", "chrome").toLowerCase();
    }

    public static boolean headless() {
        return Boolean.parseBoolean(System.getProperty("headless", "true"));
    }

    public static Duration explicitWait() {
        return Duration.ofSeconds(Long.parseLong(System.getProperty("waitSeconds", "10")));
    }
}
