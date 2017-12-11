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
 * 위치는 NORTH_WEST, NORTH, NORTH_EAST, WEST, CENTER, EAST, SOUTH_WEST, SOUTH, SOUTH_EAST로 총 9종류가 있다.
 * 본 클래스의 LayoutManager는 필요하지 않기 때문에 setLayout매서드를 막아놓았다.
 * 본 클래스의 컴포넌트 순서는 우선순위에 의존적이므로 컴포넌트의 index를 직접적으로 변경하는 setComponentZOrder매서드에 @Deprecated 처리를 하였다.(사용은 가능함)
 * 본 클래스에서 무의미한 매개변수를 입력받는 add매서드를 @Deprecated 처리하였다.
 * @author Kwon
 *
 */
public class BorderLayeredPane extends JLayeredPane {
	private final Map<Component, CompBoundsData> COMP_MAP = new HashMap<>();
	
	private final CompBoundsUpdater COMP_BOUNDS_UPDATER = new CompBoundsUpdater();
	
	@Override
	public void paint( Graphics g ) {
		super.paint(g);
		
		for( Component comp : getComponents() )	COMP_BOUNDS_UPDATER.updateComponent( comp );
		
		if( COMP_BOUNDS_UPDATER.isUpdated ) {
			repaint();
			revalidate();
		}
	}
	/**
	 * 컴포넌트의 크기를 변경하는 매서드
	 * @param comp 크기를 변경하고자하는 컴포넌트
	 * @param widthRate 변경하고자하는 width의 비율
	 * @param heightRate 변경하고자하는 height의 비율
	 */
	public void resizeComponent( Component comp, float widthRate, float heightRate ) {
		COMP_MAP.get(comp).setSizeRate( widthRate, heightRate );
		
		repaint();
	}
	/**
	 * 컴포넌트의 크기를 변경하는 매서드
	 * @param index 크기를 변경하고자하는 컴포넌트의 인덱스
	 * @param widthRate 변경하고자하는 width의 비율
	 * @param heightRate 변경하고자하는 height의 비율
	 */
	public void resizeComponent( int index, float widthRate, float heightRate ) {
		if( index < 0 && index >= getComponentCount() )	throw new IndexOutOfBoundsException();
		
		resizeComponent( getComponent(index), widthRate, heightRate );
	}
	/**
	 * 컴포넌트의 위치를 변경하는 매서드
	 * @param comp 위치를 변경하고자하는 컴포넌트
	 * @param locationType 변경하고자하는 위치의 타입
	 */
	public void relocateComponent( Component comp, CompLocationType locationType ) {
		COMP_MAP.get(comp).setLocationType( locationType );
		
		repaint();
	}
	/**
	 * 컴포넌트의 위치를 변경하는 매서드
	 * @param index 위치를 변경하고자하는 컴포넌트의 인덱스
	 * @param locationType 변경하고자하는 위치의 타입
	 */
	public void relocateComponent( int index, CompLocationType locationType ) {
		if( index < 0 && index >= getComponentCount() )	throw new IndexOutOfBoundsException();
		
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
	public void add( Component comp, float widthRate, float heightRate, CompLocationType locationType, Integer priority ) {
		if( comp == null )	throw new IllegalArgumentException( "comp is null" );
		
		priority = ( priority == null ) ? highestLayer() + 1 : priority;
		
		super.add( comp, priority );
		COMP_MAP.put( comp, new CompBoundsData( widthRate, heightRate, locationType ) );
	}
	/**
	 * 크기, 위치를 정하여 컴포넌트를 추가하는 매서드, 우선순위는 default로 들어간다
	 * @param comp 추가하고자하는 컴포넌트
	 * @param widthRate 설정하고자하는 width비율
	 * @param heightRate 설정하고자하는 height비율
	 * @param locationType 설정하고자하는 위치 타입
	 */
	public void add( Component comp, float widthRate, float heightRate, CompLocationType locationType ) {
		add( comp, widthRate, heightRate, locationType, null );
	}
	/**
	 * 크기, 우선순위를 정하여 컴포넌트를 추가하는 매서드, 위치 타입은 default로 들어간다
	 * @param comp 추가하고자하는 컴포넌트
	 * @param widthRate 설정하고자하는 width비율
	 * @param heightRate 설정하고자하는 height비율
	 * @param priority 설정하고자하는 우선순위
	 */
	public void add( Component comp, float widthRate, float heightRate, Integer priority ) {
		add( comp, widthRate, heightRate, null, priority );
	}
	/**
	 * 크기를 정하여 컴포넌트를 추가하는 매서드, 우선순위와 위치 타입은 default로 들어간다
	 * @param comp 추가하고자하는 컴포넌트
	 * @param widthRate 설정하고자하는 width비율
	 * @param heightRate 설정하고자하는 height비율
	 */
	public void add( Component comp, float widthRate, float heightRate ) {
		add( comp, widthRate, heightRate, null, null );
	}
	/**
	 * 위치 타입과 우선순위를 정하여 컴포넌트를 추가하는 매서드, 크기비율은 default로 들어간다
	 * @param comp 추가하고자하는 컴포넌트
	 * @param locationType 설정하고자하는 locationType
	 * @param priority 설정하고자하는 우선순위
	 */
	public void add( Component comp, CompLocationType locationType, Integer priority ) {
		add( comp, 0, 0, locationType, priority );
	}
	/**
	 * 위치 타입을 정하여 컴포넌트를 추가하는 매서드, 크기 비율과 우선순위는 default로 들어간다
	 * @param comp 추가하고자하는 컴포넌트
	 * @param locationType 설정하고자하는 locationType
	 */
	public void add( Component comp, CompLocationType locationType ) {
		add( comp, 0, 0, locationType );
	}
	/**
	 * 우선순위를 정하여 컴포넌트를 추가하는 매서드, 크기 비율과 위치 타입은 default로 들어간다
	 * @param comp 추가하고자하는 컴포넌트
	 * @param priority 설정하고자하는 우선순위
	 */
	public void add( Component comp, Integer priority ) {
		add( comp, 0, 0, priority );
	}

	@Override
	public Component add( Component comp ) {
		add( comp, 0, 0 );
		return comp;
	}
	
	@Override
	public void remove( int index ) {
		remove( getComponent( index ) );
	}
	
	@Override
	public void remove( Component comp ) {
		super.remove(comp);
		COMP_MAP.remove( comp );
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
	public void setLayout( LayoutManager layoutManager ) {
		super.setLayout( null );
	}
	
	@Deprecated
	@Override
	public void setComponentZOrder(Component comp, int index) {
		super.setComponentZOrder(comp, index);
	}
	/**
	 * 본 클래스의 컴포넌트의 Bounds(size, location) 정보를 가지고 있는 클래스
	 * @author Kwon
	 *
	 */
	private static class CompBoundsData {
		private float widthRate;
		private float heightRate;
		
		private CompLocationType locationType;
		
		private CompBoundsData() {
			this( 0, 0, null );
		}
		
		private CompBoundsData( float widthRate, float heightRate, CompLocationType locationType ) {
			setSizeRate( widthRate, heightRate );
			setLocationType( locationType );
		}
		
		private void setSizeRate( float widthRate, float heightRate ) {
			this.widthRate = isValidRate( widthRate ) ? widthRate : 0.5f;
			this.heightRate = isValidRate( heightRate ) ? heightRate : 0.5f;
		}
		
		private void setLocationType( CompLocationType locationType ) {
			this.locationType = locationType != null ? locationType : CompLocationType.CENTER;
		}
		
		private boolean isValidRate( float rate ) {
			return rate > 0.0f && rate <= 1.0f;
		}
	}
	/**
	 * 본 클래스가 paint될 시점에 컴포넌트의 크기와 위치를 최신화시켜주는 클래스
	 * @author Kwon
	 *
	 */
	private class CompBoundsUpdater {
		private final Dimension UPDATED_SIZE = new Dimension();	// paint 시점에서 컴포넌트가 가져야할 사이즈
		
		private boolean isUpdated;		// paint시점에서 컴포넌트의 사이즈에 변화가 있어서 update가 진행되었는지를 판단하는 변수
		
		private Dimension getValidSize( float widthRate, float heightRate ) {
			return new Dimension( (int)( getWidth() * widthRate ), (int)( getHeight() * heightRate ) );
		}
		
		private Point getValidLocation( Dimension compSize, CompLocationType locationType ) {
			int centerX = ( getWidth() - compSize.width ) / 2;
			int centerY = ( getHeight() - compSize.height ) / 2;
			
			int x = centerX + centerX * locationType.X_VALUE;
			int y = centerY + centerY * locationType.Y_VALUE;
			
			return new Point( x, y );
		}
		
		private void updateComponent( Component comp ) {
			isUpdated = false;
			
			CompBoundsData compBoundsData = COMP_MAP.get( comp );
			UPDATED_SIZE.setSize( getValidSize( compBoundsData.widthRate, compBoundsData.heightRate ) );
			
			boolean isSizeChanged = comp.getSize().getWidth() != UPDATED_SIZE.getWidth() || comp.getSize().getHeight() != UPDATED_SIZE.getHeight();
			// 컴포넌트의 원래 사이즈와 paint시점에서 가져야할 사이즈가 같은지 다른지를 판단하는 변수
			if( !isSizeChanged )	return;		// 사이즈의 변화가 없다면 그대로 매서드 종료
			
			comp.setSize( UPDATED_SIZE );
			comp.setLocation( getValidLocation( UPDATED_SIZE, compBoundsData.locationType ) );
				
			isUpdated = true;
		}
	}
	/**
	 * BorderLayeredPane에 추가될 컴포넌트의 위치 타입을 표현하는 enum 클래스 
	 * @author Kwon
	 *
	 */
	public static enum CompLocationType {
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
		
		private CompLocationType( int xValue, int yValue ) {
			X_VALUE = xValue;
			Y_VALUE = yValue;
		}
	}
}
