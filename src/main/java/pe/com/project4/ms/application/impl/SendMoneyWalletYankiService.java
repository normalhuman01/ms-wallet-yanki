package pe.com.project4.ms.application.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.com.project4.ms.application.SendMoneyWalletYankiUseCase;
import pe.com.project4.ms.application.repository.WalletYankiRepository;
import pe.com.project4.ms.domain.WalletYanki;
import pe.com.project4.ms.infrastructure.event.SendMoneyEvent;
import pe.com.project4.ms.infrastructure.producer.WalletSendMoneyProducer;
import pe.com.project4.ms.infrastructure.rest.request.SendMoneyRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendMoneyWalletYankiService implements SendMoneyWalletYankiUseCase {

    private final WalletYankiRepository walletYankiRepository;
    private final WalletSendMoneyProducer walletSendMoneyProducer;

    @Override
    public Mono<WalletYanki> sendMoney(SendMoneyRequest sendMoneyRequest) {
        log.info("<---- Estoy dentro de sendMoney{}", sendMoneyRequest.toString());
        Mono<WalletYanki> walletYankiSenderMono = walletYankiRepository.findByPhoneNumber(sendMoneyRequest.getWalletAccountSenderId())
                .switchIfEmpty(Mono.error(new RuntimeException("Esta cuenta no existe!")))
                .filter(walletYanki -> sendMoneyRequest.getAmount().compareTo(walletYanki.getBalance()) <= 0)
                .doOnNext(w -> log.info("se imprime luego de verificar el saldo {}", w))
                .switchIfEmpty(Mono.error(new RuntimeException("Saldo Insuficiente!!!!!")));

        Mono<WalletYanki> walletYankiReceiverMono = walletYankiRepository.findByPhoneNumber(sendMoneyRequest.getWalletAccountReceiverId())
                .switchIfEmpty(Mono.error(new RuntimeException("Esta cuenta no existe!")));

        return Mono.zip(walletYankiSenderMono, walletYankiReceiverMono, (walletYankiSender, walletYankiReceiver) -> {
            BigDecimal money = sendMoneyRequest.getAmount();
            walletYankiSender.debitMoney(money);
            walletYankiReceiver.creditMoney(money);
            return Stream.of(walletYankiSender, walletYankiReceiver);
        }).flatMapMany(walletYankis -> {
            walletSendMoneyProducer.sendMoneyEvent(new SendMoneyEvent(sendMoneyRequest));
            return walletYankiRepository.saveAll(Flux.fromStream(walletYankis));
        }).filter(walletYankis -> walletYankis.getPhoneNumber().equals(sendMoneyRequest.getWalletAccountSenderId()))
                .elementAt(0);
    }
}
