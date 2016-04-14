package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;

/**
 *
 */
public class DriveTurnConstantCommand extends NRCommand {

	boolean PID;
	double val;
	
	
    public DriveTurnConstantCommand(double val) {
    	this.val = val;
        requires(Drive.getInstance());
    }

	@Override
	protected void onStart() {
			Drive.getInstance().setRawMotorSpeed(-val, val);
	}
    
}
