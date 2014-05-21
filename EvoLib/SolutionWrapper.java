/**
 * Solution interface for GeneticaAlgorithm
 *
 */

package EvoLib;

public class SolutionWrapper implements Comparable {

	public Solution solution;
	public int fitness;

	public SolutionWrapper(Solution solution) {
		this.solution = solution;
		fitness = Integer.MIN_VALUE;
	}


	public void evaluate() {
		if (!evaluated()) {
			fitness = solution.fitness();
		}
	}

	public boolean evaluated() {
		return !(fitness == Integer.MIN_VALUE);
	}

	public int compareTo(Object s) throws RuntimeException {
		if (!(s instanceof SolutionWrapper)) {
			throw new RuntimeException("cannot compare to non-SolutionWrapper");
		}
		evaluate();
		if (fitness < ((SolutionWrapper)s).fitness()) {
			return -1;
		} else if (fitness == ((SolutionWrapper)s).fitness()) {
			return 0;
		} else {
			return 1;
		}
	}

	public int fitness() {
		evaluate();
		return fitness;
	}

	public SolutionWrapper clone() {
		return new SolutionWrapper(solution.clone());
	}

	public SolutionWrapper mutate() {
		return new SolutionWrapper(solution.mutate());
	}

	public Solution solution() {
		return solution;
	}
}
