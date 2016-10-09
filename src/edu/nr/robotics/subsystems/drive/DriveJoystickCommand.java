package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.AngleGyroCorrection;
import edu.nr.lib.NRCommand;
import edu.nr.lib.NRMath;
import edu.nr.robotics.DrivingMode;
import edu.nr.robotics.OI;

/**
 *
 */
public class DriveJoystickCommand extends NRCommand {
	private double oldTurn;
	
	AngleGyroCorrection gyroCorrection;

	public DriveJoystickCommand() {
		requires(Drive.getInstance());
	}

	@Override
	protected void onStart() {
        gyroCorrection = new AngleGyroCorrection();
		oldTurn = OI.getInstance().getArcadeMoveValue();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void onExecute() {
		if (OI.getInstance().drivingModeChooser.getSelected().equals(DrivingMode.ARCADE)) {
			double moveValue = OI.getInstance().getArcadeMoveValue();
			
			double rotateAdjustValue = OI.getInstance().getTurnAdjust();

			double rotateValue = OI.getInstance().getArcadeTurnValue() * rotateAdjustValue;

			moveValue = NRMath.squareWithSign(moveValue);
			rotateValue = NRMath.squareWithSign(rotateValue);

			if(Math.abs(rotateValue) < 0.05)
	    	{
	    		if (Math.abs(moveValue) > .1)
	    		{
	    			rotateValue = gyroCorrection.getTurnValue();
	    		}
		    	else
		    	{	    		
		    		gyroCorrection.clearInitialValue();
		    	}
	    	}
	    	else
	    	{	    		
	    		gyroCorrection.clearInitialValue();

	    	}
			
			/*double negInertia = rotateValue - oldTurn;

			// Negative inertia! TODO: Fix NegInertia
			double negInertiaScalar;

			if (rotateValue * negInertia > 0) {
				negInertiaScalar = 0.5;
			} else {
				if (Math.abs(rotateValue) > 0.65) {
					negInertiaScalar = 1.0;
				} else {
					negInertiaScalar = 0.6;
				}
			}

			rotateValue = rotateValue + negInertia * negInertiaScalar;*/

			Drive.getInstance().arcadeDrive(moveValue,rotateValue,true);

			oldTurn = rotateValue;
		} else {
			// Get values of the joysticks
			double left = OI.getInstance().getTankLeftValue();
			double right = OI.getInstance().getTankRightValue();

			// Do the math for turning
			if (Math.abs(left - right) < .25) {
				left = (Math.abs(left) + Math.abs(right)) / 2 * Math.signum(left);
				right = (Math.abs(left) + Math.abs(right)) / 2 * Math.signum(right);
			}

			// cube the inputs (while preserving the sign) to increase fine
			// control while permitting full power
			right = right * right * right;
			left = left * left * left;

			Drive.getInstance().tankDrive(OI.getInstance().speedMultiplier * left,
					-OI.getInstance().speedMultiplier * right);

		}
	}

	// Always return false for a default command
	@Override
	protected boolean isFinishedNR() {
		return false;
	}

	// Called once after isFinished returns true
	@Override
	protected void onEnd(boolean interrupted) {
		Drive.getInstance().arcadeDrive(0, 0);
	}
}