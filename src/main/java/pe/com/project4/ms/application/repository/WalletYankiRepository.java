package pe.com.project4.ms.application.repository;

import pe.com.project4.ms.domain.WalletYanki;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WalletYankiRepository {
    Mono<WalletYanki> save(WalletYanki walletYanki);

    Mono<WalletYanki> findByPhoneNumber(String phone);

    Flux<WalletYanki> saveAll(Flux<WalletYanki> walletAccounts);
}
