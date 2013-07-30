package com.teddytailor.research.compostion.aima.data;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Set;

import org.junit.Assert;

import com.teddytailor.research.ocr.util.ImageFrame;
import com.teddytailor.research.ocr.util.ImageFrame.ImageProcessHandler;

public class ModelTest {
	
	public static void main(String[] args) throws Exception {
		testReversal();
	}
	
	public static void testReversal() throws Exception{
		new ImageFrame(new ImageProcessHandler() {
			@Override
			public Image process(File f) throws Exception {
				Model m = ModelFactory.readImg(f).reversal();
				Set<Point> ps = m.fillPoint();
				
				
				BufferedImage im = new BufferedImage(m.getWidth(), m.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
				for(Point p: ps) {
					im.setRGB(p.x, p.y, Color.WHITE.getRGB());
				}
				return im;
			}
		});
	}
	
	
	
	public void testIntersect() throws Exception{
		File p = new File("E:\\learn\\技术资料\\排板材料备份\\单件\\edge");
		File f1 = new File(p, "鼻梁_1.jpg");
		File f2 = new File(p, "尾巴_1.bmp");
		
		Model m1 = ModelFactory.readImg(f1);
		Model m2 = ModelFactory.readImg(f2);
		
		//肯定相交
		Point offset1 = new Point(0, 0);
		Assert.assertEquals(m1.intersect(m2, offset1), true);
		
		//肯定不相交
		Point offset2 = new Point(m1.getWidth(), m1.getHeight());
		Assert.assertEquals(m1.intersect(m2, offset2), false);
		
		//差点不相交
		Point offset3 = new Point(m1.getWidth()-1, 0);
		Assert.assertEquals(m1.intersect(m2, offset3), true);
		
		//差点相交
		Point offset4 = new Point(m1.getWidth(), 0);
		Assert.assertEquals(m1.intersect(m2, offset4), false);
		
		//中心相交
		Point offset5 = new Point(m1.getWidth()/2, m1.getHeight()/2);
		Assert.assertEquals(m1.intersect(m2, offset5), true);
	}
}
