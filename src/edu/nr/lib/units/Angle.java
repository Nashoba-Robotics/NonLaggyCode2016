package edu.nr.lib.units;

public class Angle {
	
	public enum Unit {
		RADIAN, DEGREE, REVOLUTION;
	}

	public static Unit defaultType = Unit.REVOLUTION;
	
	public static Angle zero = new Angle(Unit.REVOLUTION,0);
	
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
			return value * (1.0/360.0 /*revolutions per degree*/);
		} else if(this.type == Unit.RADIAN && type == Unit.REVOLUTION) {
			return value * (1.0/(2.0*Math.PI) /*revolutions per radian*/);
		} else if(type == Unit.RADIAN) {
			return get(Unit.REVOLUTION) * 2.0*Math.PI /*radians per revolution*/;
		} else if(type == Unit.DEGREE) {
			return get(Unit.REVOLUTION) * 360.0 /*degrees per revolution*/;
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
