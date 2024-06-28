package service.impl;

import model.DebitCard;
import repository.CardRepositoryInterface;
import exception.*;
import service.ATMServiceInterface;
import java.time.LocalDateTime;
import java.util.List;

public class ATMService implements ATMServiceInterface {
    private List<DebitCard> cards;
    private CardRepositoryInterface cardRepository;
    private static final double ATM_MAX_CASH = 1000000.0;
    private double atmCash;

    public ATMService(CardRepositoryInterface cardRepository) {
        this.cardRepository = cardRepository;
        this.cards = cardRepository.loadDebitCards();
        this.atmCash = ATM_MAX_CASH;
    }

    @Override
    public DebitCard authenticate(String cardNumber, String pinCode) throws CardNotFoundException, CardBlockedException {
        DebitCard foundCard = null;
        for (DebitCard card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                if (card.isBlocked()) {
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime unblockTime = card.getBlockTime().plusDays(1);
                    if (now.isBefore(unblockTime)) {
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
                        cardRepository.saveDebitCards(cards);
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

    @Override
    public void checkBalance(DebitCard card) {
        System.out.println("Текущий баланс: " + card.getBalance());
    }

    @Override
    public void withdraw(DebitCard card, double amount) throws InsufficientFundsException, ATMLimitExceededException {
        if (amount > card.getBalance()) {
            throw new InsufficientFundsException(amount);
        } else if (amount > atmCash) {
            throw new ATMLimitExceededException(amount);
        } else {
            card.setBalance(card.getBalance() - amount);
            atmCash -= amount;
            System.out.println("Операция успешна. Снято: " + amount);
            cardRepository.saveDebitCards(cards);
        }
    }

    @Override
    public void deposit(DebitCard card, double amount) throws ExcessiveDepositException {
        if (amount > 1000000) {
            throw new ExcessiveDepositException(amount);
        } else {
            card.setBalance(card.getBalance() + amount);
            atmCash += amount;
            System.out.println("Операция успешна. Пополнено: " + amount);
            cardRepository.saveDebitCards(cards);
        }
    }

    @Override
    public void blockCard(DebitCard card) {
        card.setBlocked(true);
        card.setBlockTime(LocalDateTime.now());
        cardRepository.saveDebitCards(cards);
    }

    public List<DebitCard> getCards() {
        return cards;
    }
}
