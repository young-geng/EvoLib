

package EvoLib;

import java.util.*;



public class SimplePool<S extends Solution> implements SolutionPool<S> {
	int selectionFactor;
	int size;
	int threadSize;
	protected Solution[] pool;


	public SimplePool(int size, S origin, int selectionFactor, int threadSize) {
		this.size = size;
		pool = new Solution[size];
		for (int i = 0; i < size; i++) {
			pool[i] = origin.clone();
		}
		this.selectionFactor = selectionFactor;
		this.threadSize = threadSize;
	}

	public SimplePool(int size, S origin) {
		this(size, origin, 4, 1);
	}

	public SimplePool(int size, S origin, int selectionFactor) {
		this(size, origin, selectionFactor, 1);
	}


	@SuppressWarnings("unchecked")
	public Iterable<S> getSolutions() {
		ArrayList<S> solutions = new ArrayList<S>();
		for (int i = 0; i < size; i++) {
			solutions.add((S)(pool[i].clone()));
		}
		return solutions;
	}

	@SuppressWarnings("unchecked")
	public S getBest() {
		return (S)(pool[0].clone());
	}


	public void evolve() {
		Solution[] mutated = new Solution[(selectionFactor + 1) * size];
		ParallelEvalutator[] evaluator = new ParallelEvalutator[threadSize];
		ParallelMutator[] mutator = new ParallelMutator[threadSize];


		for (int i = 0; i < threadSize; i++) {
			mutator[i] = new ParallelMutator(i, threadSize, selectionFactor, pool, mutated);
		}
		for (int i = 0; i < threadSize; i++) {
			mutator[i].start();
		}
		for (int i = 0; i < threadSize; i++) {
			try {
				mutator[i].join();
			} catch(InterruptedException e) {
				System.err.println("Computation thread interrupted!");
				System.exit(1);
			}
		}

		

		for (int i = 0; i < threadSize; i++) {
			evaluator[i] = new ParallelEvalutator(i, threadSize, mutated);
		}
		for (int i = 0; i < threadSize; i++) {
			evaluator[i].start();
		}
		for (int i = 0; i < threadSize; i++) {
			try {
				evaluator[i].join();
			} catch(InterruptedException e) {
				System.err.println("Computation thread interrupted!");
				System.exit(1);
			}
		}


		Arrays.sort(mutated);
		for (int i = 0, j = size * (selectionFactor + 1) - 1; i < size; i++, j--) {
			pool[i] = mutated[j];
		}
	}


	public void evolve(int generation) {
		for (int i = 0; i < generation; i++) {
			evolve();
		}
	}


	public int averageFitness() {
		long total = 0;
		for (int i = 0; i < size; i++) {
			total += pool[i].fitness();
		}
		return (int)(total / (long)size);
	}
}


class ParallelEvalutator extends Thread {
	int increment;
	int base;
	Solution[] array;


	public ParallelEvalutator(int base, int increment, Solution[] array) {
		this.increment = increment;
		this.base = base;
		this.array = array;
	}

	public void run() {
		for(int i = base; i < array.length; i += increment) {
			array[i].evaluate();
		}
	}

}


class ParallelMutator extends Thread {
	Solution[] source;
	Solution[] target;
	int base, increment, selectionFactor;

	public ParallelMutator(int base, int increment, int selectionFactor, Solution[] source, Solution[] target) {
		this.increment = increment;
		this.base = base;
		this.source = source;
		this.target = target;
		this.selectionFactor = selectionFactor;
	}

	public void run() {
		for (int i = base; i < source.length; i += increment) {
			target[i * (selectionFactor + 1)] = source[i].clone();
			for (int j = 1; j <= selectionFactor; j++) {
				target[i * (selectionFactor + 1) + j] = source[i].mutate();
			}
		}
	}


}
