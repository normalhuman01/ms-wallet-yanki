package pe.com.project4.ms.infrastructure.consumer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pe.com.project4.ms.application.SendMoneyWalletYankiUseCase;
import pe.com.project4.ms.infrastructure.event.SendMoneyRequestEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class SendMoneyRequestConsumer {
    private final SendMoneyWalletYankiUseCase sendMoneyWalletYankiUseCase;

    @KafkaListener(topics = "SEND-MONEY-REQUEST-EVENT", groupId = "BOOTCOIN")
    public void consume(SendMoneyRequestEvent sendMoneyRequestEvent) {
        log.info("Consuming Message MONEY-REQUEST {}", sendMoneyRequestEvent);
        sendMoneyWalletYankiUseCase.sendMoney(sendMoneyRequestEvent.toSendMoneyEvent())
                .subscribe(result -> log.debug(result.toString()));
    }
}
