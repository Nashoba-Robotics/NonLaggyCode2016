package edu.nr.lib.units;

public class Distance {
	
	private static final double METERS_PER_FOOT = 0.3048;
	private static final double INCHES_PER_FOOT = 12;
	
	public enum Unit {
		METER, FOOT, INCH;
	}

	public static Unit defaultType = Unit.METER;
	
	public static Distance zero = new Distance(defaultType,0);
	
	public Unit type;

	private double value;
	
	public Distance(Unit type, double value) {
		this.type = type;
		this.value = value;
	}
	
	public Distance convertTo(Unit type) {
		return new Distance(type, get(type));
	}
	
	public double get(Unit type) {
		if(this.type == type) {
			return this.value;
		} else if(this.type == Unit.FOOT && type == Unit.METER) {
			return value * METERS_PER_FOOT;
		} else if(this.type == Unit.INCH && type == Unit.METER) {
			return value * METERS_PER_FOOT / INCHES_PER_FOOT;
		} else if(type == Unit.FOOT) {
			return get(Unit.METER) / METERS_PER_FOOT;
		} else if(type == Unit.INCH) {
			return get(Unit.METER) / (METERS_PER_FOOT / INCHES_PER_FOOT);
		}
		return 0;
	}
}
