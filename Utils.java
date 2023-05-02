import javafx.scene.input.KeyCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Corey Walker
 * utility class with methods for combing a list of keycodes into a string
 * and for reading in words from a document containing 5 letter words
 */
public class Utils {
    /**
     * Takes a list of keycodes and builds a string representing the typed keys
     * @param keyCodes list of keys that have been pressed
     * @return string representation of pressed keys
     */
    public static String combineList(List<KeyCode> keyCodes) {
        StringBuilder sb = new StringBuilder();

        for (KeyCode keyCode : keyCodes) {
            sb.append(keyCode.getChar());
        }

        return sb.toString();
    }

    /**
     * Reads words from a file using a scanner and adds the words to a List
     * @param path file location
     * @return list of words that can be shown on the screen
     * @throws FileNotFoundException thrown when the path is incorrect
     */
    public static List<String> readWords(String path) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(path));
        List<String> words = new ArrayList<>();

        while (sc.hasNextLine()) {
            words.add(sc.nextLine());
        }

        return words;
    }
}
