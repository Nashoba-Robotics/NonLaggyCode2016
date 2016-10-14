package edu.nr.lib.units;

public class Angle {
	
	public static final double DEGREES_PER_REV = 360;
	public static final double RADIANS_PER_REV = 2.0*Math.PI;
	
	public enum Unit {
		RADIAN, DEGREE, REVOLUTION;
	}

	public static Unit defaultType = Unit.REVOLUTION;
	
	public static Angle zero = new Angle(defaultType,0);
	
	public Unit type;

	private double value;
	
	public Angle(Unit type, double value) {
		this.type = type;
		this.value = value;
	}
	
	public Angle convertTo(Unit type) {
		return new Angle(type, get(type));
	}
	
	public double get(Unit type) {
		if(this.type == type) {
			return this.value;
		} else if(this.type == Unit.DEGREE && type == Unit.REVOLUTION) {
			return value / DEGREES_PER_REV;
		} else if(this.type == Unit.RADIAN && type == Unit.REVOLUTION) {
			return value / RADIANS_PER_REV;
		} else if(type == Unit.RADIAN) {
			return get(Unit.REVOLUTION) * RADIANS_PER_REV;
		} else if(type == Unit.DEGREE) {
			return get(Unit.REVOLUTION) * DEGREES_PER_REV;
		}
		return 0;
	}
	
	public double sin() {
		return Math.sin(get(Unit.RADIAN));
	}
	
	public double cos() {
		return Math.cos(get(Unit.RADIAN));
	}
}
