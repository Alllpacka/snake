package at.htlhl;

public class Main {
    public static Game game;
    public static Menu menu;

    public static void main(String[] args) {
        menu = new Menu();
        try {
            switch (args.length) {
                case 0 -> game = new Game();
                case 1 -> game = new Game(Integer.parseInt(args[0]));
                case 2 -> game = new Game(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            }
        } catch (Exception exception) {
            System.out.println("  _   _      _       \n" +
                    " | | | | ___| |_ __  \n" +
                    " | |_| |/ _ \\ | '_ \\ \n" +
                    " |  _  |  __/ | |_) |\n" +
                    " |_| |_|\\___|_| .__/ \n" +
                    "              |_|    ");
            System.out.println("Verwende W,A,S,D oder die Pfeiltasten um die \"Snake\" (#) zu steuern. ");
            System.out.println("Beiße dir dabei nicht in den Schwanz. :)");
            System.out.println("Das Ziel des Spieles ist die Bitcoins aufzufressen damit deine Schlange länger wird. ");
            System.out.println("Du kannst beim Starten des Spieles optionale Parameter angeben. Die Standardgröße ist 16x12. ");
            System.out.println("-> ein Parameter: Dimension");
            System.out.println("-> zwei Parameter: Länge x Breite");
            System.exit(0);
        }
        game.connect();
        game.startGame();
    }
}