package edu.nr.robotics.subsystems.climb;

import edu.nr.lib.NRCommand;
import edu.nr.robotics.RobotMap;

/**
 *
 */
public class ElevatorUpUntilGreaterThanCommand extends NRCommand {
    
   	double value;
		
    public ElevatorUpUntilGreaterThanCommand(double value) {
    	this.value = value;
    	requires(Elevator.getInstance());
    }
    
 // Called just before this Command runs the first time
    @Override
	protected void onStart() {
    	Elevator.getInstance().setMotorValue(RobotMap.ELEVATOR_UP_SPEED);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
	protected boolean isFinishedNR() {
        return Elevator.getInstance().getEncoder() < value;
    }
}
