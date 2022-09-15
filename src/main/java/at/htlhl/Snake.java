package at.htlhl;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class Snake {
    private LinkedList<Point> body;
    private Point head;

    private Point lastHeadPoint;

    private boolean grow;

    public Snake(Point head) {
        body = new LinkedList<>();
        this.head = head;
    }

    public void addBodyPoint() {
        body.add(new Point(head.getX(), head.getY()));
    }

    public void removeLastBodyPoint() {
        Main.game.area.setField(body.get(0), Type.Void);
        body.remove(0);
    }

    public Point[] getBodyPoints() {
        Point[] array = new Point[body.size()];
        array = body.toArray(array);

        return array;
    }

    public Point getHeadPoint() {
        return head;
    }

    public void move(Direction direction) {
        if (body.size() > 0 && !grow) {
            addBodyPoint();
            removeLastBodyPoint();
        }

        switch (direction) {
            case Up -> {
                Main.game.area.setField(this.getHeadPoint(), Type.Void);
                if (this.head.getY() - 1 >= 0) {
                    this.head.setY(this.head.getY() - 1);
                } else {
                    gameOver();
                }
            }
            case Left -> {
                Main.game.area.setField(this.getHeadPoint(), Type.Void);
                if (this.head.getX() - 1 >= 0) {
                    this.head.setX(this.head.getX() - 1);
                } else {
                    gameOver();
                }
            }
            case Down -> {
                Main.game.area.setField(this.getHeadPoint(), Type.Void);
                if (this.head.getY() + 1 <= Main.game.height-1) {
                    this.head.setY(this.head.getY() + 1);
                } else {
                    gameOver();
                }
            }
            case Right -> {
                Main.game.area.setField(this.getHeadPoint(), Type.Void);
                if (this.head.getX() + 1 <= Main.game.width-1) {
                    this.head.setX(this.head.getX() + 1);
                } else {
                    gameOver();
                }
            }
        }

        lastHeadPoint = new Point(head.getX(), head.getY());
        grow = false;
    }

    public void grow() {
        grow = true;
        body.add(lastHeadPoint);
    }

    private void gameOver(){
        Main.game.gameOver = true;
        System.out.println("   ____                         ___                 _ \n" +
                "  / ___| __ _ _ __ ___   ___   / _ \\__   _____ _ __| |\n" +
                " | |  _ / _` | '_ ` _ \\ / _ \\ | | | \\ \\ / / _ \\ '__| |\n" +
                " | |_| | (_| | | | | | |  __/ | |_| |\\ V /  __/ |  |_|\n" +
                "  \\____|\\__,_|_| |_| |_|\\___|  \\___/  \\_/ \\___|_|  (_)\n" +
                "                                                      ");
        var scan = new java.util.Scanner(System.in);
        char input = ' ';
        do {
            try {
                Robot robot = new Robot();
                for (int i = 0; i < Input.keyCount; i++) {
                    robot.keyPress(KeyEvent.VK_BACK_SPACE);
                }
            } catch (AWTException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Drücke 0 um zu beenden");
            System.out.println("Drücke 1 um neuzustarten");
            input = scan.nextLine().charAt(0);
            if (input == '0') {
                System.exit(0);
            } else if (input == '1') {
                Direction.direction = Direction.Right;
                Main.game.gameOver = false;
                Main.game.startGame();
            }
        } while (input != '1');
    }
}