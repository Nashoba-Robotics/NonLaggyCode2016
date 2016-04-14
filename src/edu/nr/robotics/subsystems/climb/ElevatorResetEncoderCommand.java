package edu.nr.robotics.subsystems.climb;

import edu.nr.lib.NRCommand;

/**
 *
 */
public class ElevatorResetEncoderCommand extends NRCommand {
    
    public  ElevatorResetEncoderCommand() {
    	requires(Elevator.getInstance());
    }
    
    @Override
    public void onStart() {
    	Elevator.getInstance().resetEncoder();
    }
}
