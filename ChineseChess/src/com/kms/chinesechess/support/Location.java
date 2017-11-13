package com.kms.chinesechess.support;

/**
 * 장기판 안에서 가질 수 있는 모든 좌표 정보를 제공해주는 클래스
 * @author Kwon
 *
 */
public class Location {
	
	public static final int MIN_ROW = 1, MIN_COL = 1;
	public static final int MAX_ROW = 10, MAX_COL = 9;
	
	private int row, col;
	
	private static final Location[][] INSTANCE = new Location[MAX_ROW + 1][MAX_COL + 1];
	
	private Location( int row, int col ) {
		
		this.row = row;
		this.col = col;
		
	}
	/**
	 * 원하는 좌표 정보를 가지고 있는 INSTANCE를 반환해주는 매서드
	 * @param row
	 * @param col
	 * @return INSTANCE[row][col]
	 */
	public static Location instanceAt( int row, int col ) {
		
		if( row > MAX_ROW || col > MAX_COL || row < MIN_ROW || col < MIN_COL )	return null;
		
		if( INSTANCE[row][col] != null )	return INSTANCE[row][col];
		
		return INSTANCE[row][col] = new Location( row, col );
		
	}
	
	public int getRow() {
		
		return row;
		
	}
	
	public int getCol() {
		
		return col;
		
	}
	
}