package edu.nr.lib;

/**
 *
 */
public class WaitUntilGyroCommand extends NRCommand {

	AngleGyroCorrection gyroCorrection;
	double angle;

	public WaitUntilGyroCommand(double angle) {
		this.angle = angle;
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinishedNR() {
		System.out.println("Error: " + (gyroCorrection.get() - angle));
		return gyroCorrection.get() > angle;
	}

	@Override
	protected void onStart() {
        gyroCorrection = new AngleGyroCorrection();
	}
}
