import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.nr.lib.AngleUnit;
import edu.nr.lib.Position;

public class PositionTest {

	@Test
	public void testOrigin() {
		Position pos = new Position();
		assertEquals(pos.x,0,0);
		assertEquals(pos.y,0,0);
	}
	
	@Test
	public void testInteger() {
		int x = 1;
		int y = 2;
		Position pos = new Position(x,y);
		
		assertEquals(pos.x,x,0);
		assertEquals(pos.y,y,0);
	}
	
	@Test
	public void testSetInteger() {
		int x = 1;
		int y = 2;
		Position pos = new Position();
		pos.setXY(x, y);
		
		assertEquals(pos.x,x,0);
		assertEquals(pos.y,y,0);
	}
	
	@Test
	public void testDouble() {
		double xD = 1.57925;
		double yD = 2.93723;
		
		Position pos = new Position(xD, yD);
		assertEquals(pos.x,xD,0);
		assertEquals(pos.y,yD,0);
	}
	
	@Test
	public void testSetDouble() {
		double xD = 1.57925;
		double yD = 2.93723;
		
		Position pos = new Position();
		pos.setXY(xD, yD);
		assertEquals(pos.x,xD,0);
		assertEquals(pos.y,yD,0);
	}
	
	@Test
	public void testSetPolarDegree() {
		double angle = 150.43;
		double magnitude = 2.93723;
		
		Position pos = new Position();
		pos.setPolar(magnitude, angle, AngleUnit.DEGREE);
		assertEquals(pos.getAngle(AngleUnit.DEGREE),angle,0.0001);
		assertEquals(pos.getMagnitude(),magnitude,0.0001);
		assertEquals(pos.x, -2.554666, 0.000001);
		assertEquals(pos.y, 1.449483, 0.000001);
	}
	
	@Test
	public void testPolarDegree() {
		double angle = 150.43;
		double magnitude = 2.93723;
		
		Position pos = new Position(magnitude, angle, AngleUnit.DEGREE);
		assertEquals(pos.getAngle(AngleUnit.DEGREE),angle,0.0001);
		assertEquals(pos.getMagnitude(),magnitude,0.0001);
		assertEquals(pos.x, -2.554666, 0.000001);
		assertEquals(pos.y, 1.449483, 0.000001);
	}
	
	@Test
	public void testSetPolarRadian() {
		double angle = 2.4 * Math.PI;
		double magnitude = 2.93723;
		
		Position pos = new Position();
		pos.setPolar(magnitude, angle, AngleUnit.RADIAN);
		assertEquals(pos.getAngle(AngleUnit.RADIAN),angle%(2*Math.PI),0.0001);
		assertEquals(pos.getMagnitude(),magnitude,0.0001);
		assertEquals(pos.x, 0.907654, 0.000001);
		assertEquals(pos.y, 2.793472, 0.000001);
	}
	
	@Test
	public void testPolarRadian() {
		double angle = 2.4 * Math.PI;
		double magnitude = 2.93723;
		
		Position pos = new Position(magnitude, angle, AngleUnit.RADIAN);
		assertEquals(pos.getAngle(AngleUnit.RADIAN),angle%(2*Math.PI),0.0001);
		assertEquals(pos.getMagnitude(),magnitude,0.0001);
		assertEquals(pos.x, 0.907654, 0.000001);
		assertEquals(pos.y, 2.793472, 0.000001);
	}
	
	@Test
	public void testScaleSingle() {
		double scale = 0.2;
		double x = 5.2;
		double y = 3.7;
		Position pos = new Position(x,y);
		pos.scale(scale);
		assertEquals(pos.x, x * scale, 0.00001);
		assertEquals(pos.y, y * scale, 0.00001);
	}
	
	@Test
	public void testScaleDouble() {
		double xScale = 0.2;
		double yScale = 0.5;
		double x = 5.2;
		double y = 3.7;
		Position pos = new Position(x,y);
		pos.scale(xScale,yScale);
		assertEquals(pos.x, x * xScale, 0.00001);
		assertEquals(pos.y, y * yScale, 0.00001);
	}
	
	@Test
	public void testScalePosition() {
		double xScale = 0.2;
		double yScale = 0.5;
		Position scale = new Position(xScale, yScale);
		double x = 5.2;
		double y = 3.7;
		Position pos = new Position(x,y);
		pos.scale(scale);
		assertEquals(pos.x, x * xScale, 0.00001);
		assertEquals(pos.y, y * yScale, 0.00001);
	}
	
	@Test
	public void testRotate() {
		double initialAngle = 3.5 * Math.PI;
		double angle = 2.4 * Math.PI;
		double magnitude = 2.93723;
		
		Position pos = new Position(magnitude, initialAngle, AngleUnit.RADIAN);
		pos.rotate(angle, AngleUnit.RADIAN);
		assertEquals(pos.getAngle(AngleUnit.RADIAN),((angle + initialAngle) % (2*Math.PI)) - (2 * Math.PI),0.0001);
		assertEquals(pos.getMagnitude(),magnitude,0.0001);
		assertEquals(pos.x, magnitude * (Math.cos((angle + initialAngle))), 0.000001);
		assertEquals(pos.y, magnitude * (Math.sin((angle + initialAngle))), 0.000001);
	}
	
	@Test
	public void testClone() {
		double x = 5.1235;
		double y = 8.12376;
		Position pos = new Position(x,y);
		Position posClone = pos.clone();
		assertEquals(posClone.x, pos.x, 0.00001);
		assertEquals(posClone.y, pos.y, 0.00001);
	}

}
