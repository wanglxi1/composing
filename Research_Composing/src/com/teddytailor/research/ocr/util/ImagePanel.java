package com.teddytailor.research.ocr.util;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Image img;

	public void setImg(Image img) {
		this.img = img;
		
		this.setSize(img.getWidth(null), img.getHeight(null));
		this.repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		if(this.img != null) {
			g.drawImage(this.img, 0, 0, null);
		}else {
			super.paintComponent(g);
		}
	}
}
