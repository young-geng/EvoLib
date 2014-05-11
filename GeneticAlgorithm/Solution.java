/**
 * Solution interface for GeneticaAlgorithm
 *
 */

package GeneticAlgorithm;

public interface Solution extends Comparable {



	public void evaluate();

	public int compareTo(Solution s);

	public int fitness();

	public Solution clone();

	public Solution mutate();

}