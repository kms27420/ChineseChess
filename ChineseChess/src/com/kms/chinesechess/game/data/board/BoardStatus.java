package com.kms.chinesechess.game.data.board;

import com.kms.chinesechess.game.data.ChessManType;
import com.kms.chinesechess.game.data.TeamType;
import com.kms.chinesechess.support.Location;

/**
 * 장기판 위의 각 칸들의 상태 정보를 가지고 위치 정보를 이용하여 해당 위치의 칸의 정보를 읽고 변경할 수 있는 클래스
 * @author Kwon
 *
 */
public class BoardStatus {
	private final BoxStatus[][] BOARD_STATUS = new BoxStatus[Location.MAX_ROW + 1][Location.MAX_COL + 1];
	
	public BoardStatus() {
		for(Location[] locations : Location.values()) {
			for(Location location : locations) {
				setBoxStatus(location, BoxStatus.SPACE);
			}
		}
	}
	/**
	 * copy의 데이터를 본 인스턴스의 데이터로 복사하는 매서드
	 * @param copy 복사하고자 하는 대상
	 */
	public void copy(BoardStatus copy) {
		for(Location[] locations : Location.values()) {
			for(Location location : locations) {
				setBoxStatus(location, copy.getBoxStatus(location));
			}
		}
	}
	/**
	 * 해당 위치의 BoxStatus 데이터를 ChessManType, TeamType에 맞는 BoxStatus로 변경하는 매서드
	 * @param location 변경하고자 하는 위치
	 * @param chessManType 대입하고자하는 ChessManType
	 * @param teamType 대입하고자하는 TeamType
	 */
	public void setBoxStatus(Location location, ChessManType chessManType, TeamType teamType) {
		setBoxStatus(location, BoxStatus.getInstance(chessManType, teamType));
	}
	/**
	 * 해당 위치의 BoxStatus 데이터를 원하는 BoxStatus로 변경하는 매서드
	 * @param location 변경하고자 하는 위치
	 * @param boxStatus 대입하고자하는 BoxStatus
	 */
	public void setBoxStatus(Location location, BoxStatus boxStatus) {
		BOARD_STATUS[location.ROW][location.COL] = boxStatus;
	}
	/**
	 * before의 위치에 있는 BoxStatus를 after위치로 옮기는 매서드, before 위치에는 BoxStatus.SPACE가 들어간다.
	 * @param before 이동 전의 위치
	 * @param after 이동 후의 위치
	 */
	public void moveBoxStatus(Location before, Location after) {
		setBoxStatus(after, getBoxStatus(before));
		setBoxStatus(before, BoxStatus.SPACE);
	}
	
	private BoxStatus getBoxStatus(Location location) {
		return BOARD_STATUS[location.ROW][location.COL];
	}
	/**
	 * 해당 위치의 BoxStatus의 ChessManType을 반환하는 매서드
	 * @param location 원하는 위치
	 * @return location 위치의 ChessManType
	 */
	public ChessManType getChessManType(Location location) {
		return getBoxStatus(location).CHESS_MAN_TYPE;
	}
	/**
	 * 해당 위치의 BoxStatus의 TeamType을 반환하는 매서드
	 * @param location 원하는 위치
	 * @return location 위치의 TeamType
	 */
	public TeamType getTeamType(Location location) {
		return getBoxStatus(location).TEAM_TYPE;
	}
	/**
	 * 해당 위치의 BoxStatus가 SPACE인지 판단하는 매서드
	 * @param location 확인할 위치
	 * @return location 위치의 BoxStatus가 SPACE면 true, 그렇지 않다면 false
	 */
	public boolean isSpace(Location location) {
		return getBoxStatus(location) == BoxStatus.SPACE;
	}
}
