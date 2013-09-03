package com.teddytailor.research.compostion.aima.search;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import aima.core.search.framework.GoalTest;
import aima.core.search.local.Individual;

import com.Main;
import com.teddytailor.research.compostion.aima.data.ComposingBoard;
import com.teddytailor.research.compostion.aima.data.ModelFactory;

public class ComposingGoalTest implements GoalTest {
	public final static int BASE = 10000;
	public final static File BEST_DST = new File(ModelFactory.RESOURCE.getParentFile().getParentFile(), "best");
	static { if(!BEST_DST.exists())  BEST_DST.mkdirs(); }
	
	public ComposingBoard board = null;
	public Individual<Integer> best = null;
	
	@Override
	public boolean isGoalState(Object state) {
		Individual<Integer> im = (Individual<Integer>)state;
		
		String orderStr = Main.ORDER_DOWN_MODEL + ComposingBoard.orderIntegers(im).toString();
		
		if(im.score < BASE) return false;
		
		System.out.println(im.score + "\t" + orderStr);
		
		if(best==null || im.score>best.score) {
			best = im;
			showImage(board.draw(im), best.score +"_"+ orderStr);
		}
		return false;
	}
	
	public static void showImage(BufferedImage img, String name) {
		try {
			ImageIO.write(img, "png", new File(BEST_DST, name+".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
