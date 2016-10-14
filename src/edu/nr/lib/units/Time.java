package edu.nr.lib.units;

public class Time {
	
	public enum Unit {
		SECOND, MINUTE,HOUR,HUNDRED_MILLISECONDS;
		
		double perSecond() {
			switch(this) {
			case SECOND:
				return 1;
			case HOUR:
				return 1/3600;
			case MINUTE:
				return 1/60;
			case HUNDRED_MILLISECONDS:
				return 10;
			default:
				return 1;
			}
		}
		
		static Unit defaultType = Unit.SECOND;
	}
	
	public static Time zero = new Time(Unit.defaultType,0);
	
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
		} else if(type == Unit.defaultType) {
			return value / this.type.perSecond();
		} else {
			return get(Unit.defaultType) * type.perSecond();
		}
	}
	
}
