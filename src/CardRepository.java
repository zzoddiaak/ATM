import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CardRepository {
    private static final String FILE_NAME = "cards.txt";

    public List<Card> loadCards() {
        List<Card> cards = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            initializeFile(file);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(" ");
                String cardNumber = data[0];
                String pinCode = data[1];
                double balance = Double.parseDouble(data[2]);
                boolean isBlocked = Boolean.parseBoolean(data[3]);
                LocalDateTime blockTime = LocalDateTime.parse(data[4]);
                cards.add(new Card(cardNumber, pinCode, balance, isBlocked, blockTime));
            }
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке карт: " + e.getMessage());
        }
        return cards;
    }

    private void initializeFile(File file) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("1234-5678-1234-5678 1234 5000.0 false 2023-06-24T12:00:00\n");
            bw.write("8765-4321-8765-4321 4321 10000.0 false 2023-06-23T15:30:00\n");
        } catch (IOException e) {
            System.out.println("Ошибка при инициализации файла: " + e.getMessage());
        }
    }

    public void saveCards(List<Card> cards) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Card card : cards) {
                bw.write(card.getCardNumber() + " " +
                        card.getPinCode() + " " +
                        card.getBalance() + " " +
                        card.isBlocked() + " " +
                        card.getBlockTime() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении карт: " + e.getMessage());
        }
    }
}
