package assignment5;
/* CRITTERS Longhorn.java
 * EE422C Project 4 submission by
 * Rooshi Patidar
 * rsp983
 * 15500
 * Spring 2018
 */

import javafx.scene.paint.Color;
import sun.font.CreatedFontTracker;

import java.util.List;
import java.util.Set;

/*
 * LONGHORN Critter
 * This critter is a representation of a Longhorn as in it
 * has a low chance of moving because its eating grass and other plants.
 * However, it will always accept a fight.
 */
public class Longhorn extends Critter {

	private int distanceTraveled;
	private int chancesToMove;
	private int childrenHad;

	@Override
	public String toString() { return "1"; }

	public Longhorn() {
		distanceTraveled = 0;
		chancesToMove = 0;
		childrenHad = 0;
	}

	/**
	 * This is how the Longhorn decides to fight
	 * @param critter The critter its fighting
	 * @return true; it will always fight
	 */
	public boolean fight(String critter) {
		return true;
	}

	@Override
	/**
	 * The Longhorn grazes slowly so it has small chance of walking each turn and a lower chance or reproducing but they'll have 3 calves
	 */
	public void doTimeStep() {

		//slow grazing
		chancesToMove++;
		boolean moved = false;
		int moveRandom = Critter.getRandomInt(8);
		if (look(moveRandom, false) != null) {
			walk(moveRandom);
			moved = true;
		}
		if (!moved) {
			moveRandom = Critter.getRandomInt(8);
			if (look(moveRandom, false) != null) {
				walk(moveRandom);
			}
		}
		if (getEnergy() > Params.min_reproduce_energy && Critter.getRandomInt(3) == 0) {
			Longhorn child = new Longhorn();
			Longhorn child2 = new Longhorn();
			Longhorn child3 = new Longhorn();
			reproduce(child, Critter.getRandomInt(8));
			reproduce(child2, Critter.getRandomInt(8));
			reproduce(child3, Critter.getRandomInt(8));
			childrenHad += 3;
		}

	}

	public static String runStats(List<Critter> longhorns) {
		String result = "";
		int totalDistance = 0;
		int totalChancesToMove = 0;
		int totalChildrenHad = 0;
		int totalParents = 0;
		try {
			for (Critter critter : longhorns) {
				if (critter instanceof Longhorn) {
					int traveled = ((Longhorn) critter).distanceTraveled;
					int chance = ((Longhorn) critter).chancesToMove;
					int children = ((Longhorn) critter).childrenHad;
					totalDistance += traveled;
					totalChancesToMove += chance;
					totalChildrenHad += children;
					if (children != 0) {
						totalParents++;
					}
				}
			}
		} catch (Exception e) {
			result += e;
		}

		int nothing = totalChancesToMove-totalDistance;

		result += "Time spent chillin': " + nothing + "/" + totalChancesToMove + '\n';
		result += "The Longhorns are eating grass " + ((double)nothing/totalChancesToMove)*100 + "% of the time" + '\n';
		result += "For all of the Longhorns that have had at least 1 child, they have, on average, " + ((double)totalChildrenHad/totalParents) + " children" + '\n';
		return result;
	}

    @Override
    public CritterShape viewShape() { return CritterShape.DIAMOND; }

    @Override
    public Color viewOutlineColor() { return Color.ORANGE; }

    @Override
    public Color viewFillColor() {return Color.ORANGERED;}
}
