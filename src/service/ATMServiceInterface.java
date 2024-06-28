package service;

import model.DebitCard;
import exception.*;

import java.util.List;

public interface ATMServiceInterface {
    DebitCard authenticate(String cardNumber, String pinCode) throws CardNotFoundException, CardBlockedException;
    void checkBalance(DebitCard card);
    void withdraw(DebitCard card, double amount) throws InsufficientFundsException, ATMLimitExceededException;
    void deposit(DebitCard card, double amount) throws ExcessiveDepositException;
    void blockCard(DebitCard card);
    List<DebitCard> getCards();
}
