import repository.impl.CardRepository;
import repository.CardRepositoryInterface;
import service.impl.ATMService;
import service.ATMServiceInterface;
import ui.impl.Menu;
import ui.MenuInterface;

public class ApplicationContext {
    private static ApplicationContext instance;

    private CardRepositoryInterface cardRepository;
    private ATMServiceInterface atmOperations;
    private MenuInterface menu;

    private ApplicationContext() {
        cardRepository = new CardRepository();
        atmOperations = new ATMService(cardRepository);
        menu = new Menu(atmOperations, cardRepository);
    }

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public CardRepositoryInterface getCardRepository() {
        return cardRepository;
    }

    public ATMServiceInterface getATMOperations() {
        return atmOperations;
    }

    public MenuInterface getMenu() {
        return menu;
    }
}
