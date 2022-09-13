package com.company;

public class Area {
    private Field[][] field;

    public Area(int width, int height) {
        field = new Field[width][height];

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = new Field(State.Void);
            }
        }
    }

    public void setField(Point point, State state) {
        field[point.getX()][point.getY()].setState(state);
    }

    public Field[][] getField() {
        return field;
    }
}