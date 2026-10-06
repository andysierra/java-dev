package com.andressierra.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    private static final String FUNDS = "/api/v1/funds";
    private static final String SUBSCRIPTIONS = "/api/v1/subscriptions";
    private static final String TRANSACTIONS = "/api/v1/transactions";

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route()
                .GET(FUNDS, handler::getFunds)
                .POST(SUBSCRIPTIONS, handler::subscribe)
                .DELETE(SUBSCRIPTIONS, handler::cancelSubscription)
                .GET(TRANSACTIONS + "/{clientId}", handler::getTransactionHistory)
                .build();
    }
}