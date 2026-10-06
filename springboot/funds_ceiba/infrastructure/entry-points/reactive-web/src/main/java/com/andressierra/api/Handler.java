package com.andressierra.api;

import com.andressierra.api.helpers.Mapper;
import com.andressierra.api.helpers.RestValidator;
import com.andressierra.api.rest.ResponseBuilder;
import com.andressierra.api.rest.request.CancelSubscriptionRequest;
import com.andressierra.api.rest.request.SubscribeRequest;
import com.andressierra.model.messages.MessagesEnum;
import com.andressierra.usecase.cancel.CancelSubscriptionUseCase;
import com.andressierra.usecase.getfunds.GetFundsUseCase;
import com.andressierra.usecase.gettransactions.GetTransactionHistoryUseCase;
import com.andressierra.usecase.subscribe.SubscribeToFundUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private static final String CLIENT_ID_PATH_VARIABLE = "clientId";

    private final SubscribeToFundUseCase subscribeToFundUseCase;
    private final CancelSubscriptionUseCase cancelSubscriptionUseCase;
    private final GetTransactionHistoryUseCase getTransactionHistoryUseCase;
    private final GetFundsUseCase getFundsUseCase;

    public Mono<ServerResponse> subscribe(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SubscribeRequest.class)
                .map(RestValidator::validate)
                .flatMap(request -> subscribeToFundUseCase.subscribe(Mapper.toSubscribeCommand(request)))
                .flatMap(tx -> ResponseBuilder.success(Mapper.toSubscribeResponse(tx), MessagesEnum.FUND_SUBSCRIBED))
                .onErrorResume(ResponseBuilder::handleError);
    }

    public Mono<ServerResponse> cancelSubscription(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CancelSubscriptionRequest.class)
                .map(RestValidator::validate)
                .flatMap(request -> cancelSubscriptionUseCase.cancel(Mapper.toCancelCommand(request)))
                .flatMap(tx -> ResponseBuilder.success(Mapper.toCancelSubscriptionResponse(tx), MessagesEnum.FUND_CANCELLED))
                .onErrorResume(ResponseBuilder::handleError);
    }

    public Mono<ServerResponse> getTransactionHistory(ServerRequest serverRequest) {
        String clientId = serverRequest.pathVariable(CLIENT_ID_PATH_VARIABLE);
        return getTransactionHistoryUseCase.getByClientId(clientId)
                .map(Mapper::toTransactionResponse)
                .collectList()
                .flatMap(list -> ResponseBuilder.success(list, MessagesEnum.TRANSACTIONS_FOUND))
                .onErrorResume(ResponseBuilder::handleError);
    }

    @SuppressWarnings("java:S1172")
    public Mono<ServerResponse> getFunds(ServerRequest serverRequest) {
        return getFundsUseCase.getAll()
                .map(Mapper::toFundResponse)
                .collectList()
                .flatMap(list -> ResponseBuilder.success(list, MessagesEnum.FUNDS_FOUND))
                .onErrorResume(ResponseBuilder::handleError);
    }
}
