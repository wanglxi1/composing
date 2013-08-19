package com.teddytailor.research.compostion.aima.cache;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.teddytailor.research.compostion.aima.data.ModelFactory;

public class CacheManager {

	public final static int MAX_CACHE_LIST_SIZE = 10;
	
	private final static ByteBuffer STRUCT = ByteBuffer.allocateDirect(1024);
	private final static File FILE = new File(ModelFactory.RESOURCE, "cache");
	private static FileChannel SYNC_CHANNEL = null;
	
	public static boolean CACHE_CONTROL = false;
	
	private static Map<List<Byte>, Point> CACHE = read();
	
	private static Map<List<Byte>, Point> read() {
		CACHE = new HashMap<List<Byte>, Point>();
		
		if(!FILE.exists()) return CACHE;
		
		readFile(FILE);
		
		return CACHE;
	}
	
	public static void main(String[] args) throws Exception{
		File nf = new File(ModelFactory.RESOURCE, "cache1");
		readFile(nf);
		File wf = new File(ModelFactory.RESOURCE, "cachew");
		FileChannel fc = new FileOutputStream(wf, true).getChannel();
		for(Entry<List<Byte>, Point> entry: CACHE.entrySet()) {
			writeFile(fc, entry.getKey(), entry.getValue());
		}
	}

	
	public static Point get(Object hashCode) {
		if(CACHE_CONTROL) return null;
		if(hashCode==null) return null;
		return CACHE.get(hashCode);
	}
	
	public static void put(List<Byte> key, Point p) {
		if(key==null) return;
		if(!CACHE.containsKey(key)) {
			CACHE.put(key, new Point(p));
			sync(key, p);
		}else {
			System.out.printf("ZC: %s \t %s \t %s\n", key, CACHE.get(key), p);
		}
	}
	
	private static void sync(List<Byte> key, Point p) {
		if(SYNC_CHANNEL == null) {
			try {
				SYNC_CHANNEL = new FileOutputStream(FILE, true).getChannel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		writeFile(SYNC_CHANNEL, key, p);
	}
	
	private static void writeFile(FileChannel fc, List<Byte> key, Point p) {
		ByteBuffer buf = STRUCT;
		buf.putInt(p.x)
			.putInt(p.y)
			.putInt(key.size());
		for(byte c: key) {
			buf.put(c);
		}
		buf.flip();
		
		try {
			fc.write(buf);
			buf.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void readFile(File f) {
		try {
			long start = System.currentTimeMillis();
			FileChannel fc = new FileInputStream(f).getChannel();
			ByteBuffer buf = ByteBuffer.allocateDirect(12);
			while(fc.read(buf) != -1) {
				buf.flip();
				int x = buf.getInt();
				int y = buf.getInt();
				
				int len = buf.getInt();
				buf.clear();
				
				if(len > MAX_CACHE_LIST_SIZE) {
					fc.position(fc.position()+len);
				}else {
					ByteBuffer sbuf = ByteBuffer.allocateDirect(len);
					fc.read(sbuf);
					sbuf.flip();
					List<Byte> cs = new ArrayList<Byte>(len);
					for(int i=0;i<len;i++) {
						cs.add( sbuf.get() );
					}
					
					Point p = new Point(x, y);
					if(CACHE.containsKey(cs)) {
						System.out.printf("CF: %s \t %s \t %s\n", cs, CACHE.get(cs), p);
					}else {
						CACHE.put(cs, p);
					}
					
					sbuf = null;
				}
			}
			
			System.out.printf("use %s, read %s\n", System.currentTimeMillis()-start, CACHE.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
