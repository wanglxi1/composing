package com.teddytailor.research.compostion.aima.search;

import java.util.List;

import aima.core.search.local.FitnessFunction;
import aima.core.search.local.Individual;

import com.teddytailor.research.compostion.aima.data.ComposingBoard;
import com.teddytailor.research.compostion.aima.data.ComposingModel;

public class ComposingFitnessFunction implements FitnessFunction<ComposingModel> {

	private ComposingBoard board;
	
	public ComposingFitnessFunction(ComposingBoard board) {
		this.board = board;
	}
	
	@Override
	public double getValue(Individual<ComposingModel> individual) {
		List<ComposingModel> ms = individual.getRepresentation();
		int size = ms.size();
		
		for(int i=0,imax=size-2; i<imax; i++) {
			ComposingModel keyCm = ms.get(i);
			for(int j=i+1,jmax=size-1; j<jmax; j++) {
				ComposingModel compareCm = ms.get(j);
				if(keyCm.intersect(compareCm)) {
					return -1;
				}
			}
		}
		
		return board.calcUsePersent(individual);
	}

	
}
