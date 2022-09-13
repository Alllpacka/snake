package com.company;

import java.util.Date;

public class Game implements Runnable {
    public final int width;
    public final int height;

    public Area area;
    public Snake snake;
    private boolean gameOver;
    private int timeBetweenTick = 200;

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
        Thread gameLoop = new Thread(this);
        gameLoop.start();
    }

    private void tick(){
        draw();
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

    @Override
    public void run() {
        Date startTime = new Date();
        Date currentTime;

        while (!gameOver) {
            System.out.print("");
            currentTime = new Date();

            if ((currentTime.getTime() - startTime.getTime()) >= timeBetweenTick) {
                startTime.setTime(startTime.getTime() + timeBetweenTick);
                tick();
            }
        }
    }
}