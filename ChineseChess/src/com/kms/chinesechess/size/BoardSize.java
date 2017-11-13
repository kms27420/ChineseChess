package com.kms.chinesechess.size;

import java.awt.Dimension;

public class BoardSize {
	
	private final Dimension BOARD_SIZE = new Dimension();
	
	private static final BoardSize INSTANCE = new BoardSize();
	
	private BoardSize() {
		
		BOARD_SIZE.setSize( DeviceSize.getWindowHeight() * 3.0 / 5.0, DeviceSize.getWindowHeight() * 2.0 / 3.0 );
		
	}
	
	public static double getWidth() {
		
		return INSTANCE.BOARD_SIZE.getWidth();
		
	}
	
	public static double getHeight() {
		
		return INSTANCE.BOARD_SIZE.getHeight();
		
	}
	
}
