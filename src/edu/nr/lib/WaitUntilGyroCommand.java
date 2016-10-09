package edu.nr.lib;

import edu.nr.lib.units.Angle;
import edu.nr.lib.units.Angle.Unit;
import edu.nr.robotics.auton.AutonFollowInstructionsShootCommand.GetGyro;

/**
 *
 */
public class WaitUntilGyroCommand extends NRCommand {

	AngleGyroCorrection gyroCorrection;
	Angle angle;
	GetGyro gyro;

	public WaitUntilGyroCommand(Angle angle) {
		this.angle = angle;
	}

	public WaitUntilGyroCommand(Angle angle, GetGyro gyro) {
		this.angle = angle;
		this.gyro = gyro;
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinishedNR() {
		System.out.println("Error: " + (gyroCorrection.get().get(Unit.REVOLUTION) - angle.get(Unit.REVOLUTION)));
		if (angle.get(Unit.REVOLUTION) > 0)
			return gyroCorrection.get().get(Unit.REVOLUTION) > angle.get(Unit.REVOLUTION);
		else
			return gyroCorrection.get().get(Unit.REVOLUTION) < angle.get(Unit.REVOLUTION);
	}

	@Override
	protected void onStart() {
        if(gyro != null)
        	gyroCorrection = gyro.getCorrection();
        else
            gyroCorrection = new AngleGyroCorrection();
	}
}
