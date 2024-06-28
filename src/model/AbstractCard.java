package model;

import java.time.LocalDateTime;

public abstract class AbstractCard {
    private String cardNumber;
    private String pinCode;
    private double balance;
    private boolean isBlocked;
    private LocalDateTime blockTime;
    private int failedAttempts;

    public AbstractCard(String cardNumber, String pinCode, double balance, boolean isBlocked, LocalDateTime blockTime) {
        this.cardNumber = cardNumber;
        this.pinCode = pinCode;
        this.balance = balance;
        this.isBlocked = isBlocked;
        this.blockTime = blockTime;
        this.failedAttempts = 0;
    }

    // Геттеры и сеттеры

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public LocalDateTime getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(LocalDateTime blockTime) {
        this.blockTime = blockTime;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public void incrementFailedAttempts() {
        this.failedAttempts++;
    }

    public void resetFailedAttempts() {
        this.failedAttempts = 0;
    }
}
