package edu.nr.lib;

import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Unit;

public class Position {
	//Based off of https://github.com/Team488/SeriouslyCommonLib/blob/master/src/xbot/common/math/XYPair.java
	public static final Position ZERO = new Position();
	
	public double x;
	public double y;

	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Position() {
		this(0,0);
	}
	
	public Position setXY(double x, double y) {
		this.x = x;
		this.y = y;
		
		return this;
	}
	
	public Position(double magnitude, Angle angle) {
        x = magnitude * Math.cos(angle.get(Unit.RADIAN));
        y = magnitude * Math.sin(angle.get(Unit.RADIAN));
	}
	
	/**
	 * Set the coordinates using polar coordinates
	 * @param magnitude
	 * @param angle
	 * @param unit the unit for the coordinates
	 */
	public Position setPolar(double magnitude, Angle angle) {
        x = magnitude * Math.cos(angle.get(Unit.RADIAN));
        y = magnitude * Math.sin(angle.get(Unit.RADIAN));
        
        return this;
	}
	
	public Position scale(double magnitude) {
		x *= magnitude;
		y *= magnitude;
		return this;
	}
	
	public Position scale(double xMagnitude, double yMagnitude) {
		x *= xMagnitude;
		y *= yMagnitude;
		return this;
	}
	
	public Position scale(Position scale) {
		x *= scale.x;
		y *= scale.y;
		
		return scale;
	}
	
	/**
     * Rotates the current coordinates by a given angle.
     * 
     * @param angle the angle to rotate the pair by
     * @param unit the unit of the angle
     * @return the rotated object
     */
    public Position rotate(Angle angle) {
		double mag = getMagnitude();
		double currentAngle = getAngle().get(Unit.RADIAN);
        x = mag * Math.cos(angle.get(Unit.RADIAN) + currentAngle);
        y = mag * Math.sin(angle.get(Unit.RADIAN) + currentAngle);

        return this;
    }
    
    /**
     * Gets the current angle
     * @param unit the AngleUnit to return in
     * @return the angle in radians
     */
    public Angle getAngle() {
        return new Angle(Unit.RADIAN,Math.atan2(y, x));
    }

    /**
     * Gets the polar magnitude of the position
     * (The length of the line from 0,0 to the current position)
     * @return the magnitude
     */
    public double getMagnitude() {
        return Math.hypot(x, y);
    }
    
    public Position add(Position pair) {
        x += pair.x;
        y += pair.y;
        return this;
    }
    
    public Position add(double x, double y) {
    	this.x += x;
    	this.y += y;
    	return this;
    }
	
	@Override
	public Position clone() {
		return new Position(x, y);
	}

    @Override
    public String toString() {
        return "(X:" + x + ", Y:" + y + ")";
    }
}
