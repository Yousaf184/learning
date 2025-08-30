package com.ysf.api.gateway.filter;

import com.ysf.api.gateway.util.CorrelationIdFilterUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Component
public class RequestTracePreFilter implements GlobalFilter, Ordered {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("============ RUNNING PRE FILTER ================");
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        String correlationId = CorrelationIdFilterUtil.getCorrelationId(requestHeaders);

        if (correlationId != null) {
            log.info(this.getClass().getName() + " :: existing Correlation id:" + correlationId);
            return chain.filter(exchange);
        } else {
            String generatedCorrelationId = CorrelationIdFilterUtil.generateCorrelationId();

            log.info(this.getClass().getName() + " :: generated new Correlation id: " + generatedCorrelationId);

            ServerHttpRequest.Builder mutatedRequestBuilder = exchange.getRequest().mutate()
                    .header(CorrelationIdFilterUtil.CORRELATION_ID_KEY, generatedCorrelationId);

            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(mutatedRequestBuilder.build())
                    .build();
            return chain.filter(mutatedExchange);
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
