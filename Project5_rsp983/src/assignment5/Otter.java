package assignment5;
/* CRITTERS Otter.java
 * EE422C Project 4 submission by
 * Rooshi Patidar
 * rsp983
 * 15500
 * Spring 2018
 */

import javafx.scene.paint.Color;

/*
 * OTTER Critter
 * This critter is a representation of a Otter as in it runs away from
 * most fights because it is so small. It also moves twice as much as other creatures
 * so it runs twice in one turn.
 */
public class Otter extends Critter {

	@Override
	public String toString() { return "2"; }

	private int distanceTraveled;


	public Otter() {
		distanceTraveled = 0;
	}

	/**
	 * Custom fight method for the Otter and how it decides when to fight. It will run from everything
	 * that is not Algae
	 * @param critter The critter the Otter is fighting against
	 * @return a boolean saying if it wants to fight or not
	 */
	public boolean fight(String critter) {
		if ("@".equals(critter)) {
			return true;
		} else {
			runFromFight();
			return false;
		}
	}

	@Override
	/**
	 * The timeStep for the Otter is running twice in a similar direction and then reproducing if
	 * it has enough energy
	 */
	public void doTimeStep() {

		//swim around real quick
		//the second move is in a similar direction as the first
		int firstMove = Critter.getRandomInt(8);
		run(firstMove);
		resetMovedFlag();
		int nextMove = firstMove - 3 + Critter.getRandomInt(3);
		if (nextMove < 0) {
			run(0);
		} else {
			run(nextMove);
		}

		if (getEnergy() > Params.min_reproduce_energy) {
			Otter child = new Otter();
			reproduce(child, Critter.getRandomInt(8));
		}

	}


	public static String runStats(java.util.List<Critter> otters) {
		String result = "";
		int totalDistance = 0;
		int farthestOtter = 0;
		try {
			for (Critter critter : otters) {
				if (critter instanceof Otter) {
					int traveled = ((Otter) critter).distanceTraveled;

					totalDistance += traveled;
					if (traveled > farthestOtter) {
						farthestOtter = traveled;
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		double widthTraveled = (double)totalDistance/Params.world_width;
		double heightTraveled = (double)totalDistance/Params.world_height;

		result += "" + otters.size() + " total otters    ";
		result += "Collectively, all living Otters have traveled " + totalDistance + " steps" + '\n';
		result += "This is equivalent to " + widthTraveled + " times across the world width or " + heightTraveled + " times across the world height" + '\n';
		result += "The farthest any single Otter has gotten is " + farthestOtter + " steps" + '\n';

		return result;
	}

    @Override
    public CritterShape viewShape() { return CritterShape.STAR; }

    @Override
    public Color viewOutlineColor() { return Color.BROWN; }

    @Override
    public Color viewFillColor() {return Color.CHOCOLATE;}
}
