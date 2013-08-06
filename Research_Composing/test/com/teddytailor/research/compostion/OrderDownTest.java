package com.teddytailor.research.compostion;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import aima.core.search.local.Individual;

import com.teddytailor.research.compostion.aima.data.ComposingBoard;
import com.teddytailor.research.compostion.aima.data.ComposingModel;
import com.teddytailor.research.compostion.aima.data.ModelFactory;
import com.teddytailor.research.compostion.aima.search.ComposingFitnessFunction;
import com.teddytailor.research.ocr.util.ImageFrame;

public class OrderDownTest {
	
	public static void main(String[] args) throws Exception{
		test();
	}
	
	
	public static void test() throws Exception{
		File parent = new File(ModelFactory.RESOURCE, "dat");
		
		ComposingModel foot = new ComposingModel(ModelFactory.readData(new File(parent, "脚_左_2.dat")));
		ComposingModel nose = new ComposingModel(ModelFactory.readData(new File(parent, "鼻梁_1.dat")));
		
		List<ComposingModel> cms = new ArrayList<ComposingModel>();
		cms.add(foot);
		cms.add(nose);
		
		foot.order = 0;
		foot.reversal = true;
		nose.order = 1;
		
		nose.pos.y = foot.getCurModel().getHeight()/2;
		
		Individual<ComposingModel> individual = new Individual<ComposingModel>(cms);
		
		ComposingBoard board = new ComposingBoard(1024);
		board.height = 768;
		ComposingFitnessFunction cff = new ComposingFitnessFunction(board);
		cff.getValue(individual);
		
		BufferedImage img = board.draw(individual);
		new ImageFrame().show(img);
	}
	
}
