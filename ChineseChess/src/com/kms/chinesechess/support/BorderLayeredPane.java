package com.kms.chinesechess.support;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLayeredPane;

/**
 * 컴포넌트들의 경계(크기, 위치)를 변경, 설정할 수 있고 우선순위가 높은 컴포넌트를 화면의 위로 보여주는 LayeredPane
 * 컴포넌트의 크기는 실제 크기가 아닌 본 Pane의 크기에 대한 비율로써 다룬다. 크기 비율은 너비와 높이 비율이 있다.
 * 위치 또한 실제 위치값이 아닌 위치 타입으로써 다룬다.
 * 위치 타입은 NORTH_WEST, NORTH, NORTH_EAST, WEST, CENTER, EAST, SOUTH_WEST, SOUTH, SOUTH_EAST로 총 9종류가 있다.
 * 크기 비율, 위치 타입 그리고 우선순위는 매개변수로 입력받으며 입력되지않을 시에는 기본값으로 저장한다.
 * 크기 비율의 기본값은 본 Pane의 크기의 절반, 위치 타입의 기본값은 CENTER, 우선순위는 본 Pane에 add되는 시점이 늦을 수록 높게 저장된다.
 * @author Kwon
 *
 */
public class BorderLayeredPane extends JLayeredPane {
	private final Map<Component, CompBoundsData> COMP_MAP = new HashMap<>();
	
	@Override
	public void paint( Graphics g ) {
		super.paint(g);
		
		for( Component comp : getComponents() ) {
			comp.setSize( COMP_MAP.get(comp).getValidSize() );
			comp.setLocation( COMP_MAP.get(comp).getValidLocation() );
			
			comp.revalidate();
			comp.repaint();
		}
	}
	/**
	 * 컴포넌트의 크기 비율을 변경하는 매서드
	 * @param comp 크기를 변경하고자하는 컴포넌트
	 * @param widthRate 변경하고자하는 width비율
	 * @param heightRate 변경하고자하는 height비율
	 */
	public void resizeComponent( Component comp, float widthRate, float heightRate ) {
		if( !COMP_MAP.containsKey(comp) )	throw new IllegalArgumentException( "there is no comp in this pane" );
		
		COMP_MAP.get(comp).setData( widthRate, heightRate, COMP_MAP.get(comp).locationType );
		repaint();
	}
	/**
	 * 컴포넌트의 크기 비율을 변경하는 매서드
	 * @param index 크기를 변경하고자하는 컴포넌트의 인덱스
	 * @param widthRate 변경하고자하는 width비율
	 * @param heightRate 변경하고자하는 height비율
	 */
	public void resizeComponent( int index, float widthRate, float heightRate ) {
		if( index < 0 || index >= getComponentCount() )	throw new IndexOutOfBoundsException();
		
		resizeComponent( getComponent(index), widthRate, heightRate );
	}
	/**
	 * 컴포넌트의 위치 타입을 변경하는 매서드
	 * @param comp 위치 타입을 변경하고자하는 컴포넌트
	 * @param locationType 변경하고자하는 위치 타입
	 */
	public void relocateComponent( Component comp, BorderLocationType locationType ) {
		if( !COMP_MAP.containsKey(comp) )	throw new IllegalArgumentException( "there is no comp in this pane" );
		
		COMP_MAP.get(comp).setData( COMP_MAP.get(comp).widthRate, COMP_MAP.get(comp).heightRate, locationType );
		repaint();
	}
	/**
	 * 컴포넌트의 위치 타입을 변경하는 매서드
	 * @param index 위치 타입을 변경하고자하는 컴포넌트의 인덱스
	 * @param locationType 변경하고자하는 위치 타입
	 */
	public void relocateComponent( int index, BorderLocationType locationType ) {
		if( index < 0 || index >= getComponentCount() )	throw new IndexOutOfBoundsException();
		
		relocateComponent( getComponent(index), locationType );
	}
	/**
	 * 크기, 위치, 우선순위를 정하여 컴포넌트를 추가하는 매서드
	 * @param comp 추가하고자하는 컴포넌트
	 * @param widthRate 설정하고자하는 컴포넌트의 width비율
	 * @param heightRate 설정하고자하는 컴포넌트의 height비율
	 * @param locationType 설정하고자하는 컴포넌트의 위치타입
	 * @param priority 설정하고자하는 컴포넌트의 우선순위
	 */
	public void add( Component comp, float widthRate, float heightRate, BorderLocationType locationType, Integer priority ) {
		if( comp == null )	throw new IllegalArgumentException( "comp is null" );
		
		priority = ( priority == null ) ? highestLayer() + 1 : priority;
		
		COMP_MAP.put( comp, new CompBoundsData( widthRate, heightRate, locationType ) );
		super.add( comp, priority );
	}
	/**
	 * 크기, 위치를 정하여 컴포넌트를 추가하는 매서드, 우선순위는 default로 들어간다
	 * @param comp 추가하고자하는 컴포넌트
	 * @param widthRate 설정하고자하는 width비율
	 * @param heightRate 설정하고자하는 height비율
	 * @param locationType 설정하고자하는 위치 타입
	 */
	public void add( Component comp, float widthRate, float heightRate, BorderLocationType locationType ) {
		add( comp, widthRate, heightRate, locationType, highestLayer() + 1 );
	}
	/**
	 * 크기, 우선순위를 정하여 컴포넌트를 추가하는 매서드, 위치 타입은 default로 들어간다
	 * @param comp 추가하고자하는 컴포넌트
	 * @param widthRate 설정하고자하는 width비율
	 * @param heightRate 설정하고자하는 height비율
	 * @param priority 설정하고자하는 우선순위
	 */
	public void add( Component comp, float widthRate, float heightRate, Integer priority ) {
		add( comp, widthRate, heightRate, BorderLocationType.CENTER, priority );
	}
	/**
	 * 크기를 정하여 컴포넌트를 추가하는 매서드, 우선순위와 위치 타입은 default로 들어간다
	 * @param comp 추가하고자하는 컴포넌트
	 * @param widthRate 설정하고자하는 width비율
	 * @param heightRate 설정하고자하는 height비율
	 */
	public void add( Component comp, float widthRate, float heightRate ) {
		add( comp, widthRate, heightRate, BorderLocationType.CENTER, highestLayer() + 1 );
	}
	/**
	 * 위치 타입과 우선순위를 정하여 컴포넌트를 추가하는 매서드, 크기비율은 default로 들어간다
	 * @param comp 추가하고자하는 컴포넌트
	 * @param locationType 설정하고자하는 locationType
	 * @param priority 설정하고자하는 우선순위
	 */
	public void add( Component comp, BorderLocationType locationType, Integer priority ) {
		add( comp, 0.5f, 0.5f, locationType, priority );
	}
	/**
	 * 위치 타입을 정하여 컴포넌트를 추가하는 매서드, 크기 비율과 우선순위는 default로 들어간다
	 * @param comp 추가하고자하는 컴포넌트
	 * @param locationType 설정하고자하는 locationType
	 */
	public void add( Component comp, BorderLocationType locationType ) {
		add( comp, 0.5f, 0.5f, locationType );
	}
	/**
	 * 우선순위를 정하여 컴포넌트를 추가하는 매서드, 크기 비율과 위치 타입은 default로 들어간다
	 * @param comp 추가하고자하는 컴포넌트
	 * @param priority 설정하고자하는 우선순위
	 */
	public void add( Component comp, Integer priority ) {
		add( comp, 0.5f, 0.5f, priority );
	}

	@Override
	public Component add( Component comp ) {
		add( comp, 0.5f, 0.5f );
		return comp;
	}
	
	@Override
	public void remove( int index ) {
		if( index < 0 || index >= getComponentCount() )	throw new IndexOutOfBoundsException();
		
		remove( getComponent( index ) );
	}
	
	@Override
	public void remove( Component comp ) {
		super.remove(comp);
		COMP_MAP.remove(comp);
	}
	
	@Override
	public void removeAll() {
		super.removeAll();
		COMP_MAP.clear();
	}
	
	@Deprecated
	@Override
	public void add( Component comp, Object constraints ) {
		add( comp );
	}
	
	@Deprecated
	@Override
	public void add( Component comp, Object constraints, int priority ) {
		add( comp, new Integer( priority ) );
	}
	
	@Deprecated
	@Override
	public Component add( Component comp, int priority ) {
		add( comp, new Integer(priority) );
		
		return comp;
	}
	
	@Deprecated
	@Override
	public Component add( String name, Component comp ) {
		return add( comp );
	}
	
	@Deprecated
	@Override
	public void setLayout( LayoutManager layoutManager ) {}
	
	@Deprecated
	@Override
	public void setComponentZOrder(Component comp, int index) {
		super.setComponentZOrder(comp, index);
	}
	/**
	 * 본 클래스의 컴포넌트의 너비 비율, 높이 비율, 위치 타입 정보를 가지고 있으며, Bounds(size, location) 정보를 구할 수 있는 클래스
	 * @author Kwon
	 *
	 */
	private class CompBoundsData {
		private float widthRate;
		private float heightRate;
		
		private BorderLocationType locationType;
		
		private CompBoundsData() {
			this( 0.5f, 0.5f, BorderLocationType.CENTER );
		}
		
		private CompBoundsData( float widthRate, float heightRate, BorderLocationType locationType ) {
			setData( widthRate, heightRate, locationType );
		}
		
		private void setData( float widthRate, float heightRate, BorderLocationType locationType ) {
			this.widthRate = isValidRate( widthRate ) ? widthRate : 0.5f;
			this.heightRate = isValidRate( heightRate ) ? heightRate : 0.5f;
			this.locationType = locationType != null ? locationType : BorderLocationType.CENTER;
		}
		
		private Dimension getValidSize() {
			return new Dimension( (int)( getWidth() * widthRate ), (int)( getHeight() * heightRate ) );
		}
		
		private Point getValidLocation() {
			int centerX = ( getWidth() - getValidSize().width ) / 2;
			int centerY = ( getHeight() - getValidSize().height ) / 2;
			
			int x = centerX + centerX * locationType.X_VALUE;
			int y = centerY + centerY * locationType.Y_VALUE;
			
			return new Point( x, y );
		}
		
		private boolean isValidRate( float rate ) {
			return rate > 0.0f && rate <= 1.0f;
		}
	}
	/**
	 * BorderLayeredPane에 추가될 컴포넌트의 위치 타입을 표현하는 enum 클래스
	 * 각 인스턴스는 X_VALUE와 Y_VALUE를 가지고 이 값은 실제 위치 좌표를 계산할 때 사용된다.
	 * CENTER(0,0)를 기준으로  X_VALUE에 1일 경우 오른쪽, -1일 경우 왼쪽이고 Y_VALUE에 1일 경우 아래쪽, Y_VALUE에 -1일 경우 위쪽이다.
	 * @author Kwon
	 *
	 */
	public static enum BorderLocationType {
		NORTH(0, -1),
		SOUTH(0, 1),
		WEST(-1, 0),
		EAST(1, 0),
		CENTER(0, 0),
		
		NORTH_WEST( -1, -1 ),
		NORTH_EAST( 1, -1 ),
		SOUTH_WEST( -1, 1 ),
		SOUTH_EAST( 1, 1 );
		
		private final int X_VALUE;
		private final int Y_VALUE;
		
		private BorderLocationType( int xValue, int yValue ) {
			X_VALUE = xValue;
			Y_VALUE = yValue;
		}
	}
}
