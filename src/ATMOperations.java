import java.time.LocalDateTime;
import java.util.List;

public class ATMOperations {
    private List<Card> cards;
    private static final double ATM_MAX_CASH = 1000000.0;
    private double atmCash;

    public ATMOperations(List<Card> cards) {
        this.cards = cards;
        this.atmCash = ATM_MAX_CASH;
    }

    public Card authenticate(String[] cardNumber, String pinCode) {
        for (Card card : cards) {
            if (isEqual(card.getCardNumber(), cardNumber)) {
                if (card.isBlocked()) {
                    if (LocalDateTime.now().isAfter(card.getBlockTime().plusDays(1))) {
                        card.setBlocked(false);
                        card.setBlockTime(null);
                        System.out.println("Блокировка карты снята. Попробуйте снова.");
                    } else {
                        System.out.println("Карта заблокирована. Попробуйте позже.");
                        return null;
                    }
                }
                if (card.getPinCode().equals(pinCode)) {
                    return card;
                } else {
                    System.out.println("Неверный ПИН-код.");
                    return null;
                }
            }
        }
        System.out.println("Карта не найдена.");
        return null;
    }

    private boolean isEqual(String[] cardNumber1, String[] cardNumber2) {
        if (cardNumber1.length != cardNumber2.length) {
            return false;
        }
        for (int i = 0; i < cardNumber1.length; i++) {
            if (!cardNumber1[i].equals(cardNumber2[i])) {
                return false;
            }
        }
        return true;
    }

    public void checkBalance(Card card) {
        System.out.println("Текущий баланс: " + card.getBalance());
    }

    public void withdraw(Card card, double amount) {
        if (amount > card.getBalance()) {
            System.out.println("Недостаточно средств на счете.");
        } else if (amount > atmCash) {
            System.out.println("Недостаточно средств в банкомате.");
        } else {
            card.setBalance(card.getBalance() - amount);
            atmCash -= amount;
            System.out.println("Операция успешна. Снято: " + amount);
        }
    }

    public void deposit(Card card, double amount) {
        if (amount > 1000000) {
            System.out.println("Сумма пополнения не должна превышать 1 000 000.");
        } else {
            card.setBalance(card.getBalance() + amount);
            atmCash += amount;
            System.out.println("Операция успешна. Пополнено: " + amount);
        }
    }

    public void blockCard(Card card) {
        card.setBlocked(true);
        card.setBlockTime(LocalDateTime.now());
    }

    public List<Card> getCards() {
        return cards;
    }
}
