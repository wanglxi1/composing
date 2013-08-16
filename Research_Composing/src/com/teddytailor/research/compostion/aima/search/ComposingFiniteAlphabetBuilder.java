package com.teddytailor.research.compostion.aima.search;

import java.util.Random;

import com.teddytailor.research.compostion.aima.data.ComposingBoard;

public class ComposingFiniteAlphabetBuilder implements FiniteAlphabetBuilder<Integer> {

	protected Random random;
	private int len;
	
	public ComposingFiniteAlphabetBuilder(ComposingBoard board) {
		this.random = new Random();
		this.len = board.getModels().size();
	}
	
	@Override
	public Integer build(Integer origin) {
		boolean reversal = random.nextBoolean();
		int order = random.nextInt(this.len);
		
		return order * (reversal? -1: 1);
	}
	
	
}
