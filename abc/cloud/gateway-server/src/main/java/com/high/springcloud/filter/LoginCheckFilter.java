package com.high.springcloud.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Component
public class LoginCheckFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 权限校验
        String token = exchange.getRequest().getHeaders().getFirst("token");

        // 有token放行
        if (!StringUtils.hasText(token)) {
            return chain.filter(exchange);
        }

        // 无token拦截
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", HttpStatus.UNAUTHORIZED.value());
        map.put("message", "无权限访问");
        ObjectMapper objectMapper = new ObjectMapper();
        // 将map转换成字节
        byte[] bytes = new byte[0];
        try {
            bytes = objectMapper.writeValueAsBytes(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // HttpServletResponse 这个是web中的(传统web)
        // ServerHttpResponse 这个是webflux中的(响应式web)
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().set("Content-Type", "application/json;charset=utf-8");
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
