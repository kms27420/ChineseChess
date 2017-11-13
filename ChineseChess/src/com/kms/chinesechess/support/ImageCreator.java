package com.kms.chinesechess.support;

import java.awt.Image;
import java.awt.Toolkit;

import com.kms.chinesechess.game.chessman.ChessManData.ChessManType;
import com.kms.chinesechess.game.chessman.ChessManData.ColorType;

/**
 * 본 프로그램에서 사용하는 이미지 파일들을 Image 인스턴스로 생성하여 제공하는 클래스
 * @author Kwon
 *
 */
public class ImageCreator {

	private static final String CHESS_MAN_IMAGE_PATH = ImageCreator.class.getResource( "../" ).getPath() + "image/";
	private static final String CHESS_MAN_IMAGE_FORMAT = ".png";
	
	private ImageCreator(){}
	
	/**
	 * 장기 말의 이미지를 색과 장기 말 타입에 따라 Image 인스턴스로 만들어서 제공해주는 매서드
	 * @param colorType 장기 말의 색 타입
	 * @param chessManType 장기 말의 종류 타입
	 * @return Image
	 */
	public static Image createChessManImage( ColorType colorType, ChessManType chessManType ) {
		
		return Toolkit.getDefaultToolkit().getImage( getChessManImagePath( colorType, chessManType ) );
		
	}
	
	private static String getChessManImagePath( ColorType colorType, ChessManType chessManType ) {
		
		return CHESS_MAN_IMAGE_PATH + colorType.toString() + chessManType.toString() + CHESS_MAN_IMAGE_FORMAT;
		
	}
	
}
