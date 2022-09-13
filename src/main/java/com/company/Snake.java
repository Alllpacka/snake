package com.company;

import java.util.ArrayList;

public class Snake {
    private ArrayList<Point> body;
    private Point head;

    public Snake(Point head) {
        body = new ArrayList<>();
        this.head = head;
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
}