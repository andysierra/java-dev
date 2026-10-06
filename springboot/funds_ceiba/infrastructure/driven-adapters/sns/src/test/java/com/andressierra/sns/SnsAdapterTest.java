package com.andressierra.sns;

import com.andressierra.model.client.Client;
import com.andressierra.model.client.enums.NotificationPreferenceEnum;
import com.andressierra.model.exception.BusinessException;
import com.andressierra.model.fund.Fund;
import com.andressierra.model.fund.enums.FundCategoryEnum;
import com.andressierra.sns.config.SnsProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SnsAdapterTest {

    @Mock private SnsClient snsClient;

    private SnsNotificationAdapter adapter;
    private Client client;
    private Fund fund;

    @BeforeEach
    void setUp() {
        var properties = new SnsProperties("us-east-1", "arn:aws:sns:us-east-1:123456:test-topic");
        adapter = new SnsNotificationAdapter(snsClient, properties);

        client = Client.builder()
                .id("CLI-001").name("Andres").email("a@test.com").phone("300")
                .balance(new BigDecimal("500000"))
                .notificationPreference(NotificationPreferenceEnum.EMAIL)
                .build();

        fund = Fund.builder()
                .id(1L).name("FPV_BTG_PACTUAL_RECAUDADORA")
                .minimumAmount(new BigDecimal("75000"))
                .category(FundCategoryEnum.FPV)
                .build();
    }

    @Test
    void shouldPublishNotificationSuccessfully() {
        when(snsClient.publish(any(PublishRequest.class))).thenReturn(PublishResponse.builder().build());

        StepVerifier.create(adapter.notify(client, fund))
                .verifyComplete();

        var captor = ArgumentCaptor.forClass(PublishRequest.class);
        verify(snsClient).publish(captor.capture());

        var request = captor.getValue();
        assertThat(request.topicArn()).isEqualTo("arn:aws:sns:us-east-1:123456:test-topic");
        assertThat(request.subject()).isEqualTo("BTG Pactual - Suscripcion exitosa");
        assertThat(request.message()).contains("Andres");
        assertThat(request.message()).contains("FPV_BTG_PACTUAL_RECAUDADORA");
        assertThat(request.message()).contains("75000");
    }

    @Test
    void shouldMapSnsExceptionToNotificationError() {
        when(snsClient.publish(any(PublishRequest.class))).thenThrow(new RuntimeException("SNS failure"));

        StepVerifier.create(adapter.notify(client, fund))
                .expectErrorMatches(e -> e instanceof BusinessException
                        && ((BusinessException) e).getOpcode().equals("51"))
                .verify();
    }
}
