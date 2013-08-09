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
//		testIntersect();
	}
	
	
	public static void testIntersect() throws Exception{
		File parent = new File(ModelFactory.RESOURCE, "dat");
		
		ComposingModel o1 = new ComposingModel(ModelFactory.readData(new File(parent, "身体_右_1.dat")));
		o1.reversal = true;
		
		ComposingModel o2 = new ComposingModel(ModelFactory.readData(new File(parent, "内胳_右_1.dat")));
		
		
		List<ComposingModel> cms = new ArrayList<ComposingModel>();
		cms.add(o1);
		
		ComposingBoard board = new ComposingBoard(1024);
		board.height = 500;
		ComposingFitnessFunction cff = new ComposingFitnessFunction(board);
		
		o2.pos.x = 288;
		boolean i = cff.isIntersect(o2, cms);
		System.out.println(i);
		
	}
	
	public static void test() throws Exception{
		File parent = new File(ModelFactory.RESOURCE, "dat");
		
		
		ComposingModel o1 = new ComposingModel(ModelFactory.readData(new File(parent, "身体_右_1.dat")));
		o1.reversal = true;
		
		ComposingModel o2 = new ComposingModel(ModelFactory.readData(new File(parent, "内胳_右_1.dat")));
		
		
		List<ComposingModel> cms = new ArrayList<ComposingModel>();
		cms.add(o1);
		cms.add(o2);
		
//		cms.add(new ComposingModel(ModelFactory.readData(new File(parent, "手掌_右_1.dat"))));
//		cms.add(new ComposingModel(ModelFactory.readData(new File(parent, "手掌_左_1.dat"))));
		
		
		Individual<ComposingModel> individual = new Individual<ComposingModel>(cms);
		
		ComposingBoard board = new ComposingBoard(1024);
		board.height = 500;
		ComposingFitnessFunction cff = new ComposingFitnessFunction(board);
		cff.getValue(individual);
		
		System.out.println(o2.pos.x);
		
		BufferedImage img = board.draw(individual);
		new ImageFrame().show(img);
	}
	
}
