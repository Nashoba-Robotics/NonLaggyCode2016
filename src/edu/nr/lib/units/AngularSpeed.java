package edu.nr.lib.units;

public class AngularSpeed {
				
	private Angle angle;
	private Time time;
	
	public AngularSpeed(Angle angle, Time time) {
		this.time = time;
		this.angle = angle;
	}
	
	public AngularSpeed convertTo(Angle.Unit angleUnit, Time.Unit timeUnit) {
		return new AngularSpeed(angle.convertTo(angleUnit), time.convertTo(timeUnit));
	}
	
	public double get(Angle.Unit angleUnit, Time.Unit timeUnit) {
		return angle.get(angleUnit) / time.get(timeUnit);
	}
}
