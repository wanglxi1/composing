package com.teddytailor.research.compostion.aima.search.thread;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import aima.core.search.local.FitnessFunction;
import aima.core.search.local.Individual;
import aima.core.util.Util;

import com.teddytailor.research.compostion.aima.search.FiniteAlphabetBuilder;

public class GAThread<A> extends Thread {

	public int individualLength;
	public FiniteAlphabetBuilder<A> finiteAlphabet;
	public double mutationProbability;
	public Set<Individual<A>> population;
	public FitnessFunction<A> fitnessFn;
	
	private Random random = new Random();
	
	@Override
	public void run() {
		while(true) {
			if(ThreadGoalTest.isGoal()) return;
			Individual<A> best = this.nextGeneration();
			System.out.println(this.getName() + "\t" +best.score);
			if(ThreadGoalTest.isGoalState(best)) return;
		}
	}

	//
	// PROTECTED METHODS
	//
	// Note: Override these protected methods to create your own desired
	// behavior.
	//
	protected Individual<A> nextGeneration() {
		// new_population <- empty set
		Set<Individual<A>> newPopulation = new HashSet<Individual<A>>();

		// Represent the population as a list to simplify/optimize random
		// selection.
		List<Individual<A>> populationAsList = new ArrayList<Individual<A>>(population);

		// for i = 1 to SIZE(population) do
		for (int i = 0; i < population.size(); i++) {
			// x <- RANDOM-SELECTION(population, FITNESS-FN)
			Individual<A> x = randomSelection(populationAsList, fitnessFn);
			// y <- RANDOM-SELECTION(population, FITNESS-FN)
			Individual<A> y = randomSelection(populationAsList, fitnessFn);
			// child <- REPRODUCE(x, y)
			Individual<A> child = reproduce(x, y);
			// if (small random probability) then child <- MUTATE(child)
			if (random.nextDouble() <= this.mutationProbability) {
				child = mutate(child);
			}
			// add child to new_population
			newPopulation.add(child);
		}
		// population <- new_population
		population.clear();
		population.addAll(newPopulation);

		return retrieveBestIndividual(population, fitnessFn);
	}

	// RANDOM-SELECTION(population, FITNESS-FN)
	protected Individual<A> randomSelection(List<Individual<A>> population, FitnessFunction<A> fitnessFn) {
		Individual<A> selected = null;

		// Determine all of the fitness values
		double[] fValues = new double[population.size()];
		for (int i = 0; i < population.size(); i++) {
			fValues[i] = fitnessFn.getValue(population.get(i));
		}

		// Normalize the fitness values
		fValues = Util.normalize(fValues);
		double prob = random.nextDouble();
		double totalSoFar = 0.0;
		for (int i = 0; i < fValues.length; i++) {
			// Are at last element so assign by default
			// in case there are rounding issues with the normalized values
			totalSoFar += fValues[i];
			if (prob <= totalSoFar) {
				selected = population.get(i);
				break;
			}
		}

		// selected may not have been assigned
		// if there was a rounding error in the
		// addition of the normalized values (i.e. did not total to 1.0)
		if (null == selected) {
			// Assign the last value
			selected = population.get(population.size() - 1);
		}

		return selected;
	}

	// function REPRODUCE(x, y) returns an individual
	// inputs: x, y, parent individuals
	protected Individual<A> reproduce(Individual<A> x, Individual<A> y) {
		// n <- LENGTH(x);
		// Note: this is = this.individualLength
		// c <- random number from 1 to n
		int c = randomOffset(individualLength);
		// return APPEND(SUBSTRING(x, 1, c), SUBSTRING(y, c+1, n))
		List<A> childRepresentation = new ArrayList<A>();
		childRepresentation.addAll(x.getRepresentation().subList(0, c));
		childRepresentation.addAll(y.getRepresentation().subList(c, individualLength));

		Individual<A> child = new Individual<A>(childRepresentation);
		return child;
	}

	protected Individual<A> mutate(Individual<A> child) {
		int mutateOffset = randomOffset(individualLength);

		List<A> mutatedRepresentation = new ArrayList<A>(child.getRepresentation());

		A orginA = mutatedRepresentation.get(mutateOffset);
		mutatedRepresentation.set(mutateOffset, finiteAlphabet.build(orginA));

		Individual<A> mutatedChild = new Individual<A>(mutatedRepresentation);

		return mutatedChild;
	}

	protected Individual<A> retrieveBestIndividual(Set<Individual<A>> population, FitnessFunction<A> fitnessFn) {
		Individual<A> bestIndividual = null;
		double bestSoFarFValue = Double.NEGATIVE_INFINITY;

		for (Individual<A> individual : population) {
			double fValue = fitnessFn.getValue(individual);
			if (fValue > bestSoFarFValue) {
				bestIndividual = individual;
				bestSoFarFValue = fValue;
			}
		}
		
		bestIndividual.score = bestSoFarFValue;
		return bestIndividual;
	}

	protected int randomOffset(int length) {
		return random.nextInt(length);
	}
}
