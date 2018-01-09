package com.kms.chinesechess.game.chess_men_paths;

import com.kms.chinesechess.game.data.ChessManType;

/**
 * ChessManType에 따른 위치 이동 특성을 분석하고 제공해주는 클래스
 * @author Kwon
 *
 */
public class ChessManAnalyzer {
	/**
	 * 장기말이 점프 가능한 장기말인지를 반환해주는 매서드
	 * @param chessManType 장기말
	 * @return 점프 가능 여부
	 */
	static boolean isJumpable(ChessManType chessManType) {
		switch(chessManType) {
		case CANNON : return true;
		default : return false;
		}
	}
	/**
	 * 장기말이 뒤로 이동 가능한지를 반환해주는 매서드
	 * @param chessManType 장기말
	 * @return 뒤로 이동 가능 여부
	 */
	static boolean isBackwardable(ChessManType chessManType) {
		switch(chessManType) {
		case SOLDIER : return false;
		default : return true;
		}
	}
	/**
	 * 장기말이 궁성 안에 있어야하는지를 반환해주는 매서드
	 * @param chessManType 장기말
	 * @return 궁성안에 있어야하는지에 대한 여부
	 */
	static boolean isMustInPalace(ChessManType chessManType) {
		switch(chessManType) {
		case ADVISER : return true;
		case KING : return true;
		default : return false;
		}
	}
	/**
	 * 장기말의 직진 이동 횟수를 반환해주는 매서드
	 * @param chessManType 장기말
	 * @return 장기말의 직진 이동 횟수
	 */
	static int getStraightCount(ChessManType chessManType) {
		switch(chessManType) {
		case CANNON : return Integer.MAX_VALUE;
		case CHARIOT :	return Integer.MAX_VALUE;
		default :	return 1;
		}
	}
	/**
	 * 장기말의 대각선 이동 횟수를 반환해주는 매서드
	 * @param chessManType 장기말
	 * @return 장기말의 대각선 이동 횟수
	 */
	static int getDiagonalCount(ChessManType chessManType) {
		switch(chessManType) {
		case HORSE : return 1;
		case ELEPHANT : return 2;
		default : return 0;
		}
	}
	/**
	 * 장기말이 일정 지역을 타겟으로 이동하는지를 반환해주는 매서드
	 * @param chessManType 장기말
	 * @return 장기말이 일정 위치를 타겟으로 이동하면 true, 장기말이 이동 횟수만큼의 위치를 모두 이동하면 false 
	 */
	static boolean isTargetingMovement(ChessManType chessManType) {
		switch(chessManType) {
		case HORSE : return true;
		case ELEPHANT : return true;
		default : return false;
		}
	}
}