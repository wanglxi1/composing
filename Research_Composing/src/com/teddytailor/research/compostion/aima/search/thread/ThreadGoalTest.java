package com.teddytailor.research.compostion.aima.search.thread;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import aima.core.search.framework.GoalTest;
import aima.core.search.local.Individual;

public class ThreadGoalTest {
	private static AtomicBoolean isGoal = new AtomicBoolean(false);
	
	public static GoalTest test = null;
	public static Runnable goalCallBack = null;
	public static Individual best = null;
	
	private static long startTime = System.currentTimeMillis();
	public static long maxTimeMilliseconds = 0;
	
	private static AtomicInteger COUNTER = new AtomicInteger(0);
	
	public static boolean isGoalState(Individual state) {
		System.out.println(COUNTER.incrementAndGet());
		
		if(best==null || state.score>best.score) {
			best = state;
		}
		
		boolean result = test.isGoalState(state);
		if(result) {
			goal();
		}
		
		if (maxTimeMilliseconds > 0L) {
			if ((System.currentTimeMillis() - startTime) > maxTimeMilliseconds) {
				goal();
			}
		}
		
		return result;
	}
	
	public static boolean isGoal() {
		return isGoal.get();
	}
	
	private static void goal() {
		isGoal.set(true);
		goalCallBack.run();
	}
	
}
