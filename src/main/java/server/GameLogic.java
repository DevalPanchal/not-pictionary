package server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameLogic {
    private final List<String> wordList;
    private static String currentWord = "";
    private final List<Player> playerList;
    private int round = 1;

    public GameLogic(List<Player> pList){
        this.playerList = pList;

        //load the dictionary
        this.wordList = parseFile(new File("./src/main/resources/word-lists/pokemon-word-list.csv"));
    }

    // Choose a player to draw
    /**
     * This function iterates through a list of players and chooses 
     * the next player to be the drawer
     */
    public Player chooseDrawer(){
        playerList.get((this.getRound()-1)%playerList.size()).setDrawer(false);
        playerList.get(this.getRound()%playerList.size()).setDrawer(true);
        return playerList.get(this.getRound() % playerList.size());
    }

    /**
     * This function parses a comma-separated-values file 
     * (will work with a comma separated text file).
     * Adds every word to a word list as long as the words are 
     * separated by commas
     * @param file - file that contains list of words (.csv)
     */
    public static ArrayList<String> parseFile(File file){
        try {
            ArrayList<String> words = new ArrayList<>();
            Scanner scan = new Scanner(file);
            scan.useDelimiter(",");
            while(scan.hasNext()){
                words.add(scan.next().replaceAll("\\s",""));
            }
            scan.close();
            return words;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  Picks a random word from the loaded words, stores it as the current word
     */
    public void setRandomWord(){
        Random rand = new Random();
        int index = rand.nextInt(wordList.size());
        setCurrentWord(wordList.get(index));
    }

    /**
     * Determines if the parameter is the current word
     * @param word word to test
     * @return boolean, is the parameter the current word?
     */
    public boolean isCorrectWord(String word){
        // Remove need to check upper or lower case by casting to uppercase
        return word.equalsIgnoreCase(currentWord);
    }

    // Getters and Setters
    public void setCurrentWord(String word){
        currentWord = word;
    }

    public String getCurrentWord(){ return currentWord; }

    /**
     * Increment the round number by 1
     */
    public void incRound() {
        this.round++;
    }

    public int getRound(){
        return this.round;
    }
}
