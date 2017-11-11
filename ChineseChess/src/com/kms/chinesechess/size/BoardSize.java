package com.kms.chinesechess.size;

import java.awt.Dimension;

public class BoardSize {

	private final Dimension BOARD_SIZE = new Dimension();
	
	private static final BoardSize INSTANCE = new BoardSize();
	
	private BoardSize() {
		
		BOARD_SIZE.setSize( ChessManSize.getChessManWidth() * 9, ChessManSize.getChessManHeight() * 10 );
		
	}
	
	public static double getBoardWidth() {
		
		return INSTANCE.BOARD_SIZE.getWidth();
		
	}
	
	public static double getBoardHeight() {
		
		return INSTANCE.BOARD_SIZE.getHeight();
		
	}
	
}
