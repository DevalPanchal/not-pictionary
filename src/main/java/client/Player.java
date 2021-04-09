package client;

public class Player {
    private static String name;
    private static double playerX;
    private static double playerY;

    public Player() { }

    public Player(String name, double playerX, double playerY) {
        Player.name = name;
        Player.playerX = playerX;
        Player.playerY = playerY;
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
}