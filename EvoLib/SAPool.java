/**
 * Simulated annealing algorithm
 */


package EvoLib;
import java.util.*;

public class SAPool<S extends Solution> implements SolutionPool<S> {
	SolutionWrapper bestSolution, currentSolution;
	long generation;
	static double ACCEPT_COEFFICIENT = 5;

	public SAPool(S origin) {
		currentSolution = new SolutionWrapper(origin);
		bestSolution = currentSolution.clone();
		generation = 0;
	}


	public void evolve() {
		SolutionWrapper newSolution = currentSolution.mutate();
		if (newSolution.compareTo(bestSolution) > 0) {
			bestSolution = newSolution.clone();
		}
		if (accept(newSolution.fitness() - currentSolution.fitness())) {
			currentSolution = newSolution;
		}
		generation++;
	}

	public void evolve(int generations) {
		for (int i = 0; i < generations; i++) {
			evolve();
		}
	}


	private boolean accept(int delta) {
		if (delta > 0) {
			return true;
		}
		return trial(Math.exp(((double)delta * Math.log((double)generation + Math.E) * ACCEPT_COEFFICIENT)));
	}


	private boolean trial(double probability) {
		return Math.random() <= probability;
	}

	public int averageFitness() {
		return bestSolution.fitness;
	}

	@SuppressWarnings("unchecked")
	public S getBest() {
		return (S)(bestSolution.clone().solution());
	}

	@SuppressWarnings("unchecked")
	public Iterable<S> getSolutions() {
		ArrayList<S> solutions = new ArrayList<S>();
		solutions.add((S)(bestSolution.clone().solution()));
		return solutions;
	}

}