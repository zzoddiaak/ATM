package exception;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(double amount) {
        super(String.format("Недостаточно средств для снятия: %.2f", amount));
    }
}
