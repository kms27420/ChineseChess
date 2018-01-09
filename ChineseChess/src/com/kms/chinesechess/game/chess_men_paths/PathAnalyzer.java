package com.kms.chinesechess.game.chess_men_paths;

import java.util.ArrayList;
import java.util.List;

import com.kms.chinesechess.game.data.TeamType;
import com.kms.chinesechess.game.data.board.BoardStatus;
import com.kms.chinesechess.support.Location;

/**
 * 경로로의 이동에 관한 정보들을 제공하는 클래스
 * @author Kwon
 *
 */
public class PathAnalyzer {
	/**
	 * 궁성안의 대각선 위치 리스트
	 */
	private static final List<Location> DIAGONAL_IN_PALACE = new ArrayList<>();
	static {
		DIAGONAL_IN_PALACE.add(Location.instanceAt(Location.MAX_ROW, Location.MAX_COL / 2));
		DIAGONAL_IN_PALACE.add(Location.instanceAt(Location.MAX_ROW, Location.MAX_COL / 2 + 2));
		DIAGONAL_IN_PALACE.add(Location.instanceAt(Location.MAX_ROW - 1, Location.MAX_COL / 2 + 1));
		DIAGONAL_IN_PALACE.add(Location.instanceAt(Location.MAX_ROW - 2, Location.MAX_COL / 2));
		DIAGONAL_IN_PALACE.add(Location.instanceAt(Location.MAX_ROW - 2, Location.MAX_COL / 2 + 2));
	}
	
	private final BoardStatus BOARD_STATUS;	// 장기판의 현황을 참조, 본 클래스에서 변경을 불허한다.
	
	PathAnalyzer(BoardStatus boardStatus) {
		this.BOARD_STATUS = boardStatus;
	}
	/**
	 * 해당 경로를 지나칠 수 있는지를 반환하는 매서드
	 * @param path 경로
	 * @return 경로에 장기말이 없고 장기판 내의 위치면 true, 경로에 장기말이 존재하거나 장기판 내의 위치가 아니면 false
	 */
	boolean isPassable(Location path) {
		if(path == null)	return false;
		if(!BOARD_STATUS.isSpace(path))	return false;
		return true;
	}
	/**
	 * source위치의 장기말을 path위치로 이동가능한지를 반환하는 매서드
	 * @param source 장기말의 위치
	 * @param path 경로
	 * @return 장기말이 뒤로 이동 불가능하다면 경로는 앞 방향이어야 하고, 경로가 대각선 방향이라면 장기말의 위치와 경로 모두 궁성안의 대각선의 위치여하하고, 
	 * 			장기말이 점프형이라면 경로상의 장기말은 점프형이 아니어야하고, 장기말이 궁성에서만 이동 가능하면 경로는 궁성안에 존재해야하고, 
	 * 			장기말과 경로 상의 장기말은 팀이 달라야 true
	 */
	boolean isReplacable(Location source, Location path) {
		if(path == null)	return false;
		
		return checkTeamCondition(source, path) && checkBackwardCondition(source, path) && checkDiagonalCondition(source, path)
				&& checkJumpableCondition(source, path) && checkPalaceCondition(source, path);
	}
	/**
	 * 해당 경로를 Jump할 수 있는지를 반환하는 매서드
	 * @param path 경로
	 * @return 경로 위에 장기말이 존재하고 그 장기말이 점프가 불가능한 장기말이면 true, 그렇지 않으면 false
	 */
	boolean isCrossable(Location path) {
		if(path == null)	return false;
		if(!BOARD_STATUS.isSpace(path) && !ChessManAnalyzer.isJumpable(BOARD_STATUS.getChessManType(path)))	return true;
		return false;
	}
	/**
	 * 팀 조건을 판별하는 매서드, 이동하는 장기말과 경로 상의 장기말의 팀이 달라야한다.
	 * @param source 장기말의 위치
	 * @param path 경로
	 * @return 이동하는 장기말과 경로 상의 장기말의 팀이 다르면 true, 그렇지 않으면 false
	 */
	private boolean checkTeamCondition(Location source, Location path) {
		return BOARD_STATUS.getTeamType(source) != BOARD_STATUS.getTeamType(path);
	}
	/**
	 * 궁성 조건을 판별하는 매서드, 이동하는 장기말이 궁성안에 존재해야만 하면 경로는 궁성안의 위치여야한다.
	 * @param source 장기말의 위치
	 * @param path 경로
	 * @return 이동하는 장기말이 궁성안에 존재해야만 하면 경로는 궁성안의 위치여야 true, 이동하는 장기말이 궁성안에 존재해야만 하는게 아니라면 true
	 */
	private boolean checkPalaceCondition(Location source, Location path) {
		return (ChessManAnalyzer.isMustInPalace(BOARD_STATUS.getChessManType(source)) && isInPalace(path))
				|| !ChessManAnalyzer.isMustInPalace(BOARD_STATUS.getChessManType(source));
	}
	/**
	 * 대각선 이동 조건을 판별하는 매서드, 경로의 위치가 대각선 방향이라면 장기말의 위치와 경로의 위치 모두 궁성안의 대각선 위치여야한다.
	 * @param source 장기말의 위치
	 * @param path 경로
	 * @return 경로의 위치가 대각선 방향이라면 장기말의 위치와 경로의 위치가 모두 궁성안의 대각선 위치여야 true, 경로의 위치가 대각선 방향이 아니라면 true
	 */
	private boolean checkDiagonalCondition(Location source, Location path) {
		return (isPathDiagonal(source, path) && isDiagonalInPalace(source) && isDiagonalInPalace(path))
				|| !isPathDiagonal(source, path);
	}
	/**
	 * 점프 이동 조건을 판별하는 매서드, 장기말이 점프형이라면 경로상의 장기말은 점프형이 아니어야한다.
	 * @param source 장기말의 위치
	 * @param path 경로
	 * @return 이동하는 장기말이 점프형이라면 경로상의 장기말은 점프형이 아니어야 true, 이동하는 장기말이 점프형이 아니라면 true
	 */
	private boolean checkJumpableCondition(Location source, Location path) {
		return ( ChessManAnalyzer.isJumpable(BOARD_STATUS.getChessManType(source)) 
				&& (BOARD_STATUS.isSpace(path) || !ChessManAnalyzer.isJumpable(BOARD_STATUS.getChessManType(path))) )
				|| !ChessManAnalyzer.isJumpable(BOARD_STATUS.getChessManType(source));
	}
	/**
	 * 뒤 이동 조건을 판별하는 매서드, 장기말이 뒤로 이동 불가라면 경로는 뒤 방향이 아니어야한다.
	 * @param source 장기말의 위치
	 * @param path 경로
	 * @return 이동하는 장기말이 뒤로 이동 불가라면 경로는 뒤 방향이 아니어야 true, 이동하는 장기말이 뒤로 이동 가능하면 true
	 */
	private boolean checkBackwardCondition(Location source, Location path) {
		return (!ChessManAnalyzer.isBackwardable(BOARD_STATUS.getChessManType(source)) && !isPathBackward(source, path))
				|| ChessManAnalyzer.isBackwardable(BOARD_STATUS.getChessManType(source));
	}
	/**
	 * 해당 경로가 궁성안의 대각선 위치에 해당하는지를 반환하는 매서드
	 * @param path 경로
	 * @return 해당 경로가 궁성안의 대각선 위치에 해당하면 true, 그렇지 않으면 false
	 */
	private boolean isDiagonalInPalace(Location path) {
		return DIAGONAL_IN_PALACE.contains(path) || DIAGONAL_IN_PALACE.contains(path.getAwayLocation());
	}
	/**
	 * 경로가 대각선 방향인지를 반환하는 매서드
	 * @param source 장기말의 위치
	 * @param path 경로
	 * @return 경로가 대각선 방향이면 true, 그렇지 않으면 false
	 */
	private boolean isPathDiagonal(Location source, Location path) {
		return source.ROW - path.ROW == source.COL - path.COL
				|| source.ROW - path.ROW == -(source.COL - path.COL);
	}
	/**
	 * 경로가 뒤 방향인지를 반환하는 매서드
	 * @param source 장기말의 위치
	 * @param path 경로
	 * @return 경로가 뒤 방향이면 true, 그렇지 않으면 false
	 */
	private boolean isPathBackward(Location source, Location path) {
		return BOARD_STATUS.getTeamType(source) == TeamType.AWAY ? source.ROW > path.ROW : source.ROW < path.ROW;
	}
	/**
	 * 경로가 궁성안의 위치인지를 반환하는 매서드
	 * @param path 경로
	 * @return 경로가 궁성안의 위치이면 true, 그렇지 않으면 false
	 */
	private boolean isInPalace(Location path) {
		return ((path.ROW >= Location.MIN_ROW && path.ROW <= Location.MIN_ROW + 2) 
				&& ( path.COL >= Location.MAX_COL / 2 && path.COL <= Location.MAX_COL / 2 + 2 ))
				|| 
				((path.ROW >= Location.MAX_ROW - 2 && path.ROW <= Location.MAX_ROW) 
				&& ( path.COL >= Location.MAX_COL / 2 && path.COL <= Location.MAX_COL / 2 + 2 ));
	}
}
