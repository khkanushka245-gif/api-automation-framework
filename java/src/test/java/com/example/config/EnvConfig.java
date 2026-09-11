package com.example.config;

/**
 * Central environment configuration.
 *
 * Values are resolved in this order: JVM system property (-Dkey=...), then
 * environment variable, then a safe default. This keeps tests portable across
 * local runs and CI without hardcoding anything in test bodies.
 *
 * Anti-flakiness: timeouts are explicit and finite so a slow/hung endpoint
 * fails fast and deterministically instead of blocking the whole suite.
 */
public final class EnvConfig {

    private EnvConfig() { }

    /** Base URI of the system under test (public sample API by default). */
    public static String baseUri() {
        return resolve("baseUri", "BASE_URI", "https://jsonplaceholder.typicode.com");
    }

    /** Optional bearer token; when blank, no Authorization header is added. */
    public static String authToken() {
        return resolve("authToken", "AUTH_TOKEN", "");
    }

    /** TCP connect timeout in milliseconds. */
    public static int connectTimeoutMs() {
        return resolveInt("connectTimeoutMs", "CONNECT_TIMEOUT_MS", 5000);
    }

    /** Socket read timeout in milliseconds. */
    public static int socketTimeoutMs() {
        return resolveInt("socketTimeoutMs", "SOCKET_TIMEOUT_MS", 15000);
    }

    /** Max retries for transient failures (network/5xx). */
    public static int maxRetries() {
        return resolveInt("maxRetries", "MAX_RETRIES", 2);
    }

    // ---- resolution helpers -------------------------------------------------

    private static String resolve(String sysProp, String envVar, String def) {
        String v = System.getProperty(sysProp);
        if (v == null || v.isBlank()) v = System.getenv(envVar);
        return (v == null || v.isBlank()) ? def : v.trim();
    }

    private static int resolveInt(String sysProp, String envVar, int def) {
        String v = resolve(sysProp, envVar, String.valueOf(def));
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
