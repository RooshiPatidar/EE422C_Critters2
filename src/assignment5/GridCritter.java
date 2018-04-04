package assignment5;

public class GridCritter {
    private Critter critter;
    private int x;
    private int y;

    public GridCritter(Critter c, int x, int y) {
        critter = c;
        this.x = x;
        this.y = y;
    }

    public Critter getCritter() {
        return critter;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
