package com.company;

public class Field {
    private State state;

    public Field(State state) {
        this.state = state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public char translateState() {
        switch (state) {
            case Void -> {
                return ' ';
            }
            case Head -> {
                return '#';
            }
            case Body -> {
                return '*';
            }
            case Coin -> {
                return '$';
            }
        }

        return ' ';
    }
}
