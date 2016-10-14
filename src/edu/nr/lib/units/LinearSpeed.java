package edu.nr.lib.units;

public class LinearSpeed {
				
	private Distance distance;
	private Time time;
	
	public LinearSpeed(Distance distance, Time time) {
		this.time = time;
		this.distance = distance;
	}
	
	public LinearSpeed convertTo(Distance.Unit distanceUnit, Time.Unit timeUnit) {
		return new LinearSpeed(distance.convertTo(distanceUnit), time.convertTo(timeUnit));
	}
	
	public double get(Distance.Unit distanceUnit, Time.Unit timeUnit) {
		return distance.get(distanceUnit) / time.get(timeUnit);
	}
}
