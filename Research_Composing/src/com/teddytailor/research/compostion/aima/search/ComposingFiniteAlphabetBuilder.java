package com.teddytailor.research.compostion.aima.search;

import java.util.Random;

import com.teddytailor.research.compostion.aima.data.ComposingBoard;
import com.teddytailor.research.compostion.aima.data.ComposingModel;

public class ComposingFiniteAlphabetBuilder implements FiniteAlphabetBuilder<ComposingModel> {

	private ComposingBoard board;
	protected Random random;
	
	public ComposingFiniteAlphabetBuilder(ComposingBoard board) {
		this.board = board;
		this.random = new Random();
	}
	
	@Override
	public ComposingModel build(ComposingModel origin) {
		origin.reversal = random.nextBoolean();
		origin.order = random.nextFloat();
		
		int yDist = board.height - origin.getCurModel().getHeight();
		if(yDist == 0) {
			origin.pos.y = 0;
		}else {
			origin.pos.y = random.nextInt(yDist);
		}
		
		return origin;
	}
	
	
}
