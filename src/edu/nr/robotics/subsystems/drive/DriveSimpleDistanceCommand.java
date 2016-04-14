package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;

/**
 *
 */
public class DriveSimpleDistanceCommand extends NRCommand {

	double distance; // in meters
	double speed; // from 0 to 1

	public DriveSimpleDistanceCommand(double distance, double speed) {
		this.speed = speed;
		this.distance = distance;
		requires(Drive.getInstance());
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinishedNR() {
		System.out.println("Distance: " + FieldCentric.getInstance().getDistance());
		return Math.abs(FieldCentric.getInstance().getDistance()) > Math.abs(distance);
	}

	@Override
	protected void onStart() {
		FieldCentric.getInstance().reset();
	}

	@Override
	protected void onExecute() {
		Drive.getInstance().arcadeDrive(speed, 0);
	}

	@Override
	protected void onEnd(boolean interrupted) {
		Drive.getInstance().tankDrive(0, 0);
	}
}
