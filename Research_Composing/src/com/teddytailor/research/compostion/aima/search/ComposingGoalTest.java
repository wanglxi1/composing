package com.teddytailor.research.compostion.aima.search;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import aima.core.search.framework.GoalTest;
import aima.core.search.local.Individual;

import com.teddytailor.research.compostion.aima.data.ComposingBoard;
import com.teddytailor.research.compostion.aima.data.ModelFactory;

public class ComposingGoalTest implements GoalTest {
	public final static int BASE = 10000;
	private final static int MIN = BASE - 250;
	public final static File BEST_DST = new File(ModelFactory.RESOURCE.getParentFile().getParentFile(), "best");
	static { if(!BEST_DST.exists())  BEST_DST.mkdirs(); }
	
	public ComposingBoard board = null;
	public Individual<Integer> best = null;
	
	@Override
	public boolean isGoalState(Object state) {
		Individual<Integer> im = (Individual<Integer>)state;
		
		System.out.println(im.score + "\t" + im);
		if(im.score < MIN) return false;
		
		if(best==null || im.score>best.score) {
			best = im;
			showImage(board.draw(im), best.score +"_"+ im.toString());
		}
		return im.score >= BASE;
	}
	
	public static void showImage(BufferedImage img, String name) {
		try {
			ImageIO.write(img, "png", new File(BEST_DST, name+".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
