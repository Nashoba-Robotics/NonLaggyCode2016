package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;
import edu.nr.lib.navx.NavX;

/**
 *
 */
public class DriveAccelerometerDistanceCommand extends NRCommand {

	double distance; // in meters
	double speed; // from 0 to 1
	
	double distCurrent;
	double velCurrent;

	public DriveAccelerometerDistanceCommand(double distance, double speed) {
		this.speed = speed;
		this.distance = distance;
		requires(Drive.getInstance());
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinishedNR() {
		return distCurrent > distance;
	}

	@Override
	protected void onStart() {
		distCurrent = 0;
		velCurrent = 0;
		FieldCentric.getInstance().reset();
	}

	@Override
	protected void onExecute() {
		velCurrent += NavX.getInstance().getX();
		distCurrent += velCurrent;
		Drive.getInstance().arcadeDrive(speed, 0);
	}

	@Override
	protected void onEnd(boolean interrupted) {
		Drive.getInstance().tankDrive(0, 0);
	}
}
