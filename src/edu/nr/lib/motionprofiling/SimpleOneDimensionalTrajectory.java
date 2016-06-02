package edu.nr.lib.motionprofiling;

public class SimpleOneDimensionalTrajectory implements Trajectory {
	
	double maxVelocity;
	double maxAccel;
	
	double totalTime;
	double timeAccelPlus;
	double timeAccelMinus;
	double timeAtCruise;
	
	public SimpleOneDimensionalTrajectory(double goalDistance, double maxVelocity, double maxAccel) {
		this.maxVelocity = maxVelocity;
		this.maxAccel = maxAccel;
		timeAccelPlus = timeAccelMinus = maxVelocity / maxAccel;
		double timeToEndOfFull = goalDistance / maxVelocity;
		totalTime = timeToEndOfFull + timeAccelMinus;
		timeAtCruise = timeToEndOfFull - timeAccelPlus;
		if(1/2 * maxVelocity * timeAccelPlus + 1/2 * maxVelocity * timeAccelMinus + maxVelocity * timeAtCruise != goalDistance) {
			System.err.println("Something's gone wrong in trajectory calculation!!! goal: " + goalDistance + " I say: " + (1/2 * maxVelocity * timeAccelPlus + 1/2 * maxVelocity * timeAccelMinus + maxVelocity * timeAtCruise));
		}
		
		if(timeAccelPlus + timeAccelMinus + timeAtCruise != totalTime) {
			System.err.println("Something's gone wrong in trajectory calculation (part 2)!!! total time: " + totalTime + " I say: " + (timeAccelPlus + timeAccelMinus + timeAtCruise));
		}
	}

	public double getGoalVelocity(double time) {
		if(time < 0) return 0;
		if(time < timeAccelPlus) return time * maxAccel;
		if(time < timeAccelPlus + timeAtCruise) return maxVelocity;
		double timeSlowingDownSoFar = time - (timeAccelPlus + timeAtCruise);
		if(time < totalTime) return maxVelocity + timeSlowingDownSoFar * -maxAccel;
		return 0;
	}
	
	public double getGoalPosition(double time) {
		if(time < 0) return 0;
		
		if(time < timeAccelPlus) {
			//We're on the positive slope of the trapezoid
			return 1/2 * time * time * maxAccel;
		}
		
		double speedUpDistance =  1/2 * timeAccelPlus * maxVelocity;
		
		if(time < timeAccelPlus + timeAtCruise) {
			//We're on the top part of the trapezoid
			double timeAtFullSoFar = time - timeAccelPlus;
			return speedUpDistance + timeAtFullSoFar * maxVelocity;
		}
		
		double fullSpeedDistance = maxVelocity * timeAtCruise;
		
		if(time < totalTime) {
			//We're on the negative slope of the trapezoid
			double timeSlowingDownSoFar = time - (timeAccelPlus + timeAtCruise);
			double currentVelocity = maxVelocity + timeSlowingDownSoFar * -maxAccel;
			return speedUpDistance + fullSpeedDistance + (maxVelocity + currentVelocity)/2 * timeSlowingDownSoFar;
		}
		
		double speedDownDistance = 1/2 * timeAccelMinus * maxVelocity;
		
		return speedUpDistance + fullSpeedDistance + speedDownDistance;
	}

	public double getGoalAccel(double time) {
		if(time < 0) return 0;
		if(time < timeAccelPlus) return maxAccel;
		if(time < timeAccelPlus + timeAtCruise) return 0;
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