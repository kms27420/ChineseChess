package com.kms.chinesechess.game.chess_men_paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kms.chinesechess.game.data.TeamType;
import com.kms.chinesechess.game.data.board.BoardStatus;
import com.kms.chinesechess.support.Location;

/**
 * 장기판 위의 모든 위치에 대해서 이동 가능한 위치들의 리스트를 설정하고 제공해주는 클래스
 * @author Kwon
 *
 */
public class MovablePaths {
	private final Map<Location, List<Location>> MOVABLE_PATHS = new HashMap<>();
	
	private final BoardStatus BOARD_STATUS;			// 장기판의 현황을 참조, 본 클래스에서 변경을 불허한다.
	private final PathAnalyzer PATH_ANALYZER;
	
	MovablePaths(BoardStatus boardStatus) {
		this.BOARD_STATUS = boardStatus;
		this.PATH_ANALYZER = new PathAnalyzer(this.BOARD_STATUS);
		
		for(Location[] locations : Location.values()) {
			for(Location location : locations) {
				MOVABLE_PATHS.put(location, new ArrayList<>());
			}
		}
	}
	/**
	 * 장기말 위치에서 장기말의 이동 경로 리스트를 반환하는 매서드
	 * @param source 장기말의 위치
	 * @return 해당 위치의 장기말의 이동 경로 리스트
	 */
	List<Location> getPathList(Location source) {
		return MOVABLE_PATHS.get(source);
	}
	/**
	 * 해당 팀의 모든 이동 경로 리스트
	 * @param teamType 모든 이동 경로를 알고자 하는 팀
	 * @return 해당 팀의 모든 이동 경로 리스트
	 */
	List<Location> getAllPathList(TeamType teamType) {
		List<Location> allMovablePaths = new ArrayList<>();
		
		for(Location[] locations : Location.values()) {
			for(Location source : locations) {
				if(BOARD_STATUS.isSpace(source))	continue;
				if(BOARD_STATUS.getTeamType(source) != teamType)	continue;
				
				for(Location movablePath : MOVABLE_PATHS.get(source)) {
					if(allMovablePaths.contains(movablePath))	continue;
					allMovablePaths.add(movablePath);
				}
			}
		}
		
		return allMovablePaths;
	}
	/**
	 * 모든 위치에 대하여 이동 경로를 설정해주는 매서드
	 */
	void setPaths() {
		Location startLocation = null;
		for(Location[] locations : Location.values()) {
			for(Location source : locations) {
				MOVABLE_PATHS.get(source).clear();
				if(BOARD_STATUS.isSpace(source))	continue;
				
				for(Direction direction : Direction.values()) {
					startLocation = getMovingStartLocation(source, direction);
					
					if(ChessManAnalyzer.isTargetingMovement(BOARD_STATUS.getChessManType(source))) {
						for(Direction targetingDirection : getTargetingDirections(direction)) {
							addTargetingLocation(source, startLocation, targetingDirection);
						}
					} else {
						addStraightPaths(source, startLocation, direction);
					}
				}
			}
		}
	}
	/**
	 * source위치의 장기말이 이동을 시작하는 위치를 반환하는 매서드
	 * @param source 장기말의 위치
	 * @param direction 이동하는 방향
	 * @return 이동을 시작하는 위치
	 */
	private Location getMovingStartLocation(Location source, Direction direction) {
		Location startLocation = null;
		
		boolean isJumpable = ChessManAnalyzer.isJumpable(BOARD_STATUS.getChessManType(source));
		boolean isTargetingMovement = ChessManAnalyzer.isTargetingMovement(BOARD_STATUS.getChessManType(source));
		
		if(isJumpable) {
			startLocation = Location.instanceAt(source.ROW + direction.ROW_INCREASE, source.COL + direction.COL_INCREASE);
			boolean isStartJumpable;
			
			while(startLocation != null && !PATH_ANALYZER.isCrossable(startLocation)) {
				isStartJumpable = !BOARD_STATUS.isSpace(startLocation)
									&& ChessManAnalyzer.isJumpable(BOARD_STATUS.getChessManType(startLocation));
				startLocation = isStartJumpable ? null 
						: Location.instanceAt(startLocation.ROW + direction.ROW_INCREASE, startLocation.COL + direction.COL_INCREASE);
			}
			if(startLocation != null)
				startLocation = Location.instanceAt(startLocation.ROW + direction.ROW_INCREASE, startLocation.COL + direction.COL_INCREASE);
		} else if(isTargetingMovement) {
			startLocation = source;
			for(int count = 1; count <= ChessManAnalyzer.getStraightCount(BOARD_STATUS.getChessManType(source)); count++) {
				startLocation = Location.instanceAt(startLocation.ROW + direction.ROW_INCREASE, startLocation.COL + direction.COL_INCREASE);
				if(!PATH_ANALYZER.isPassable(startLocation))	startLocation = null;
			}
		} else {
			startLocation = Location.instanceAt(source.ROW + direction.ROW_INCREASE, source.COL + direction.COL_INCREASE);
		}
		
		return startLocation;
	}
	/**
	 * 타겟 위치로 이동하는 장기말의 타겟의 방향 집합을 반환하는 매서드
	 * @param direction 현재 방향
	 * @return 크기 2(이동 방향의 좌,우 대각선)의 Direction 배열
	 */
	private Direction[] getTargetingDirections(Direction direction) {
		Direction[] returnValue = new Direction[2];
		switch(direction) {
		case NORTH : 
			returnValue[0] = Direction.NORTH_WEST;
			returnValue[1] = Direction.NORTH_EAST;
			break;
		case SOUTH :
			returnValue[0] = Direction.SOUTH_WEST;
			returnValue[1] = Direction.SOUTH_EAST;
			break;
		case WEST :
			returnValue[0] = Direction.SOUTH_WEST;
			returnValue[1] = Direction.NORTH_WEST;
			break;
		case EAST :
			returnValue[0] = Direction.NORTH_EAST;
			returnValue[1] = Direction.SOUTH_EAST;
			break;
		default : break;
		}
		
		return returnValue;
	}
	/**
	 * 타겟 위치로 이동하는 장기말의 이동 경로를 추가하는 매서드
	 * @param source 장기말의 위치
	 * @param startLocation 타겟 방향으로의 탐색을 시작하는 위치
	 * @param targetingDirection 타겟의 방향
	 */
	private void addTargetingLocation(Location source, Location startLocation, Direction targetingDirection) {
		if(targetingDirection == null || !isDiagonal(targetingDirection))	return;
		
		Location targetingPath = startLocation;
		for(int count = 1; count <= ChessManAnalyzer.getDiagonalCount(BOARD_STATUS.getChessManType(source)); count++) {
			if(!PATH_ANALYZER.isPassable(targetingPath))	return;
			targetingPath = Location.instanceAt(targetingPath.ROW + targetingDirection.ROW_INCREASE, 
					targetingPath.COL + targetingDirection.COL_INCREASE);
		}
		if(PATH_ANALYZER.isReplacable(source, targetingPath))	MOVABLE_PATHS.get(source).add(targetingPath);
	}
	/**
	 * 넌타겟형(직진형) 장기말의 이동 경로를 추가하는 매서드
	 * @param source 장기말의 위치
	 * @param startLocation 이동 시작 위치
	 * @param direction 이동 방향
	 */
	private void addStraightPaths(Location source, Location startLocation, Direction direction) {
		Location path = startLocation;
		for(int count = 1; count <= ChessManAnalyzer.getStraightCount(BOARD_STATUS.getChessManType(source)); count++) {
			if(!PATH_ANALYZER.isReplacable(source, path))	break;
			MOVABLE_PATHS.get(source).add(path);
			if(!PATH_ANALYZER.isPassable(path))	break;
			path = Location.instanceAt(path.ROW + direction.ROW_INCREASE, path.COL + direction.COL_INCREASE);
		}
	}
	/**
	 * direction이 대각선 방향인지를 반환하는 매서드
	 * @param direction 방향
	 * @return 대각선이면 true, 그렇지 않으면 false
	 */
	private boolean isDiagonal(Direction direction) {
		return direction.ROW_INCREASE != 0 && direction.COL_INCREASE != 0;
	}
	
	/**
	 * 8방향(북서, 북, 북동, 동, 남동, 남, 남서, 서)을 표현한 enum클래스
	 * row, col의 증가량을 통해 방향의 정체성을 갖는다.
	 * @author Kwon
	 *
	 */
	private enum Direction {
		NORTH(-1,0), SOUTH(1,0), WEST(0,-1), EAST(0,1),
		NORTH_WEST(-1,-1), NORTH_EAST(-1,1),
		SOUTH_WEST(1,-1), SOUTH_EAST(1,1);
		
		private final int ROW_INCREASE, COL_INCREASE;
		
		private Direction(int rowIncrease, int colIncrease) {
			this.ROW_INCREASE = rowIncrease;
			this.COL_INCREASE = colIncrease;
		}
	}
}