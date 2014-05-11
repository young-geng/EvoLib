/**
 * Solution interface for GeneticaAlgorithm
 *
 */

package EvoLib;

public interface Solution extends Comparable {



	public void evaluate();

	public int compareTo(Solution s);

	public int fitness();

	public Solution clone();

	public Solution mutate();

}
