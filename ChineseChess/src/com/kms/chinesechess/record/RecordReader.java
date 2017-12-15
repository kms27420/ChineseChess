package com.kms.chinesechess.record;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * 지정 File에 문자열로 저장되어있는 사용자의 게임 전적을 실질적인 데이터로써 읽어오는 클래스
 * @author Kwon
 *
 */
public class RecordReader {
	private final String FILE_PATH = RecordReader.class.getResource("./").getPath() + "record.txt";
	final File FILE = new File( FILE_PATH );
	
	private final Record RECORD = new Record();
	
	static final RecordReader INSTANCE = new RecordReader();
	
	private RecordReader() {}
	/**
	 * 파일에 String 형식으로 저장되어있는 게임 전적의 정보를 실질적인 데이터의 집합으로 구성된 Record 클래스의 인스턴스로 생성하여 반환해주는 매서드
	 * @return 파일을 문제없이 읽어올 경우에 파일에 저장되어있는 전적의 정보를 Record 인스턴스로 반환, 파일을 읽어오는데 문제가 발생한 경우에는 초기화된 게임 전적 정보를 반환한다.
	 */
	Record getRecord() {
		DataInputStream reader = null;
		
		try {
			 reader = new DataInputStream( new FileInputStream( FILE ) );
			 RECORD.setRecord( reader.readUTF() );
		} catch(Exception e) { RECORD.setRecord(null); }
		finally {
			try {
				if( reader != null )	reader.close();
			} catch(Exception e) {}
			
			reader = null;
		}
		
		return RECORD;
	}
	/**
	 * String으로 저장되어있는 게임 전적을 실질적인 값(승, 무, 패, 승률)으로 치환하여 저장하고 있는 데이터 클래스
	 * String.format( "%03d%03d%03d", win, draw, lose ) 형식의 문자열에서 실질적인 값인 integer형식의 win, draw, lose로 저장한다.
	 * @author Kwon
	 *
	 */
	static class Record {
		private int win, draw, lose;
		
		private Record() {}
		
		int getWinCount() {
			return win;
		}
		
		int getDrawCount() {
			return draw;
		}
		
		int getLoseCount() {
			return lose;
		}
		
		float getWinPercentage() {
			return ( win + draw + lose > 0 ) ? (float)win * 100 / ( win + draw + lose ) : 0.0f;
		}
		
		private void setRecord( String record ) {
			win = convertToWinCount( record );
			draw = convertToDrawCount( record );
			lose = convertToLoseCount( record );
		}
		
		private int convertToWinCount( String record ) {
			if( record == null )	return 0;
			
			return Integer.parseInt( record.substring( 0, 3 ) );
		}
		
		private int convertToDrawCount( String record ) {
			if( record == null )	return 0;
			
			return Integer.parseInt( record.substring( 3, 6 ) );
		}
		
		private int convertToLoseCount( String record ) {
			if( record == null )	return 0;
			
			return Integer.parseInt( record.substring( 6, 9 ) );
		}
	}
}
