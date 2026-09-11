package com.example.config;

import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;

/**
 * Builds a {@link RestAssuredConfig} with explicit, finite HTTP timeouts.
 *
 * Deterministic timeouts are the single most important anti-flakiness lever for
 * API tests: without them a slow or hung endpoint causes indefinite waits and
 * intermittent, hard-to-reproduce failures. We never use Thread.sleep — the
 * client itself enforces connect + socket deadlines.
 */
public final class RestAssuredConfigFactory {

    private RestAssuredConfigFactory() { }

    public static RestAssuredConfig timeoutConfig() {
        return RestAssuredConfig.config().httpClient(
                HttpClientConfig.httpClientConfig()
                        // time to establish the TCP connection
                        .setParam("http.connection.timeout", EnvConfig.connectTimeoutMs())
                        // time to wait for data once connected
                        .setParam("http.socket.timeout", EnvConfig.socketTimeoutMs()));
    }
}
