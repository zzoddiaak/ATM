package exception;

import java.time.LocalDateTime;

public class CardBlockedException extends Exception {
    private LocalDateTime blockTime;

    public CardBlockedException(LocalDateTime blockTime) {
        super("Карта заблокирована.");
        this.blockTime = blockTime;
    }

    public LocalDateTime getBlockTime() {
        return blockTime;
    }
}
