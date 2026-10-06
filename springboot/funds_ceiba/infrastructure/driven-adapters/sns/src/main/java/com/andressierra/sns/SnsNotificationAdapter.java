package com.andressierra.sns;

import com.andressierra.model.client.Client;
import com.andressierra.model.exception.BusinessException;
import com.andressierra.model.fund.Fund;
import com.andressierra.model.messages.MessagesEnum;
import com.andressierra.model.notification.NotificationGateway;
import com.andressierra.sns.config.SnsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Slf4j
@Component
public class SnsNotificationAdapter implements NotificationGateway {

    private final SnsClient snsClient;
    private final String topicArn;

    public SnsNotificationAdapter(SnsClient snsClient, SnsProperties properties) {
        this.snsClient = snsClient;
        this.topicArn = properties.topicArn();
    }

    @Override
    public Mono<Void> notify(Client client, Fund fund) {
        return Mono.fromRunnable(() -> {
                    var message = String.format(
                            "Hola %s, su suscripcion al fondo %s ha sido exitosa. Monto: COP $%s",
                            client.getName(),
                            fund.getName(),
                            fund.getMinimumAmount().toPlainString()
                    );

                    var request = PublishRequest.builder()
                            .topicArn(topicArn)
                            .subject("BTG Pactual - Suscripcion exitosa")
                            .message(message)
                            .build();

                    snsClient.publish(request);
                    log.info("[SNS] Notificacion enviada a topic: {}", topicArn);
                })
                .then()
                .onErrorMap(e -> !(e instanceof BusinessException),
                        e -> BusinessException.fromMessage(MessagesEnum.NOTIFICATION_ERROR));
    }
}
