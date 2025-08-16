package com.ysf.api.gateway.filter;

import com.ysf.api.gateway.util.CorrelationIdFilterUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Component
public class ResponsePostFilter implements GlobalFilter, Ordered {

    private final Logger log = Logger.getLogger(getClass().getName());
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .then(Mono.just(exchange))
                .map((serverWebExchange) -> {
                    HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                    String correlationId = CorrelationIdFilterUtil.getCorrelationId(requestHeaders);
                    log.info("Setting correlation id in response header: " + correlationId);

                    serverWebExchange.getResponse()
                            .getHeaders().set(CorrelationIdFilterUtil.CORRELATION_ID_KEY, correlationId);
                    return serverWebExchange;
                })
                .then();
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
