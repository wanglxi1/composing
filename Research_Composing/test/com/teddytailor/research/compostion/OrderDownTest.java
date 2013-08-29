package com.teddytailor.research.compostion;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import aima.core.search.local.Individual;

import com.Main;
import com.teddytailor.research.compostion.aima.cache.CacheManager;
import com.teddytailor.research.compostion.aima.data.ComposingBoard;
import com.teddytailor.research.compostion.aima.data.ComposingModel;
import com.teddytailor.research.compostion.aima.data.OrderInteger;
import com.teddytailor.research.compostion.aima.search.ComposingFitnessFunction;

public class OrderDownTest {
	static {CacheManager.CACHE_CONTROL = false;}
	
	public static void showImage(BufferedImage img, String name) throws Exception {
		javax.imageio.ImageIO.write(img, "png", new java.io.File("e:/"+name+".png"));
	}
	
	public static void showImage(BufferedImage img) throws Exception {
//		new com.teddytailor.research.ocr.util.ImageFrame().show(img);
	}
	
	public static void main(String[] args) throws Exception{
		manTest();
//		showOrderName();
//		drawFoot();
	}
	
	public static void drawFoot()  throws Exception{
		int[] orders = {
				50, 	30, 					//侧咀_右, 	侧咀_左,
				260, 					//侧脸, 
				180, -290, 				//内胳_右, 	内胳_左, 
				140, 60, 					//堵头, 		堵头, 
				160, 150, 				//外胳_右, 	外胳_左, 
				190, 					//尾巴, 
				191, 40, 					//手掌_右, 	手掌_左, 
				210, 170,	90, -80, 			//耳, 		耳, 			耳, 			耳, 
				70, 	200, -280, 			//脑_中, 	脑_后_右, 	脑_后_左, 
				1,-1, -2, 2, 		//脚_右, 	脚_右, 		脚_左, 		脚_左, 
				-120, -20, 				//脚掌, 		脚掌, 
				20, 30, 				//身体_右, 	身体_左, 
				171						//鼻梁, 
		};
		
		int len = 28;
//		len = 6;
		
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
		
		show(individual, cms);
	}
	
	/*
	 * 2	侧咀_右, 侧咀_左,			1	2
	 * 1	侧脸, 						3
	 * 2	内胳_右, 内胳_左, 			4	5
	 * 2	堵头, 堵头, 					6	7
	 * 2	外胳_右, 外胳_左, 			8	9
	 * 1	尾巴, 						10
	 * 2	手掌_右, 手掌_左, 			11	12
	 * 4	耳, 耳, 耳, 耳, 				13	14	15	16
	 * 3	脑_中, 脑_后_右, 脑_后_左, 	17	18	19
	 * 4	脚_右, 脚_右, 脚_左, 脚_左, 	20	21	22	23
	 * 2	脚掌, 脚掌, 					24	25
	 * 2	身体_右, 身体_左, 			26	27
	 * 1	鼻梁, 						28
	 */
	protected static void manTest() throws Exception{
		ComposingFitnessFunction.CARE_ALL_NEXT = false;
		CacheManager.CACHE_CONTROL = false;
		
		Main.ORDER_DOWN_MODEL = 1;
		boolean oldVer = false;
		
		//[9998.0] 20, -21, 14, -4, -25, 19, 5, -6, 16, -22, 23, -2, -18, -11, -1, 12, -26, -7, 15, -3, -13, 8, 28, -9, -10, -17, 24, -27
		//[10011.0] [28, 20, -21, 14, -4, -25, 19, 5, -6, 16, -22, 23, -2, -18, -11, -1, 12, -26, -7, 15, -3, -13, 8, -9, -10, 17, 24, 27]
		int[] orderInts = {
			28,20, -21, 14, -4, -25, 19, 5, 
			-6, 16, -22, 23, -2, -18, -11, 
			-1, 12, -26, -7, 15, -3, -13, 
			8, -9, -10, 17, 24,   27
		};
		List<OrderInteger> oils = new ArrayList<OrderInteger>();
		for(int i=0,imax=orderInts.length;i<imax;i++) {
			int oi = orderInts[i];
			int aoi = Math.abs(oi) + (oldVer? 0: -1);
			int order = (i+1)*(oi<0? -1: 1);
			oils.add(OrderInteger.valueOf(aoi, order));
		}
		System.out.println(oils.size() + oils.toString());
		
		Collections.sort(oils, new Comparator<OrderInteger>() {
			@Override public int compare(OrderInteger o1, OrderInteger o2) {
				return Integer.valueOf(o1.origin).compareTo(o2.origin);
		}});
		
		List<Integer> ils = new ArrayList<Integer>(orderInts.length);
		for(OrderInteger oi: oils) {
			ils.add(oi.toInt());
		}
		
		System.out.println(ils.size() + ils.toString());
				
		Individual<Integer> individual = new Individual<Integer>(ils);
		List<ComposingModel> cms = Main.buildModels();
		
		show(individual, cms, true);
	}
	
	
	protected static void showOrderName() throws Exception{
		List<ComposingModel> cms = Main.buildModels();
		for(ComposingModel cm: cms) {
			System.out.println(cm.name+", " + cm.getCurModel().getHeight());
		}
	}
	
	
	protected static void mock() throws Exception{
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
		
		show(individual, cms);
	}

	private static void show(Individual<Integer> individual, List<ComposingModel> cms) throws Exception {
		show(individual, cms, false);
	}
	private static void show(Individual<Integer> individual, List<ComposingModel> cms, boolean showName) throws Exception {
		ComposingBoard board = new ComposingBoard(cms, 2564, 437);
		ComposingFitnessFunction cff = new ComposingFitnessFunction(board);
		double value = cff.getValue(individual);
		
		System.out.println(value);
		
		BufferedImage img = board.draw(individual);
		if(showName) {
			List<OrderInteger> ois = ComposingBoard.orderIntegers(individual);
			showImage(img, String.valueOf(value + ois.toString()));
		}else {
			showImage(img);
		}
		
	}
	
}
