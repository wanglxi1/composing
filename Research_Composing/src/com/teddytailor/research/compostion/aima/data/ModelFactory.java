package com.teddytailor.research.compostion.aima.data;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import com.teddytailor.research.ocr.util.ImageFrame;
import com.teddytailor.research.ocr.util.ImageFrame.ImageProcessHandler;

public class ModelFactory {

	private final static File PARENT = new File("E:\\learn\\技术资料\\排板材料备份\\单件\\edge");
	
	public static void main(String[] args) throws Exception{
		new ImageFrame(new ImageProcessHandler() {
			@Override
			public Image process(File f) throws Exception {
				Model m = read(f);
				Set<Point> ps = m.fillPoint();
				
				
				BufferedImage im = new BufferedImage(m.getWidth(), m.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
				for(Point p: ps) {
					im.setRGB(p.x, p.y, Color.WHITE.getRGB());
				}
				return im;
			}
			
		});
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
