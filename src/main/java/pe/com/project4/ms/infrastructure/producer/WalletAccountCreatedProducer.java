package pe.com.project4.ms.infrastructure.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pe.com.project4.ms.infrastructure.event.WalletAccountCreatedEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WalletAccountCreatedProducer {

    private final KafkaTemplate<String, WalletAccountCreatedEvent> kafkaTemplate;

    public void sendAccountCreate(WalletAccountCreatedEvent walletAccountCreatedEvent) {
        log.debug("==> Producing message {}", walletAccountCreatedEvent.toString());
        kafkaTemplate.send("WALLET-ACCOUNT-CREATED", walletAccountCreatedEvent);
    }
}
