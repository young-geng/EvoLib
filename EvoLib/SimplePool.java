

package EvoLib;

import java.util.*;



public class SimplePool<S extends Solution> implements SolutionPool<S> {
	int selectionFactor;
	int size;
	int threadSize;
	SolutionWrapper[] pool;


	class ParallelEvalutator extends Thread {
		int increment;
		int base;
		SolutionWrapper[] array;


		public ParallelEvalutator(int base, int increment, SolutionWrapper[] array) {
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
		SolutionWrapper[] source;
		SolutionWrapper[] target;
		int base, increment, selectionFactor;

		public ParallelMutator(int base, int increment, int selectionFactor, SolutionWrapper[] source, SolutionWrapper[] target) {
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


	public SimplePool(int size, S origin, int selectionFactor, int threadSize) {
		this.size = size;
		pool = new SolutionWrapper[size];
		for (int i = 0; i < size; i++) {
			pool[i] = new SolutionWrapper(origin.clone());
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
			solutions.add((S)(pool[i].solution().clone()));
		}
		return solutions;
	}

	@SuppressWarnings("unchecked")
	public S getBest() {
		return (S)(pool[0].solution().clone());
	}


	public void evolve() {
		SolutionWrapper[] mutated = new SolutionWrapper[(selectionFactor + 1) * size];
		ArrayList<ParallelEvalutator> evaluator = new ArrayList<ParallelEvalutator>();
		ArrayList<ParallelMutator> mutator = new ArrayList<ParallelMutator>();


		for (int i = 0; i < threadSize; i++) {
			mutator.add(new ParallelMutator(i, threadSize, selectionFactor, pool, mutated));
		}
		for (int i = 0; i < threadSize; i++) {
			mutator.get(i).start();
		}
		for (int i = 0; i < threadSize; i++) {
			try {
				mutator.get(i).join();
			} catch(InterruptedException e) {
				System.err.println("Computation thread interrupted!");
				System.exit(1);
			}
		}

		

		for (int i = 0; i < threadSize; i++) {
			evaluator.add(new ParallelEvalutator(i, threadSize, mutated));
		}
		for (int i = 0; i < threadSize; i++) {
			evaluator.get(i).start();
		}
		for (int i = 0; i < threadSize; i++) {
			try {
				evaluator.get(i).join();
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

