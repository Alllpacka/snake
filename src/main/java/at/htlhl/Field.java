package at.htlhl;

public class Field {
    private Type state;

    public Field(Type type) {
        this.state = type;
    }

    public void setState(Type type) {
        this.state = type;
    }

    public Type getState() {
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
