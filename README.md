# Wordle: Simple Wordle game made with JavaFX

This is a simple game made in Apache Netbeans with JavaFX. UI is simple and focuses on getting familiar with JavaFX. 

## How game works:

The game uses the same idea as Wordle game so the main idea is to guess the correct word. The game displays empty table in which row by row user tries to guess the correct word. User has total of 6 guesses and after each guess, user can see what letters were correct:
- Grey = letter isn't part of the word
- Yellow = letter is part of the word but not in the correct place
- Green = letter is part of the word and also in the correct place

<img src="https://github.com/heidise/wordle/blob/main/docs/example_pictures/game.PNG" width="300"/>

Incomplete words are not accepted so user needs to insert as many letters as the table displays.

<img src="https://github.com/heidise/wordle/blob/main/docs/example_pictures/incomplete.PNG" width="300"/>

User wins the game if they manage guess the word correctly before they run out of attempts. Game is lost if the correct word isn't guessed after 6 attempts.

<img src="https://github.com/heidise/wordle/blob/main/docs/example_pictures/winning.PNG" width="300"/>

<img src="https://github.com/heidise/wordle/blob/main/docs/example_pictures/lost.PNG" width="300"/>

New game can be started with the button.

## How to run the game:

This is a Maven based JavaFX game so to run it, you need to:

- `mvn clean install`
- `mvn javafx:run`

