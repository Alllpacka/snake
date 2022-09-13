package com.company;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = Math.min(x, Main.game.width - 1);
    }

    public void setY(int y) {
        this.y = Math.min(y, Main.game.height - 1);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}