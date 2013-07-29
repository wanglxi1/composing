package com.teddytailor.research.ocr.util;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;

public class ImageFrame extends JFrame {

	private ImagePanel jp;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageFrame() throws Exception {
		this(null);
	}
	
	public ImageFrame(ImageProcessHandler dropListener) throws Exception {
		super();
		
		
		
		this.setLayout(new GridLayout(1, 1));
		this.setSize(1000, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.jp = new ImagePanel();
		
		this.add(jp);
		this.setVisible(true);
		
		if(dropListener  != null) {
			this.setDropTarget(new DropTarget(this, new DropTargetListenerAdapter(this, dropListener)));
		}
	}
	
	
	public void show(Image img) {
		this.jp.setImg(img);
	}
	
	private static class DropTargetListenerAdapter implements DropTargetListener{
		private ImageProcessHandler dropListener;
		private ImageFrame frame;
		
		public DropTargetListenerAdapter(ImageFrame frame, ImageProcessHandler dropListener) {
			this.frame = frame;
			this.dropListener = dropListener;
		}
		
		@Override public void dragEnter(DropTargetDragEvent dtde) {}
		@Override public void dragOver(DropTargetDragEvent dtde) {}
		@Override public void dropActionChanged(DropTargetDragEvent dtde) {}
		@Override public void dragExit(DropTargetEvent dte) {}

		@Override
		public void drop(DropTargetDropEvent dtde) {
			try {
                // Transferable tr = dtde.getTransferable();
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    List<File> list = (List) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                    File f = list.get(0);
                    
                    Image img = this.dropListener.process(f);
                    this.frame.show(img);
                    
                    dtde.dropComplete(true);
                } else {
                    dtde.rejectDrop();
                }
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }
        }
	}
	
	public static interface ImageProcessHandler{
		Image process(File f) throws Exception;
	}
}
