import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

class Wordle {
    public static void main(String[] args) {
        // Constants for terminal color codes
        final String BG_GREEN = "\u001b[42m";
        final String BG_YELLOW = "\u001b[43m";
        final String RESET = "\u001b[0m";

        System.out.println("WORDLE!");

        // Load the list of words from the file
        ArrayList<String> wordsList = loadWordsFromFile();

        // Check if the list of words is empty
        if (wordsList.size() == 0) {
            System.out.println("No words available in the list!");
            return;
        }

        // Select a random word from the list for the game
        int wIndex = (int) (Math.random() * wordsList.size());
        String correct = wordsList.get(wIndex);

        // Remove the selected word from the list to ensure it's not selected again in
        // subsequent games
        wordsList.remove(wIndex);

        // Save the modified list back to the file
        saveWordsToFile(wordsList);

        Scanner sc = new Scanner(System.in);
        String guess = "";

        // Loop for allowing user 6 attempts to guess the word
        for (int round = 0; round < 6; round++) {
            guess = getValidatedGuess(sc);

            // Compare the guess with the correct word and provide feedback src:
            // https://stackoverflow.com/questions/19035893/finding-second-occurrence-of-a-substring-in-a-string-in-java
            for (int i = 0; i < 5; i++) {
                if (guess.substring(i, i + 1).equals(correct.substring(i, i + 1))) {
                    System.out.print(BG_GREEN + guess.substring(i, i + 1) + RESET);
                } else if (correct.indexOf(guess.substring(i, i + 1)) > -1) {
                    System.out.print(BG_YELLOW + guess.substring(i, i + 1) + RESET);
                } else {
                    System.out.print(guess.substring(i, i + 1));
                }
            }

            System.out.println("");

            // Check if the guess matches the correct word
            if (guess.equals(correct)) {
                System.out.println("Correct! You win!");
                break;
            }
        }

        // If the user didn't guess the word in 6 attempts, reveal the correct word
        if (!guess.equals(correct)) {
            System.out.println("Wrong! The correct word is " + correct + ".");
        }
    }

    /**
     * Prompt the user for a guess and validate it. The guess should be exactly 5
     * letters.
     * 
     * @param scanner The Scanner object to read user input
     * @return The validated guess from the user
     */
    // this checks if user inputs are 5 letter short or long src:
    // https://stackoverflow.com/questions/4047808/what-is-the-best-way-to-tell-if-a-character-is-a-letter-or-number-in-java-withou
    public static String getValidatedGuess(Scanner scanner) {
        String guess;
        while (true) {
            System.out.print("Guess the 5 letter word > ");
            guess = scanner.nextLine().toUpperCase();
            if (guess.length() < 5) {
                System.out.println("Your guess is too short. Try again.");
            } else if (guess.length() > 5) {
                System.out.println("Your guess is too long. Try again.");
            } else {
                break;
            }
        }
        return guess;
    }

    /**
     * Load words from a file into an ArrayList.
     * 
     * @return ArrayList containing words.
     */
    public static ArrayList<String> loadWordsFromFile() {
        ArrayList<String> wordsList = new ArrayList<>();
        try {
            Scanner fileScanner = new Scanner(new File("sgb-words.txt"));
            while (fileScanner.hasNextLine()) {
                wordsList.add(fileScanner.nextLine().trim().toUpperCase());
            }
            fileScanner.close();
        } catch (Exception e) {
            System.out.println("Error reading words from file: " + e.getMessage());
        }
        return wordsList;
    }

    /**
     * Save an ArrayList of words back to the file.
     * 
     * @param wordsList The list of words to save.
     */
    public static void saveWordsToFile(ArrayList<String> wordsList) {
        try {
            java.io.PrintWriter writer = new java.io.PrintWriter(new File("sgb-words.txt"));
            for (String word : wordsList) {
                writer.println(word);
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error writing words to file: " + e.getMessage());
        }
    }
}
