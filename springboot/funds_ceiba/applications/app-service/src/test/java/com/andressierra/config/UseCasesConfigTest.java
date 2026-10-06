package com.andressierra.config;

import com.andressierra.model.client.gateways.ClientRepository;
import com.andressierra.model.fund.gateways.FundRepository;
import com.andressierra.model.notification.NotificationGateway;
import com.andressierra.model.transaction.gateways.TransactionRepository;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public ClientRepository clientRepository() {
            return mock(ClientRepository.class);
        }

        @Bean
        public FundRepository fundRepository() {
            return mock(FundRepository.class);
        }

        @Bean
        public TransactionRepository transactionRepository() {
            return mock(TransactionRepository.class);
        }

        @Bean
        public NotificationGateway notificationGateway() {
            return mock(NotificationGateway.class);
        }

        @Bean
        public MyUseCase myUseCase() {
            return new MyUseCase();
        }
    }

    static class MyUseCase {
        public String execute() {
            return "MyUseCase Test";
        }
    }
}