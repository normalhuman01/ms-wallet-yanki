package pe.com.project4.ms.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.project4.ms.application.SaveWalletYankiUseCase;
import pe.com.project4.ms.application.SendMoneyWalletYankiUseCase;
import pe.com.project4.ms.application.repository.WalletYankiRepository;
import pe.com.project4.ms.domain.WalletYanki;
import pe.com.project4.ms.infrastructure.rest.request.CreateAccountRequest;
import pe.com.project4.ms.infrastructure.rest.request.SendMoneyRequest;
import reactor.core.CorePublisher;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class WalletYankiHandler {

    private final SaveWalletYankiUseCase saveWalletYankiService;
    private final SendMoneyWalletYankiUseCase sendMoneyWalletYankiUseCase;
    private final WalletYankiRepository walletYankiRepository;

    public Mono<ServerResponse> postWalletYanki(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateAccountRequest.class)
                .map(saveWalletYankiService::createAccount)
                .flatMap(respuesta -> this.toServerResponse(respuesta, HttpStatus.CREATED));
    }

    public Mono<ServerResponse> getYankiPhone(ServerRequest request) {
        Mono<WalletYanki> yanki = request.queryParam("phoneNumber")
                .map(walletYankiRepository::findByPhoneNumber)
                .orElseGet(Mono::empty);
        return this.toServerResponse(yanki, HttpStatus.OK);
    }

    public Mono<ServerResponse> postTransaction(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SendMoneyRequest.class)
                .map(sendMoneyWalletYankiUseCase::sendMoney)
                .flatMap(walletYanki -> this.toServerResponse(walletYanki, HttpStatus.CREATED));
    }

    private Mono<ServerResponse> toServerResponse(CorePublisher<WalletYanki> yankiMono, HttpStatus status) {
        log.info("==> Antes de responder {} " + yankiMono.toString());
        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(yankiMono, WalletYanki.class);
    }

}
