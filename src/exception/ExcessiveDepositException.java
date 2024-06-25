package exception;

public class ExcessiveDepositException extends Exception {
    public ExcessiveDepositException(double amount) {
        super(String.format("Сумма пополнения не должна превышать 1 000 000: %.2f", amount));
    }
}
