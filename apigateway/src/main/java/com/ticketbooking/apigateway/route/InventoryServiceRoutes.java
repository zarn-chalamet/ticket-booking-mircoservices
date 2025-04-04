package com.ticketbooking.apigateway.route;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

@Configuration
public class InventoryServiceRoutes {

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> inventoryRoutes() {
        return GatewayRouterFunctions.route("inventory-service")
                .route(RequestPredicates.path("/api/v1/inventory/venue/{venueId}"),
                        request -> forwardWithPathVariable(request,"venueId",
                                inventoryServiceUrl+"/venue/"))

                .route(RequestPredicates.GET("/api/v1/inventory/events"),
                        HandlerFunctions.http(inventoryServiceUrl+"/events"))

                .route(RequestPredicates.path("/api/v1/inventory/event/{eventId}"),
                        request -> forwardWithPathVariable(request,"eventId",
                                inventoryServiceUrl+"/event/"))

                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceCircuitBreaker",
                        URI.create("forward:/inventoryFallbackRoute")))
                .build();
    }

    private static ServerResponse forwardWithPathVariable(ServerRequest request,
                                                   String pathVariable,
                                                   String baseUrl) throws Exception {
        String value = request.pathVariable(pathVariable);
        return HandlerFunctions.http(baseUrl+value).handle(request);
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryFallbackRoute() {
        return GatewayRouterFunctions.route("inventoryFallbackRoute")
                .POST("/inventoryFallbackRoute",
                        request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body("Inventory service is down"))
                .build();
    }
}
