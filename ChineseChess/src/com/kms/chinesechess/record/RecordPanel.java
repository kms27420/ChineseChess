package com.kms.chinesechess.record;

import javax.swing.JButton;
import javax.swing.JTextPane;

import com.kms.chinesechess.exectue.IntroView;
import com.kms.chinesechess.record.RecordReader.Record;
import com.kms.chinesechess.size.GuideViewSize;
import com.kms.chinesechess.support.CommonComponent;
import com.kms.chinesechess.view.MainFrame;
import com.mommoo.flat.component.FlatPanel;
import com.mommoo.flat.layout.linear.LinearLayout;
import com.mommoo.flat.layout.linear.Orientation;
import com.mommoo.flat.layout.linear.constraints.LinearConstraints;
import com.mommoo.flat.layout.linear.constraints.LinearSpace;

/**
 * 사용자의 게임 전적(승, 무, 패, 승률)을 텍스트 형식으로 보여주는 Panel 클래스
 * RecordReader에서 읽어온 Record의 정보를 텍스트 형식으로 만들어서 보여준다.
 * @author Kwon
 *
 */
public class RecordPanel extends FlatPanel {
	private static final RecordPanel INSTANCE = new RecordPanel();
	
	private RecordPanel() {
		setLayout( new LinearLayout( Orientation.VERTICAL, 0 ) );
		add( CommonComponent.createCommonLabel( "RecordView" ), new LinearConstraints( 3, LinearSpace.MATCH_PARENT ) );
		add( CommonComponent.createCommonLabel( "PercentageView" ), new LinearConstraints( 3, LinearSpace.MATCH_PARENT ) );
		add( CommonComponent.createCommonButton( "Go back to main" ), new LinearConstraints( 2, LinearSpace.MATCH_PARENT ) );
		
		addButtonListener();
	}
	
	public static void showView() {
		INSTANCE.setView();
		
		MainFrame.showOnScreen( INSTANCE, GuideViewSize.INSTANCE );
	}
	
	private void setView() {
		Record record = RecordReader.INSTANCE.getRecord();
		
		((JTextPane)getComponent(0)).setText( buildRecordText( record.getWinCount(), record.getDrawCount(), record.getLoseCount() ) );
		((JTextPane)getComponent(1)).setText( buildWinPercentageText( record.getWinPercentage() ) );
	}
	
	private String buildRecordText( int win, int draw, int lose ) {
		return "전적 : " + win + "승 " + draw + "무 " + lose + "패";
	}
	
	private String buildWinPercentageText( float winPercentage ) {
		return "승률 : " + String.format( "%.2f %%", winPercentage );
	}
	
	private void addButtonListener() {
		((JButton)getComponent(2)).addActionListener( e-> IntroView.showView() );
	}
}
