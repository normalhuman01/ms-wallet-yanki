package pe.com.project4.ms.application.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.com.project4.ms.application.SaveWalletYankiUseCase;
import pe.com.project4.ms.application.repository.WalletYankiRepository;
import pe.com.project4.ms.domain.WalletYanki;
import pe.com.project4.ms.infrastructure.producer.WalletAccountCreatedProducer;
import pe.com.project4.ms.infrastructure.rest.request.CreateAccountRequest;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaveWalletYankiService implements SaveWalletYankiUseCase {

    private final WalletYankiRepository walletYankiRepository;
    private final WalletAccountCreatedProducer walletAccountCreatedProducer;

    @Override
    public Mono<WalletYanki> createAccount(CreateAccountRequest createAccountRequest) {
        log.info("==> Entro al createAccount {} ", createAccountRequest);

        if (createAccountRequest.getPhoneNumber().length() == 9) {
            if (createAccountRequest.getDocumentNumber().length() <= 11) {
                return walletYankiRepository.findByPhoneNumber(createAccountRequest.getPhoneNumber())
                        .flatMap(accountFound -> Mono.error(new RuntimeException("==> El numero de celular ya esta registrado")))
                        .then(Mono.just(createAccountRequest))
                        .flatMap(createAccount -> walletYankiRepository.save(new WalletYanki(createAccount.getPhoneNumber()))
                                .map(walletYanki -> {
                                    walletAccountCreatedProducer.sendAccountCreate(createAccountRequest.toWalletAccountCreatedEvent());
                                    log.info("Request, {}", createAccountRequest);
                                    return walletYanki;
                                }));
            } else {
                log.warn("==> Ingrese Un Numero de documento Valido., {} " + "=>[" + createAccountRequest.getDocumentNumber() + "]");
            }
        } else {
            log.warn("==> Ingrese Un Numero de Celular Valido., {} " + "=>[" + createAccountRequest.getPhoneNumber() + "]");
        }
        return Mono.empty();
    }


}
