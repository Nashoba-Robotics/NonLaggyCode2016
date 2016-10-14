package edu.nr.lib.units;

public class Distance {
	
	public enum Unit {
		METER, FOOT, INCH;
		
		double perMeter() {
			switch(this) {
			case METER:
				return 1;
			case FOOT:
				return 3.281;
			case INCH:
				return Unit.FOOT.perMeter() * 12;
			default:
				return 1;
			}
		}
		
		static Unit defaultType = Unit.METER;
	}
	
	public static Distance zero = new Distance(Unit.defaultType,0);
	
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
		} else if(type == Unit.defaultType) {
			return value / this.type.perMeter();
		} else {
			return get(Unit.defaultType) * type.perMeter();
		}
	}
}
