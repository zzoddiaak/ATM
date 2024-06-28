package repository.impl;

import model.DebitCard;
import repository.CardRepositoryInterface;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CardRepository implements CardRepositoryInterface {
    private static final String FILE_NAME = "cards.txt";

    @Override
    public List<DebitCard> loadDebitCards() {
        List<DebitCard> cards = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 5) {
                    String cardNumber = parts[0];
                    String pinCode = parts[1];
                    double balance = Double.parseDouble(parts[2]);
                    boolean isBlocked = Boolean.parseBoolean(parts[3]);
                    LocalDateTime blockTime = null;
                    if (parts.length > 4 && !parts[4].isEmpty()) {
                        blockTime = LocalDateTime.parse(parts[4]);
                    }
                    cards.add(new DebitCard(cardNumber, pinCode, balance, isBlocked, blockTime));
                } else if (parts.length == 4) {
                    String cardNumber = parts[0];
                    String pinCode = parts[1];
                    double balance = Double.parseDouble(parts[2]);
                    boolean isBlocked = Boolean.parseBoolean(parts[3]);
                    cards.add(new DebitCard(cardNumber, pinCode, balance, isBlocked, null));
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке карт: " + e.getMessage());
        }
        return cards;
    }

    @Override
    public void saveDebitCards(List<DebitCard> debitCards) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (DebitCard card : debitCards) {
                StringBuilder sb = new StringBuilder();
                sb.append(card.getCardNumber()).append(" ");
                sb.append(card.getPinCode()).append(" ");
                sb.append(card.getBalance()).append(" ");
                sb.append(card.isBlocked()).append(" ");
                if (card.isBlocked() && card.getBlockTime() != null) {
                    sb.append(card.getBlockTime());
                }
                writer.write(sb.toString().trim());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении карт: " + e.getMessage());
        }
    }
}
