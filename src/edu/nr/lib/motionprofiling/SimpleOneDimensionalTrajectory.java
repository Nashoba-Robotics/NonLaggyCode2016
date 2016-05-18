package edu.nr.lib.motionprofiling;

public class SimpleOneDimensionalTrajectory implements Trajectory {
	
	double maxVelocity;
	double maxAccel;
	
	double totalTime;
	double speedUpTime;
	double speedDownTime;
	double timeAtFull;
	
	public SimpleOneDimensionalTrajectory(double goalDistance, double maxVelocity, double maxAccel) {
		this.maxVelocity = maxVelocity;
		this.maxAccel = maxAccel;
		speedUpTime = speedDownTime = maxVelocity / maxAccel;
		double timeToEndOfFull = goalDistance / maxVelocity;
		totalTime = timeToEndOfFull + speedDownTime;
		timeAtFull = timeToEndOfFull - speedUpTime;
		if(1/2 * maxVelocity * speedUpTime + 1/2 * maxVelocity * speedDownTime + maxVelocity * timeAtFull != goalDistance) {
			System.err.println("Something's gone wrong in trajectory calculation!!!");
		}
	}

	public double getGoalVelocity(double time) {
		if(time < 0) return 0;
		if(time < speedUpTime) return time * maxAccel;
		if(time < speedUpTime + timeAtFull) return maxVelocity;
		double timeSlowingDownSoFar = time - (speedUpTime + timeAtFull);
		if(time < totalTime) return timeSlowingDownSoFar * -maxAccel;
		return 0;
	}
	
	public double getGoalPosition(double time) {
		if(time < 0) return 0;
		
		if(time < speedUpTime) {
			//We're on the positive slope of the trapezoid
			return 1/2 * time * time * maxAccel;
		}
		
		double speedUpDistance =  1/2 * speedUpTime * maxVelocity;
		
		if(time < speedUpTime + timeAtFull) {
			//We're on the top part of the trapezoid
			double timeAtFullSoFar = time - speedUpTime;
			return speedUpDistance + timeAtFullSoFar * maxVelocity;
		}
		
		double fullSpeedDistance = maxVelocity * timeAtFull;
		
		if(time < totalTime) {
			//We're on the negative slope of the trapezoid
			double timeSlowingDownSoFar = time - (speedUpTime + timeAtFull);
			double currentVelocity = maxVelocity + timeSlowingDownSoFar * -maxAccel;
			return speedUpDistance + fullSpeedDistance + (maxVelocity + currentVelocity)/2 * timeSlowingDownSoFar;
		}
		
		double speedDownDistance = 1/2 * speedDownTime * maxVelocity;
		
		return speedUpDistance + fullSpeedDistance + speedDownDistance;
	}

	public double getGoalAccel(double time) {
		if(time < 0) return 0;
		if(time < speedUpTime) return maxAccel;
		if(time < speedUpTime + timeAtFull) return 0;
		if(time < totalTime) return -maxAccel;
		return 0;
	}
	
	public double getMaxVelocity() {
		return maxVelocity;
	}

	public double getMaxAccel() {
		return maxAccel;
	}

	
}