package com.teddytailor.research.compostion.aima.search;

import com.teddytailor.research.compostion.aima.data.ComposingModel;

import aima.core.search.framework.GoalTest;
import aima.core.search.local.Individual;

public class ComposingGoalTest implements GoalTest {
	
	public Individual<ComposingModel> best = null;
	
	@Override
	public boolean isGoalState(Object state) {
		Individual<ComposingModel> im = (Individual<ComposingModel>)state;
			
		if(best==null || im.score>best.score) {
			best = im;
		}
				
		System.out.println(im.score);
		return im.score >= 0;
	}

}
