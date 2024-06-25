package exception;

public class ATMLimitExceededException extends Exception {
    public ATMLimitExceededException(double amount) {
        super(String.format("Недостаточно наличных в банкомате для выдачи: %.2f", amount));
    }
}
