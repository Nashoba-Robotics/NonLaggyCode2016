package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;

/**
 *
 */
public class DriveConstantCommand extends NRCommand {

	boolean PID;
	double left, right;
	
	
    public DriveConstantCommand(boolean PID, boolean left, boolean right, double val) {
    	this.PID = PID;
    	this.left = left ? val : 0;
    	this.right = right ? val : 0;
        requires(Drive.getInstance());
    }

	public DriveConstantCommand(boolean PID, double left, double right) {
		this.PID = PID;
		this.left = left;
		this.right = right;
        requires(Drive.getInstance());
	}

	@Override
	protected void onStart() {
		Drive.getInstance().setPIDEnabled(PID);
	}

	@Override
	protected void onExecute() {
		if(PID)
			Drive.getInstance().setPIDSetpoint(left, right);
		else
			Drive.getInstance().setRawMotorSpeed(left, right);
	}

	@Override
	protected void onEnd(boolean interrupted) {
		if(PID)
			Drive.getInstance().setPIDSetpoint(0, 0);
		else
			Drive.getInstance().setRawMotorSpeed(0, 0);
	}

	@Override
	protected boolean isFinishedNR() {
		return false;
	}
    
}
