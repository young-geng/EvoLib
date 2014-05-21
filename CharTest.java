import EvoLib.*;
import java.util.*;

public class CharTest implements Solution{
	public int[] value;
	public int fitness;
	public final static int SIZE = 20;

	public CharTest() {
		value = new int[SIZE];
		fitness = Integer.MIN_VALUE;
	}

	public CharTest clone() {
		CharTest c = new CharTest();
		for (int i = 0; i < SIZE; i++) {
			c.value[i] = value[i];
		}
		c.fitness = fitness;
		return c;
	}

	public int fitness() {
		fitness = 0;
		for (int i = 0; i < SIZE; i++) {
			fitness += Math.abs(value[i] - 100 * i);
		}
		fitness = 0 - fitness;
		return fitness;
	}

	public CharTest mutate() {
		CharTest c = clone();
		for (int i = 0; i < SIZE; i++) {
			c.value[i] += (int)(-10.0 + (Math.random() * (10.0 + 10.0)));
		}
		c.fitness = Integer.MIN_VALUE;
		return c;
	}


	public static void main(String[] args) {
		SimplePool<CharTest> pool = new SimplePool<CharTest>(50, new CharTest(), 5, 24);
		System.out.println(pool.averageFitness());
        for (int i = 0; i < 1000; i++) {
            pool.evolve(10);
            System.out.println("Generation: " + i + "  Fitness:  " + pool.averageFitness());
		}
        return;
	}

}
