package assignment5;

/* CRITTERS Longhorn.java
 * EE422C Project 4 submission by
 * Rooshi Patidar
 * rsp983
 * 15500
 * Spring 2018
 */

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
