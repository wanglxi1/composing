package com.teddytailor.research.compostion.aima;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aima.core.search.framework.GoalTest;
import aima.core.search.local.Individual;

import com.teddytailor.research.compostion.aima.data.ComposingBoard;
import com.teddytailor.research.compostion.aima.data.ComposingModel;
import com.teddytailor.research.compostion.aima.data.Model;
import com.teddytailor.research.compostion.aima.data.ModelFactory;
import com.teddytailor.research.compostion.aima.search.ComposingFiniteAlphabetBuilder;
import com.teddytailor.research.compostion.aima.search.ComposingFitnessFunction;
import com.teddytailor.research.compostion.aima.search.ComposingGoalTest;
import com.teddytailor.research.compostion.aima.search.GeneticAlgorithm;
import com.teddytailor.research.ocr.util.ImageFrame;

public class Main {

	public static int COMPOSING_BOARD_WIDTH = 0;
	public static int COMPOSING_BOARD_HEIGHT = 0;
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		long maxTimeMilliseconds = -1;//1000L * 60;
		double mutationProbability = 0.33;
		
		List<ComposingModel> models = buildModels(new File(ModelFactory.RESOURCE, "dat"));
		int modelLen = models.size();
		
		ComposingBoard board = new ComposingBoard(COMPOSING_BOARD_WIDTH+1);
		board.height = COMPOSING_BOARD_HEIGHT+1;
		
		ComposingFiniteAlphabetBuilder finiteAlphabetBuilder = new ComposingFiniteAlphabetBuilder(board);
		ComposingFitnessFunction fitnessFunction = new ComposingFitnessFunction(board);
		GoalTest goalTest = new ComposingGoalTest();
		
		
		Set<Individual<ComposingModel>> population = new HashSet<Individual<ComposingModel>>();
		for(int i=0; i<1000; i++) {
			List<ComposingModel> ls = new ArrayList<ComposingModel>(modelLen);
			for(ComposingModel m: models) {
				ls.add(finiteAlphabetBuilder.build(m));
			}
			population.add(new Individual<ComposingModel>(ls));
		}
		
		
		GeneticAlgorithm<ComposingModel> ga = new GeneticAlgorithm<ComposingModel>(modelLen, finiteAlphabetBuilder, mutationProbability);
		
		// Run for a set amount of time
		Individual<ComposingModel> bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, goalTest, maxTimeMilliseconds);
	
		ImageFrame frame = new ImageFrame();
		frame.show(board.draw(bestIndividual));
	}
	
	
	private static List<ComposingModel> buildModels(File parent) throws Exception {
		List<ComposingModel> models = new ArrayList<ComposingModel>();
		
		for(File f: parent.listFiles()) {
			Model m = ModelFactory.readData(f);
			
			String fileName = f.getName();
			int len = fileName.length();
			
			String modelName = fileName.substring(0, len-6);
			String numStr = fileName.substring(len-5, len-4);
			int num = Integer.valueOf(numStr);
			
			if("鼻梁".equals(modelName)) {
				COMPOSING_BOARD_WIDTH = m.getHeight() * 144 / 5;
			}
			
			for(int i=0; i<num; i++) {
				ComposingModel cm = new ComposingModel(m);
				cm.name = modelName;
				models.add(cm);
			}
			
			COMPOSING_BOARD_HEIGHT = Math.max(COMPOSING_BOARD_HEIGHT, m.getHeight());
		}
		
		return models;
	}

}
