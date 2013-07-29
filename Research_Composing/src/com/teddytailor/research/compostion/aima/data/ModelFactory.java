package com.teddytailor.research.compostion.aima.data;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ModelFactory {

	private final static File PARENT = new File("E:\\learn\\技术资料\\排板材料备份\\单件\\edge");
	
	public static void main(String[] args) throws Exception{
		
	}
	
	public static Model read(File f) throws Exception {
		List<Point> ps = new ArrayList<Point>();
		
		BufferedImage img = ImageIO.read(f);
		for(int x=0,xlen=img.getWidth(); x<xlen; x++) {
			for(int y=0,ylen=img.getHeight(); y<ylen; y++) {
				int color = img.getRGB(x, y);
				boolean isFlag = (color&0xFF) == 0;
				if(isFlag) {
					ps.add(new Point(x, y));
				}
			}
		}
		return new Model(ps);
	}
	
}
