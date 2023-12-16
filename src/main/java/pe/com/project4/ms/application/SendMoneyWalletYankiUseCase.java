package pe.com.project4.ms.application;

import pe.com.project4.ms.domain.WalletYanki;
import pe.com.project4.ms.infrastructure.rest.request.SendMoneyRequest;
import reactor.core.publisher.Mono;

public interface SendMoneyWalletYankiUseCase {
    Mono<WalletYanki> sendMoney(SendMoneyRequest sendMoneyRequest);
}
