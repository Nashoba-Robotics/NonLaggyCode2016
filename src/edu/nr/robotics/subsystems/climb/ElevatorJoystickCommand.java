package edu.nr.robotics.subsystems.climb;

import edu.nr.lib.NRCommand;
import edu.nr.robotics.OI;

/**
 *
 */
public class ElevatorJoystickCommand extends NRCommand {

    public ElevatorJoystickCommand() {
        requires(Elevator.getInstance());
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
	protected void onExecute() {
    	Elevator.getInstance().setMotorValue(OI.getInstance().getElevatorMoveValue());
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
	protected boolean isFinishedNR() {
        return false;
    }
}
