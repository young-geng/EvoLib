/**
 * Solution interface for GeneticaAlgorithm
 *
 */

package EvoLib;

public interface Solution{


	public int fitness();

	public Solution clone();

	public Solution mutate();

}
