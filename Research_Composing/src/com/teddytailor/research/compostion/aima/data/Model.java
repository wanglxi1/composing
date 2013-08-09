package com.teddytailor.research.compostion.aima.data;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.teddytailor.research.img.ImageEdgeFill;

public class Model implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int width;
	private int height;
	private int area;

	private Set<Point> edge;
	private Map<Integer, List<Integer>> yX;
	
	public Model(List<Point> edge) {
		Point min = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
		Point max = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
		
		//取出边界矩形
		for(Point p: edge) {
			min.x = Math.min(min.x, p.x);
			min.y = Math.min(min.y, p.y);
			max.x = Math.max(max.x, p.x);
			max.y = Math.max(max.y, p.y);
		}
		
		//设定长宽
		this.width = max.x - min.x+1;
		this.height = max.y - min.y+1;
		

		this.yX  = new HashMap<Integer, List<Integer>>(this.height);
		this.edge = new HashSet<Point>(edge.size());
		for(Point p: edge) {
			p.x = p.x-min.x;
			p.y = p.y-min.y;
			
			this.edge.add(p);
			
			List<Integer> xs = yX.get(p.y);
			if(xs == null) {
				xs = new ArrayList<Integer>();
				yX.put(p.y, xs);
			}
			xs.add(p.x);
		}
		
		this.area = this.fillPoint().size();
	}
	
	/**
	 * 是否相交
	 * 
	 * 思路：首先把两个多边形近似成矩形，然后取相交的矩形区域，下一步是扫描此区域的每一行是否有重合的边缘点
	 * 
	 * @param model
	 * @param offset
	 * @return
	 */
	public final static int[] deltas = {-1, 1};
	public boolean intersect(Model model, Point offset) {
		Rectangle r1 = new Rectangle(this.getWidth(), this.getHeight());
		Rectangle r2 = new Rectangle(offset.x, offset.y, model.getWidth(), model.getHeight());
		
		Rectangle ir = r1.intersection(r2);
		if(ir.isEmpty()) return false;
		
		
		for(int y1=ir.y,ymax=ir.y+ir.height; y1<ymax; y1++) {
			int y2 = y1-offset.y;
			List<Integer> xs1 = this.yX.get(y1);
			List<Integer> xs2 = model.yX.get(y2);
			
			for(int x: xs2) {
				int ox = x + offset.x;
				if(ox>=ir.x && ox<=ir.getMaxX()) {
					if(xs1.contains(ox)) {
						return true;
					}else {
						//修正田字交叉造成的伪不相交的情况
						for(int dx: deltas) {
							if(xs1.contains(ox + dx)) {
								for(int dy: deltas) {
									List<Integer> xs1_d = this.yX.get(y1+dy);
									if(xs1_d!=null && xs1_d.contains(ox)) {
										List<Integer> xs2_d = model.yX.get(y2+dy);
										if(xs2_d!=null && xs2_d.contains(x + dx)) {
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 因为面对的排列对象，需要按照毛向进行排列，最多只能旋转180度，所以这里只做一个反转
	 * 
	 * @return
	 */
	public Model reversal() {
		int size = this.height;
		int before = 0;
		int after = size-1;
		
		List<Point> newEdge = new ArrayList<Point>(this.edge.size());
		
		while(true) {
			int beforeY = before++;
			int afterY = after--;
			
			if(beforeY > afterY) break;
			
			List<Integer> beforeXs = this.yX.get(beforeY);
			List<Integer> afterXs = this.yX.get(afterY);
			if(beforeXs != null) {
				for(int x: beforeXs) {
					newEdge.add(new Point(this.width-x, afterY));
				}
			}
			
			if(afterXs!=null && beforeY!=afterY) {
				for(int x: afterXs) {
					newEdge.add(new Point(this.width-x, beforeY));
				}
			}
		}
		
		return new Model(newEdge);
	}
	
	public Set<Point> fillPoint(){
		Map<Integer, Set<Integer>> fillYXs = ImageEdgeFill.scanLineComplementationAlgorithm(this.yX);
		
		Set<Point> ps = new HashSet<Point>();
		for(Entry<Integer, Set<Integer>> entry: fillYXs.entrySet()) {
			for(int x : entry.getValue()) {
				ps.add(new Point(x, entry.getKey()));
			}
		}
		return ps;
	}
	
	
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public Set<Point> getEdge() {
		return edge;
	}

	public int getArea() {
		return area;
	}
}
