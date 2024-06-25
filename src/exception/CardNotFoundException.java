package exception;

public class CardNotFoundException extends Exception {
    public CardNotFoundException(String cardNumber) {
        super(String.format("Карта с номером %s не найдена.", cardNumber));
    }
}
