package assignment5;

/*
 * Do not change or submit this file.
 */
import assignment5.Critter.TestCritter;
import javafx.scene.paint.Color;

public class Algae extends TestCritter {

	public String toString() { return "@"; }
	
	public boolean fight(String not_used) { return false; }
	
	public void doTimeStep() {
		setEnergy(getEnergy() + Params.photosynthesis_energy_amount);
	}

	@Override
	public CritterShape viewShape() { return CritterShape.CIRCLE; }

	@Override
	public Color viewOutlineColor() { return Color.FORESTGREEN; }

	@Override
	public Color viewFillColor() {return Color.GREEN;}
}
