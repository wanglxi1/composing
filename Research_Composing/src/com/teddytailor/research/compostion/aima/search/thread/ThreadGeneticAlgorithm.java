package com.teddytailor.research.compostion.aima.search.thread;

import java.util.Set;

import aima.core.search.framework.GoalTest;
import aima.core.search.local.FitnessFunction;
import aima.core.search.local.Individual;

import com.teddytailor.research.compostion.aima.search.FiniteAlphabetBuilder;

public class ThreadGeneticAlgorithm<A> {
	//
	protected int individualLength;
	protected FiniteAlphabetBuilder<A> finiteAlphabet;
	protected double mutationProbability;
	
	public final static int THREAD_SIZE = Runtime.getRuntime().availableProcessors()-1;

	public ThreadGeneticAlgorithm(int individualLength, FiniteAlphabetBuilder<A> finiteAlphabet, double mutationProbability) {
		this.individualLength = individualLength;
		this.finiteAlphabet = finiteAlphabet;
		this.mutationProbability = mutationProbability;

		assert (this.mutationProbability >= 0.0 && this.mutationProbability <= 1.0);
	}


	/**
	 * Returns the best individual in the specified population, according to the
	 * specified FITNESS-FN and goal test.
	 * 
	 * @param population
	 *            a set of individuals
	 * @param fitnessFn
	 *            a function that measures the fitness of an individual
	 * @param goalTest
	 *            test determines whether a given individual is fit enough to
	 *            return.
	 * @param maxTimeMilliseconds
	 *            the maximum time in milliseconds that the algorithm is to run
	 *            for (approximate). Only used if > 0L.
	 * @return the best individual in the specified population, according to the
	 *         specified FITNESS-FN and goal test.
	 */
	// function GENETIC-ALGORITHM(population, FITNESS-FN) returns an individual
	// inputs: population, a set of individuals
	// FITNESS-FN, a function that measures the fitness of an individual
	public void geneticAlgorithm(Set<Individual<A>>[] population, FitnessFunction<A> fitnessFn, GoalTest goalTest, long maxTimeMilliseconds) {

//		// Create a local copy of the population to work with
//		population = new LinkedHashSet<Individual<A>>(population);
//		// Validate the population and setup the instrumentation
//		validatePopulation(population);
		
		ThreadGoalTest.test = goalTest;
		ThreadGoalTest.maxTimeMilliseconds = maxTimeMilliseconds;

		for(int i=0; i<THREAD_SIZE; i++) {
			GAThread<A> ga = new GAThread<A>();
			
			ga.finiteAlphabet = this.finiteAlphabet;
			ga.fitnessFn = fitnessFn;
			ga.individualLength = this.individualLength;
			ga.mutationProbability = this.mutationProbability;
			ga.population = population[i];
			
			ga.start();
		}
	}
	

	protected void validatePopulation(Set<Individual<A>> population) {
		// Require at least 1 individual in population in order
		// for algorithm to work
		if (population.size() < 1) {
			throw new IllegalArgumentException("Must start with at least a population of size 1");
		}
		// String lengths are assumed to be of fixed size,
		// therefore ensure initial populations lengths correspond to this
		for (Individual<A> individual : population) {
			if (individual.length() != this.individualLength) {
				throw new IllegalArgumentException("Individual [" + individual + "] in population is not the required length of "
						+ this.individualLength);
			}
		}
	}
}
