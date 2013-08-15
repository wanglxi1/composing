package com.teddytailor.research.compostion.aima.cache;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teddytailor.research.compostion.aima.data.ModelFactory;

public class CacheManager {

	private final static ByteBuffer STRUCT = ByteBuffer.allocateDirect(1024);
	private final static File FILE = new File(ModelFactory.RESOURCE, "cache");
	private static FileChannel SYNC_CHANNEL = null;
	
	public static boolean CACHE_CONTROL = true;
	
	private static Map<List<Integer>, Point> CACHE = read();
	
	private static Map<List<Integer>, Point> read() {
		CACHE = new HashMap<List<Integer>, Point>();
		
		if(!FILE.exists()) return CACHE;
		
		try {
			FileChannel fc = new FileInputStream(FILE).getChannel();
			ByteBuffer buf = ByteBuffer.allocateDirect(12);
			while(fc.read(buf) != -1) {
				buf.flip();
				int x = buf.getInt();
				int y = buf.getInt();
				
				int len = buf.getInt();
				buf.clear();
				
				ByteBuffer sbuf = ByteBuffer.allocateDirect(len);
				fc.read(sbuf);
				sbuf.flip();
				List<Integer> cs = new ArrayList<Integer>(len);
				for(int i=0;i<len;i++) {
					cs.add( (int)sbuf.get() );
				}
				CACHE.put(cs, new Point(x, y));
				sbuf = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return CACHE;
	}
	
	public static Point get(Object hashCode) {
		if(CACHE_CONTROL) return null;
		return CACHE.get(hashCode);
	}
	
	public static void put(List<Integer> key, Point p) {
		if(!CACHE.containsKey(key)) {
			CACHE.put(key, new Point(p));
			sync(key, p);
		}
	}
	
	private static void sync(List<Integer> key, Point p) {
		if(SYNC_CHANNEL == null) {
			try {
				SYNC_CHANNEL = new FileOutputStream(FILE).getChannel();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		ByteBuffer buf = STRUCT;
		buf.putInt(p.x)
			.putInt(p.y)
			.putInt(key.size());
		for(int c: key) {
			buf.put((byte)c);
		}
		buf.flip();
		
		try {
			SYNC_CHANNEL.write(buf);
			buf.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
