package com.kms.chinesechess.game.chess_men_paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kms.chinesechess.game.data.ChessManType;
import com.kms.chinesechess.game.data.TeamType;
import com.kms.chinesechess.game.data.board.BoardStatus;
import com.kms.chinesechess.support.Location;

/**
 * 장기판의 모든 위치에 대해서 이동 경로와 경고 경로(이동 했을 시 이동하는 팀이 체크를 당하는 경우의 경로)들을 설정하고 제공하는 클래스
 * @author Kwon
 *
 */
public class ChessMenPaths {
	private final MovablePaths MOVABLE_PATHS;
	private final WarningPaths WARNING_PATHS = new WarningPaths();
	
	private final BoardStatus BOARD_STATUS;
	
	public ChessMenPaths(BoardStatus boardStatus) {
		this.BOARD_STATUS = boardStatus;
		MOVABLE_PATHS = new MovablePaths(this.BOARD_STATUS);
	}
	/**
	 * 장기판의 모든 위치에 대해서 이동 경로와 경고 경로를 설정하는 매서드
	 */
	public void setPaths() {
		MOVABLE_PATHS.setPaths();
		WARNING_PATHS.setPaths();
		
		for(Location[] locations : Location.values()) {
			for(Location location : locations) {
				for(int index = 0; index < MOVABLE_PATHS.getPathList(location).size(); index++) {
					if(WARNING_PATHS.getPathList(location).contains(MOVABLE_PATHS.getPathList(location).get(index))) {
						MOVABLE_PATHS.getPathList(location).remove(index);
						--index;
					}
				}
			}
		}
	}
	/**
	 * source 위치의 장기말의 이동 경로를 리스트로 반환하는 매서드
	 * @param source 장기말의 위치
	 * @return source 위치의 장기말의 이동 가능한 위치의 리스트
	 */
	public List<Location> getMovablePaths(Location source) {
		return MOVABLE_PATHS.getPathList(source);
	}
	/**
	 * source 위치의 장기말의 경고 경로를 리스트로 반환하는 매서드
	 * @param source 장기말의 위치
	 * @return source 위치의 장기말의 경고 경로의 리스트
	 */
	public List<Location> getWarningPaths(Location source) {
		return WARNING_PATHS.getPathList(source);
	}
	/**
	 * 해당 팀의 모든 이동 경로를 리스트로 반환하는 매서드
	 * @param teamType 조회하고자하는 팀
	 * @return teamType팀의 모든 이동 경로의 리스트
	 */
	public List<Location> getAllMovablePaths(TeamType teamType) {
		return MOVABLE_PATHS.getAllPathList(teamType);
	}
	/**
	 * 장기판 위의 모든 위치에 대해서 경고 경로(이동했을 시 이동한 팀이 체크를 당하는 경우의 경로)들을 설정하고 제공해주는 클래스
	 * @author Kwon
	 *
	 */
	private class WarningPaths {
		private final Map<Location, List<Location>> WARNING_PATHS = new HashMap<>();
		
		private final BoardStatus PREDICTED_BOARD_STATUS = new BoardStatus();	// 예상 장기판의 현황
		private final MovablePaths PREDICTED_MOVABLE_PATHS = new MovablePaths(PREDICTED_BOARD_STATUS);	// 예상 이동 가능 경로를 계산해주는 클래스
		
		private WarningPaths() {
			for(Location[] locations : Location.values()) {
				for(Location location : locations) {
					WARNING_PATHS.put(location, new ArrayList<>());
				}
			}
		}
		
		private List<Location> getPathList(Location source) {
			return WARNING_PATHS.get(source);
		}
		/**
		 * 장기판 위의 모든 위치에 대해서 경고 경로들을 설정하는 매서드
		 * 모든 경로에 대해서 이동 가능 경로로 이동했을때 해당 팀이 체크를 당하는 경우를 경고 경로에 추가한다.
		 */
		private void setPaths() {
			TeamType ownTeamType = null, partnerTeamType = null;
			List<Location> predictedPartnerAllMovablePaths = null;
			
			for(Location[] locations : Location.values()) {
				for(Location source : locations) {
					WARNING_PATHS.get(source).clear();
					
					for(Location predictedPath : MOVABLE_PATHS.getPathList(source)) {
						ownTeamType = BOARD_STATUS.getTeamType(source);
						partnerTeamType = (ownTeamType == TeamType.AWAY) ? TeamType.HOME : TeamType.AWAY;
						predictedPartnerAllMovablePaths = getPredictedAllMovablePaths(source, predictedPath, partnerTeamType);
						
						if(predictedPartnerAllMovablePaths.contains(getPredictedKingLocation(ownTeamType)))
							WARNING_PATHS.get(source).add(predictedPath);
					}
				}
			}
		}
		/**
		 * 장기말을 이동시켰을때 해당 팀의 예상되는 모든 경로의 리스트를 반환하는 매서드
		 * @param before 이동 전의 위치
		 * @param after 이동 후의 위치
		 * @param teamType 조회하고자하는 팀
		 * @return before위치에서 after위치로 장기말을 이동시켰을때 teamType팀의 모든 경로의 리스트
		 */
		private List<Location> getPredictedAllMovablePaths(Location before, Location after, TeamType teamType) {
			PREDICTED_BOARD_STATUS.copy(BOARD_STATUS);
			PREDICTED_BOARD_STATUS.moveBoxStatus(before, after);
			
			PREDICTED_MOVABLE_PATHS.setPaths();
			
			return PREDICTED_MOVABLE_PATHS.getAllPathList(teamType);
		}
		/**
		 * 예상 장기판의 현황에서 해당 팀의 왕의 위치를 반환해주는 매서드
		 * @param teamType 조회하고자하는 팀
		 * @return 예상 장기판의 현황에서 teamType팀의 왕의 위치
		 */
		private Location getPredictedKingLocation(TeamType teamType) {
			for(Location[] locations : Location.values()) {
				for(Location location : locations) {
					if(PREDICTED_BOARD_STATUS.getChessManType(location) == ChessManType.KING
							&& PREDICTED_BOARD_STATUS.getTeamType(location) == teamType)	return location;
				}
			}
			
			return null;
		}
	}
}
