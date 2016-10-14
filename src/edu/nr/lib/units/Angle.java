package edu.nr.lib.units;

public class Angle {
		
	public enum Unit {
		RADIAN, DEGREE, REVOLUTION;
		
		double perMeter() {
			switch(this) {
			case REVOLUTION:
				return 1;
			case RADIAN:
				return 2.0*Math.PI;
			case DEGREE:
				return 360;
			default:
				return 1;
			}
		}
		
		static Unit defaultType = Unit.REVOLUTION;
	}
	
	public static Angle zero = new Angle(Unit.defaultType,0);
	
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
		} else if(type == Unit.defaultType) {
			return value / this.type.perMeter();
		} else {
			return get(Unit.defaultType) * type.perMeter();
		}
	}
	
	public double sin() {
		return Math.sin(get(Unit.RADIAN));
	}
	
	public double cos() {
		return Math.cos(get(Unit.RADIAN));
	}
}
