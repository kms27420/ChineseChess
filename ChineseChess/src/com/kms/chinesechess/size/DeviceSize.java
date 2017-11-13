package com.kms.chinesechess.size;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class DeviceSize {

	private final Dimension SCREEN_SIZE;
	private final Rectangle WINDOW_SIZE;
	
	private static final DeviceSize INSTANCE = new DeviceSize();
	
	private DeviceSize() {
		
		SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
		WINDOW_SIZE = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		
	}
	
	public static final double getScreenWidth() {
		
		return INSTANCE.SCREEN_SIZE.getWidth();
		
	}
	
	public static final double getScreenHeight() {
		
		return INSTANCE.SCREEN_SIZE.getHeight();
		
	}
	
	public static final double getWindowWidth() {
		
		return INSTANCE.WINDOW_SIZE.getWidth();
		
	}
	
	public static final double getWindowHeight() {
		
		return INSTANCE.WINDOW_SIZE.getHeight();
		
	}
	
}
