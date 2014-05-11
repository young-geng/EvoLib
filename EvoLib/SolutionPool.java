
package EvoLib;

public interface SolutionPool<S extends Solution> {


	public void evolve(int generation);

	public void evolve();

	public int averageFitness();

	public Iterable<S> getSolutions();

	public S getBest();


}
