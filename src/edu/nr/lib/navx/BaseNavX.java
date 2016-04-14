package edu.nr.lib.navx;

import edu.nr.lib.AngleUnit;

public interface BaseNavX {

	/**
	 * Gets the current yaw of the robot in the given units
	 * @param unit the {@link AngleUnit} to return in
	 * @return the yaw
	 */
	public double getYaw(AngleUnit unit);
	
	public double getYawAbsolute(AngleUnit unit);

	/**
	 * Gets the current roll of the robot in the given units
	 * @param unit the {@link AngleUnit} to return in
	 * @return the roll
	 */
	public double getRoll(AngleUnit unit);

	/**
	 * Gets the current pitch of the robot in the given units
	 * @param unit the {@link AngleUnit} to return in
	 * @return the pitch
	 */
	public double getPitch(AngleUnit unit);
	
	public double getX();

	public double getY();

	public double getZ();
}
