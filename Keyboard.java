import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.*;

/**
 * Corey Walker
 * Class for dealing with the keyboard displayed on the screen
 * A keyboard has a list of keycodes for letters, a map of keycodes to their gui representation
 * The GUI elements to display the keyboard
 */
public class Keyboard {
    // 2 Dimensional list representing the rows of keys on the keyboard
    // Letter keys only
    private final List<List<KeyCode>> keyCodes;
    // Map that is used to access the keys JavaFX representation
    private final Map<KeyCode, WordBox> keyCodeToWordBox;
    // JavaFX control that represents the keyboard on the screen
    private final VBox keyboard;
    // Color that the keys are by default
    private static final Color from = Color.color(0.9, 0.9, 0.9);
    // Color that the keys become when pressed
    private static final Color to = Color.color(0.3, 0.3, 0.8);

    public Keyboard(double width, double height, double spacing) {
        keyCodes = initializeKeys();
        keyCodeToWordBox = new HashMap<>();

        keyboard = initializeKeyboard(width, height, keyCodes, spacing);
    }

    /**
     * getter for the keyboard VBox GUI element
     * @return
     */
    public VBox getKeyboard() {
        return keyboard;
    }

    /**
     * First checks if the given keyCode exists in the keyCodeToWordBox.
     * If it does then it starts a FillTransition
     *  --(https://openjfx.io/javadoc/18/javafx.graphics/javafx/animation/FillTransition.html)
     * to go from the from color to the to color.
     * If the keyCode does not exist then it does nothing.
     * @param keyCode KeyCode to lookup in the map and flash
     */
    public void startFillTransition(KeyCode keyCode) {
        if (keyCodeToWordBox.containsKey(keyCode)){
            WordBox wordBox = keyCodeToWordBox.get(keyCode);
            FillTransition fill = new FillTransition(Duration.millis(300), wordBox.getRect(), from, to);
            fill.play();
            FillTransition clear = new FillTransition(Duration.millis(300), wordBox.getRect(), to, from);
            clear.play();
        }
    }

    /**
     * Simply creates the 2D list that represents the keyboard.
     * Each row is an element of the outer list and each inner list
     * contains all the letter keys in that row. Only contains
     * 3 rows. All letters are uppercase.
     * @return 2D list representing the letters on the keyboard
     */
    private List<List<KeyCode>> initializeKeys() {
        List<List<KeyCode>> keyRows = new ArrayList();

        List<KeyCode> topRow = new ArrayList<>();
        List<KeyCode> midRow = new ArrayList<>();
        List<KeyCode> botRow = new ArrayList<>();

        //adding keys from top row to inner array
        topRow.add(KeyCode.getKeyCode("Q"));
        topRow.add(KeyCode.getKeyCode("W"));
        topRow.add(KeyCode.getKeyCode("E"));
        topRow.add(KeyCode.getKeyCode("R"));
        topRow.add(KeyCode.getKeyCode("T"));
        topRow.add(KeyCode.getKeyCode("Y"));
        topRow.add(KeyCode.getKeyCode("U"));
        topRow.add(KeyCode.getKeyCode("I"));
        topRow.add(KeyCode.getKeyCode("O"));
        topRow.add(KeyCode.getKeyCode("P"));

        //adding keys from middle row to inner array
        midRow.add(KeyCode.getKeyCode("A"));
        midRow.add(KeyCode.getKeyCode("S"));
        midRow.add(KeyCode.getKeyCode("D"));
        midRow.add(KeyCode.getKeyCode("F"));
        midRow.add(KeyCode.getKeyCode("G"));
        midRow.add(KeyCode.getKeyCode("H"));
        midRow.add(KeyCode.getKeyCode("J"));
        midRow.add(KeyCode.getKeyCode("K"));
        midRow.add(KeyCode.getKeyCode("L"));

        //adding keys from bottom row to inner array
        botRow.add(KeyCode.getKeyCode("Z"));
        botRow.add(KeyCode.getKeyCode("X"));
        botRow.add(KeyCode.getKeyCode("C"));
        botRow.add(KeyCode.getKeyCode("V"));
        botRow.add(KeyCode.getKeyCode("B"));
        botRow.add(KeyCode.getKeyCode("N"));
        botRow.add(KeyCode.getKeyCode("M"));

        //adding each row to the keyRows arrayList
        keyRows.add(topRow);
        keyRows.add(midRow);
        keyRows.add(botRow);


        return keyRows;
    }

    /**
     * Creates the JavaFX control that visualized the keyboard on the screen
     * Also initializes the keyCodeToWordBox map as it goes.
     * It deduces the size of each key using the 2D list and the
     * width parameter. Then creates a VBox and sets its width/height
     * and centers it. Then loops over the 2D list and creates JavaFX
     * controls, WordBox, to represent each key and adds them to HBoxes.
     * The adds the row HBox to the VBox. It also adds the WordBox to the
     * map. Then it moves on to the next row.
     * @param width Width of the screen
     * @param height Height of the screen
     * @param keyCodes 2D list that holds all the letters on the keyboard
     * @param spacing Space between each key
     * @return JavaFX control that visualizes the keyboard on the screen
     */
    private VBox initializeKeyboard(double width, double height, List<List<KeyCode>> keyCodes, double spacing) {
        WordBox letterBox;
        Double keySize = width / (keyCodes.get(0).size()+ spacing);
        String letter;

        HBox rowOne = new HBox(spacing);
        rowOne.setPrefWidth(width);
        rowOne.setAlignment(Pos.CENTER);
        HBox rowTwo = new HBox(spacing);
        rowTwo.setAlignment(Pos.CENTER);
        HBox rowThree = new HBox(spacing);
        rowThree.setAlignment(Pos.CENTER);

        VBox keyBoardVBox = new VBox(spacing);
        keyBoardVBox.setPrefHeight(height/4);
        keyBoardVBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < keyCodes.size(); i++){

            for (int j = 0; j < keyCodes.get(i).size(); j++){
                letter = keyCodes.get(i).get(j).toString();
                letterBox = new WordBox(keySize, letter, from);
                keyCodeToWordBox.put(keyCodes.get(i).get(j), letterBox);
                if (i == 0) {
                    rowOne.getChildren().add(letterBox.getWordBox());
                }
                if (i == 1) {
                    rowTwo.getChildren().add(letterBox.getWordBox());
                }
                if (i == 2) {
                    rowThree.getChildren().add(letterBox.getWordBox());
                }
            }
        }

        keyBoardVBox.getChildren().addAll(rowOne, rowTwo, rowThree);

        return keyBoardVBox;
    }
}