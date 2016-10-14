package edu.nr.lib.units;

public class Time {
	
	public static final double SECONDS_PER_MINUTE = 60;
	public static final double SECONDS_PER_HOUR = 3600;
	
	public enum Unit {
		HOUR, SECOND, MINUTE;
	}

	public static Unit defaultType = Unit.SECOND;
	
	public static Time zero = new Time(defaultType,0);
	
	public Unit type;

	private double value;
	
	public Time(Unit type, double value) {
		this.type = type;
		this.value = value;
	}
	
	public Time convertTo(Unit type) {
		return new Time(type, get(type));
	}
	
	public double get(Unit type) {
		if(this.type == type) {
			return this.value;
		} else if(this.type == Unit.HOUR && type == Unit.SECOND) {
			return value * SECONDS_PER_HOUR;
		} else if(this.type == Unit.MINUTE && type == Unit.SECOND) {
			return value * SECONDS_PER_MINUTE;
		} else if(type == Unit.MINUTE) {
			return get(Unit.SECOND) / SECONDS_PER_MINUTE;
		} else if(type == Unit.HOUR) {
			return get(Unit.SECOND) / SECONDS_PER_HOUR;
		}
		return 0;
	}
	
}
