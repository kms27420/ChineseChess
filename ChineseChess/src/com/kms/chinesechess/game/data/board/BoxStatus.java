package com.kms.chinesechess.game.data.board;

import com.kms.chinesechess.game.data.ChessManType;
import com.kms.chinesechess.game.data.TeamType;

/**
 * 장기판에서 장기말이 놓여지는 칸에 놓여진 장기말의 상태를 제공해주는 클래스
 * ChessManType, TeamType의 경우의 수만큼 인스턴스를 제공하고 빈칸일 경우 SPACE 인스턴스를 제공한다.
 * @author Kwon
 *
 */
public class BoxStatus {
	public final ChessManType CHESS_MAN_TYPE;
	public final TeamType TEAM_TYPE;
	
	private static final BoxStatus[][] INSTANCE = new BoxStatus[ChessManType.values().length][TeamType.values().length];
	public static final BoxStatus SPACE = new BoxStatus(null, null);
	
	static {
		for(ChessManType chessManType : ChessManType.values()) {
			for(TeamType teamType : TeamType.values()) {
				INSTANCE[chessManType.ordinal()][teamType.ordinal()] = new BoxStatus(chessManType, teamType);
			}
		}
	}
	
	private BoxStatus(ChessManType chessManType, TeamType teamType) {
		this.CHESS_MAN_TYPE = chessManType;
		this.TEAM_TYPE = teamType;
	}
	
	public static BoxStatus getInstance(ChessManType chessManType, TeamType teamType) {
		return INSTANCE[chessManType.ordinal()][teamType.ordinal()];
	}
}
