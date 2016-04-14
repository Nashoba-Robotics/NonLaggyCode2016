package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.AngleGyroCorrection;
import edu.nr.lib.AngleGyroCorrectionSource;
import edu.nr.lib.NRCommand;

/**
 *
 */
public class DriveSimpleDistanceWithGyroCommand extends NRCommand {

	double distance; // in meters
	double speed; // from 0 to 1
	AngleGyroCorrection gyroCorrection;

	public DriveSimpleDistanceWithGyroCommand(double distance, double speed, AngleGyroCorrectionSource correction) {
		this.speed = speed;
		this.distance = distance;
		gyroCorrection = correction;
		requires(Drive.getInstance());
	}
	
	public DriveSimpleDistanceWithGyroCommand(double distance, double speed) {
		this(distance, speed, null);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinishedNR() {
		return Math.abs(FieldCentric.getInstance().getDistance()) > Math.abs(distance);
	}

	@Override
	protected void onStart() {
		if(gyroCorrection == null)
			gyroCorrection = new AngleGyroCorrection();
		FieldCentric.getInstance().reset();
	}

	@Override
	protected void onExecute() {
		Drive.getInstance().arcadeDrive(speed, gyroCorrection.getTurnValue());
	}

	@Override
	protected void onEnd(boolean interrupted) {
		Drive.getInstance().tankDrive(0, 0);
	}
}
