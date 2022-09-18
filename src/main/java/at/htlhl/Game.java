package at.htlhl;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Game implements Runnable {
    public final int width;
    public final int height;
    public int score;

    // ID, Username, Password, Score
    public String[][] users;
    private Point coinPoint;
    public Area area;
    public Snake snake;
    public boolean gameOver;

    private Thread thread;

    private int timeBetweenTicks = 400;

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
        area = new Area(width, height);
        spawnPlayer();
        spawnCoin();
        thread = new Thread(this);
        thread.start();
        Input.startInputListener();
        this.gameOver = false;
    }

    public void stopGame(){
        thread.interrupt();
    }

    public void spawnPlayer() {
        snake = new Snake(new Point(width / 2, height / 2));
        area.setField(snake.getHeadPoint(), Type.Head);
    }

    private void tick() {
        move();
        checkCoin();
        draw();
    }

    private void spawnCoin() {
        coinPoint = getRandomPoint();
        area.setField(coinPoint, Type.Coin);
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
            try {
                throw new RuntimeException(e);
            } catch (Exception ex) {

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
        System.out.println("Score: " + score);
        drawLine(13);
        int i = 0;
        for (Field[] row : area.getField()) {
            System.out.print("|");
            for (Field field : row) {
                System.out.print(field.translateState());
                System.out.print(" ");
            }
            System.out.print("| ");
            String leftAlignFormat = "%-3s | %-12s | %-3s |";

            if (i < users.length) {

                System.out.format(leftAlignFormat, users[i][0], users[i][1], users[i][3]);

            } else {
                System.out.format(leftAlignFormat, "", "", "");
            }
            System.out.println();
            i++;
        }
        drawLine(13);
    }

    private void drawLine(int length){
        System.out.print("+");
        for (int i = 0; i < width * 2; i++) {
            System.out.print("-");
        }
        System.out.print("+");

        for (int i = 0; i < length * 2; i++) {
            System.out.print("-");
        }
        System.out.print("+ \n");
    }

    public void connect() {
        int userCount = 0;
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://branmark.ddns.net:3306/snake", "snake", "python");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from users");
            while (rs.next()) {
                userCount++;
            }
            users = new String[userCount][4];
            rs = stmt.executeQuery("select * from users");
            int currentUser = 0;
            while (rs.next()) {
                users[currentUser][0] = String.valueOf(rs.getInt(1));
                users[currentUser][1] = rs.getString(2);
                users[currentUser][2] = rs.getString(3);
                users[currentUser][3] = rs.getString(4);
                currentUser++;
            }
            con.close();
        } catch (Exception e) {
            Main.menu.printLogo();
            System.out.println();
            users = new String[0][0];
            System.out.println("Verbindung zum Server konnte nicht aufgebaut werden. ");
            var scan = new java.util.Scanner(System.in);
            char input = ' ';
            do {
                System.out.println("Im Offline-Modus starten? (y/n)");
                input = scan.nextLine().charAt(0);
                if (input == 'n') {
                    System.exit(0);
                }
            } while (input != 'y');
        }
    }

    @Override
    public void run() {
        while (!gameOver) {
            try {
                tick();
                Thread.sleep(timeBetweenTicks);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}