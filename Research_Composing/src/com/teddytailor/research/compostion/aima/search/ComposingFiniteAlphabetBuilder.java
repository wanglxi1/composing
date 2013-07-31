package com.teddytailor.research.compostion.aima.search;

import java.awt.Point;
import java.util.Random;

import com.teddytailor.research.compostion.aima.data.ComposingBoard;
import com.teddytailor.research.compostion.aima.data.ComposingModel;
import com.teddytailor.research.compostion.aima.data.Model;

public class ComposingFiniteAlphabetBuilder implements FiniteAlphabetBuilder<ComposingModel> {

	private ComposingBoard board;
	protected Random random;
	
	public ComposingFiniteAlphabetBuilder(ComposingBoard board) {
		this.board = board;
		this.random = new Random();
	}
	
	@Override
	public ComposingModel build(ComposingModel origin) {
		ComposingModel newCM = new ComposingModel(origin.getOriginModel());
		newCM.pos = randomPos(origin);
		newCM.reversal = random.nextBoolean();
		return newCM;
	}
	
	private Point randomPos(ComposingModel origin) {
		Model m = origin.getCurModel();
		
		Point p = origin.pos;
		Point np = new Point();
		np.x = random.nextInt(board.getWidth()-m.getWidth());
		np.y = Math.abs(random.nextInt(m.getHeight()) - p.y);
		return np;
	}
	
}
