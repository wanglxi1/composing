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

public class OrderDownTest {
	
	public static void showImage(BufferedImage img) throws Exception {
//		new com.teddytailor.research.ocr.util.ImageFrame().show(img);
		javax.imageio.ImageIO.write(img, "png", new java.io.File("e:/1.png"));
	}
	
	public static void main(String[] args) throws Exception{
		manTest();
//		showOrderName();
//		drawFoot();
	}
	
	public static void drawFoot()  throws Exception{
		int[] orders = {
				5, 	3, 					//侧咀_右, 	侧咀_左,
				260, 					//侧脸, 
				180, -290, 				//内胳_右, 	内胳_左, 
				140, 6, 					//堵头, 		堵头, 
				160, 150, 				//外胳_右, 	外胳_左, 
				190, 					//尾巴, 
				191, 4, 					//手掌_右, 	手掌_左, 
				210, 170,	9, -8, 			//耳, 		耳, 			耳, 			耳, 
				7, 	200, -280, 			//脑_中, 	脑_后_右, 	脑_后_左, 
				1,-1, -2, 2, 		//脚_右, 	脚_右, 		脚_左, 		脚_左, 
				-120, -20, 				//脚掌, 		脚掌, 
				-110, 10, 				//身体_右, 	身体_左, 
				171						//鼻梁, 
		};
		
		int len = 28;
//		len = 5;
		
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
	 * 2	侧咀_右, 侧咀_左,
	 * 1	侧脸, 
	 * 2	内胳_右, 内胳_左, 
	 * 2	堵头, 堵头, 
	 * 2	外胳_右, 外胳_左, 
	 * 1	尾巴, 
	 * 2	手掌_右, 手掌_左, 
	 * 4	耳, 耳, 耳, 耳, 
	 * 3	脑_中, 脑_后_右, 脑_后_左, 
	 * 4	脚_右, 脚_右, 脚_左, 脚_左, 
	 * 2	脚掌, 脚掌, 
	 * 2	身体_右, 身体_左, 
	 * 1	鼻梁, 
	 */
	protected static void manTest() throws Exception{
		int[] orders = {
				5, 	3, 					//侧咀_右, 	侧咀_左,
				260, 					//侧脸, 
				180, -290, 				//内胳_右, 	内胳_左, 
				140, 6, 					//堵头, 		堵头, 
				160, 150, 				//外胳_右, 	外胳_左, 
				190, 					//尾巴, 
				191, 4, 					//手掌_右, 	手掌_左, 
				210, 170,	9, -8, 			//耳, 		耳, 			耳, 			耳, 
				7, 	200, -280, 			//脑_中, 	脑_后_右, 	脑_后_左, 
				330,-330, -340, 340, 		//脚_右, 	脚_右, 		脚_左, 		脚_左, 
				-120, -2, 				//脚掌, 		脚掌, 
				-110, 1, 				//身体_右, 	身体_左, 
				171						//鼻梁, 
		};
		
		List<Integer> is = new ArrayList<Integer>();
		for(int i=0,imax=orders.length;i<imax;i++) {
			is.add(orders[i]);
		}
		Individual<Integer> individual = new Individual<Integer>(is);
		List<ComposingModel> cms = Main.buildModels();
		
		show(individual, cms);
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
		CacheManager.CACHE_CONTROL = true;
		
		ComposingBoard board = new ComposingBoard(cms, 2564, 436);
		ComposingFitnessFunction cff = new ComposingFitnessFunction(board);
		double value = cff.getValue(individual);
		
		System.out.println(value);
		
		BufferedImage img = board.draw(individual);
		showImage(img);
	}
}
