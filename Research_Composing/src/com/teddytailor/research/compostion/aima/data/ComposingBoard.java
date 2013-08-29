package com.teddytailor.research.compostion.aima.data;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import aima.core.search.local.Individual;

import com.teddytailor.research.compostion.aima.search.ComposingFitnessFunction;

public class ComposingBoard {
	private int width;
	private int height;
	
	private List<ComposingModel> models;
	
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
	public List<ComposingModel> getModels(){
		return models;
	}
	
	public ComposingBoard(List<ComposingModel> models, int width, int height) {
		this.models = models;
		this.width = width;
		this.height = height;
	}
	
	public double calcUsePersent(List<ComposingModel> cms) {
		int maxY = 0;
		int useArea = 0;
		
		for(ComposingModel cm: cms) {
			Model m = cm.getCurModel();
			useArea += m.getArea();
			maxY = Math.max(maxY, cm.pos.y+m.getHeight());
		}
		
		int totalArea = maxY * width;
		return 1F * useArea / totalArea;
	}
	
	public BufferedImage draw(Individual<Integer> im) {
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
		
		for(ComposingModel cm: orderPosModels(im)) {
			if(cm.pos.x == Integer.MAX_VALUE) continue;
			
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
	
	public static List<OrderInteger> orderIntegers(Individual<Integer> individual){
		List<Integer> is = new ArrayList<Integer>(individual.getRepresentation());
		int size = is.size();
		List<OrderInteger> ois = new ArrayList<OrderInteger>(size);
		for(int i=0,imax=size; i<imax; i++) {
			ois.add(OrderInteger.valueOf(i, is.get(i)));
		}
		Collections.sort(ois, OrderInteger.COMPARATOR);
		return ois;
	}
	
	public List<ComposingModel> orderModels(Individual<Integer> individual){
		return orderModels(orderIntegers(individual));
	}
	
	public List<ComposingModel> orderModels(List<OrderInteger> ois){
		List<ComposingModel> cms = new ArrayList<ComposingModel>(ois.size());
		for(OrderInteger oi: ois) {
			ComposingModel cm = this.models.get(oi.origin);
			cm.reversal = oi.reversal;
			cms.add(cm);
		}
		return cms;
	}
	
	public List<ComposingModel> orderPosModels(Individual<Integer> individual){
		List<OrderInteger> ois = orderIntegers(individual);
		
		int hashCode = ois.hashCode();
		List<Point> posLs = ComposingFitnessFunction.POINT_RESULT.get(hashCode);
		Iterator<Point> posIterator = posLs.iterator();
		
		List<ComposingModel> cms = new ArrayList<ComposingModel>(ois.size());
		for(OrderInteger oi: ois) {
			ComposingModel cm = this.models.get(oi.origin);
			cm.origin = oi.origin;
			if(posIterator.hasNext()) {
				cm.pos = new Point(posIterator.next());
			}else {
				cm.pos.x = Integer.MAX_VALUE;
			}
			cm.reversal = oi.reversal;
			cms.add(cm);
		}
		return cms;
	}
}
