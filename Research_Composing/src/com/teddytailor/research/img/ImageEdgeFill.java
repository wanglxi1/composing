package com.teddytailor.research.img;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ImageEdgeFill {
	
	
	/**
	 * 扫描线求余（反）算法
	 * 
	 * 其基本思想是，偶数次求余还是本身，奇数次就是要改变的结果
	 * 
	 * @param lineYXs 每行的边缘点
	 * @return
	 */
	public static Map<Integer, Set<Integer>> scanLineComplementationAlgorithm(Map<Integer, List<Integer>> lineYXs){
		Map<Integer, Set<Integer>> fillYXs = new HashMap<Integer, Set<Integer>>();
		
		for(int y: lineYXs.keySet()) {
			List<Integer> xs = lineYXs.get(y);
			if(xs==null || xs.size()==0) continue;
			Collections.sort(xs);
			
			List<Integer[]> distXs = new ArrayList<Integer[]>();
			Integer prefix=null, startX=null;
			for(int x: xs) {
				if(prefix != null) { 	//获取之间有间隔的点
					int dist = x - prefix;
					if(dist == 1) {
					}else {
						if(isDingDian(lineYXs, y, startX, prefix)) {
							int size = distXs.size(); 
							if(size > 0) {
								distXs.get(size-1)[1] = x;
							} 
						}else {
							distXs.add(new Integer[] {prefix, x});
						}
						startX = x;
					}
				}else {
					startX = x;
				}
				prefix = x;
			}
			
			
			Set<Integer> fillXs = new HashSet<Integer>();
			fillXs.addAll(xs);
			
			
			int distXsSize = distXs.size();			
			for(int i=0,imax=distXsSize; i<imax; i++) {
				if(i%2 == 0) {
					Integer[] dxa = distXs.get(i);
					for(int xi=dxa[0]+1, ximax=dxa[1]; xi<ximax; xi++) {
						fillXs.add(xi);
					}
				}
			}
			
			fillYXs.put(y, fillXs);
		}
		
		return fillYXs;
	}
	
	private static boolean isDingDian(Map<Integer, List<Integer>> lineYXs, int y, int x1, int x2) {
		List<Integer> preXs = lineYXs.get(y-1);
		List<Integer> nextXs = lineYXs.get(y+1);
		
		int preHas1 = hasSibling(preXs, x1, true);
		int preHas2 = hasSibling(preXs, x2, false);
		int nextHas1 = hasSibling(nextXs, x1, true);
		int nextHas2 = hasSibling(nextXs, x2, false);
		
		int preCount =  preHas1 + preHas2;
		int nextCount = nextHas1 + nextHas2;
		
		boolean isDingDian = preCount==0 || nextCount==0;
		if(!isDingDian) {
			if(Math.abs(nextCount*nextCount - preCount*preCount) >= 142) {
				isDingDian = true;
			}
		}
		
		return isDingDian;
	}
	
	private static int hasSibling(List<Integer> xs, int x, boolean rightOrder) {
		if(xs == null) return 0;
		
		int count = 0;
		for(int i=-1; i<=1; i++) {
			if(xs.contains(x+i)) {
				int shift = i+1;
				if(rightOrder) shift = 2 - shift;
				count+= 1<<shift;
			}
		}
		
		return count;
	}	
}

class MyFillAlgorithm{
	
	private int width;
//	private int height;

	private Map<Integer, List<Integer>> yX;
	
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
		if(lineXMap.containsKey(y)) return lineXMap.get(y);
		
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
					
					if(preXs == null) {
						needAdd = y>0;
					} else {
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
					}
				}else {//使用三个采样点，获取在上面一行的邻居
					int[] checkXs = new int[] {-1, -1, -1};
					checkXs[0] = x1 + dist/3;
					checkXs[1] = x1 + dist/2;
					checkXs[2] = x1 + dist*2/3;
					
					
					for(int checkX: checkXs) {
						if(checkX != -1) {
							int preY = y-1;
							
							Collection<Integer> preEdgXs = lineXMap.get(-preY);
							if(preEdgXs.contains(checkX)) {
								int prePreY = preY-1;
								Collection<Integer> prePreXs = lineXMap.get(prePreY);
								Collection<Integer> prePreEdgXs = lineXMap.get(-prePreY);
								if(prePreXs!=null && !prePreEdgXs.contains(checkX) && prePreXs.contains(checkX)) {
									needAdd = false;
									break;
								}
							}else {
								if(!hasPureSibling(lineXMap, preY, checkX)) {
									needAdd = false;
									break;
								}
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
				if(x1 == x2) { //找出下一行没有证据的点，并试图从上方找出证据
					int checkX = -1;
					if(!nextHas1) {
						for(int x=x1-1; x>=0; x--) {
							if(!preXs.contains(x)) {
								checkX = x;
								break;
							}
						}
					}else {
						for(int x=x2+1; x<this.width; x++) {
							if(!preXs.contains(x)) {
								checkX = x;
								break;
							}
						}
					}
					
					List<Integer> preFillXs = lineXMap.get(y-1);
					List<Integer> curXs = this.yX.get(y);
					return preFillXs==null || (preFillXs.contains(checkX) ^ curXs.contains(checkX)); //需要考虑当前边缘的阻隔
				} else { //多于一个的点
					int checkX = (x1+x2)/2;
					List<Integer> preFillXs = lineXMap.get(y-1);
					return preFillXs==null || !preFillXs.contains(checkX);
				}
//				else {
//					Collection<Integer> nextLineXs = fillLine(y+1, lineXMap);
//					return hasSibling(nextLineXs, x1) && hasSibling(nextLineXs, x2) && hasSibling(nextLineXs, (x1+x2)/2);
//				}
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
}
