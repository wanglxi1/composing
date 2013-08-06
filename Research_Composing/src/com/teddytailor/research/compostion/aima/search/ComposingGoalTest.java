package com.teddytailor.research.compostion.aima.search;

import com.teddytailor.research.compostion.aima.data.ComposingModel;

import aima.core.search.framework.GoalTest;
import aima.core.search.local.Individual;

public class ComposingGoalTest implements GoalTest {
	
	@Override
	public boolean isGoalState(Object state) {
		Individual<ComposingModel> im = (Individual<ComposingModel>)state;
		return im.score >= 0;
	}

}
