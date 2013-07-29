package com.teddytailor.research.img;

import java.awt.Image;
import java.io.File;

import com.teddytailor.research.img.ImageBat.ImageProcesser;
import com.teddytailor.research.ocr.EdgeDetector;
import com.teddytailor.research.ocr.EdgeDetector.EdgeDetectorException;

public class ImageEdge implements ImageProcesser {

	private EdgeDetector edgeDetector;
	
	public static void main(String[] args) throws Exception{
		ImageBat.run(new File("E:\\learn\\技术资料\\排板材料备份\\单件\\scale\\6"), new ImageEdge());
	}

	public ImageEdge() {
		edgeDetector = new EdgeDetector();
		edgeDetector.setThreshold(230);
		edgeDetector.setWidGaussianKernel(5);
	}

	@Override
	public Image process(Image src) throws Exception {
		edgeDetector.setSourceImage(src);
		try {
			edgeDetector.process();
		} catch (EdgeDetectorException e) {
			System.out.println(e.getMessage());
		}
		return edgeDetector.getEdgeImage();
	}

	@Override
	public void save(Image dst, Image src, File f) throws Exception {
		File dstF = new File(f.getParentFile().getParentFile().getParentFile(), "edge/"+f.getName());
		ImageBat.saveImg(dst, "bmp", dstF);
	}
}
