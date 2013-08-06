package com.teddytailor.research.compostion.aima.data;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

import aima.core.search.local.Individual;

public class ComposingBoard {
	private int width;
	public int height;
	
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
		
//		for(ComposingModel cm: im.getRepresentation()) {
//			Model m = cm.getCurModel();
//			maxY = Math.max(maxY, cm.pos.y+m.getHeight());
//		}
		
		maxY = height;
		
		BufferedImage img = new BufferedImage(width, maxY, BufferedImage.TYPE_BYTE_BINARY);
		int width = img.getWidth();
		int height = img.getHeight();
		
		img.getGraphics().setColor(Color.WHITE);
		img.getGraphics().fillRect(0, 0, width, height);
		
		for(ComposingModel cm: im.getRepresentation()) {
			if(cm.out) continue;
			
			Point pos = cm.pos;
			for(Point p: cm.getCurModel().fillPoint()) {
				int x = pos.x+p.x;
				int y = pos.y+p.y;
				
				if(x >= width) continue;
				if(y >= height) continue;
				
				img.setRGB(x, y, Color.BLACK.getRGB());
			}
		}
		
		return img;
	}
}
