package com.kms.chinesechess.size;

import java.awt.Dimension;

import javax.swing.ImageIcon;

public class ChessManSize {

	private final Dimension CHESS_MAN_SIZE = new Dimension();
	
	private static final ChessManSize INSTANCE = new ChessManSize();
	
	private ChessManSize() {
		
		ImageIcon imageIcon = new ImageIcon( ChessManSize.class.getResource( "../" ).getPath() + "image/before_resized/RedKing.png" );
		double squareRate = (double)imageIcon.getIconWidth() / (double)imageIcon.getIconHeight();
		
		CHESS_MAN_SIZE.setSize( DeviceSize.getWindowHeight() * squareRate / 15, DeviceSize.getWindowHeight() / 15 );
		
	}
	
	public static double getChessManWidth() {
		
		return INSTANCE.CHESS_MAN_SIZE.getWidth();
		
	}
	
	public static double getChessManHeight() {
		
		return INSTANCE.CHESS_MAN_SIZE.getHeight();
		
	}
	
}
