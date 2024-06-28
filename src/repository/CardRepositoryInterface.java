package repository;

import model.DebitCard;
import java.util.List;

public interface CardRepositoryInterface {
    List<DebitCard> loadDebitCards();
    void saveDebitCards(List<DebitCard> debitCards);
}
