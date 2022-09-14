package com.company;

import java.util.ArrayList;
import java.util.LinkedList;

public class Snake {
    private LinkedList<Point> body;
    private Point head;
    private Area area;
    private Game game;

    private Point lastHeadPoint;

    private boolean grow;

    public Snake(Point head, Area area, Game game) {
        body = new LinkedList<>();
        this.head = head;
        this.area = area;
        this.game = game;
    }

    public void addBodyPoint() {
        body.add(new Point(head.getX(), head.getY()));
    }

    public void removeLastBodyPoint() {
        area.setField(body.get(0), Type.Void);
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
                area.setField(this.getHeadPoint(), Type.Void);
                if (this.head.getY() - 1 >= 0) {
                    this.head.setY(this.head.getY() - 1);
                }
            }
            case Left -> {
                area.setField(this.getHeadPoint(), Type.Void);
                if (this.head.getX() - 1 >= 0) {
                    this.head.setX(this.head.getX() - 1);
                }
            }
            case Down -> {
                area.setField(this.getHeadPoint(), Type.Void);
                if (this.head.getY() - 1 <= game.height) {
                    this.head.setY(this.head.getY() + 1);
                }
            }
            case Right -> {
                area.setField(this.getHeadPoint(), Type.Void);
                if (this.head.getX() - 1 <= game.width) {
                    this.head.setX(this.head.getX() + 1);
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
}