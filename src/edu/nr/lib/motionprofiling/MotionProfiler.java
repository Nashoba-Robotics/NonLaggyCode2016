package edu.nr.lib.motionprofiling;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public interface MotionProfiler {

	public void run();
	
	/**
	 * Stop the profiler from running
	 */
	public void disable();
	
	/**
	 * Start the profiler running
	 */
	public void enable();
	
	/**
	 * Reset the controller.
	 * Doesn't disable the controller
	 */
	public void reset();
	
	/**
	 * Sets the trajectory for the profiler
	 * @param trajectory
	 */
	public void setTrajectory(Trajectory trajectory);

	public boolean isEnabled();

	public Trajectory getTrajectory();
}
