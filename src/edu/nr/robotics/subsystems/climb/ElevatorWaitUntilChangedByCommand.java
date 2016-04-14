package edu.nr.robotics.subsystems.climb;

import edu.nr.lib.NRCommand;

/**
 *
 */
public class ElevatorWaitUntilChangedByCommand extends NRCommand {
    
   	double value;
   	
   	double startValue;
		
    public ElevatorWaitUntilChangedByCommand(double value) {
    	this.value = value;
    }
    
   	@Override
    public void onStart() {
    	startValue = Elevator.getInstance().getEncoder();
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
	protected boolean isFinishedNR() {
        return Math.abs(Elevator.getInstance().getEncoder() - startValue) > value;
    }
}
