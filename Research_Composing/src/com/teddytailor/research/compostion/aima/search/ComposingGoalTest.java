package com.teddytailor.research.compostion.aima.search;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import aima.core.search.framework.GoalTest;
import aima.core.search.local.Individual;

import com.Main;
import com.teddytailor.research.compostion.aima.data.ComposingBoard;
import com.teddytailor.research.compostion.aima.data.ModelFactory;

public class ComposingGoalTest implements GoalTest {
	public final static int BASE = 10000;
	public static int MIN = BASE;
	public final static File BEST_DST = new File(ModelFactory.RESOURCE.getParentFile().getParentFile(), "best");
	static { if(!BEST_DST.exists())  BEST_DST.mkdirs(); }
	
	public ComposingBoard board = null;
	public Individual<Integer> best = null;
	
	@Override
	public boolean isGoalState(Object state) {
		Individual<Integer> im = (Individual<Integer>)state;
		
		String orderStr = Main.ORDER_DOWN_MODEL + ComposingBoard.orderIntegers(im).toString();
		
		if(im.score < MIN) return false;
		
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
	
	
	private final static Pattern REGEX = Pattern.compile("([^_]+)_(\\d)+\\[([^\\]]+)\\]\\.jpg");
	public static List<Individual<Integer>> getBestResults(int model, int needLen){
		List<Individual<Integer>> result = new ArrayList<Individual<Integer>>();
		
		for(String fn: BEST_DST.list()) {
			Matcher m = REGEX.matcher(fn);
			if(m.find()) {
				int curModel = Integer.valueOf(m.group(2));
				if(curModel != model) continue;
				
				String order = m.group(3);
				String[] orders = order.split(",");
				List<Integer> ls = new ArrayList<Integer>(orders.length);
				for(String s: orders) {
					ls.add(Integer.valueOf(s.trim()));
				}
				result.add(new Individual<Integer>(ls));
			}
		}
		
		Collections.reverse(result);
		
		int havLen = result.size();
		while(havLen > needLen) {
			result.remove(--havLen);
		}
		return result;
	}
	
}
