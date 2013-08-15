package com.teddytailor.research.compostion;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import aima.core.search.local.Individual;

import com.Main;
import com.teddytailor.research.compostion.aima.cache.CacheManager;
import com.teddytailor.research.compostion.aima.data.ComposingBoard;
import com.teddytailor.research.compostion.aima.data.ComposingModel;
import com.teddytailor.research.compostion.aima.data.OrderInteger;
import com.teddytailor.research.compostion.aima.search.ComposingFitnessFunction;
import com.teddytailor.research.ocr.util.ImageFrame;

public class OrderDownTest {
	
	public static void main(String[] args) throws Exception{
		mock();
	}
	
	
	public static void mock() throws Exception{
		CacheManager.CACHE_CONTROL = false;
		
		int[] orders = {
				68, -76, -76, 60, -34, 53, 13, -59, -62, -3, -67, -59, -32, 21, -31, -24, 35, -46, 73, -25, 22, -14, -55, -91, -22, 94, -42, 41
		};
		int len = 28;
		len = 4;
		
		List<Integer> is = new ArrayList<Integer>();
		for(int i=0,imax=orders.length;i<imax;i++) {
			is.add(orders[i]);
		}
		Individual<Integer> individual = new Individual<Integer>(is);
		List<OrderInteger> ois = ComposingBoard.orderIntegers(individual);
		
		
		List<ComposingModel> cms = Main.buildModels();
		if(len != orders.length) {
			ois = ois.subList(0, len);
			
			List<ComposingModel> ncms = new ArrayList<ComposingModel>(len);
			List<Integer> nis = new ArrayList<Integer>(len);
			for(OrderInteger oi: ois) {
				nis.add(oi.toInt());
				ncms.add(cms.get(oi.origin));
				
			}
			
			individual = new Individual<Integer>(nis);
			cms = ncms;
		}
		
		ComposingBoard board = new ComposingBoard(cms, 2534, 463);
		ComposingFitnessFunction cff = new ComposingFitnessFunction(board);
		cff.getValue(individual);
		
		System.out.println(cms.get(cms.size()-1).pos);
		
		BufferedImage img = board.draw(individual);
		new ImageFrame().show(img);
	}
}
