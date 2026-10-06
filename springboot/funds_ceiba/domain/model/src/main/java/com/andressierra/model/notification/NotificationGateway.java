package com.andressierra.model.notification;

import com.andressierra.model.client.Client;
import com.andressierra.model.fund.Fund;
import reactor.core.publisher.Mono;

public interface NotificationGateway {
    Mono<Void> notify(Client client, Fund fund);
}
