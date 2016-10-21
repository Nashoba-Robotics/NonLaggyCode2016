package edu.nr.lib.motionprofiling;

public class SimpleOneDimensionalTrajectory implements Trajectory {
	
	double maxPossibleVelocity;
	double maxUsedVelocity;
	double maxUsedAccel;
	
	double totalTime;
	double timeAccelPlus;
	double timeAccelMinus;
	double timeAtCruise;
	
	double goalDistance;
	
	public SimpleOneDimensionalTrajectory(double goalDistance, double maxPossibleVelocity, double maxUsedVelocity, double maxUsedAccel) {
		this.goalDistance = goalDistance;
		this.maxUsedVelocity = maxUsedVelocity;
		this.maxPossibleVelocity = maxPossibleVelocity;
		this.maxUsedAccel = maxUsedAccel;
		timeAccelPlus = timeAccelMinus = maxUsedVelocity / maxUsedAccel;
		double cruiseDistance = goalDistance - 0.5 * timeAccelPlus * maxUsedVelocity - 0.5 * timeAccelMinus * maxUsedVelocity;
		timeAtCruise = cruiseDistance / maxUsedVelocity;
		totalTime = timeAccelMinus + timeAtCruise + timeAccelMinus;
	}

	public double getGoalVelocity(double time) {
		if(time <= 0) return 0;
		if(time <= timeAccelPlus) return time * maxUsedAccel;
		if(time <= timeAccelPlus + timeAtCruise) return maxUsedVelocity;
		double timeSlowingDownSoFar = time - (timeAccelPlus + timeAtCruise);
		if(time <= totalTime) return maxUsedVelocity + timeSlowingDownSoFar * -maxUsedAccel;
		return 0;
	}
	
	public double getGoalPosition(double time) {		
		if(time <= timeAccelPlus) {
			//We're on the positive slope of the trapezoid
			return 0.5 * time * time * maxUsedAccel;
		}
		
		double speedUpDistance =  0.5 * timeAccelPlus * maxUsedVelocity;
		
		if(time <= timeAccelPlus + timeAtCruise) {
			//We're on the top part of the trapezoid
			double timeAtFullSoFar = time - timeAccelPlus;
			return speedUpDistance + timeAtFullSoFar * maxUsedVelocity;
		}
		
		double fullSpeedDistance = maxUsedVelocity * timeAtCruise;
		
		if(time <= totalTime) {
			//We're on the negative slope of the trapezoid
			double timeSlowingDownSoFar = time - (timeAccelPlus + timeAtCruise);
			return speedUpDistance + fullSpeedDistance 
					+ maxUsedVelocity * timeSlowingDownSoFar 
					- 0.5 * maxUsedAccel * timeSlowingDownSoFar * timeSlowingDownSoFar;
		}
				
		return goalDistance;
	}

	public double getGoalAccel(double time) {
		if(time < 0) return 0;
		if(time < timeAccelPlus) return maxUsedAccel;
		if(time < timeAccelPlus + timeAtCruise) return 0;
		if(time < totalTime) return -maxUsedAccel;
		return 0;
	}
	
	public double getMaxUsedVelocity() {
		return maxUsedVelocity;
	}

	public double getMaxAccel() {
		return maxUsedAccel;
	}

	@Override
	public double getMaxPossibleVelocity() {
		return maxPossibleVelocity;
	}

	@Override
	public double getMaxUsedAccel() {
		return maxUsedAccel;
	}

	
}