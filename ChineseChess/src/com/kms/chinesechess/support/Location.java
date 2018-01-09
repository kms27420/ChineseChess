package com.kms.chinesechess.support;

/**
 * 장기판 안에서의 좌표 정보를 제공해주는 클래스
 * @author Kwon
 *
 */
public class Location {
	public static final int MIN_ROW = 1, MIN_COL = 1;
	public static final int MAX_ROW = 10, MAX_COL = 9;
	
	public final int ROW, COL;
	
	private static final Location[][] INSTANCE = new Location[MAX_ROW + 1][MAX_COL + 1];
	
	static {
		for( int row = 1; row <= MAX_ROW; row++ ) 
			for( int col = 1; col <= MAX_COL; col++ ) 
				INSTANCE[row][col] = new Location(row, col);
	}
	
	private Location( int row, int col ) {
		this.ROW = row;
		this.COL = col;
	}
	/**
	 * 원하는 좌표 정보를 가지고 있는 INSTANCE를 반환해주는 매서드
	 * @param row MIN_ROW ~ MAX_ROW까지의 row
	 * @param col MIN_COL ~ MAX_COL까지의 col
	 * @return INSTANCE[row][col]
	 */
	public static Location instanceAt( int row, int col ) {
		if( row > MAX_ROW || col > MAX_COL || row < MIN_ROW || col < MIN_COL )	return null;
		
		return INSTANCE[row][col];
	}
	/**
	 * 장기판에서의 모든 좌표를 반환하는 매서드
	 * @return 장기판에서의 모든 좌표
	 */
	public static Location[][] values() {
		Location[][] values = new Location[MAX_ROW][MAX_COL];
		
		for(int row = 0; row < MAX_ROW; row++) {
			for(int col = 0; col < MAX_COL; col++) {
				values[row][col] = INSTANCE[row + 1][col + 1];
			}
		}
		
		return values;
	}
	/**
	 * 장기판에서 본 위치에서 상하좌우 반전시킨 위치를 반환하는 매서드 
	 * @return 장기판에서 본 위치에서 상하좌우 반전시킨 위치
	 */
	public Location getAwayLocation() {
		return instanceAt( MAX_ROW + MIN_ROW - ROW, MAX_COL + MIN_COL - COL );
	}
}