package com.khb.hu.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Set;

@Component
public class JwtFilter implements GlobalFilter{

    private static final String BEARER = "Bearer ";

    @Autowired
    private JwtService jwtService;

    private PathPattern currencyPathPattern = PathPatternParser.defaultInstance.parse("/currency/**");
    private PathPattern tokenPathPattern = PathPatternParser.defaultInstance.parse("/token/**");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if(isCurrencyRequest(exchange) || isTokenRequest(exchange))
            return chain.filter(exchange);

        ServerHttpRequest request = exchange.getRequest();
        List<String> authHeaders = request.getHeaders().get("Authorization");

        if(ObjectUtils.isEmpty(authHeaders)) {
            return send401Response(exchange);
        }else {
            String authHeader = authHeaders.get(0);
            UsernamePasswordAuthenticationToken userDetails = null;
            try {
                userDetails = createUserDetailsFromAuthHeader(authHeader);
                String userName = userDetails.getName();
                String authCsv = String.join(",", userDetails.getAuthorities().stream().map(auth -> auth.getAuthority()).toList());

                ServerHttpRequest requestWithCustomHeaders = request
                        .mutate()
                        .header("x-jwt-username", userName)
                        .header("x-jwt-auth", authCsv)
                        .build();

                exchange = exchange
                        .mutate()
                        .request(requestWithCustomHeaders)
                        .build();

            }catch (Exception e) {
                e.printStackTrace();
            }
            if(userDetails == null)
                return send401Response(exchange);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> send401Response(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private boolean isCurrencyRequest(ServerWebExchange exchange) {
        Set<URI> origUrls = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);

        URI originalUri = origUrls.iterator().next();
        return currencyPathPattern.matches(PathContainer.parsePath(originalUri.toString()).subPath(4));

    }

    private boolean isTokenRequest(ServerWebExchange exchange) {
        Set<URI> origUrls = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);

        URI originalUri = origUrls.iterator().next();
        return tokenPathPattern.matches(PathContainer.parsePath(originalUri.toString()).subPath(4));

    }

    private UsernamePasswordAuthenticationToken createUserDetailsFromAuthHeader(String authHeader) {

        if(authHeader != null && authHeader.startsWith(BEARER)) {
            String jwtToken = authHeader.substring(BEARER.length());
            UserDetails principal = jwtService.parseJwt(jwtToken);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
            return authentication;
        }
        return null;
    }
}
