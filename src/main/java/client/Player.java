package client;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private static String name;
    private static boolean Drawer = false;
    private static double playerX;
    private static double playerY;


    private static ArrayList<String> players;
    private static String word;
  
    public Player() { /**/ }

    public Player(String name, double playerX, double playerY, String word) {
        Player.name = name;
        Player.playerX = playerX;
        Player.playerY = playerY;
      
        Player.players = new ArrayList<String>();
        Player.word = word;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Player.name = name;
    }

    public static double getPlayerX() {
        return playerX;
    }

    public static void setPlayerX(double playerX) {
        Player.playerX = playerX;
    }

    public static double getPlayerY() {
        return playerY;
    }

    public static void setPlayerY(double playerY) {
        Player.playerY = playerY;
    }

    public static boolean isDrawer() {
        return Drawer;
    }

    public static void setDrawer(boolean drawer) {
        Player.Drawer = drawer;
    }

    public static void setPlayerList(String name) {
        Player.players.add(name);
    }

    public static void printPlayerList() {
        System.out.println(Player.players);

    public static void setWord(String word) {
        Player.word = word;
    }

    public static String getWord() {
        return word;
    }
}