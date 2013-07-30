package com.teddytailor.research.compostion.aima.data;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Set;

import org.junit.Test;

import com.teddytailor.research.ocr.util.ImageFrame;
import com.teddytailor.research.ocr.util.ImageFrame.ImageProcessHandler;

public class ModelFactoryTest {
	
	@Test
	public void testReadFile() throws Exception {
		new ImageFrame(new ImageProcessHandler() {
			@Override
			public Image process(File f) throws Exception {
				Model m = ModelFactory.readImg(f);
				Set<Point> ps = m.fillPoint();
				
				
				BufferedImage im = new BufferedImage(m.getWidth(), m.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
				for(Point p: ps) {
					im.setRGB(p.x, p.y, Color.WHITE.getRGB());
				}
				return im;
			}
			
		});
	}
	
}
