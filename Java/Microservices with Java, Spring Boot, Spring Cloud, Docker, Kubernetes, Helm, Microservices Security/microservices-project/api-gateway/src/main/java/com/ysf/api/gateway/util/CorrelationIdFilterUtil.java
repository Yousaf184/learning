package com.ysf.api.gateway.util;

import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CorrelationIdFilterUtil {

    public static final String CORRELATION_ID_KEY = "eazybank-correlation-id";

    public static String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

    public static String getCorrelationId(HttpHeaders requestHeaders) {
        List<String> correlationIdHeaderList = requestHeaders.get(CORRELATION_ID_KEY);

        if (correlationIdHeaderList == null) {
            return null;
        }

        Optional<String> correlationId = correlationIdHeaderList.stream().findFirst();
        return correlationId.orElse(null);
    }
}
