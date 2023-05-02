/**
 * Corey Walker Assignment4
 * Typing game with GUI
 * This class is the main method to run the game.
 * The class sets up the GUI elements and handles animation
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicBoolean;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Setups up all the JavaFX GUI controls and creates instances of
     * all the helper classes.
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Always make sure to set the title of the window
        primaryStage.setTitle("Key Shooter");
        // Width/height variables so that we can mess with the size of the window
        double width = 600;
        double height = 600;
        // BorderPane (https://openjfx.io/javadoc/18/javafx.graphics/javafx/scene/layout/BorderPane.html)
        // Provides the basis which we basis the rest of the GUI on
        BorderPane window = new BorderPane();
        // VBox for the top part of the GUI
        VBox topVBox = new VBox(5);
        topVBox.setAlignment(Pos.CENTER);
        // Label which displays the score
        Label scoreLabel = new Label("0");
        scoreLabel.setFont(new Font(40));
        // Button to end the game
        Button endButton = new Button("End");
        Alert endScreen = new Alert(Alert.AlertType.INFORMATION);

        // Slider to change how often a word shows up on screen
        Slider wordsFrequencySlider = new Slider(1, 5, 3);
        wordsFrequencySlider.setMajorTickUnit(1);
        wordsFrequencySlider.setShowTickMarks(true);
        wordsFrequencySlider.setShowTickLabels(true);

        // add to hbox
        HBox controls = new HBox(5);
        controls.getChildren().addAll(endButton, wordsFrequencySlider);
        // Label which displays the currently typed letters
        Label typedLabel = new Label();
        typedLabel.setFont(new Font(40));
        // Add them all to the VBox
        topVBox.getChildren().addAll(controls, scoreLabel, typedLabel);
        // Put them in the top of the BorderPane
        window.setTop(topVBox);
        // Create an instance of our helper Words class
        Words words = new Words("./docs/words.txt", width, (height * 3) / 4,
                                scoreLabel, typedLabel);
        // Put it in the middle of the BorderPane
        window.setCenter(words.getWordsPane());
        // Create a VBox for the keyboard
        VBox keyBoardWindow = new VBox(10);
        // Create an instance of our helper class Keyboard
        Keyboard keyboard = new Keyboard(width, height / 4, 10);
        // Add a horizontal line above the keyboard to create clear seperation
        keyBoardWindow.getChildren().addAll(new Separator(Orientation.HORIZONTAL), keyboard.getKeyboard());
        // Put it in the bottom of the BorderPane
        window.setBottom(keyBoardWindow);
        // Create the scene
        Scene scene = new Scene(window, width, height);
        // The scene is the best place to capture keyboard input
        // First get the KeyCode of the event
        // Then start the fill transition, which blinks the key
        // Then add it to the typed letters
        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            keyboard.startFillTransition(keyCode);
            words.addTypedLetter(keyCode);
        });
        // Set the scene
        primaryStage.setScene(scene);
        // Showtime!
        primaryStage.show();

        // We also need an AnimationTimer to create words on the
        // screen every 3 seconds. This is done by call createWord
        // from the Words class.
        int minutesPlayed;
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            double elapsedTime = 0;
            @Override
            public void handle(long now) {
                double wordsFrequency = wordsFrequencySlider.getValue();
                if (now - lastUpdate >= wordsFrequency*1_000_000_000L) {
                    words.createWord();
                    elapsedTime += now;
                    lastUpdate = now;
                }
            }
        };

        // We will need to track the play time of the user, so get the start time
        Instant start = Instant.now();
        timer.start();

        // When the button is pressed, get end time, display words per minute in a new window, then quit
        endButton.setOnAction(event -> {
            timer.stop();
            Instant end = Instant.now();
            long timePassed = Duration.between(start, end).toMinutes();
            long wpm = Long.parseLong(scoreLabel.getText());
            try {
                wpm = Integer.valueOf(scoreLabel.getText()) / timePassed;
            } catch (ArithmeticException e){}
            endScreen.setTitle("Words per Minute");
            endScreen.setHeaderText("Good job!\nWords Typed: " + scoreLabel.getText());
            endScreen.setContentText("You typed: " + wpm + " words per minute");
            endScreen.showAndWait();

            System.exit(0);
        });
    }
}
