package com.ysf.api.gateway.filter;

import com.ysf.api.gateway.util.CorrelationIdFilterUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Component
public class ResponsePostFilter implements GlobalFilter, Ordered {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("============ RUNNING POST FILTER ================");

        ServerHttpResponse response = exchange.getResponse();

        response.beforeCommit(() -> {
            HttpHeaders responseHeaders = response.getHeaders();

            if (CorrelationIdFilterUtil.getCorrelationId(responseHeaders) == null) {
                HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                String correlationId = CorrelationIdFilterUtil.getCorrelationId(requestHeaders);

                log.info("Setting correlation id in response header: " + correlationId);

                responseHeaders.set(CorrelationIdFilterUtil.CORRELATION_ID_KEY, correlationId);
            }

            return Mono.empty();
        });

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
