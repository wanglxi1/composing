package com.teddytailor.research.compostion.aima.data;

import java.awt.Point;
import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class ModelTest {
	
	@Test
	public void testIntersect() throws Exception{
		File p = new File("E:\\learn\\技术资料\\排板材料备份\\单件\\edge");
		File f1 = new File(p, "鼻梁_1.jpg");
		File f2 = new File(p, "尾巴_1.bmp");
		
		Model m1 = ModelFactory.read(f1);
		Model m2 = ModelFactory.read(f2);
		
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
