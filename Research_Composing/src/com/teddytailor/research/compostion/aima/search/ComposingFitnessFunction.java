package com.teddytailor.research.compostion.aima.search;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aima.core.search.local.FitnessFunction;
import aima.core.search.local.Individual;

import com.teddytailor.research.compostion.aima.cache.CacheManager;
import com.teddytailor.research.compostion.aima.data.ComposingBoard;
import com.teddytailor.research.compostion.aima.data.ComposingModel;
import com.teddytailor.research.compostion.aima.data.OrderInteger;

public class ComposingFitnessFunction implements FitnessFunction<Integer> {
	private ComposingBoard board;
	
	private static Map<Integer, Double> VALUE_CACHE = new HashMap<Integer, Double>();//ConcurrentHashMap
	public static Map<Integer, List<Point>> POINT_RESULT = new HashMap<Integer, List<Point>>();
	
	public ComposingFitnessFunction(ComposingBoard board) {
		this.board = board;
	}
	
	
	@Override
	public double getValue(Individual<Integer> individual) {
		List<OrderInteger> ois = ComposingBoard.orderIntegers(individual);
		int OIS_HASHCODE = ois.hashCode();
		
		
		Double value = VALUE_CACHE.get(OIS_HASHCODE);
		if(value != null) {
			return value;
		}
		
		long start = System.currentTimeMillis();
		
		int boardWidth = this.board.getWidth();
		
		List<ComposingModel> cms = this.board.orderModels(ois);
		
		List<ComposingModel> downCms = new ArrayList<ComposingModel>(cms.size());
		int preMaxX = 0;
		for(int i=0,imax=cms.size(); i<imax; i++) {
			ComposingModel cm = cms.get(i);
			if(preMaxX > boardWidth) {
				preMaxX += cm.getCurModel().getWidth();
				continue;
			}
			ComposingModel cmNext = null;
			int iNext = i+1;
			if(iNext < imax) {
				cmNext = cms.get(iNext);
			}
			
			
			int minX;
			
			List<Byte> cacheHashCode = calcHashCode(ois, downCms);
			Point cache = CacheManager.get(cacheHashCode);			
			if(cache == null) {
				cm.pos.y = 0; 
				cm.pos.x = preMaxX;
				
				minX = orderDown(cm, downCms, cmNext, preMaxX);
				cm.pos.x = minX;
				
				CacheManager.put(cacheHashCode, cm.pos);
			}else {
				cm.pos = new Point(cache);
				minX = cm.pos.x;
			}
			downCms.add(cm);
			
			preMaxX = Math.max(preMaxX, minX + cm.getCurModel().getWidth());
		}
		
		double result = boardWidth-preMaxX + ComposingGoalTest.BASE;
		if(result < 0) {
			result = 0;
		}
		
		System.out.println((System.currentTimeMillis()-start)/1000f);
		
		
		//保存位置记录
		List<Point> pointResult = new ArrayList<Point>();
		for(int i=0,imax=downCms.size(); i<imax; i++) {
			ComposingModel cm = downCms.get(i);
			Point pos = new Point(cm.pos);
			pointResult.add(pos);
		}
		downCms = null;
		POINT_RESULT.put(OIS_HASHCODE, pointResult);
		VALUE_CACHE.put(OIS_HASHCODE, result);
		
		return result;
	}
	
	private List<Byte> calcHashCode(List<OrderInteger> ois, List<ComposingModel> downCms) {
		int size = downCms.size()+2; 
		if(size > CacheManager.MAX_CACHE_LIST_SIZE) {
			return null;
		}else if(size > ois.size()) {
			return null;
		}
		
		List<Byte> sb = new ArrayList<Byte>(size);
		for(OrderInteger oi: ois.subList(0, size)) {
			sb.add(oi.toByte());
		}
		return sb;
	}
	
	public int orderDown(ComposingModel cm, List<ComposingModel> downCms, ComposingModel cmNext, int preMaxX) {
		if(cmNext == null) return orderDown(cm, downCms);
		
		int cmW = cm.getCurModel().getWidth();
		
		int boardH = this.board.getHeight();
		int cmH = cm.getCurModel().getHeight();
		int nextH = cmNext.getCurModel().getHeight();
		
		boolean needCareNext = 
				cmH>=boardH/2 && cmH<boardH &&
				nextH>=boardH/2 && nextH<boardH
				;
		if(!needCareNext) return orderDown(cm, downCms);
		
		long start = System.currentTimeMillis();
		
		//生成下个模型的downCms
		List<ComposingModel> nDownCms = new ArrayList<ComposingModel>(downCms.size()+1);
		nDownCms.addAll(downCms);
		nDownCms.add(cm);
		
		//记录 当前 和下个 模型 最合适的 位置
		int tMinx = Integer.MAX_VALUE;
		
		int minX = preMaxX;
		int minY = cm.pos.y;
		int nmx = Integer.MAX_VALUE;
		int nmy = 0;
		
		int cyMax = boardH - cmH;
		for(int cy=0; cy<=cyMax; cy++) {
			for(int cx=preMaxX; cx>=0; cx--) {
				cm.pos.x = cx;
				
				boolean isFind = (cx == 0);
				if(!isFind && isIntersect(cm, downCms, cy)) {//相交的上一个则为边缘
					isFind = true;
					cm.pos.x = cx+1;
				}
				
				if(isFind) {
					cm.pos.y = cy;
					
					int cMaxX = cm.pos.x+cmW;
					cmNext.pos.x = Math.max(preMaxX, cMaxX);
					int tmpNMinX = orderDown(cmNext, nDownCms);
					int nMaxX = tmpNMinX + nextH;
					
					int maxX = Math.max(preMaxX, Math.max(nMaxX, nMaxX));
					if(maxX < tMinx) {
						tMinx = maxX;
						nmx = tmpNMinX;
						nmy = cmNext.pos.y;
						
						//同时，更新当前模型的最小X
						minX = cx+1;
						minY = cy;
					}
					
					break;
				}
			}
		}
				
		nDownCms.clear(); nDownCms=null;
		
		cmNext.pos.x = nmx;
		cmNext.pos.y = nmy;
		
		cm.pos.x = minX;
		cm.pos.y = minY;
		
		System.out.printf("size: %d, use: %s\n", downCms.size(), (System.currentTimeMillis()-start)/1000F);
		
		return minX;
	}
	
	
	public int orderDown(ComposingModel cm, List<ComposingModel> downCms) {
		int minX = cm.pos.x;
		for(int x = minX-1; x>=0; x--) {
			cm.pos.x = x;
			int originY = cm.pos.y;
			if(isIntersect(cm, downCms)) {
				cm.pos.y = originY;
				return minX;
			}
			minX = x;
		}
		return minX;
	}
	
	private final static int[] REGION = {1, -1};
	public boolean isIntersect(ComposingModel cm, List<ComposingModel> downCms) {
		int curY = cm.pos.y;
		boolean isIntersect = isIntersect(cm, downCms, curY);
		if(!isIntersect) return false;
		
		int ly = 0;
		int ry = board.getHeight()-cm.getCurModel().getHeight();
		
		int lyd = curY-ly;
		int ryd = ry-curY;
		int delta = Math.min(lyd, ryd);
		int leaveL, leaveR;
		if(lyd >= ryd) {
			leaveL = ly;
			leaveR = curY - delta - 1;
		}else {
			leaveL = curY + delta + 1;
			leaveR = ry;
		}
		
		for(int dy=1; dy<=delta; dy++) {
			for(int r: REGION) {
				int y = curY + dy*r;
				isIntersect = isIntersect(cm, downCms, y);
				if(!isIntersect) return false;
			}
		}
		
		for(int y=leaveL; y<=leaveR; y++) {
			isIntersect = isIntersect(cm, downCms, y);
			if(!isIntersect) return false;
		}
		
		return true;
	}
	
	public boolean isIntersect(ComposingModel cm, List<ComposingModel> downCms, int y) {
		cm.pos.y = y;
		
		for(ComposingModel dCm: downCms) {
			if(cm.intersect(dCm)) {
				return true;
			}
		}
		return false;
	}
}

