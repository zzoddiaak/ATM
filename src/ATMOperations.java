import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import exception.ATMLimitExceededException;
import exception.CardBlockedException;
import exception.CardNotFoundException;
import exception.ExcessiveDepositException;
import exception.InsufficientFundsException;

public class ATMOperations {
    private List<Card> cards;
    private CardRepository cardRepository;
    private static final double ATM_MAX_CASH = 1000000.0;
    private double atmCash;

    public ATMOperations(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
        this.cards = cardRepository.loadCards();
        this.atmCash = ATM_MAX_CASH;
    }

    public Card authenticate(String cardNumber, String pinCode) throws CardNotFoundException, CardBlockedException {
        Card foundCard = null;
        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                if (card.isBlocked()) {
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime unblockTime = card.getBlockTime().plusDays(1);
                    if (now.isBefore(unblockTime)) {
                        long hoursLeft = now.until(unblockTime, ChronoUnit.HOURS);
                        long minutesLeft = now.until(unblockTime, ChronoUnit.MINUTES) % 60;
                        long secondsLeft = now.until(unblockTime, ChronoUnit.SECONDS) % 60;

                        throw new CardBlockedException(unblockTime);
                    } else {
                        card.setBlocked(false);
                        card.setBlockTime(null);
                        card.resetFailedAttempts();
                        System.out.println("Блокировка карты автоматически снята.");
                    }
                }

                if (card.getPinCode().equals(pinCode)) {
                    card.resetFailedAttempts();
                    foundCard = card;
                    break;
                } else {
                    card.incrementFailedAttempts();
                    if (card.getFailedAttempts() >= 3) {
                        card.setBlocked(true);
                        card.setBlockTime(LocalDateTime.now());
                        cardRepository.saveCards(cards);
                        throw new CardBlockedException(card.getBlockTime());
                    } else {
                        throw new IllegalArgumentException("Неверный ПИН-код. Попыток осталось: " + (3 - card.getFailedAttempts()));
                    }
                }
            }
        }

        if (foundCard == null) {
            throw new CardNotFoundException(cardNumber);
        }

        return foundCard;
    }

    public void checkBalance(Card card) {
        System.out.println("Текущий баланс: " + card.getBalance());
    }

    public void withdraw(Card card, double amount) throws InsufficientFundsException, ATMLimitExceededException {
        if (amount > card.getBalance()) {
            throw new InsufficientFundsException(amount);
        } else if (amount > atmCash) {
            throw new ATMLimitExceededException(amount);
        } else {
            card.setBalance(card.getBalance() - amount);
            atmCash -= amount;
            System.out.println("Операция успешна. Снято: " + amount);
            cardRepository.saveCards(cards);
        }
    }

    public void deposit(Card card, double amount) throws ExcessiveDepositException {
        if (amount > 1000000) {
            throw new ExcessiveDepositException(amount);
        } else {
            card.setBalance(card.getBalance() + amount);
            atmCash += amount;
            System.out.println("Операция успешна. Пополнено: " + amount);
            cardRepository.saveCards(cards);
        }
    }

    public void blockCard(Card card) {
        card.setBlocked(true);
        card.setBlockTime(LocalDateTime.now());
        cardRepository.saveCards(cards);
    }

    public List<Card> getCards() {
        return cards;
    }
}
