import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Words {
    // Pane (https://openjfx.io/javadoc/18/javafx.graphics/javafx/scene/layout/Pane.html)
    // which represents the floating words part of the game
    private final Pane wordsPane;
    // List of all available words
    private final List<String> words;
    // List of all JavaFX floating words currently on the screen
    private final List<WordBox> activeWords;
    // List of all keys that have been pressed since the last correct word
    private final List<KeyCode> typed;
    // JavaFX Label which shows the score on the screen
    private final Label scoreLabel;
    // Keeps track of the number of correct words
    private int score = 0;
    // JavaFX Label which shows what the user has typed since the last correct word
    private final Label typedLabel;
    // Width/height of the screen
    private final double width;
    private final double height;

    public Words(String path, double width, double height,
                 Label scoreLabel, Label typedLabel) throws FileNotFoundException {
        wordsPane = new Pane();
        wordsPane.setPrefWidth(width);
        wordsPane.setPrefHeight(height);

        this.words = Utils.readWords(path);

        activeWords = new ArrayList<>();
        typed = new ArrayList<>();

        this.scoreLabel = scoreLabel;
        this.typedLabel = typedLabel;

        this.width = width;
        this.height = height;
    }

    public Pane getWordsPane() {
        return wordsPane;
    }

    /**
     * Removes the wordBox from the wordsPane as well as
     * removing it from activeWords.
     * @param wordBox WordBox to remove
     */
    private void removeWord(WordBox wordBox) {
        wordsPane.getChildren().remove(wordBox.getWordBox());
        activeWords.remove(wordBox);
    }

    /**
     * Creates a random floating word.
     * Choses a random word from the list of words.
     * Then chooses a starting point on any edge of the screen.
     * Then creates a Timeline (https://openjfx.io/javadoc/18/javafx.graphics/javafx/animation/Timeline.html)
     * that moves the WordBox from its starting point to a random ending
     * point over 10 seconds.
     */
    public void createWord() {
        String randomWord = words.get(ThreadLocalRandom.current().nextInt(words.size()));

        WordBox randomWordBox = new WordBox(25, randomWord, Color.WHITE);
//        int startingX = ThreadLocalRandom.current().nextInt((int)width);
        int startingY = ThreadLocalRandom.current().nextInt((int)(height - (height/4)));
        int endingX = ThreadLocalRandom.current().nextInt((int)width - 50);
        int endingY = ThreadLocalRandom.current().nextInt((int)(height));

        randomWordBox.getWordBox().relocate(0, startingY);

        Timeline moveWord = new Timeline();
        moveWord.setRate(2);
        moveWord.setAutoReverse(true);
        moveWord.getKeyFrames().add(new KeyFrame(Duration.seconds(10),
                new KeyValue (randomWordBox.getWordBox().translateXProperty(), endingX)));
        moveWord.getKeyFrames().add(new KeyFrame(Duration.seconds(10),
                new KeyValue (randomWordBox.getWordBox().translateYProperty(), endingY - (height/4))));
        moveWord.play();

        wordsPane.getChildren().add(randomWordBox.getWordBox());
        activeWords.add(randomWordBox);
    }

    /**
     * Adds the keyCode to typed if it is a letter key.
     * Removes the first element of typed if it is the backspace key.
     * Either way it checks for a correct word and updates the typedLabel.
     * @param keyCode KeyCode to add to the state
     */
    public void addTypedLetter(KeyCode keyCode) {
        if (keyCode.isLetterKey()) {
            typed.add(keyCode);

        }
        else if (keyCode.compareTo(keyCode.BACK_SPACE) == 0){
            typed.remove(typed.size() - 1);
        }
        checkForCorrectWord(Utils.combineList(typed));
        typedLabel.setText(Utils.combineList(typed));
    }

    /**
     * Checks if the given String is equal to any of the currently
     * active words. If it is then it updates the score and scoreLabel.
     * It also removes the wordBox and clears the typed list.
     * @param s Word to check
     */
    private void checkForCorrectWord(String s) {
        for (WordBox activeWord : activeWords) {
            if (activeWord.getWord().equals(s)){
                score++;
                scoreLabel.setText(Integer.toString(score));
                typed.clear();
                wordsPane.getChildren().remove(activeWord.getWordBox());
            }
        }
    }
}
