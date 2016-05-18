package edu.nr.lib.motionprofiling;

import java.util.Timer;
import java.util.TimerTask;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OneDimensionalMotionProfiler extends TimerTask {

	private final Timer timer;
	
	//In milliseconds
	private final long period;
	private static final long defaultPeriod = 5; //200 Hz 
	
	private double prevTime;
	private double startTime;
	
	private boolean enabled = false;
	private PIDOutput out;
	private PIDSource source;
	
	private double ka, kp, kd;
	private double errorLast;
		
	private Trajectory trajectory;
	
	public OneDimensionalMotionProfiler(PIDOutput out, PIDSource source, Trajectory trajectory, double ka, double kp, double kd, long period) {
		this.out = out;
		this.source = source;
		this.period = period;
		this.trajectory = trajectory;
		timer = new Timer();
		timer.schedule(this, 0, this.period);
		prevTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
		this.source.setPIDSourceType(PIDSourceType.kDisplacement);
		this.ka = ka;
		this.kp = kp;
		this.kd = kd;
	}
	
	public OneDimensionalMotionProfiler(PIDOutput out, PIDSource source, Trajectory trajectory, double ka, double kp, double kd) {
		this(out, source, trajectory, ka, kp, kd, defaultPeriod);
	}
	
	@Override
	public void run() {
		if(startTime == 0) {
			startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
		}
		if(enabled) {
			double dt = edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - prevTime;

			double output = 0;
						
			output += trajectory.getGoalVelocity(edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime) / trajectory.maxVelocity;
			
			output += trajectory.getGoalAccel(edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime) * ka;
			
			double error = trajectory.getGoalPosition(edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime) - source.pidGet();
			
			output += error * kp;
			
			output += (error - errorLast) / dt * kd;
			errorLast = error;
			
			out.pidWrite(output);
			
			SmartDashboard.putNumber("Motion Profiler Output", output);
		}
		prevTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
	}
		
	/**
	 * Stop the profiler from running
	 */
	public void disable() {
		enabled = false;
	}
	
	/**
	 * Start the profiler running
	 */
	public void enable() {
		enabled = true;
	}
	
	/**
	 * Reset the previous time to the current time.
	 * TODO: reset motion profiler
	 * Doesn't disable the controller
	 */
	public void reset() {
		errorLast = 0;
		startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
		prevTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
	}

	/**
	 * Sets the PIDOutput for the profiler
	 * @param out
	 */
	public void setOut(PIDOutput out) {
		this.out = out;
	}

	/**
	 * Sets the PIDSource for the profiler
	 * @param source
	 */
	public void setSource(PIDSource source) {
		this.source = source;
	}

	public class Trajectory {
		
		double maxVelocity;
		double maxAccel;
		
		double totalTime;
		double speedUpTime;
		double speedDownTime;
		double timeAtFull;
		
		public Trajectory(double goalDistance, double maxVelocity, double maxAccel) {
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
	
}
