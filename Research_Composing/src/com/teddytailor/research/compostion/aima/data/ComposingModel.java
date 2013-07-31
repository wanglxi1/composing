package com.teddytailor.research.compostion.aima.data;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class ComposingModel {
	
	public String name;
	public Point pos;
	public boolean reversal;
	
	private Map<Boolean, Model> modelMap;
	
	
	public ComposingModel(Model m) {
		this.pos = new Point(0, 0);
		this.reversal = false;
		
		this.modelMap = new HashMap<Boolean, Model>();
		this.modelMap.put(false, m);
		this.modelMap.put(true, m.reversal());
	}
	
	public boolean intersect(ComposingModel m) {
		Point offset = new Point(m.pos.x-this.pos.x, m.pos.y-this.pos.y);
		return this.getCurModel().intersect(m.getCurModel(), offset);
	}
	
	
	public Model getCurModel() {
		return this.modelMap.get(this.reversal);
	}
	
	public Model getOriginModel() {
		return this.modelMap.get(false);
	}
}
