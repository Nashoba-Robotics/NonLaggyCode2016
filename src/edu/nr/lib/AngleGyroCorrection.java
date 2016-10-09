package edu.nr.lib;

import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Unit;
import edu.nr.lib.NavX;

public class AngleGyroCorrection {

	private static final double ANGLE_CORRECTION_INTENSITY = 0.05, MAX_ANGLE_CORRECTION_SPEED = 0.2;
	private boolean initialized = false;
	
	public double getTurnValue(double correctionIntensity)
	{
		if(initialized == false)
		{
			reset();
			initialized = true;
		}
		
		double turn = getAngleError().get(Unit.DEGREE) * correctionIntensity;
    	if(turn<0)
    		turn = Math.max(-MAX_ANGLE_CORRECTION_SPEED, turn);
    	else
    		turn = Math.min(MAX_ANGLE_CORRECTION_SPEED, turn);
    	
    	return turn;
	}
	
	public double getTurnValue()
	{
		return this.getTurnValue(ANGLE_CORRECTION_INTENSITY);
	}
			
	/**
	 * Causes the initial angle value to be reset the next time getTurnValue() is called. Use this in the end() and interrupted()
	 * functions of commands to make sure when the commands are restarted, the initial angle value is reset.
	 */
	public void clearInitialValue()
	{
		initialized = false;
	}
	
	private Angle initialAngle;
	Angle goalAngle;
	NavX navx;
		
	public AngleGyroCorrection(Angle angle, NavX navx) {
		if(navx == null) {
			navx = NavX.getInstance();
		}
		this.navx = navx;
		goalAngle = angle;
		initialAngle = navx.getYaw();
	}
	
	public AngleGyroCorrection(Angle angle) {
		this(angle, NavX.getInstance());
	}
	
	public AngleGyroCorrection(NavX navx) {
		this(Angle.zero, navx);
	}
	
	public AngleGyroCorrection() {
		this(Angle.zero);
	}

	public Angle get() {
		return getAngleError();
	}
	
	public Angle getAngleError()
	{
		//Error is just based off initial angle
    	return new Angle(Unit.REVOLUTION,(navx.getYaw().get(Unit.REVOLUTION) - initialAngle.get(Unit.REVOLUTION)) + goalAngle.get(Unit.REVOLUTION));
	}
	
	public void reset()
	{
		initialAngle = navx.getYaw();
	}
}
