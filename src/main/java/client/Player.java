package client;

public class Player {
    private static String name;

    public Player() { }

    public Player(String name) {
        Player.name = name;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Player.name = name;
    }
}