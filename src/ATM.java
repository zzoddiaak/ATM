import ui.MenuInterface;

import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        ApplicationContext context = ApplicationContext.getInstance();

        MenuInterface menu = context.getMenu();

        menu.start(new Scanner(System.in));

        context.getCardRepository().saveDebitCards(context.getATMOperations().getCards());
    }
}
