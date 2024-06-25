import java.util.List;
import java.util.Scanner;

public class Menu {
    private ATMOperations atmOperations;
    private CardRepository cardRepository;
    private Card currentCard;
    private static final int MAX_ATTEMPTS = 3;

    public Menu() {
        cardRepository = new CardRepository();
        List<Card> cards = cardRepository.loadCards();
        atmOperations = new ATMOperations(cards);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Введите номер карты (в формате ХХХХ ХХХХ ХХХХ ХХХХ): ");
            String[] cardNumberParts = scanner.nextLine().split(" ");
            System.out.print("Введите ПИН-код: ");
            String pinCode = scanner.nextLine();

            currentCard = atmOperations.authenticate(cardNumberParts, pinCode);
            if (currentCard != null) {
                showMenu(scanner);
                cardRepository.saveCards(atmOperations.getCards());
                break;
            } else {
                attempts++;
                if (attempts == MAX_ATTEMPTS) {
                    Card cardToBlock = findCardByNumber(cardNumberParts);
                    if (cardToBlock != null) {
                        System.out.println("Карта заблокирована из-за слишком большого количества неверных попыток.");
                        atmOperations.blockCard(cardToBlock);
                        cardRepository.saveCards(atmOperations.getCards());
                    }
                }
            }
        }
    }

    private Card findCardByNumber(String[] cardNumber) {
        for (Card card : atmOperations.getCards()) {
            if (isEqual(card.getCardNumber(), cardNumber)) {
                return card;
            }
        }
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

    private void showMenu(Scanner scanner) {
        while (true) {
            System.out.println("1. Проверить баланс");
            System.out.println("2. Снять средства");
            System.out.println("3. Пополнить баланс");
            System.out.println("4. Выйти");
            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline character

            switch (choice) {
                case 1:
                    atmOperations.checkBalance(currentCard);
                    break;
                case 2:
                    System.out.print("Введите сумму для снятия: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine(); // consume newline character
                    atmOperations.withdraw(currentCard, amount);
                    break;
                case 3:
                    System.out.print("Введите сумму для пополнения: ");
                    amount = scanner.nextDouble();
                    scanner.nextLine(); // consume newline character
                    atmOperations.deposit(currentCard, amount);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }
}
