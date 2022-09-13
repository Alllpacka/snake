package com.company;

import java.util.Random;

public class Game {
    public final int width;
    public final int height;

    private Area area;
    private Snake snake;

    private boolean gameOver;

    public Game() {
        this.width = 8;
        this.height = 8;
    }

    public Game(int size) {
        this.width = size;
        this.height = size;
    }

    public Game(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void startGame() {
        area = new Area(width, height);

        spawnPlayer();
        gameLoop();
    }

    private void spawnPlayer() {
        snake = new Snake(new Point(width / 2, height / 2));
        area.setField(snake.getHeadPoint(), State.Head);
    }

    private void gameLoop() {
        while (!gameOver) {

            draw();
        }
    }

    private void spawnCoin() {

    }

    private void gameOver() {

    }

    private void draw() {
        drawEdge();
        for (Field[] row : area.getField()) {
            System.out.print("|");
            for (Field field : row) {
                System.out.print(field.translateState());
                System.out.print(" ");
            }
            System.out.println("|");
        }
        drawEdge();
    }

    private void drawEdge() {
        System.out.print("|");

        for (int i = 0; i < width * 2; i++) {
            System.out.print("-");
        }

        System.out.print("| \n");
    }
}