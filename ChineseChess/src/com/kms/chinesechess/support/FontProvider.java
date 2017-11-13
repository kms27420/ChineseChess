package com.kms.chinesechess.support;

import java.awt.Font;

/**
 * 본 프로그램에서 사용하는 폰트를 제공하는 클래스
 * @author Kwon
 *
 */
public class FontProvider {
	
	private final Font DEFAULT_FONT = new Font( "MD아트체", Font.BOLD, 15 );
	
	private static final FontProvider INSTANCE = new FontProvider();
	
	private FontProvider() {}
	
	/**
	 * 본 프로그램에서 사용하는 기본 폰트를 반환해주는 매서드
	 * @return Font
	 */
	public static final Font getDefaultFont() {
		
		return INSTANCE.DEFAULT_FONT;
		
	}
	/**
	 * 본 프로그램에서 사용하는 기본 폰트에서 폰트 사이즈만 변경하여 반환해주는 매서드
	 * @param fontSize 설정할 폰트 사이즈
	 * @return Font
	 */
	public static final Font getDefaultFont( float fontSize ) {
		
		return INSTANCE.DEFAULT_FONT.deriveFont( fontSize );
		
	}
	
}
