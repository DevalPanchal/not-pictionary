package server;

import client.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


public class GameLogic {

    private static List<String> wordList = new ArrayList<>();
    private String currentWord = "";
    private List<Player> playerList = new ArrayList<>();
    private static Timer timer;
    private int round = 1;
    private static int timeLimit = 60;


    // Constructor
    public GameLogic(Player player1, Player player2, Player player3, Player player4){
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        playerList.add(player4);
    }

    public GameLogic(List<Player> pList){
        this.playerList = new ArrayList<>(pList);
    }

    // Choose a player to draw
    /**
     * This function iterates through a list of players and chooses
     * the next player to be the drawer
     */
    public Player chooseDrawer(){
        if (round == 1) {
            // If it's the first round then set player 1 to drawer
            playerList.get(0).setDrawer(true);
            return playerList.get(0);
        } else {
            // Check for current drawer
            for (int i = 0; i < playerList.size(); i++){
                // If player(i) is the drawer, then player(i+1) is the next drawer
                if (playerList.get(i).getDrawer()){
                    // If last player is drawer, go back to first player
                    if (i + 1 > playerList.size()){
                        playerList.get(0).setDrawer(true);
                        return playerList.get(0);
                    } else {
                        // Set next player to drawer and previous to not drawer
                        playerList.get(i+1).setDrawer(true);
                        playerList.get(i).setDrawer(false);
                        return playerList.get(i+1);
                    }
                }
            }
        }
        return null;
    }

    /**
     * This function parses a comma-separated-values file
     * (will work with a comma separated text file).
     * Adds every word to a word list as long as the words are
     * separated by commas
     * @param file - file that contains list of words (.csv)
     */
    public static void parseFile(File file){
        try {
            Scanner scan = new Scanner(file);
            scan.useDelimiter(",");
            while(scan.hasNext()){
                wordList.add(scan.next());
            }
            scan.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Choose a word
    /**
     * This function returns a list of 3 word choices.
     * It works by copying the contents of the wordlist into a temporary list
     * and then in a for loop, randomly selects a word to add to the
     * resulting list. To eliminate multiples of the same word being chosen,
     * the temporary list removes each word that is added to the result list
     * @return List<String> of 3 words
     */
    public static List<String> setWordChoices(){
        List<String> result = new ArrayList<>();
        List<String> temp = new ArrayList<>(wordList);

        Random rand = new Random();

        for (int i = 0; i < 3; i++){
            int index = rand.nextInt(temp.size());
            String word = temp.get(index);
            result.add(word);
            temp.remove(word);
        }

        return result;
    }

    // Guessing Words
    public boolean isCorrectWord(String word){
        // Remove need to check upper or lower case by casting to uppercase
        if (word.toUpperCase().equals(currentWord.toUpperCase())) {
            return true;
        } else {
            return false;
        }
    }

    // Timer
    public static void countDownTimer(){
        int delay = 1000;
        int period = 1000;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            public void run(){
                System.out.println(tickDown());
            }
        }, delay, period);
    }

    private static int tickDown(){
        if (timeLimit >= 0){
            timer.cancel();
        }
        return timeLimit--;
    }

    // Getters and Setters
    public void setCurrentWord(String word){
        this.currentWord = word;
    }

    public String getCurrentWord(){
        return this.currentWord;
    }

    /**
     * Increment the round number by 1
     */
    public void incRound() {
        this.round++;
    }

    public void setRound(int number){
        this.round = number;
    }

    public int getRound(){
        return this.round;
    }

    public static void main(String[] args){
        File file = new File("./src/main/resources/word lists/pokemon-word-list.csv");
        parseFile(file);
        List<String> words = setWordChoices();
        // countDownTimer();
        // for (String word : words){
        //     System.out.println(word);
        // }
    }
}