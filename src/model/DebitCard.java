package model;

import java.time.LocalDateTime;

public class DebitCard extends AbstractCard {
    public DebitCard(String cardNumber, String pinCode, double balance, boolean isBlocked, LocalDateTime blockTime) {
        super(cardNumber, pinCode, balance, isBlocked, blockTime);
    }
}
