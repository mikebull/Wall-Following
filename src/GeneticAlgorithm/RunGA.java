/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GeneticAlgorithm;

import Maze.Maze;
import Robot.Robot;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Mike
 */
public class RunGA {

    // Temporary List to separate best fitness values
    protected static ArrayList<Double> tmp = new ArrayList<Double>();

    // Implementation of Elitism
    protected static int ELITISM = 5;
    // Population size
    protected static int POPULATION_SIZE = 500 + ELITISM;
    // Max number of Iterations
    protected static int MAX_ITERATIONS = 200;
    // Probability of Mutation
    protected static double MUTATION_PROB = 0.05;
    // Probability of Crossover
    protected static double CROSSOVER_PROB = 0.7;

    // Instantiate Random object
    private static Random rand = new Random();
    // Instantiate Population of Individuals
    private Individual[] startPopulation;
    // Total Fitness of Population
    private double totalFitness;

    Robot robot = new Robot();
    //Maze maze;

    /**
     * Setter for Elitism
     * @param result
     */
    public void setElitism(int result) {
        ELITISM = result;
    }

    /**
     * Setter for Population Size
     * @param result
     */
    public void setPopSize(int result) {
        POPULATION_SIZE = result + ELITISM;
    }

    /**
     * Setter for Maximum Iterations
     * @param result
     */
    public void setMaxIt(int result) {
        MAX_ITERATIONS = result;
    }

    /**
     * Setter for Mutation Probability
     * @param result
     */
    public void setMutProb(double result) {
        MUTATION_PROB = result;
    }

    /**
     * Setter for Crossover Probability
     * @param result
     */
    public void setCrossoverProb(double result) {
        CROSSOVER_PROB = result;
    }

    /**
     * Constructor for Population
     */
    public RunGA() {
        // Create a population of population plus elitism
        startPopulation = new Individual[POPULATION_SIZE];

        // For every individual in population fill with x genes from 0 to 1
        for (int i = 0; i < POPULATION_SIZE; i++) {
            startPopulation[i] = new Individual();
            startPopulation[i].randGenes();
        }

        // Evaluate the current population's fitness
        this.evaluate();
    }

    /**
     * Set Population
     * @param newPop
     */
    public void setPopulation(Individual[] newPop) {
        System.arraycopy(newPop, 0, this.startPopulation, 0, POPULATION_SIZE);
    }

    /**
     * Get Population
     * @return
     */
    public Individual[] getPopulation() {
        return this.startPopulation;
    }

    /**
     * Evaluate fitness
     * @return
     */
    public double evaluate() {
        this.totalFitness = 0.0;
        ArrayList<Double> fitnesses = new ArrayList<Double>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            // Instantiate new maze each time
            Maze maze = new Maze(8, 8);
            maze.fillMaze();
            fitnesses.add(startPopulation[i].evaluate(maze));
        }

        StringBuilder sb = new StringBuilder();

        for(Double tmp : fitnesses) {
            sb.append(tmp + ", ");
            totalFitness += tmp;
        }

        return this.totalFitness;
    }

    /**
     * Roulette Wheel Selection
     * @return
     */
    public Individual rouletteWheelSelection() {
        // Get random number between total fitness and 0
        double randNum = rand.nextDouble() * this.getTotalFitness();
        int i;
        for (i = 0; i < POPULATION_SIZE && randNum > 0; ++i) {
            randNum -= startPopulation[i].getFitness();
        }
        return startPopulation[i-1];
    }

    /**
     * Tournament Selection
     * @return
     */
    public Individual tournamentSelection() {
        double randNum = rand.nextDouble() * this.totalFitness;
        // Get random number of population (add 1 to stop NullPointerException)
        int k = rand.nextInt(POPULATION_SIZE) + 1;
        int i;
        for (i = 1; i < POPULATION_SIZE && i < k && randNum > 0; ++i) {
            randNum -= startPopulation[i].getFitness();
        }
        return startPopulation[i-1];
    }

    /**
     * Finds the best individual
     * @return
     */
    public Individual findBestIndividual() {
        int maxIndex = 0;
        double max = 0.0;
        double min = 1.0;
        double tmp;

        for (int i = 0; i < POPULATION_SIZE; ++i) {
            tmp = startPopulation[i].getFitness();
            if (max < min) {
                max = min = tmp;
                maxIndex = i;
            }
            if (tmp > max) {
                max = tmp;
                maxIndex = i;
            }
        }

        // Maximisation
        return startPopulation[maxIndex];
    }

    /**
     * One Point Crossover
     * @param firstPerson
     * @param secondPerson
     * @return
     */
    public static Individual[] onePointCrossover(Individual firstPerson, Individual secondPerson) {
        Individual[] newPerson = new Individual[2];
        newPerson[0] = new Individual();
        newPerson[1] = new Individual();

        int size = Individual.SIZE;

        int randPoint = rand.nextInt(size);
        int i;
        for (i = 0; i < randPoint; ++i) {
            newPerson[0].setGene(i, firstPerson.getGene(i));
            newPerson[1].setGene(i, secondPerson.getGene(i));
        }
        for (; i < Individual.SIZE; ++i) {
            newPerson[0].setGene(i, secondPerson.getGene(i));
            newPerson[1].setGene(i, firstPerson.getGene(i));
        }

        return newPerson;
    }

    /**
     * Uniform Crossover
     * @param firstPerson
     * @param secondPerson
     * @return
     */
    public static Individual[] uniformCrossover(Individual firstPerson, Individual secondPerson) {
        Individual[] newPerson = new Individual[2];
        newPerson[0] = new Individual();
        newPerson[1] = new Individual();

        for(int i = 0; i < Individual.SIZE; ++i) {
            double r = rand.nextDouble();
            if (r > 0.5) {
                newPerson[0].setGene(i, firstPerson.getGene(i));
                newPerson[1].setGene(i, secondPerson.getGene(i));
            } else {
                newPerson[0].setGene(i, secondPerson.getGene(i));
                newPerson[1].setGene(i, firstPerson.getGene(i));
            }
        }

        return newPerson;
    }

    /**
     * Getter for Total Fitness
     * @return
     */
    public double getTotalFitness() {
        return totalFitness;
    }

    public static void main(String[] args) {
        // Instantiate Population
        RunGA pop = new RunGA();
        // Instantiate Individuals for Population
        Individual[] newPop = new Individual[POPULATION_SIZE];
        // Instantiate two individuals to use for selection
        Individual[] people = new Individual[2];

        // Print Current Population
        /*System.out.println("Total Fitness: " +
                pop.getTotalFitness() + " - Best Fitness: " +
                pop.findBestIndividual().getFitness());*/

        // Instantiate counter for selection
        int count;
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            count = 0;
            // Get best individual from population and add to new population
            for (int j = 0; j < ELITISM; ++j) {
                newPop[count] = pop.findBestIndividual();
                count++;
            }
            // Build New Population
            while (count < POPULATION_SIZE) {
                // Roulette Wheel Selection
                //people[0] = pop.rouletteWheelSelection();
                //people[1] = pop.rouletteWheelSelection();
                // Tournament Selection
                people[0] = pop.tournamentSelection();
                people[1] = pop.tournamentSelection();
                // Crossover
                if (rand.nextDouble() < CROSSOVER_PROB) {
                    // One Point Crossover
                    people = onePointCrossover(people[0], people[1]);
                    // Uniform Crossover
                    //people = uniformCrossover(people[0], people[1]);
                }
                // Mutation
                if (rand.nextDouble() < MUTATION_PROB) {
                    people[0].mutate();
                }
                if (rand.nextDouble() < MUTATION_PROB) {
                    people[1].mutate();
                }
                // Add to New Population
                newPop[count] = people[0];
                newPop[count+1] = people[1];
                count += 2;
            }
            // Make new population the current population
            pop.setPopulation(newPop);

            // Re-evaluate the current population
            pop.evaluate();

            tmp.add(pop.findBestIndividual().getFitness());
            // Print results to screen
            System.out.println(pop.getTotalFitness());
            //System.out.println("Total Fitness: " + pop.totalFitness + " - Best Fitness: " + pop.findBestIndividual().getFitness());
            //result += "\nTotal Fitness: " + pop.totalFitness + " - Best Fitness: " + pop.findBestIndividual().getFitnessValue();
        }
        
        StringBuilder sb = new StringBuilder();

        for(Double d : tmp) {
            sb.append(d + "\n");
        }

        System.out.println(sb.toString());
    }

}
