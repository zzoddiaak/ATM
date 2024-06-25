import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import exception.ATMLimitExceededException;
import exception.CardBlockedException;
import exception.CardNotFoundException;
import exception.ExcessiveDepositException;
import exception.InsufficientFundsException;

public class Menu {
    private ATMOperations atmOperations;
    private CardRepository cardRepository;
    private List<Card> cards;
    private Card currentCard;

    public Menu(ATMOperations atmOperations, CardRepository cardRepository) {
        this.atmOperations = atmOperations;
        this.cardRepository = cardRepository;
        this.cards = cardRepository.loadCards();
    }

    public void start(Scanner scanner) {
        while (true) {
            System.out.println("1. Ввести карту");
            System.out.println("2. Добавить новую карту");
            System.out.println("3. Выйти");
            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline character

            switch (choice) {
                case 1:
                    authenticateCard(scanner);
                    break;
                case 2:
                    addNewCard(scanner);
                    break;
                case 3:
                    cardRepository.saveCards(cards);
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private void authenticateCard(Scanner scanner) {
        System.out.print("Введите номер карты: ");
        String cardNumber = scanner.nextLine();

        System.out.print("Введите ПИН-код: ");
        String pinCode = scanner.nextLine();

        try {
            currentCard = atmOperations.authenticate(cardNumber, pinCode);
            if (currentCard != null) {
                showMenu(scanner);
            }
        } catch (CardNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (CardBlockedException e) {
            LocalDateTime unblockTime = e.getBlockTime();
            LocalDateTime now = LocalDateTime.now();
            long hoursLeft = now.until(unblockTime, ChronoUnit.HOURS);
            long minutesLeft = now.until(unblockTime, ChronoUnit.MINUTES) % 60;
            long secondsLeft = now.until(unblockTime, ChronoUnit.SECONDS) % 60;

            System.out.println(e.getMessage());
            System.out.printf("Осталось времени до разблокировки: %d часов %d минут %d секунд\n",
                    hoursLeft, minutesLeft, secondsLeft);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addNewCard(Scanner scanner) {
        System.out.print("Введите номер карты: ");
        String cardNumber = scanner.nextLine();

        System.out.print("Введите ПИН-код: ");
        String pinCode = scanner.nextLine();

        System.out.print("Введите начальный баланс: ");
        double balance = scanner.nextDouble();
        scanner.nextLine(); // consume newline character

        Card newCard = new Card(cardNumber, pinCode, balance, false, null);
        cards.add(newCard);
        atmOperations.getCards().add(newCard);
        cardRepository.saveCards(cards);
        System.out.println("Карта успешно добавлена.");
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
                    try {
                        System.out.print("Введите сумму для снятия: ");
                        double amount = scanner.nextDouble();
                        scanner.nextLine(); // consume newline character
                        atmOperations.withdraw(currentCard, amount);
                    } catch (InsufficientFundsException | ATMLimitExceededException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        System.out.print("Введите сумму для пополнения: ");
                        double amount = scanner.nextDouble();
                        scanner.nextLine(); // consume newline character
                        atmOperations.deposit(currentCard, amount);
                    } catch (ExcessiveDepositException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }
}
