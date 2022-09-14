package com.company;

import java.util.ArrayList;

public class Snake {
    private ArrayList<Point> body;
    private Point head;
    private Area area;
    private Game game;

    public Snake(Point head, Area area, Game game) {
        body = new ArrayList<>();
        this.head = head;
        this.area = area;
        this.game = game;
    }

    public void addBodyPoint(Point point) {
        body.add(point);
    }

    public void removeLastBodyPoint() {
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
        switch (direction) {
            case Up -> {
                area.setField(this.getHeadPoint(), Type.Void);
                if (this.head.getX() - 1 >= 0) {
                    this.head.setX(this.head.getX() - 1);
                }
            }
            case Left -> {
                area.setField(this.getHeadPoint(), Type.Void);
                if (this.head.getY() - 1 >= 0) {
                    this.head.setY(this.head.getY() - 1);
                }
            }
            case Down -> {
                area.setField(this.getHeadPoint(), Type.Void);
                if (this.head.getX() - 1 <= game.height) {
                    this.head.setX(this.head.getX() + 1);
                }
            }
            case Right -> {
                area.setField(this.getHeadPoint(), Type.Void);
                if (this.head.getY() - 1 <= game.width) {
                    this.head.setY(this.head.getY() + 1);
                }
            }
        }
    }

    public void grow() {

    }
}