package com.kans.gatewayserver.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudGatewayRouting {

    //@Bean
    public RouteLocator configureRoute(RouteLocatorBuilder builder) {
       return builder.routes()
      .route("productId", r->r.path("/product/**").uri("http://localhost:8081")) //static routing
      .route("productId2", r->r.path("/product/**").uri("http://localhost:8084"))
      .route("orderId", r->r.path("/order/**").uri("lb://ORDER-SERVICE")) //dynamic routing
      .route("shoppingCartId", r->r.path("/shoppingcart/**").uri("lb://SHOPPING-CART")) //dynamic routing
      .build();
    }
    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("path_route", r -> r.path("/product/**").and().method("POST", "PUT", "DELETE").and().host("localhost*")
            .uri("http://localhost:8081"))
            .route("path_route", r -> r.path("/product/**").and().method("GET")
            		.filters(f -> f.addResponseHeader("Cache-Control", "max-age=300"))
            .uri("http://localhost:8084"))
            .build();
 
    }
}
