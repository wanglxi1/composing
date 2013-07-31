package com.teddytailor.research.compostion.aima.data;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ModelFactory {

	public final static File RESOURCE = new File(new File(ClassLoader.getSystemResource(".").getFile()).getParentFile(), "resource");
	
	public static void main(String[] args) throws Exception{
		File src = new File(RESOURCE, "img");
		File dst = new File(RESOURCE, "dat");
		buildData(src, dst);
	}
	
	public static Model readImg(File f) throws Exception {
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
	
	public static void buildData(File srcParent, File dstParent) throws Exception{
		dstParent.mkdirs();
		
		for(File img: srcParent.listFiles()) {
			Model m = readImg(img);
			
			String originName = img.getName();
			String dstName = originName.substring(0, originName.indexOf(".")+1)+"dat";
			File dst = new File(dstParent, dstName);
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dst));
			oos.writeObject(m);
			oos.flush();
			oos.close();
			oos = null;
		}
	}
	
	public static Model readData(File f) throws Exception{
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(f));
			Object o = ois.readObject();
			Model m = (Model)o; 
			return m;
		}finally {
			if(ois!=null) {
				ois.close();
				ois = null;
			}
		}
	}
	
	
}
