package viselica;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Viselica {
    static final String DICTIONARY_FILE_PATH = "C:/Users/adama/Documents/projects/Viselica/src/viselica/dictionary.txt";
    static int errorsLeft = 6;
    static String[] hangmanStages = {
            "  ____\n |    |\n      |\n      |\n      |\n      |\n=========", // Пустая виселица
            "  ____\n |    |\n O    |\n      |\n      |\n      |\n=========", // Ошибка №1: голова
            "  ____\n |    |\n O    |\n |    |\n      |\n      |\n=========", // Ошибка №2: голова и туловище
            "  ____\n |    |\n O    |\n/|    |\n      |\n      |\n=========", // Ошибка №3: голова, туловище и левая рука
            "  ____\n |    |\n O    |\n/|\\   |\n      |\n      |\n=========", // Ошибка №4: голова, туловище и обе руки
            "  ____\n |    |\n O    |\n/|\\   |\n/     |\n      |\n=========", // Ошибка №5: голова, туловище, обе руки и левая нога
            "  ____\n |    |\n O    |\n/|\\   |\n/ \\   |\n      |\n========="  // Ошибка №6: вся фигура виселицы
    };

    static void startGame() {
        String word = generateWord();
        playGame(word);
    }

    static String generateWord() {
        // Создаем список для хранения слов из словаря
        ArrayList<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(DICTIONARY_FILE_PATH))) {
            String line;
            // Считываем слова из файла и добавляем их в список
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Random random = new Random();
        int randomIndex = random.nextInt(words.size());
        return words.get(randomIndex);
    }

    static void checkLetter(String word, char letter, char[] maskedWord) {
        boolean found = false;


        // Проверяем наличие введенной буквы
        for (int i = 0; i < word.length(); i++) {
            if (Character.toLowerCase(word.charAt(i)) == Character.toLowerCase(letter)
                    && maskedWord[i] == '*') {
                maskedWord[i] = word.charAt(i);
                found = true;
            }
        }
        if (found) {
            System.out.println("Буква " + letter + " открыта!");
        } else {
            errorsLeft--;
            System.out.println("Ошибка, такой буквы в слове нет");
            System.out.println("У вас осталось " + errorsLeft + " ошибок");
            if (errorsLeft >= 0 && errorsLeft < hangmanStages.length) {
                System.out.println(hangmanStages[Math.min(hangmanStages.length - 1, hangmanStages.length - 1 - errorsLeft)]);
            }
        }
    }

    static void playGame(String word) {
        char[] maskedWord = new char[word.length()];
        Arrays.fill(maskedWord, '*');

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Угадайте слово " + String.valueOf(maskedWord));
            System.out.print("Введите букву: ");
            String guess = sc.nextLine().toLowerCase();
            checkLetter(word, guess.charAt(0), maskedWord);

            // Проверяем, все ли буквы открыты
            boolean allLettersRevealed = true;
            for (char c : maskedWord) {
                if (c == '*') {
                    allLettersRevealed = false;
                    break;
                }
            }

            if (allLettersRevealed) {
                System.out.println("Вы отгадали слово");
                break;
            }

            if (errorsLeft == 0) {
                System.out.println("Вы проиграли!");
                break;
            }
        }
        question();
    }

    public static void question() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Вы хотите начать новую игру? 1 - ДА. 2 - НЕТ: ");
            String question = sc.nextLine();
            if (question.equals("2")) {
                System.out.println("Игра закончена");
                break;
            } else if (question.equals("1")) {
                errorsLeft = 6;
                startGame();
                break;
            } else {
                System.out.println("Введите верное значение: ");
                continue;
            }
        }
    }

    public static void main(String[] args) {
        question();
    }
}
