package hw01_jotto_VeronicaCortezD.src.main.java;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author VeronicaCortezD
 * @version 0.1.0
 * @Since 1/29/26
 **/

public class Jotto {
    private static final int WORD_SIZE = 5;
    private String currentWord;
    private int score;
    private ArrayList<String> playGuesses = new ArrayList<>();
    private ArrayList<String> playWords = new ArrayList<>();
    private String filename;
    private ArrayList<String> wordList = new ArrayList<>();
    private static final boolean DEBUG = false;

    public Jotto(String filename){
        this.filename = filename;
        readWords();
    }

    public ArrayList<String> getPlayedWords() {
        return playWords;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public ArrayList<String> getPlayGuesses() {
        return playGuesses;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    public ArrayList<String> readWords(){
        try (FileReader fr = new FileReader(filename)){
            Scanner scan = new Scanner(fr);
            while (scan.hasNext()) {
                String newWord = scan.next();
                if(!wordList.contains(newWord)){
                    wordList.add(newWord);
                }
            }
        } catch (Exception e) {
            System.out.println("Couldn't open " + filename);
        }
        return wordList;
    }

    public void play(){
        Scanner scan = new Scanner(System.in);
        String userChoice;
        System.out.println("Welcome to the game.\n" + "Current Score:" + score);
        while (true){
            System.out.println("=-=-=-=-=-=-=-=-=-=-=");
            System.out.println("Choose one of the following:\n" +
                    "1:      Start the game\n" +
                    "2:      See the word list\n" +
                    "3:      See the chosen words\n" +
                    "4:      Show Player guesses\n" +
                    "zz to exit");
            System.out.print("=-=-=-=-=-=-=-=-=-=-=\nWhat is your choice: ");
            userChoice = scan.nextLine().toLowerCase();
            if(userChoice.equals("zz")){
                break;
            }
            if(userChoice.equals("1") || userChoice.equals("one")){
                if(!pickWord()){
                    showPlayerGuesses();
                } else{
                    score += guess();
                    System.out.println("Current Score: " + score);
                }
            } else if(userChoice.equals("2") || userChoice.equals("two")){
                System.out.print(showWordList());
            } else if(userChoice.equals("3") || userChoice.equals("three")){
                System.out.print(showPlayedWords());
            } else if(userChoice.equals("4") || userChoice.equals("four")) {
                showPlayerGuesses();
            } else{
                System.out.println("I don't know what \"" + userChoice + "\" is.");
            }
            System.out.println("Press enter to continue");
            scan.nextLine();
        }
        System.out.println("Final score: " + score + "\nThank you for playing");
    }

    public String showPlayedWords(){
        StringBuilder sb = new StringBuilder();
        if(playWords.isEmpty()){
            return "No words have been played.";
        }
        sb.append("Current list of played words:\n");
        for(int i = 0; i < playWords.size(); i++){
            sb.append(playWords.get(i)).append("\n");
        }
        return sb.toString();
    }

    public String showWordList(){
        StringBuilder sb = new StringBuilder();
        sb.append("Current word list:\n");
        for(int i = 0; i < wordList.size(); i++){
            sb.append(wordList.get(i)).append("\n");
        }
        return sb.toString();
    }

    public ArrayList<String> showPlayerGuesses(){
        if(playGuesses.isEmpty()){
            System.out.println("No guesses yet");
            return playGuesses;
        }
        System.out.println("Current player guesses:");
        for(int i = 0; i < playGuesses.size(); i++){
            System.out.println(playGuesses.get(i));
        }
        Scanner scan = new Scanner(System.in);
        System.out.println("Would you like to add the words to the word list? (y/n)");
        String answer = "n";
        if(scan.hasNextLine()){
            answer = scan.nextLine().toLowerCase();
        }
        if(answer.equals("y")){
            System.out.println("Updating word list.");
            updateWordList();
            System.out.print(showWordList());
        }
        return playGuesses;
    }

    public int guess(){
        ArrayList<String> currentGuesses = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        int letterCount;
        int score = WORD_SIZE + 1;
        String wordGuess;
        while (true){
            System.out.println("Current Score: " + score);
            System.out.print("What is your guess (q to quit): ");
            wordGuess = scan.nextLine().toLowerCase();
            if(wordGuess.equals("q")){
                score = Math.min(score, 0);
                break;
            }
            if(wordGuess.length() != WORD_SIZE){
                System.out.println("Word must be " + WORD_SIZE + " characters (" + wordGuess + " has " + wordGuess.length() +")");
                continue;
            }
             if(currentGuesses.contains(wordGuess)){
                System.out.println("You already guessed " + wordGuess + " - Try again!");
                continue;
             }
            addPlayerGuess(wordGuess);
            currentGuesses.add(wordGuess);
             if(wordGuess.equals(currentWord)){
                System.out.println("DINGDINGDING!!! the word was " + currentWord);
                playerGuessScores(currentGuesses);
                return score;
            }
             letterCount = getLetterCount(wordGuess);
            if(letterCount != WORD_SIZE) {
                System.out.println(wordGuess + " has a Jotto score of " + letterCount);
            } else {
                System.out.println(wordGuess + " is an ANAGRAM");
            }
            score--;
            playerGuessScores(currentGuesses);
        }
        return score;
    }

    public int getLetterCount(String wordGuess){
        String guess = wordGuess.toLowerCase();
        int count = 0;
        if(guess.equals(currentWord)){
            return WORD_SIZE;
        }
        ArrayList<Character> uniqueChar = new ArrayList<>();
        for(int i = 0; i < currentWord.length(); i++){
            char c = currentWord.charAt(i);
            if(!uniqueChar.contains(c)){
                uniqueChar.add(c);
            }
        }
        for(int i = 0; i < guess.length(); i++){
            char c = guess.charAt(i);
            if(uniqueChar.contains(c)){
                count++;
                uniqueChar.remove((Character) c);
            }
        }
        return count;
    }

    public void updateWordList(){
        for (int i = 0; i < playGuesses.size(); i++) {
            if (!wordList.contains(playGuesses.get(i))) {
                wordList.add(playGuesses.get(i));
            }
        }
        try (FileWriter fw = new FileWriter(filename)){
            for (int i = 0; i < wordList.size(); i++) {
                    fw.write(wordList.get(i) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Couldn't open " + filename);
        }
    }

    public boolean pickWord(){
        int index = (int) (Math.random() * wordList.size());
        currentWord = wordList.get(index);
        if(playWords.contains(currentWord) && playWords.size() == wordList.size()){
            System.out.println("You've guessed them all!");
            return false;
        } else if (playWords.contains(currentWord) && playWords.size() != wordList.size()) {
            return pickWord();
        }
        playWords.add(currentWord);
        if(DEBUG){
            System.out.println(currentWord);
        }
        return true;
    }

    public boolean addPlayerGuess(String wordGuess){
        if(!playGuesses.contains(wordGuess)){
            playGuesses.add(wordGuess);
            return true;
        }
        return false;
    }

    public void playerGuessScores(ArrayList<String> guesses){
        System.out.println("Guess Score");
        for(int i = 0; i < guesses.size(); i++){
            System.out.println(guesses.get(i) + " " + getLetterCount(guesses.get(i)));
        }
    }
}
