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
	
	private Map<List<ComposingModel>, Integer> valueCache = new HashMap<List<ComposingModel>, Integer>();//ConcurrentHashMap
	
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
			
		Integer value = valueCache.get(cms);
		if(value != null) {
			return value;
		}
		
		int boardWidth = this.board.getWidth();
		
		List<ComposingModel> downCms = new ArrayList<ComposingModel>(cms.size());
		int preMaxX = 0;
		for(ComposingModel cm: cms) {
			cm.out = preMaxX > boardWidth; 
			if(cm.out) {
				continue;
			}
			
			int minX = orderDown(preMaxX, cm, downCms);
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

	
	public int orderDown(int startX, ComposingModel cm, List<ComposingModel> downCms) {
		int minX = startX;
		for(int x = startX-1; x>=0; x--) {
			cm.pos.x = x;
			
			if(isIntersect(cm, downCms)) {
				int orginY = cm.pos.y;
				
				cm.pos.y = orginY - 1;
				if(isIntersect(cm, downCms)) {
					cm.pos.y = orginY + 1;
					if(isIntersect(cm, downCms)) {
						cm.pos.y = orginY;
						break;
					}
				}
			}
			minX = x;
		}
		return minX;
	}
	
	public boolean isIntersect(ComposingModel cm, List<ComposingModel> downCms) {
		if(cm.pos.y<0 || cm.pos.y+cm.getCurModel().getHeight()>this.board.height) return true;
		
		boolean isIntersect = false;
		for(ComposingModel dCm: downCms) {
			if(cm.intersect(dCm)) {
				isIntersect = true;
				break;
			}
		}
		return isIntersect;
	}
}
