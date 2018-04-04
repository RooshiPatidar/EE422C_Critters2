package assignment5;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Rooshi Patidar
 * rsp983
 * 15500
 * Spring 2018
 */



import sun.awt.image.ShortInterleavedRaster;

import java.util.ArrayList;
import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {

    public enum CritterShape {
        CIRCLE,
        SQUARE,
        TRIANGLE,
        DIAMOND,
        STAR
    }
    /* the default color is white, which I hope makes critters invisible by default
     * If you change the background color of your View component, then update the default
     * color to be the same as you background
     *
     * critters must override at least one of the following three methods, it is not
     * proper for critters to remain invisible in the view
     *
     * If a critter only overrides the outline color, then it will look like a non-filled
     * shape, at least, that's the intent. You can edit these default methods however you
     * need to, but please preserve that intent as you implement them.
     */

    public javafx.scene.paint.Color viewColor() {
        return javafx.scene.paint.Color.WHITE;
    }

    public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
    public javafx.scene.paint.Color viewFillColor() { return viewColor(); }

    public abstract CritterShape viewShape();



    private static String myPackage;
    private	static List<Critter> population = new ArrayList<Critter>();
    private static List<Critter> babies = new ArrayList<Critter>();
    private static int time = 0;
    /**
     * This is a flag that says if the critter has already moves during this time step
     */
    private boolean movedAlready = false;

    // Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    private static java.util.Random rand = new java.util.Random();

    /**
     * This method is the only Random function for the program and can be given a seed
     * @param max the exclusive upper bound of the function
     * @return an int between 0 and max-1 inclusive
     */
    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

    /**
     * This sets the seed for the Random
     * @param new_seed a long that is the seed of the Random
     */
    public static void setSeed(long new_seed) {
        rand = new java.util.Random(new_seed);
    }


    /* a one-character long string that visually depicts your critter in the ASCII interface */
    public String toString() { return ""; }

    private int energy = 0;
    protected int getEnergy() { return energy; }

    private int x_coord;
    private int y_coord;

    /**
     * A method to reset the moved flag either because the time step is new or in
     * the case of an Otter, it can be reset to move again
     */
    void resetMovedFlag() {
        movedAlready = false;
    }

    protected final String look(int direction, boolean steps) {
        String[][] popMap = new String[Params.world_width][Params.world_height];
        for (Critter c : population) {
            popMap[c.x_coord][c.y_coord] = c.toString();
        }
        int x = this.x_coord;
        int y = this.y_coord;
        if (direction == 3 || direction == 4 || direction == 5) {
            x_coord -= 1;
        }
        if (direction == 0 || direction == 1 || direction == 6) {
            x_coord += 1;
        }
        if (direction == 1 || direction == 2 || direction == 5) {
            y_coord -= 1;
        }
        if (direction == 5 || direction == 6 || direction == 7) {
            y_coord += 1;
        }

        if (x_coord == -1) {
            x_coord = Params.world_width - 1;
        }
        if (x_coord == Params.world_width) {
            x_coord = 0;
        }
        if (y_coord == -1) {
            y_coord = Params.world_height - 1;
        }
        if (y_coord == Params.world_height) {
            y_coord = 0;
        }

        this.energy -= Params.look_energy_cost;
        return popMap[x][y];
    }

    protected final void walk(int direction) {
        if (!movedAlready) {
            move(direction, 1);
        }
        energy -= Params.walk_energy_cost;
    }

    protected final void run(int direction) {
        if (!movedAlready) {
            move(direction, 2);
        }
        energy -= Params.run_energy_cost;
    }

    /**
     * This method is the backbone of the walk and run methods. It will move the creature one step at a time
     * for "speed" times and will make sure the creatures will wrap around the world
     * @param direction an int between 0 and 7 which is a cardinal or intercardinal direction
     * @param speed an int which is how far the creature will move
     */
    private void move(int direction, int speed) {
        for (int i = 0; i < speed; i++) {
            if (direction == 3 || direction == 4 || direction == 5) {
                x_coord -= 1;
            }
            if (direction == 0 || direction == 1 || direction == 6) {
                x_coord += 1;
            }
            if (direction == 1 || direction == 2 || direction == 5) {
                y_coord -= 1;
            }
            if (direction == 5 || direction == 6 || direction == 7) {
                y_coord += 1;
            }

            if (x_coord == -1) {
                x_coord = Params.world_width - 1;
            }
            if (x_coord == Params.world_width) {
                x_coord = 0;
            }
            if (y_coord == -1) {
                y_coord = Params.world_height - 1;
            }
            if (y_coord == Params.world_height) {
                y_coord = 0;
            }
        }
        movedAlready = true;
    }

    /**
     * This method, if called from fight, will attempt to run during the fight.
     * It will pick location to run to and if it's occupied it will not move.
     * This will cost Params.run_energy_cost if the move is successful or not
     */
    protected final void runFromFight() {
        int runDirection = Critter.getRandomInt(8);
        int oldX = x_coord;
        int oldY = y_coord;
        run(runDirection);
        boolean spotFilled = false;
        for (Critter critter : population) {
            if (critter != this && critter.x_coord == x_coord && critter.y_coord == y_coord) {
                spotFilled = true;
                break;
            }
        }
        if (spotFilled) {
            x_coord = oldX;
            y_coord = oldY;
        }

    }

    /**
     * This method takes in a previously created Critter subclass and will set it's location
     * and energy and add it to the babies list
     * @param offspring The critter that is added to the world
     * @param direction the direction directly next to the parent that it will be born to
     */
    protected final void reproduce(Critter offspring, int direction) {
        if (this.energy < Params.min_reproduce_energy) {
            return;
        }
        offspring.energy = this.energy/2;
        this.energy = (int)Math.ceil(energy/2.0);
        offspring.x_coord = x_coord;
        offspring.y_coord = y_coord;
        offspring.walk(direction);
        babies.add(offspring);
    }

    public abstract void doTimeStep();
    public abstract boolean fight(String opponent);

    /**
     * create and initialize a Critter subclass.
     * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
     * an InvalidCritterException must be thrown.
     * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
     * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
     * an Exception.)
     * @param critter_class_name
     * @throws InvalidCritterException
     */
    public static void makeCritter(String critter_class_name) throws InvalidCritterException {
        try {
            Class c = Class.forName(myPackage + "." + critter_class_name);
            Critter critter = (Critter)c.newInstance();
            critter.x_coord = Critter.getRandomInt(Params.world_width);
            critter.y_coord = Critter.getRandomInt(Params.world_height);
            critter.energy = Params.start_energy;
            population.add(critter);
        } catch (Exception e) {throw new InvalidCritterException(critter_class_name);}
        catch (NoClassDefFoundError e) {
            throw new InvalidCritterException(critter_class_name);
        }
    }

    /**
     * Gets a list of critters of a specific type.
     * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
        List<Critter> result = new java.util.ArrayList<Critter>();
        try {
            Class c = Class.forName("assignment5." + critter_class_name);
            for (Critter critter : population) {
                if (c.isInstance(critter)) {
                    result.add(critter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Prints out how many Critters of each type there are on the board.
     * @param critters List of Critters.
     */
    public static String runStats(List<Critter> critters) {
        StringBuilder result = new StringBuilder();
        result.append("").append(critters.size()).append(" critters as follows -- ");
        java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
        for (Critter crit : critters) {
            String crit_string = crit.toString();
            Integer old_count = critter_count.get(crit_string);
            if (old_count == null) {
                critter_count.put(crit_string,  1);
            } else {
                critter_count.put(crit_string, old_count.intValue() + 1);
            }
        }
        String prefix = "";
        for (String s : critter_count.keySet()) {
            result.append(prefix).append(s).append(":").append(critter_count.get(s));
            prefix = ", ";
        }
        return result.toString();
    }

    /* the TestCritter class allows some critters to "cheat". If you want to
     * create tests of your Critter model, you can create subclasses of this class
     * and then use the setter functions contained here.
     *
     * NOTE: you must make sure that the setter functions work with your implementation
     * of Critter. That means, if you're recording the positions of your critters
     * using some sort of external grid or some other data structure in addition
     * to the x_coord and y_coord functions, then you MUST update these setter functions
     * so that they correctly update your grid/data structure.
     */
    static abstract class TestCritter extends Critter {
        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;
        }

        protected void setX_coord(int new_x_coord) {
            super.x_coord = new_x_coord;
        }

        protected void setY_coord(int new_y_coord) {
            super.y_coord = new_y_coord;
        }

        protected int getX_coord() {
            return super.x_coord;
        }

        protected int getY_coord() {
            return super.y_coord;
        }


        /*
         * This method getPopulation has to be modified by you if you are not using the population
         * ArrayList that has been provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.
         */
        protected static List<Critter> getPopulation() {
            return population;
        }

        /*
         * This method getBabies has to be modified by you if you are not using the babies
         * ArrayList that has been provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.  Babies should be added to the general population
         * at either the beginning OR the end of every timestep.
         */
        protected static List<Critter> getBabies() {
            return babies;
        }
    }

    /**
     * Clear the world of all critters, dead and alive
     */
    public static void clearWorld() {
        population.clear();
        babies.clear();
    }

    /**
     * This will simulate one time unit of the Critters world. It will call doTimeStep()
     * for each of the Critters in the population, have them fight if needed, change their energies,
     * generate new Algae, and add the babies created to the general population
     */
    public static void worldTimeStep() {
        // 1. increment timestep; timestep++;
        // 2. doTimeSteps();
        // 3. Do the fights. doEncounters();
        // 4. updateRestEnergy();
        // 5. Generate Algae genAlgae();
        // 6. Move babies to general population. population.addAll(babies); babies.clear();

        //1. timekeeping
        time++;

        //2. critter time steps
        doCritterTimeSteps();

        //3. fighting
        doFights();

        //4. rest energy
        takeRestEnergyFromAll();

        //5. add algae
        genAlgae();

        //6.. add babies
        addBabies();
    }

    /**
     * This method calls all of the Critters individual timesteps.
     * These steps can include moving reproducing or photosynthesizing
     */
    private static void doCritterTimeSteps() {
        for (int i = population.size() - 1; i >= 0; i--) {
            Critter critter = population.get(i);
            critter.resetMovedFlag();
            critter.doTimeStep();
            if (critter.getEnergy() <= 0) {
                population.remove(critter);
            }
        }
    }

    /**
     * This simulates the fight/encounters between all of the critters if any of them are at the same location.
     * The critters can run away or can "roll" to decide which one wins.
     * The loser will die and be removed and the winner will get half of the loser's health
     */
    private static void doFights() {

        //Will check all of the possible critter combinations
        //The inner loop is not "optimized" in case there is a fluke when critters on the list move around because the losers are removed right away
        for (int i = population.size() - 1; i >= 0; i--) {
            Critter critter1 = population.get(i);
            for (int j = population.size() - 1; j >= 0; j--) {
                Critter critter2 = population.get(j);


                //The critters will encounter if they are at the same location and there are 2 distinct critters
                if (critter1.x_coord == critter2.x_coord && critter1.y_coord == critter2.y_coord && !critter1.equals(critter2)) {

                    //each critter will decide to fight the other critter or not
                    boolean AfightsB = critter1.fight(critter2.toString());
                    boolean BfightsA = critter2.fight(critter1.toString());

                    //If either of the critters tried to run and died then they will be removed
                    if (critter1.getEnergy() <= 0) {
                        population.remove(critter1);
                    }
                    if (critter2.getEnergy() <= 0) {
                        population.remove(critter2);
                        i--;
                    }

                    //Since the critter run or die when running, their existence in the world is checked and if they are in the same location
                    if (population.contains(critter1) && population.contains(critter2) && critter1.x_coord == critter2.x_coord && critter1.y_coord == critter2.y_coord) {

                        //the critter roll defaults to 0 if they don't want to fight
                        int critter1Roll = 0;
                        int critter2Roll = 0;

                        //If they do want to fight then a number between 0 ad the critter's energy is picked
                        if (AfightsB) {
                            critter1Roll = Critter.getRandomInt(critter1.energy + 1);
                        }
                        if (BfightsA) {
                            critter2Roll = Critter.getRandomInt(critter2.energy + 1);
                        }

                        //This critter is now referenced as winner and loser instead of critter1 and critter2
                        //so there isn't duplicate code
                        Critter winner;
                        Critter loser;
                        if (critter1Roll > critter2Roll) {
                            winner = critter1;
                            loser = critter2;
                        } else if (critter2Roll > critter1Roll) {
                            winner = critter2;
                            loser = critter1;
                        } else {
                            //If both score the same then the winner is chosen at random
                            int randWinner = Critter.getRandomInt(2);
                            if (randWinner == 0) {
                                winner = critter1;
                                loser = critter2;
                            } else {
                                winner = critter2;
                                loser = critter1;
                            }
                        }

                        winner.energy += (loser.energy / 2);
                        population.remove(loser);

                        //If critter1 is the loser that means the outer loop index is no longer correct and since the inner loop is
                        //somewhere in the middle, the next critter is chosen and the inner loop is reset
                        if (loser == critter1) {
                            if (population.size() > 0) {
                                critter1 = population.get(--i);
                            }
                            j = population.size();
                        }
                        //if critter2 loses then the array is moved forward one so if i is not adjusted it will loop over
                        // the same critter twice
                        if (loser == critter2) {
                            i--;
                        }


                    }
                }
            }
        }
    }

    /**
     * This method takes the rest energy from all of the critters which is the energy needed to survive
     */
    private static void takeRestEnergyFromAll() {
        for (int i = population.size() - 1; i >= 0; i--) {
            Critter critter = population.get(i);
            critter.energy -= Params.rest_energy_cost;
            if (critter.energy <= 0) {
                population.remove(critter);
            }
        }
    }

    /**
     * This method generates Algae which is added every worldTimeStep
     */
    private static void genAlgae() {
        for (int i = 0; i < Params.refresh_algae_count; i++) {
            try {
                makeCritter("Algae");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    /**
     * This adds the babies to the general population at the end of every World Time Step
     */
    private static void addBabies() {
        population.addAll(babies);
        babies.clear();
    }

    public static List<GridCritter> displayWorld() {
        List<GridCritter> critters = new ArrayList<>();
        for (Critter c : population) {
            critters.add(new GridCritter(c, c.x_coord, c.y_coord));
        }
        return critters;
    }
	/* Alternate displayWorld, where you use Main.<pane> to reach into your
	   display component.


    /**
     * This method prints out a ASCII representation of the world
     */
    /*public static void displayWorld() {
        String[][] worldArr = new String[Params.world_height][Params.world_width];
        for (Critter critter : population) {
            worldArr[critter.y_coord][critter.x_coord] = critter.toString();
        }
        printTopBottomBorder();
        for (int row = 0; row < Params.world_height; row++) {
            System.out.print("|");
            for (int col = 0; col < Params.world_width; col++) {
                String rep = worldArr[row][col];
                if (rep == null) {
                    System.out.print(" ");
                } else {
                    System.out.print(rep);
                }
            }
            System.out.println("|");
        }
        printTopBottomBorder();
    }
    */

    /**
     * This method prints out the top and bottom bars used in displayWorld()
     */
    /*
    private static void printTopBottomBorder() {

        System.out.print("+");
        for (int i = 0; i < Params.world_width; i++) {
            System.out.print("-");
        }
        System.out.println("+");
    }
    */
}
