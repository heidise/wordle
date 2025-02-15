package fi.tuni.prog3.wordle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class GameLogic {
    
    private int guesses;
    private ArrayList<String> wordList = new ArrayList<>();
    private int wordLength;
    private int listIndex = 0;
    private String word; // Word to guess
    
    public GameLogic() throws IOException {
        this.wordList = new ArrayList<>();
        readFile("words.txt");
        initGame();
    }
    
    public void readFile(String fileName) throws IOException {
    try(var file = new BufferedReader(new FileReader(fileName))) {
        String line;
        while((line = file.readLine()) != null) {
            wordList.add(line.toUpperCase());
            }
        }
    }

    public void initGame() {
        // First, randomize the word list.
        Collections.shuffle(this.wordList);
        this.word = wordList.get(listIndex);
        this.wordLength = word.length();
        this.listIndex += 1; // Let's take another word from the list for the next game. 
        this.guesses = 0;
        // ERRORS HAVEN*T BEEN HANDLED IF THERE ARE NO WORDS LEFT!
    }
    
    public int checkLetter(String letter, int index) {
        // Returns -1 -> letter is incorrect
        //          0 -> letter is correct but in wrong place
        //          1 -> letter is correct
        String correctLetter = Character.toString(word.charAt(index));
        if (letter.equals(correctLetter)) {
            return 1;
        }
        else if (word.contains(letter)) {
            return 0;
        }
        else {
            return -1;
        }
    }
    
    public void startNewRow() {
        this.guesses += 1;
    }
    
    public int getWordLength() {
        return wordLength;
    }
    
    public int getGuesses() {
        return guesses;
    }

}