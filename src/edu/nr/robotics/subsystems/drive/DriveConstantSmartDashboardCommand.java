package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveConstantSmartDashboardCommand extends NRCommand {

	boolean PID;
	boolean left, right;
	
	
    public DriveConstantSmartDashboardCommand(boolean PID, boolean left, boolean right) {
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
			Drive.getInstance().setPIDSetpoint(left ? SmartDashboard.getNumber("Drive Constant Value") : 0, right  ? SmartDashboard.getNumber("Drive Constant Value") : 0);
		else
			Drive.getInstance().setRawMotorSpeed(left ? SmartDashboard.getNumber("Drive Constant Value") : 0, right  ? SmartDashboard.getNumber("Drive Constant Value") : 0);
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
