package com.teddytailor.research.compostion.aima.search;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import aima.core.search.framework.GoalTest;
import aima.core.search.local.Individual;

import com.teddytailor.research.compostion.aima.data.ComposingBoard;

public class ComposingGoalTest implements GoalTest {
	public final static int BASE = 10000;
	
	public ComposingBoard board = null;
	
	public Individual<Integer> best = null;
	
	@Override
	public boolean isGoalState(Object state) {
		Individual<Integer> im = (Individual<Integer>)state;
			
		if(best==null || im.score>best.score) {
			best = im;
			showImage(board.draw(im), best.score +"_"+ System.currentTimeMillis());
		}
				
		System.out.println(im.score + "\t" + im);
		return im.score >= BASE;
	}
	
	public static void showImage(BufferedImage img, String name) {
		try {
			ImageIO.write(img, "jpg", new File("E:\\best_"+name+".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
