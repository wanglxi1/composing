package com.teddytailor.research.compostion.aima.search;

import java.util.Random;

import com.teddytailor.research.compostion.aima.data.ComposingBoard;
import com.teddytailor.research.compostion.aima.data.ComposingModel;

public class ComposingFiniteAlphabetBuilder implements FiniteAlphabetBuilder<ComposingModel> {

	protected Random random;
	
	public ComposingFiniteAlphabetBuilder(ComposingBoard board) {
		this.random = new Random();
	}
	
	@Override
	public ComposingModel build(ComposingModel origin) {
		origin.reversal = random.nextBoolean();
		origin.order = random.nextFloat();
		
		return origin;
	}
	
	
}
