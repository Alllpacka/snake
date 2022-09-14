package com.company;

public class Main {
    public static Game game;

    public static void main(String[] args) {
        try {
            switch (args.length) {
                case 0 -> game = new Game();
                case 1 -> game = new Game(Integer.parseInt(args[0]));
                case 2 -> game = new Game(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            }
        } catch (Exception exception) {
            //TODO HELP
        }
        game.startGame();
    }
}