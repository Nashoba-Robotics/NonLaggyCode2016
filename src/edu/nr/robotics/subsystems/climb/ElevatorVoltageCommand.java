package edu.nr.robotics.subsystems.climb;

import edu.nr.lib.NRCommand;

/**
 *
 */
public class ElevatorVoltageCommand extends NRCommand {

	double val;
	
    public ElevatorVoltageCommand(double val) {
    	this.val = val;
    	requires(Elevator.getInstance());
    }

    // Called just before this Command runs the first time
    @Override
	protected void onStart() {
    	Elevator.getInstance().setMotorValue(val);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
	protected boolean isFinishedNR() {
        return false;
    }
}
