package com.teddytailor.research.compostion.aima.data;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

import aima.core.search.local.Individual;

public class ComposingBoard {
	private int width;
	
	public ComposingBoard(int width) {
		this.width = width;
	}
	
	public double calcUsePersent(Individual<ComposingModel> im) {
		int maxY = 0;
		int useArea = 0;
		
		for(ComposingModel cm: im.getRepresentation()) {
			Model m = cm.getCurModel();
			useArea += m.getArea();
			maxY = Math.max(maxY, cm.pos.y+m.getHeight());
		}
		
		int totalArea = maxY * width;
		return 1F * useArea / totalArea;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	
	public BufferedImage draw(Individual<ComposingModel> im) {
		int maxY = 0;
		
		for(ComposingModel cm: im.getRepresentation()) {
			Model m = cm.getCurModel();
			maxY = Math.max(maxY, cm.pos.y+m.getHeight());
		}
		
		BufferedImage img = new BufferedImage(width, maxY, BufferedImage.TYPE_BYTE_BINARY);
		img.getGraphics().setColor(Color.WHITE);
		img.getGraphics().fillRect(0, 0, img.getWidth(), img.getHeight());
		
		for(ComposingModel cm: im.getRepresentation()) {
			Point pos = cm.pos;
			for(Point p: cm.getCurModel().fillPoint()) {
				img.setRGB(pos.x+p.x, pos.y+p.y, Color.BLACK.getRGB());
			}
		}
		
		return img;
	}
}
