package edu.nr.lib.motionprofiling;

import java.util.Timer;
import java.util.TimerTask;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OneDimensionalMotionProfiler extends TimerTask implements MotionProfiler  {

	private final Timer timer;
	
	//In milliseconds
	private final long period;
	private static final long defaultPeriod = 5; //200 Hz 
	
	private double prevTime;
	private double startTime;
	
	private boolean enabled = false;
	private PIDOutput out;
	private PIDSource source;
	
	private double ka, kp, kd, kv;
	private double errorLast;
	
	private double initialPosition;
		
	private Trajectory trajectory;
	
	public OneDimensionalMotionProfiler(PIDOutput out, PIDSource source, Trajectory trajectory, double ka, double kp, double kd, double kvMult, long period) {
		this.out = out;
		this.source = source;
		this.period = period;
		this.trajectory = trajectory;
		timer = new Timer();
		timer.schedule(this, 0, this.period);
		reset();
		this.source.setPIDSourceType(PIDSourceType.kDisplacement);
		this.ka = ka;
		this.kp = kp;
		this.kd = kd;
		this.kv = 1/trajectory.getMaxPossibleVelocity() * kvMult;
	}
	
	public OneDimensionalMotionProfiler(PIDOutput out, PIDSource source, Trajectory trajectory, double ka, double kp, double kd, double kvMult) {
		this(out, source, trajectory, ka, kp, kd, kvMult, defaultPeriod);
	}
	
	double timeOfVChange = 0;
	double prevV;
	
	@Override
	public void run() {
		if(enabled) {
			double dt = edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - prevTime;
						
			double velocityGoal = trajectory.getGoalVelocity(edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime);
			
			double accelGoal = trajectory.getGoalAccel(edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime);
			
			double error = trajectory.getGoalPosition(edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime) - source.pidGet();
						
			double errorDeriv = (error - errorLast) / dt;
			
			double output = velocityGoal * kv + accelGoal * ka + error * kp + errorDeriv * kd;
			
			out.pidWrite(output);

			errorLast = error;

			source.setPIDSourceType(PIDSourceType.kRate);
			SmartDashboard.putString("Motion Profiler V", source.pidGet() + ":" + output * trajectory.getMaxPossibleVelocity());
			source.setPIDSourceType(PIDSourceType.kDisplacement);
			SmartDashboard.putString("Motion Profiler X", source.pidGet() + ":" 
					+ (trajectory.getGoalPosition(edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime) + initialPosition));
		}
		
		if(source.pidGet() != prevV) {
			//System.out.println("delta t for motion profiler: " + (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - timeOfVChange));
			timeOfVChange = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
			prevV = source.pidGet();
		}
		System.out.println("delta t for motion profiler: " + (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - prevTime));

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
		errorLast = 0;
		startTime = prevTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
		PIDSourceType type = source.getPIDSourceType();
		source.setPIDSourceType(PIDSourceType.kDisplacement);
		initialPosition = source.pidGet();
		source.setPIDSourceType(type);
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
	
	/**
	 * Sets the trajectory for the profiler
	 * @param trajectory
	 */
	public void setTrajectory(Trajectory trajectory) {
		this.trajectory = trajectory;
	}
	
}
