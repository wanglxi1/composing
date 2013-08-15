package com.teddytailor.research.compostion.aima.cache;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class CacheManagerTester {
	
	private final static Map<String, Point> DATA = new HashMap<String, Point>();
	static {
		DATA.put(Integer.MAX_VALUE+"", new Point(Integer.MAX_VALUE, Integer.MAX_VALUE));
		DATA.put(Integer.MIN_VALUE+"", new Point(Integer.MIN_VALUE, Integer.MIN_VALUE));
		DATA.put(0+"", new Point(0, 0));
	}
	
	static String ZERO = "0";
	
	@Test
	public void testInit() {
		Assert.assertEquals(CacheManager.get(ZERO), DATA.get(0));
	}
	
	
	public void testWrite() {
		for(String i: DATA.keySet()) {
			CacheManager.put(null, DATA.get(i));
		}
		Assert.assertEquals(CacheManager.get(ZERO), DATA.get(0));
	}
	
}
