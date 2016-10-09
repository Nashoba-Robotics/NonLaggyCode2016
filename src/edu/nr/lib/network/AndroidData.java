package edu.nr.lib.network;

import edu.nr.lib.units.Angle;

public class AndroidData {

	public Angle turnAngle;
	public double distance;
	public long time;
	
	public AndroidData(Angle turnAngle, double distance, long time) {
		this.turnAngle = turnAngle;
		this.distance = distance;
		this.time = time;
	}
	
	public Angle getTurnAngle() {
		return turnAngle;
	}

	public double getDistance() {
		return distance;
	}

	public long getTime() {
		return time;
	}
	
}
