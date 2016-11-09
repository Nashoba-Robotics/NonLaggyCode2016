package edu.nr.lib.motionprofiling;

import java.util.Timer;
import java.util.TimerTask;

import edu.nr.lib.interfaces.DoublePIDOutput;
import edu.nr.lib.interfaces.DoublePIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TwoMotorOneDimensionalMotionProfiler extends TimerTask implements MotionProfiler  {

	private final Timer timer;
	
	//In milliseconds
	private final long period;
	private static final long defaultPeriod = 5; //200 Hz 
	
	private double prevTime;
	private double startTime;
	
	private boolean enabled = false;
	private DoublePIDOutput out;
	private DoublePIDSource source;
	
	private double ka, kp, kd, kv;
	private double errorLastLeft;
	private double errorLastRight;
	
	private double initialPositionLeft;
	private double initialPositionRight;
			
	private Trajectory trajectory;
		
	public TwoMotorOneDimensionalMotionProfiler(DoublePIDOutput out, DoublePIDSource source, double kv, double ka, double kp, double kd, long period) {
		this.out = out;
		this.source = source;
		this.period = period;
		this.trajectory = new SimpleOneDimensionalTrajectory(0,1,1,1);
		timer = new Timer();
		timer.schedule(this, 0, this.period);
		reset();
		this.source.setPIDSourceType(PIDSourceType.kDisplacement);
		this.ka = ka;
		this.kp = kp;
		this.kd = kd;
		this.kv = kv;
		this.initialPositionLeft = source.pidGetLeft();
		this.initialPositionRight = source.pidGetRight();
	}
	
	public TwoMotorOneDimensionalMotionProfiler(DoublePIDOutput out, DoublePIDSource source, double kv, double ka, double kp, double kd) {
		this(out, source, kv, ka, kp, kd, defaultPeriod);
	}
	
	double timeOfVChange = 0;
	double prevV;
	
	@Override
	public void run() {
		if(enabled) {
			double dt = edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - prevTime;
			
			double currentTimeSinceStart = edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime;
			
			double velocityGoal = trajectory.getGoalVelocity(currentTimeSinceStart);
			double positionGoal = trajectory.getGoalPosition(currentTimeSinceStart);
			double accelGoal = trajectory.getGoalAccel(currentTimeSinceStart);
			
			double errorLeft = positionGoal - source.pidGetLeft() + initialPositionLeft;			
			double errorDerivLeft = (errorLeft - errorLastLeft) / dt;
			double outputLeft = velocityGoal * kv + accelGoal * ka + errorLeft * kp + errorDerivLeft * kd;
			errorLastLeft = errorLeft;
			
			double errorRight = positionGoal - source.pidGetRight() + initialPositionRight;			
			double errorDerivRight = (errorRight - errorLastRight) / dt;
			double outputRight = velocityGoal * kv + accelGoal * ka + errorRight * kp + errorDerivRight * kd;
			errorLastRight = errorRight;
			
			out.pidWrite(outputLeft, outputRight);
			
			source.setPIDSourceType(PIDSourceType.kRate);
			SmartDashboard.putString("Motion Profiler V Left", source.pidGetLeft() + ":" + outputLeft * trajectory.getMaxPossibleVelocity() * Math.signum(trajectory.getMaxPossibleVelocity()));
			SmartDashboard.putString("Motion Profiler V Right", source.pidGetRight() + ":" + outputRight * trajectory.getMaxPossibleVelocity() * Math.signum(trajectory.getMaxPossibleVelocity()));
			source.setPIDSourceType(PIDSourceType.kDisplacement);
			SmartDashboard.putString("Motion Profiler X Left", source.pidGetLeft() + ":" + (positionGoal + initialPositionLeft));
			SmartDashboard.putString("Motion Profiler X Right", source.pidGetRight() + ":" + (positionGoal + initialPositionRight));
		}
		
		prevTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
	}
		
	/**
	 * Stop the profiler from running and resets it
	 */
	public void disable() {
		enabled = false;
		reset();
	}
	
	/**
	 * Reset the profiler and start it running
	 */
	public void enable() {
		enabled = true;
		reset();
	}
	
	/**
	 * Reset the previous time to the current time.
	 * Doesn't disable the controller
	 */
	public void reset() {
		errorLastLeft = 0;
		errorLastRight = 0;
		startTime = prevTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
		PIDSourceType type = source.getPIDSourceType();
		source.setPIDSourceType(PIDSourceType.kDisplacement);
		initialPositionLeft = source.pidGetLeft();
		initialPositionRight = source.pidGetRight();
		source.setPIDSourceType(type);
	}
	
	/**
	 * Sets the trajectory for the profiler
	 * @param trajectory
	 */
	public void setTrajectory(Trajectory trajectory) {
		this.trajectory = trajectory;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public Trajectory getTrajectory() {
		return trajectory;
	}
	
}
