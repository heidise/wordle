package fi.tuni.prog3.wordle;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Wordle extends Application {
    private final String WIN = "Congratulations, you won!";
    private final String LOSS = "Game over, you lost!";
    private final String INFO = "Check if the word is correct by pressing enter.";
    private final String NOT_COMPLETE = "Give a complete word before pressing Enter!";
    
    private GridPane grid;
    private GameLogic game;
    private Label infoLabel;
    
    public void createGrid(int wordLength) {
        for (int i = 0; i < 6; i++) {
            HBox hb = new HBox(2);
            for (int j = 0; j < wordLength; j++) {
                TextField box = new TextField();
                box.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
                box.setStyle("-fx-text-box-border: black;");
                box.setTextFormatter(new TextFormatter<String>((Change change) -> {
                    String newText = change.getControlNewText();
                    change.setText(change.getText().toUpperCase());
                    if (newText.length() > 1) {
                        return null;
                    } else {
                        return change;
                    }
                }));
                box.setId(String.format("%d_%d",i,j));
                box.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        String Id = box.getId();
                        int j = Character.getNumericValue(Id.charAt(Id.length()-1));
                        int i = game.getGuesses();
                        int wordLength = game.getWordLength();

                        // If user uses backspace to delete letters
                        if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                            if (j > 0) {
                                j -= 1;
                                Node node = grid.lookup("#" + i + "_" + j);
                                ((TextField)node).requestFocus();
                            }
                            box.setText("");   
                        }

                        // When user presses enter, and letters will be checked
                        else if (event.getCode().equals(KeyCode.ENTER)){
                            if(j != wordLength-1) {
                                // Word is not ready
                                infoLabel.setText(NOT_COMPLETE);
                            }
                            else {
                                infoLabel.setText(INFO);
                                int correct_letter_amount = 0;
                                for (int a = 0; a < wordLength; a++) {
                                    Node node = grid.lookup("#" + i + "_" + a);
                                    String letter = ((TextField)node).getText();
                                    int result = game.checkLetter(letter, a);
                                    ((TextField)node).setEditable(false);
                                    //((TextField)node).setDisable(true);
                                    ((TextField)node).setFocusTraversable(false);
                                    if (result == -1) {
                                        ((TextField)node).setBackground(new Background(new BackgroundFill(Color.GREY,null,null)));
                                    }
                                    else if (result == 0) {
                                        ((TextField)node).setBackground(new Background(new BackgroundFill(Color.ORANGE,null,null)));
                                    }
                                    else {
                                        correct_letter_amount += 1;
                                        ((TextField)node).setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));
                                    }
                                    ((TextField)node).setStyle("-fx-text-fill: white");

                                }
                                // Checks if the word was guessed and new game needs to be started
                                if (correct_letter_amount == wordLength) {
                                    infoLabel.setText(WIN);
                                    grid.requestFocus();
                                    for (int r = i+1; r < 6; r++) {
                                        for (int c = 0; c < wordLength; c++) {
                                            Node node = grid.lookup("#" + r + "_" + c);
                                            ((TextField)node).setEditable(false);
                                            ((TextField)node).setDisable(true);
                                            ((TextField)node).setStyle("-fx-opacity: 1.0;");
                                        }
                                    }
                                }
                                // Checks if there is no more guesses left
                                else if (i == 5) {
                                    infoLabel.setText(LOSS);
                                    grid.requestFocus();
                                }
                                // Otherwise, new row will start.
                                else {
                                    game.startNewRow();
                                    int new_row = i+1;
                                    Node node = grid.lookup("#" + new_row + "_0");
                                    ((TextField)node).requestFocus();
                                    infoLabel.setText(INFO);
                                }
                            }
                        }
                    
                    else {
                        if(box.getText().length() == 1) {
                            if(j != wordLength-1) {
                                j += 1;
                                Node node = grid.lookup("#" + i + "_" + j);
                                ((TextField)node).requestFocus();
                            }
                        }
                        }
                    }
                });
                
                hb.getChildren().add(box);
            }
            hb.setId("row" + i);
            grid.add(hb, 1, i+1);
            grid.setVgap(10);
        }
        
        // Let's set the focus on the first row's first box
        grid.setPadding(new Insets(10,10,10,10));
    }

    @Override
    public void start(Stage stage) throws IOException {
        
        game = new GameLogic();
        
        stage.setTitle("Wordle");
        
        grid = new GridPane();
        Scene scene = new Scene(grid,400,300);
        stage.setScene(scene);
        
        Button startButton = new Button("Start new game");
        startButton.setMinWidth(100);
        startButton.setId("newGameBtn");
    grid.add(startButton, 0,0);
        
        infoLabel = new Label(INFO);
        infoLabel.setId("infoBox");
        grid.add(infoLabel, 1, 0);
        grid.setMargin(infoLabel, new Insets(10));
        
        int wordLength = game.getWordLength();
        createGrid(wordLength);
        Node node = grid.lookup("#0_0");
        ((TextField)node).requestFocus();

        // "Start game" button actions
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent e) {
                
                grid.getChildren().remove((grid.lookup("#0_0")));
                // Let's remove previos text boxes
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < wordLength; j++) {
                        Node node = grid.lookup("#" + i + "_" + j); // Doesn't work????????+
                        grid.getChildren().remove(node);
                    }
                    grid.getChildren().remove(grid.lookup("#row" + i));
                }
                game.initGame();
                int wordLength = game.getWordLength();
                createGrid(wordLength); 
                Node node = grid.lookup("#0_0");
                ((TextField)node).requestFocus();
                infoLabel.setText(INFO);
            }
        });
        
        
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}