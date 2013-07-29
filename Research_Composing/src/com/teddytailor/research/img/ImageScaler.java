package com.teddytailor.research.img;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import com.teddytailor.research.img.ImageBat.ImageProcesser;

public class ImageScaler implements ImageProcesser {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		File p = new File("E:\\learn\\技术资料\\排板材料备份\\单件\\JPEG");
		ImageBat bat = new ImageBat(p, new ImageScaler());
		bat.run();
	}
	
	private final static int SPLIT = 10;
	private final static int RATE = 6;
	
	@Override
	public Image process(Image src) {
		BufferedImage img = (BufferedImage)src;
		
		int w = img.getWidth();
		int h = img.getHeight();	
		
		int nw = w/RATE + SPLIT*2;
		int nh = h/RATE + SPLIT*2;
		
		BufferedImage newImg = new BufferedImage(nw, nh, BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = newImg.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, nw, nh);
		g.drawImage(img, SPLIT, SPLIT, nw-SPLIT*2, nh-SPLIT*2, Color.WHITE, null);
		
		return newImg;
	}
	
	@Override
	public void save(Image dst, Image src, File f) throws Exception {
		File dstF = new File(f.getParentFile().getParentFile(), "scale/"+RATE+"/"+f.getName());
		ImageBat.saveImg(dst, "bmp", dstF);
	}
}
