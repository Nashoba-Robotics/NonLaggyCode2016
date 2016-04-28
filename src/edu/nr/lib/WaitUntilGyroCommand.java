package edu.nr.lib;

import edu.nr.robotics.auton.AutonFollowInstructionsShootCommand.GetGyro;

/**
 *
 */
public class WaitUntilGyroCommand extends NRCommand {

	AngleGyroCorrection gyroCorrection;
	double angle;
	GetGyro gyro;

	public WaitUntilGyroCommand(double angle) {
		this.angle = angle;
	}

	public WaitUntilGyroCommand(double angle, GetGyro gyro) {
		this.angle = angle;
		this.gyro = gyro;
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinishedNR() {
		System.out.println("Error: " + (gyroCorrection.get() - angle));
		return gyroCorrection.get() > angle;
	}

	@Override
	protected void onStart() {
        if(gyro != null)
        	gyroCorrection = gyro.getCorrection();
        else
            gyroCorrection = new AngleGyroCorrection();
	}
}
