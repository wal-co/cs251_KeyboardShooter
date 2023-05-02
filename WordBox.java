import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * Corey Walker
 * A word box is a stackPane on which a word is printed and then displayed to the screen.
 * A wordBox has three getter methods
 */
public class WordBox {
    private final StackPane wordBox;
    private final Rectangle rect;
    private final String word;

    public WordBox(double size, String word, Color color) {
        wordBox = new StackPane();
        rect = new Rectangle(size, size, color);
        this.word = word.toUpperCase();
        Label text = new Label(this.word);
        text.setFont(new Font(size - 2));
        wordBox.getChildren().addAll(rect, text);
    }

    /**
     * getter for the stackPane gui element
     * @return StackPane
     */
    public StackPane getWordBox() {
        return wordBox;
    }

    /**
     * getter for the rectangle the word label is on
     * @return rect
     */
    public Rectangle getRect() {
        return rect;
    }

    /**
     * getter for the string representation of the word
     * @return word
     */
    public String getWord() {
        return word;
    }
}
