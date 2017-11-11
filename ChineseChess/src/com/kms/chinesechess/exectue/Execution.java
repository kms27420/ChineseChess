package com.kms.chinesechess.exectue;

import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JFrame;

import com.kms.chinesechess.board.BoardView;


public class Execution {

	public static void main(String[] args) {
		
		/*String imagePath = Execution.class.getResource( "../" ).getPath() + "image/RedKing.png";
		Image image = Toolkit.getDefaultToolkit().getImage( imagePath );
		FlatImagePanel p = new FlatImagePanel( image, ImageOption.MATCH_PARENT );
		*/
		
		BoardView p = new BoardView();
		JFrame f = new JFrame();
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		f.setBounds( 100,100,400,400 );
		f.getContentPane().setLayout( new GridLayout( 1, 1 ) );
		f.getContentPane().add( p );
		f.setVisible( true );
		
	}
	
}