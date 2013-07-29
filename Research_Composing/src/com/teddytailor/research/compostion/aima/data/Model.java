package com.teddytailor.research.compostion.aima.data;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	public boolean intersect(Model model, Point offset) {
		Rectangle r1 = new Rectangle(this.getWidth(), this.getHeight());
		Rectangle r2 = new Rectangle(offset.x, offset.y, model.getWidth(), model.getHeight());
		
		Rectangle ir = r1.intersection(r2);
		if(ir.isEmpty()) return false;
		
		if(ir.width>=r1.width || ir.width>=r2.width 
				|| ir.height>=r1.height || ir.height>=r2.height
		) {
			return true;
		}
		
		boolean intersect = false;
		for(int y=ir.y,ymax=ir.y+ir.height; y<ymax; y++) {
			List<Integer> xs2 = model.yX.get(y-offset.y);
			List<Integer> xs1 = this.yX.get(y);
			
			for(int x: xs2) {
				int ox = x + offset.x;
				if(ox>=ir.x && ox<=ir.getMaxX()) {
					
					if(xs1.contains(ox)) {
						intersect = true;
						break;
					}
				}
			}
		}
		
		return intersect;
	}
	
	public Set<Point> fillPoint(){
		Set<Point> ps = new HashSet<Point>();
		
		List<Integer> xs = new ArrayList<Integer>(this.yX.keySet());
		Collections.sort(xs);
		
		Map<Integer, List<Integer>> lineXMap = new HashMap<Integer, List<Integer>>();
		
		for(int y: xs) {
			Collection<Integer> lineXs;
			
			if(lineXMap.containsKey(y)) {
				lineXs = lineXMap.get(y);
			}else {
				Collection<Integer> lps = fillLine(y, lineXMap);
				lineXs = lps;
				lineXMap.put(y, new ArrayList<Integer>(lps));
			}
			
			for(int x: lineXs) {
				ps.add(new Point(x, y));
			}
		}
		
		return ps;
	}
	
	private Collection<Integer> fillLine(int y, Map<Integer, List<Integer>> lineXMap){
		Set<Integer> ps = new HashSet<Integer>();
		List<Integer> xs = this.yX.get(y);
		if(xs == null) return ps;
		if(lineXMap.containsKey(yX)) return lineXMap.get(y);
		
		Collections.sort(xs);
		lineXMap.put(-y, xs);
		
		List<Integer[]> distXs = new ArrayList<Integer[]>();
		Integer prefix = null;
		Integer startX = null; 
		
		for(int x: xs) {
			ps.add(x);
			
			if(prefix == null) {
				startX = x;
			}else {
				//获取之间有间隔的点
				int dist = x - prefix;
				if(dist == 1) {
				}else {
					boolean needAdd = false;
					
					if(hasSibling(y, prefix, startX, lineXMap)) {
						needAdd = true;
					}
					
					if(needAdd) {
						distXs.add(new Integer[] {prefix, x});
						startX = x;
					}else {
						if(distXs.size() > 0) {
							distXs.remove(distXs.size()-1);
						}
					}
				}
			}
			prefix = x;
		}
		
		if(!hasSibling(y, prefix, startX, lineXMap)) {
			if(distXs.size() > 0) {
				distXs.remove(distXs.size()-1);
			}
		}
		
		
//		System.out.printf(y+":\t" +xs + "\t{");
//		for(Integer[] is: distXs) {
//			System.out.print(is[0] +"-"+ is[1] + ", ");
//		}
//		System.out.println("}");
		
		int len = distXs.size();
		int front = 0;
		int after = len - 1;
		
		for(int index: new int[]{after, front}) {
			if(len-- <=0) break;
			
			Integer[] is = distXs.remove(index);
			
			int x1 = is[0];
			int x2 = is[1];
			for(int i=x1+1; i<=x2-1; i++) {
				ps.add(i);
			}
		}
		if(len > 0) {
			for(Integer[] is: distXs) {
				int x1 = is[0];
				int x2 = is[1];
				
				boolean needAdd = true;
				
				int dist = x2 - x1 - 1;
				if(dist < 3) { 
					Collection<Integer> preXs = lineXMap.get(y-1);
					Collection<Integer> preEdgeXs = lineXMap.get(-(y-1));
					
					boolean isUpright = false;
					for(int i=x1+1; i<x2; i++) {
						if(preEdgeXs.contains(i)) { //上方有边界点则为正三角
							isUpright = true;
							break;
						}
					}
					
					if(isUpright) { //正三角: 两侧点有，则就三角区域内没有
						if(preXs.contains(x1) || preXs.contains(x2)) {
							needAdd = false;
						}
					}else {// 倒三角
						if(!preXs.contains(x1) && !preXs.contains(x2)) {
							needAdd = false;
						}
					}
				}else {//使用三个采样点，获取在上面一行的邻居
					int[] checkXs = new int[] {-1, -1, -1};
					checkXs[0] = x1 + dist/3;
					checkXs[1] = x1 + dist/2;
					checkXs[2] = x1 + dist*2/3;
					
					
					for(int checkX: checkXs) {
						if(checkX != -1) {
							if(!hasPureSibling(lineXMap, y-1, checkX)) {
								needAdd = false;
								break;
							}
						}
					}
				}
				
				if(needAdd) {
					for(int i=x1+1; i<=x2-1; i++) {
						ps.add(i);
					}
				}
			}			
		}
		
		return ps;
	}
	
	private boolean hasSibling(int y, int x1, int x2, Map<Integer, List<Integer>> lineXMap) {
		List<Integer> preXs = this.yX.get(y-1);
		List<Integer> nextXs = this.yX.get(y+1);
		
		boolean preHas1 = hasSibling(preXs, x1);
		boolean preHas2 = hasSibling(preXs, x2);
		boolean nextHas1 = hasSibling(nextXs, x1);
		boolean nextHas2 = hasSibling(nextXs, x2);
		
		boolean has =  (nextHas1||nextHas2) && (preHas1 || preHas2);  //(preHas1 || nextHas1) && (preHas2 || nextHas2); 
		if(!has) {
			if((preHas1 && preHas2) && !(nextHas1 && nextHas2)) { //处理 凹弧的切线
//				System.out.print("->\t");
				Collection<Integer> nextLineXs = fillLine(y+1, lineXMap);
				return hasSibling(nextLineXs, x1) && hasSibling(nextLineXs, x2) && hasSibling(nextLineXs, (x1+x2)/2);
			}else if(!(preHas1 && preHas2) && (nextHas1 && nextHas2)) { //处理 凸弧的外切线
				Collection<Integer> lineXs = fillLine(y-1, lineXMap);
				return hasSibling(lineXs, x1) && hasSibling(lineXs, x2) && hasSibling(lineXs, (x1+x2)/2);
			}
		}else {
			
		}
		
		return has;
	}
	
	
	private boolean hasSibling(Collection<Integer> xs, int x) {
		if(xs == null) return false;
		return xs.contains(x) || xs.contains(x-1) || xs.contains(x+1);
	}
	
	private boolean hasPureSibling(Map<Integer, List<Integer>> lineXMap, int y, int x) {
		Collection<Integer> xs = lineXMap.get(y); 
		Collection<Integer> keyXs = lineXMap.get(-y);
		if(xs == null) return false;
		
		for(int i=-1; i<=1; i++) {
			if(!keyXs.contains(i+x) && xs.contains(i+x)) {
				return true;
			}
		}
		return false;
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
