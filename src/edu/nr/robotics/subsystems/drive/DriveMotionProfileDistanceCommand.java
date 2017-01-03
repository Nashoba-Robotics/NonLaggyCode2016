package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;

public class DriveMotionProfileDistanceCommand extends NRCommand {

	double distance;
	
	public DriveMotionProfileDistanceCommand(double distance) {
		this.distance = distance;
		requires(Drive.getInstance());
	}

	@Override
	protected void onStart() {
		Drive.getInstance().disableProfiler();
		Drive.getInstance().enableProfiler(distance);
	}
	
	@Override
	protected boolean isFinishedNR() {
		return false;
	}
	
	@Override
	protected void onEnd() {
		Drive.getInstance().disableProfiler();
	}

}
