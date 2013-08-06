package com.teddytailor.research.compostion.aima.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aima.core.search.local.FitnessFunction;
import aima.core.search.local.Individual;

import com.teddytailor.research.compostion.aima.data.ComposingBoard;
import com.teddytailor.research.compostion.aima.data.ComposingModel;

public class ComposingFitnessFunction implements FitnessFunction<ComposingModel>, Comparator<ComposingModel> {

	private ComposingBoard board;
	
	private Map<List<ComposingModel>, Integer> valueCache = new HashMap<List<ComposingModel>, Integer>();
	
	public ComposingFitnessFunction(ComposingBoard board) {
		this.board = board;
	}
	
	@Override
	public int compare(ComposingModel o1, ComposingModel o2) {
		return Float.valueOf(o1.order).compareTo(o2.order);
	}
	
	@Override
	public double getValue(Individual<ComposingModel> individual) {
		List<ComposingModel> cms = new ArrayList(individual.getRepresentation());
		Collections.sort(cms, this);
		
		if(valueCache.containsKey(cms)) {
			return valueCache.get(cms);
		}
		
		int boardWidth = this.board.getWidth();
		
		List<ComposingModel> downCms = new ArrayList<ComposingModel>(cms.size());
		int preMaxX = 0;
		for(ComposingModel cm: cms) {
			cm.out = preMaxX > boardWidth; 
			if(cm.out) {
				continue;
			}
			
			int minX = preMaxX;
			for(int x = preMaxX-1; x>=0; x--) {
				cm.pos.x = x;
				
				boolean isIntersect = false;
				for(ComposingModel dCm: downCms) {
					if(cm.intersect(dCm)) {
						isIntersect = true;
						break;
					}
				}
				if(isIntersect) break;
				minX = x;
			}
			cm.pos.x = minX;
			downCms.add(cm);
			
			preMaxX = minX + cm.getCurModel().getWidth();
		}
		
		int result;
		
		int undown = downCms.size() - cms.size();
		if(undown < 0) {
			result = undown;
		}else {
			result = boardWidth - preMaxX;
		}
		
		downCms = null;
		valueCache.put(cms, result);
		
		return result;
	}

	
	public void orderDown(Individual<ComposingModel> im) {
		
	}
	
}
