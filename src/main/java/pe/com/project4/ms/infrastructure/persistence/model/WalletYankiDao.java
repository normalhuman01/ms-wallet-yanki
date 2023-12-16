package pe.com.project4.ms.infrastructure.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pe.com.project4.ms.domain.WalletYanki;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("walletAccounts")
public class WalletYankiDao {
    @Id
    private String id;
    private BigDecimal balance;
    private String phoneNumber;
    private LocalDateTime createdAt;

    public WalletYankiDao(WalletYanki walletYanki) {
        id = walletYanki.getId();
        balance = walletYanki.getBalance();
        phoneNumber = walletYanki.getPhoneNumber();
        createdAt = walletYanki.getCreatedAt();
    }

    public WalletYanki toWalletYanki() {
        WalletYanki walletYanki = new WalletYanki();
        walletYanki.setId(id);
        walletYanki.setBalance(balance);
        walletYanki.setPhoneNumber(phoneNumber);
        walletYanki.setCreatedAt(createdAt);
        return walletYanki;
    }
}
