package pe.com.project4.ms.application;

import pe.com.project4.ms.domain.WalletYanki;
import pe.com.project4.ms.infrastructure.rest.request.CreateAccountRequest;
import reactor.core.publisher.Mono;

public interface SaveWalletYankiUseCase {
    Mono<WalletYanki> createAccount(CreateAccountRequest createAccountRequest);
}
