import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        // Создание объекта CardRepository
        CardRepository cardRepository = new CardRepository();

        // Создание объекта ATMOperations с передачей CardRepository в конструктор
        ATMOperations atmOperations = new ATMOperations(cardRepository);

        // Создание объекта Menu с передачей ATMOperations и CardRepository в конструктор
        Menu menu = new Menu(atmOperations, cardRepository);

        // Запуск меню
        menu.start(new Scanner(System.in));

        // Сохранение изменений в карты при завершении программы
        cardRepository.saveCards(atmOperations.getCards());
    }
}
