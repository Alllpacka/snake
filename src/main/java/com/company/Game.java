package com.company;

import java.io.IOException;
import java.util.Date;

public class Game implements Runnable {
    public final int width;
    public final int height;
    public int score;
    private Point coinPoint;
    public Area area;
    public Snake snake;
    public boolean gameOver;
    private int timeBetweenTick = 800;

    public Game() {
        this.width = 16;
        this.height = 12;
    }

    public Game(int size) {
        this.width = size;
        this.height = size;
    }

    public Game(int width, int height) {
        this.width = width;
        this.height = height;
    }

    private void printTitleText() {
        System.out.println("  ____              _        _ \n" +
                " / ___| _ __   __ _| | _____| |\n" +
                " \\___ \\| '_ \\ / _` | |/ / _ \\ |\n" +
                "  ___) | | | | (_| |   <  __/_|\n" +
                " |____/|_| |_|\\__,_|_|\\_\\___(_)\n" +
                "                               ");
    }

    public void startGame() {
        printTitleText();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        area = new Area(width, height);
        spawnPlayer();
        spawnCoin();
        gameLoop();
        Input.startInputListener(snake);
    }

    private void spawnPlayer() {
        snake = new Snake(new Point(width / 2, height / 2));
        area.setField(snake.getHeadPoint(), Type.Head);
    }

    private void gameLoop() {
        Thread gameLoop = new Thread(this);
        gameLoop.start();
    }

    private void tick() {
        move();
        if (!gameOver) {
            checkCoin();
            draw();
        }
    }

    private void spawnCoin() {
        coinPoint = getRandomPoint();
        area.setField(coinPoint, Type.Coin);
    }

    private void gameOver() {

    }

    private Point getRandomPoint() {
        Point point = new Point((int) (width * Math.random()), (int) (height * Math.random()));
        for (Point p : snake.getBodyPoints()) {
            if (p.equals(point)) {
                return getRandomPoint();
            }
        }
        if (snake.getHeadPoint().equals(point)) {
            return getRandomPoint();
        }
        return point;
    }

    private void move() {
        snake.move(Direction.direction);
        area.setField(snake.getHeadPoint(), Type.Head);
        for (Point p : snake.getBodyPoints()) {
            area.setField(p, Type.Body);
        }
    }

    private void checkCoin() {
        if (snake.getHeadPoint().equals(coinPoint)) {
            snake.grow();
            score++;
            spawnCoin();
        }
    }

    private void draw() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
        System.out.println("Score: " + score);
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
        System.out.print("+");

        for (int i = 0; i < width * 2; i++) {
            System.out.print("-");
        }

        System.out.print("+ \n");
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