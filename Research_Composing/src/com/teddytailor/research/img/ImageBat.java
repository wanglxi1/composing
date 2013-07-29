package com.teddytailor.research.img;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageBat {
	
	private List<File> files;
	private ImageProcesser processer;
	
	
	public ImageBat(File parent, ImageProcesser processer) {
		this(parent, null, processer);
	}
	
	public ImageBat(File parent, FilenameFilter filter, ImageProcesser processer) {
		this(getFiles(parent, filter), processer);
	}
	
	public ImageBat(List<File> srcFs, ImageProcesser processer) {
		this.files = srcFs;
		this.processer = processer;
	}
	
	public void run() throws Exception {
		for(File f: this.files) {
			System.out.print(f.getName()+"\t");
			
			Image src = ImageIO.read(f);
			System.out.print("...已读取"+"\t");
			
			Image dst = this.processer.process(src);
			System.out.print("...已处理"+"\t");

			this.processer.save(dst, src, f);
			System.out.println("...已存储!");
		}
	}
	
	
	public static void run(File p, ImageProcesser processer) throws Exception {
		new ImageBat(p, processer).run();
	}
	public static List<File> getFiles(File p, FilenameFilter filter){
		File[] fs = p.listFiles(filter);
		return Arrays.asList(fs);
	}
	public static void saveImg(Image dstImg, String format, File dstF) throws Exception{
		if(!(dstImg instanceof RenderedImage)) {
			int w = dstImg.getWidth(null);
			int h = dstImg.getHeight(null);
			BufferedImage bdst = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
			bdst.getGraphics().drawImage(dstImg, 0, 0, null);
			dstImg = bdst;
		}
		if(!dstF.getParentFile().exists()) {
			dstF.mkdirs();
		}
		ImageIO.write((RenderedImage)dstImg, format, dstF);
	}
	
	public static interface ImageProcesser {
		Image process(Image src) throws Exception;
		void save(Image dst, Image src, File f) throws Exception;
	}

}
