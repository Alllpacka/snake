package at.htlhl;

public class Area {
    private Field[][] field;

    public Area(int width, int height) {
        field = new Field[height][width];

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = new Field(Type.Void);
            }
        }
    }

    public void setField(Point point, Type type) {
        field[point.getY()][point.getX()].setState(type);
    }

    public Field[][] getField() {
        return field;
    }
}