package com.kms.chinesechess.record;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.kms.chinesechess.game.data.ResultType;
import com.kms.chinesechess.record.RecordReader.Record;

/**
 * 사용자의 게임 전적을 게임 결과에 맞게 최신화 시켜주는 클래스
 * 지정 File에서 Record 정보를 읽어들여서 게임 결과에 맞게 최신화 시켜준 후 정해진 문자열로 File에 입력하는 클래스이다.
 * 지정 File에 전적의 결과를 저장할 떄의 형식은 String.format("%03d%03d%03d, win, draw, lose) 형식이다.
 * @author Kwon
 *
 */
public class RecordUpdater {
	private final File FILE = RecordReader.INSTANCE.FILE;
	
	public static final RecordUpdater INSTANCE = new RecordUpdater();
	
	private RecordUpdater() {}
	/**
	 * 지정 File에 입력할 문자열을 resultType에 맞게 생성한 후 저장해주는 매서드
	 * @param resultType 게임의 결과(승, 무, 패)
	 */
	public synchronized void updateRecord( ResultType resultType ) {
		DataOutputStream writer = null;
		
		try {
			writer = new DataOutputStream( new FileOutputStream( FILE ) );
			writer.writeUTF( createCurrentRecord( RecordReader.INSTANCE.getRecord(), resultType ) );
			writer.flush();
		} catch(Exception e) { e.printStackTrace(); }
		finally {
			try {
				if( writer != null )	writer.close();
			} catch(Exception e) {}
		
			writer = null;
		}
	}
	
	private String createCurrentRecord( Record previous, ResultType resultType ) {
		switch(resultType) {
		case WIN :
			return String.format( "%03d%03d%03d", previous.getWinCount() + 1, previous.getDrawCount(), previous.getLoseCount() );
		case DRAW :
			return String.format( "%03d%03d%03d", previous.getWinCount(), previous.getDrawCount() + 1, previous.getLoseCount() );
		case LOSE :
			return String.format( "%03d%03d%03d", previous.getWinCount(), previous.getDrawCount(), previous.getLoseCount() + 1 );
		default :
			return "000000000";
		}
	}
}
