package com.teddytailor.research.compostion.aima.data;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class ComposingModel implements Cloneable {
	public String name = null;
	public Point pos = new Point(0, 0);
	public boolean reversal = false;
	
	private Map<Boolean, Model> modelMap;
	
	public ComposingModel(Model m) {
		this.modelMap = new HashMap<Boolean, Model>();
		this.modelMap.put(false, m);
		this.modelMap.put(true, m.reversal);
	}
	
	public boolean intersect(ComposingModel m) {
		Point offset = new Point(m.pos.x-this.pos.x, m.pos.y-this.pos.y);
		Model tc = this.getCurModel();
		Model mc = m.getCurModel();
		return tc.intersect(mc, offset);
	}
	
	
	public Model getCurModel() {
		return this.modelMap.get(this.reversal);
	}
	
	public Model getOriginModel() {
		return this.modelMap.get(false);
	}

}
