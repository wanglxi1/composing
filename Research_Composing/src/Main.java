
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import aima.core.search.local.Individual;

import com.teddytailor.research.compostion.aima.data.ComposingBoard;
import com.teddytailor.research.compostion.aima.data.ComposingModel;
import com.teddytailor.research.compostion.aima.data.Model;
import com.teddytailor.research.compostion.aima.data.ModelFactory;
import com.teddytailor.research.compostion.aima.search.ComposingFiniteAlphabetBuilder;
import com.teddytailor.research.compostion.aima.search.ComposingFitnessFunction;
import com.teddytailor.research.compostion.aima.search.ComposingGoalTest;
import com.teddytailor.research.compostion.aima.search.GeneticAlgorithm;

public class Main {

	public static int COMPOSING_BOARD_WIDTH = 0;
	public static int COMPOSING_BOARD_HEIGHT = 0;
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		long maxTimeMilliseconds = 0;
//		maxTimeMilliseconds = 1000L * 60;
		
		double mutationProbability = 0.9;
		int population_len = 10;
		
		List<ComposingModel> models = buildModels(new File(ModelFactory.RESOURCE, "dat"));
		int modelLen = models.size();
		
		final ComposingBoard board = new ComposingBoard(COMPOSING_BOARD_WIDTH+1);
		board.height = COMPOSING_BOARD_HEIGHT+1;
		
		ComposingFiniteAlphabetBuilder finiteAlphabetBuilder = new ComposingFiniteAlphabetBuilder(board);
		ComposingFitnessFunction fitnessFunction = new ComposingFitnessFunction(board);
		ComposingGoalTest goalTest = new ComposingGoalTest();
		goalTest.board = board;
		
//		Set<Individual<ComposingModel>>[] populations = new Set[ThreadGeneticAlgorithm.THREAD_SIZE];
//		for(int j=0,jmax=populations.length; j<jmax; j++) {
//			populations[j] = population;
//		}
		
		Set<Individual<ComposingModel>> population = new HashSet<Individual<ComposingModel>>();
		for(int i=0; i<population_len; i++) {
			List<ComposingModel> ls = new ArrayList<ComposingModel>(modelLen);
			for(ComposingModel m: models) {
				ls.add(finiteAlphabetBuilder.build(m));
			}
			population.add(new Individual<ComposingModel>(ls));
		}
		
		
		GeneticAlgorithm<ComposingModel> ga = new GeneticAlgorithm<ComposingModel>(modelLen, finiteAlphabetBuilder, mutationProbability);
		// Run for a set amount of time
		Individual<ComposingModel> bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, goalTest, maxTimeMilliseconds);
	
		bestIndividual = goalTest.best;
		
		BufferedImage img = board.draw(bestIndividual);
//		new ImageFrame().show(img);
		
		showImage(img, Double.valueOf(bestIndividual.score).intValue()+"");
		
		
		//经验证，使用多线程后，效率反而降低，可能是伪双核的原因
		
//		ThreadGoalTest.goalCallBack = new Runnable() {
//			@Override
//			public void run() {
//				Individual best = ThreadGoalTest.best;
//				BufferedImage img = board.draw(best);
//				try {
//					ImageIO.write(img, "jpg", new File("E:\\best_"+Double.valueOf(best.score).intValue()+".bmp"));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		};
//		
//		ThreadGeneticAlgorithm<ComposingModel> ga = new ThreadGeneticAlgorithm<ComposingModel>(modelLen, finiteAlphabetBuilder, mutationProbability);
//		ga.geneticAlgorithm(populations, fitnessFunction, goalTest, maxTimeMilliseconds);
	}
	
	public static void showImage(BufferedImage img, String name) {
		try {
			ImageIO.write(img, "jpg", new File("E:\\best_"+name+".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
