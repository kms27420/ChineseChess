package com.kms.chinesechess.board;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class BoardView extends JPanel {

	static final int MAX_ROW = 10, MAX_COL = 9;
	
	@Override
	public void paintComponent( Graphics g ) {
		
		super.paintComponent( g );
		
		drawBoardLine( g );
		drawMarkX( g );
		drawKingBox( g );
		
	}
	
	private void drawBoardLine( Graphics g ) {
		
		((Graphics2D)g).setStroke( new BasicStroke( 1.0f ) );
		
		for( int col = 1; col <= BoardView.MAX_COL; col++ ) {
			
			drawLine( (Graphics2D)g, new Point( 1, col ), new Point( BoardView.MAX_ROW, col ) );
			
		}
		
		for( int row = 1; row <= BoardView.MAX_ROW; row++ ) {
			
			drawLine( (Graphics2D)g, new Point( row, 1 ), new Point( row, BoardView.MAX_COL ) );
			
		}
		
	}
	
	private void drawMarkX( Graphics g ) {
	
		((Graphics2D)g).setStroke( new BasicStroke( 1.8f ) );
		
		Point firstLineStartPoint = new Point();
		Point firstLineEndPoint = new Point();
		
		Point secondLineStartPoint = new Point();
		Point secondLineEndPoint = new Point();
		
		Point[] markXPoint = getMarkXPoint();
		
		for( int index = 0; index < markXPoint.length; index++ ) {
			
			firstLineStartPoint.setPoint( markXPoint[index].row - 0.1, markXPoint[index].col - 0.1 );
			firstLineEndPoint.setPoint( markXPoint[index].row + 0.1, markXPoint[index].col + 0.1 );
			secondLineStartPoint.setPoint( markXPoint[index].row - 0.1, markXPoint[index].col + 0.1 );
			secondLineEndPoint.setPoint( markXPoint[index].row + 0.1, markXPoint[index].col - 0.1 );
			
			drawLine( (Graphics2D)g, firstLineStartPoint, firstLineEndPoint );
			drawLine( (Graphics2D)g, secondLineStartPoint, secondLineEndPoint );
			
		}
		
	}
	
	private void drawKingBox( Graphics g ) {
		
		((Graphics2D)g).setStroke( new BasicStroke( 1.5f ) );
		
		KingBoxArea kingBoxArea = new KingBoxArea();
		
		drawLine( (Graphics2D)g, kingBoxArea.redKingBoxLeftTop, kingBoxArea.redKingBoxRightBottom );
		drawLine( (Graphics2D)g, kingBoxArea.redKingBoxLeftBottom, kingBoxArea.redKingBoxRightTop );
		
		drawLine( (Graphics2D)g, kingBoxArea.blueKingBoxLeftTop, kingBoxArea.blueKingBoxRightBottom );
		drawLine( (Graphics2D)g, kingBoxArea.blueKingBoxLeftBottom, kingBoxArea.blueKingBoxRightTop );
		
	}
	
	private void drawLine( Graphics2D g, Point startPoint, Point endPoint ) {
		
		Position startPosition = new Position( startPoint );
		Position endPosition = new Position( endPoint );
		
		int startX, startY, endX, endY;
		
		startX = (int)startPosition.x;
		startY = (int)startPosition.y;
		endX = (int)endPosition.x;
		endY = (int)endPosition.y;
		
		g.drawLine( startX, startY, endX, endY );
		
	}
	
	private Point[] getMarkXPoint() {
		
		final int PO_COUNT = 4, ZOL_COUNT = 10;
		final int MARK_X_COUNT = PO_COUNT + ZOL_COUNT;
		
		Point[] markXPoint = new Point[MARK_X_COUNT];
		
		int index = 0;
		int row, col;
		
		for( int count = 0; count < PO_COUNT; count++ ) {
			
			row = count < 2 ? 3 : 8;
			col = count % 2 == 0 ? 2 : 8;
			
			markXPoint[index] = new Point( row, col );
			
			index++;
			
		}
		
		for( int count = 0; count < ZOL_COUNT; count++ ) {
			
			row = count < 5 ? 4 : 7;
			col = ( count % 5 ) * 2 + 1; 
			
			markXPoint[index] = new Point( row, col );
			
			index++;
			
		}
		
		return markXPoint;
		
	}
	
	public static class Point {
		
		private double row, col;
		
		public Point(){}
		
		public Point( double row, double col ) {
			
			setPoint( row, col );
			
		}
		
		public void setPoint( double row, double col ) {
			
			this.row = row;
			this.col = col;
			
		}
		
		public double getRow() {
			
			return row;
			
		}
		
		public double getCol() {
			
			return col;
			
		}
		
	}
	
	private class Position {
		
		private double x, y;
		
		private Position() {}
		
		private Position( Point point ) {
			
			setPosition( point );
			
		}
		
		private Position( int row, int col ) {
			
			setPosition( row, col );
			
		}
		
		private void setPosition( Point point ) {
			
			setPosition( point.row, point.col );
			
		}
		
		private void setPosition( double row, double col ) {
			
			x = (double)getWidth() * col / (double)( MAX_COL + 1 );
			y = (double)getHeight() * row / (double)( MAX_ROW + 1 );
			
		}
		
	}
	
	private class KingBoxArea {
		
		private Point redKingBoxLeftTop = new Point( 1, 4 );
		private Point redKingBoxRightTop = new Point( 1, 6 );
		private Point redKingBoxLeftBottom = new Point( 3, 4 );
		private Point redKingBoxRightBottom = new Point( 3, 6 );
		
		private Point blueKingBoxLeftTop = new Point( 8, 4 );
		private Point blueKingBoxRightTop = new Point( 8, 6 );
		private Point blueKingBoxLeftBottom = new Point( 10, 4 );
		private Point blueKingBoxRightBottom = new Point( 10, 6 );
		
	}
	
}
