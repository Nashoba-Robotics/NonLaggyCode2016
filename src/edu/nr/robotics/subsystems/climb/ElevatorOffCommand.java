package edu.nr.robotics.subsystems.climb;

import edu.nr.lib.NRCommand;

/**
 *
 */
public class ElevatorOffCommand extends NRCommand {
    	
    public ElevatorOffCommand() {
    	requires(Elevator.getInstance());
    }

    // Called just before this Command runs the first time
    @Override
	protected void onStart() {
    	Elevator.getInstance().setMotorValue(0);
    }
}
