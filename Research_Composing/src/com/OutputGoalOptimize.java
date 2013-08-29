package com;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import aima.core.search.local.Individual;

import com.Main;
import com.teddytailor.research.compostion.aima.cache.CacheManager;
import com.teddytailor.research.compostion.aima.data.ComposingBoard;
import com.teddytailor.research.compostion.aima.data.ComposingModel;
import com.teddytailor.research.compostion.aima.data.OrderInteger;
import com.teddytailor.research.compostion.aima.search.ComposingFitnessFunction;

public class OutputGoalOptimize {
	private final static File RESOURCE = new File("E:\\Download\\金山快盘\\资料\\teddytailor\\排板材料备份\\单件\\png");	
	
	public static int COMPOSING_BOARD_WIDTH = 0;
	public static int COMPOSING_BOARD_HEIGHT = 0;
	
	
	public static void main(String[] args) throws Exception{
		ComposingFitnessFunction.CARE_ALL_NEXT = false;
		CacheManager.CACHE_CONTROL = false;
		
		Main.ORDER_DOWN_MODEL = 1;
		boolean oldVer = false;
		
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
		
		show(individual, cms);
	}
	
	public static void showImage(BufferedImage img, String name) throws Exception {
		javax.imageio.ImageIO.write(img, "png", new java.io.File("e:/"+name+".png"));
	}
	
	private static void show(Individual<Integer> individual, List<ComposingModel> cms) throws Exception {
		List<ImgModel> imgs = buildModels();
		
		ComposingBoard board = new ComposingBoard(cms, Main.COMPOSING_BOARD_WIDTH, Main.COMPOSING_BOARD_HEIGHT+1);
		ComposingFitnessFunction cff = new ComposingFitnessFunction(board);
		double value = cff.getValue(individual);
		System.out.println(value);
		
		cms = board.orderPosModels(individual);
		
		double rate = COMPOSING_BOARD_HEIGHT/Main.COMPOSING_BOARD_HEIGHT;
		
		BufferedImage img = new BufferedImage(COMPOSING_BOARD_WIDTH, COMPOSING_BOARD_HEIGHT, BufferedImage.TYPE_BYTE_BINARY);
		Graphics g = img.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		for(ComposingModel cm: cms) {
			Point pos = cm.pos;
			boolean reversal = cm.reversal;
			
			BufferedImage modelImg = imgs.get(cm.origin).imgs.get(reversal);
			
			int x = (int) (pos.x * rate);
			int y = (int) (pos.y * rate);
			
//			System.out.println(pos.x + " " + pos.y + " | " + x + " " + y);
			
			g.drawImage(modelImg, x, y, null);
		}
		List<OrderInteger> ois = ComposingBoard.orderIntegers(individual);
		showImage(img, String.valueOf(value + ois.toString()));
	}
	
	
	public static List<ImgModel> buildModels() throws Exception {
		List<ImgModel> models = new ArrayList<ImgModel>();
		
		for(File f: RESOURCE.listFiles()) {
			BufferedImage img = ImageIO.read(f);
			
			String fileName = f.getName();
			int len = fileName.length();
			
			String modelName = fileName.substring(0, len-6);
			String numStr = fileName.substring(len-5, len-4);
			int num = Integer.valueOf(numStr);
			
			if("鼻梁".equals(modelName)) {
				COMPOSING_BOARD_WIDTH = img.getHeight() * 144 / 5;
			}
			
			ImgModel m = new ImgModel(img);
			for(int i=0; i<num; i++) {
				models.add(m);
			}
			
			COMPOSING_BOARD_HEIGHT = Math.max(COMPOSING_BOARD_HEIGHT, img.getHeight());
		}
		
		return models;
	}

	
	static class ImgModel{
		
		public Map<Boolean, BufferedImage> imgs;
		public ImgModel(BufferedImage img) {
			imgs = new HashMap<Boolean, BufferedImage>(2);
			
			int w = img.getWidth();
			int h = img.getHeight();
			
			BufferedImage rimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			BufferedImage aimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			
			Graphics g = rimg.getGraphics();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, w, h);
			
			g = aimg.getGraphics();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, w, h);
			
			
			int white = 0x00FFFFFF;
			
			for(int x=0; x<w; x++) {
				for(int y=0; y<h; y++) {
					int rgb = img.getRGB(x, y);
					if((rgb & 0xFF000000) == 0) {
						int nx = w-1-x;
						int ny = h-1-y;
						rimg.setRGB(nx, ny, white);
						aimg.setRGB(x, y, white);
					}
				}
			}
			
			imgs.put(false, aimg);
			imgs.put(true, rimg);
		}
		
	}
}
