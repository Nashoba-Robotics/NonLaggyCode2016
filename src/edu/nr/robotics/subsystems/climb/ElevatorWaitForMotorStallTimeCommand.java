package edu.nr.robotics.subsystems.climb;

import edu.nr.lib.NRCommand;

/**
 *
 */
public class ElevatorWaitForMotorStallTimeCommand extends NRCommand {

	long timeStalling = 0;
	
	long prevTime;
	
	long reqTime;
	
	/**
	 * 
	 * @param reqTime in seconds
	 */
    public ElevatorWaitForMotorStallTimeCommand(long reqTime) {
    	this.reqTime = reqTime*1000;
    }

    // Called just before this Command runs the first time
    @Override
    protected void onStart() {
    	prevTime = System.currentTimeMillis();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void onExecute() {
    	if(!Elevator.getInstance().isMoving()) {
    		timeStalling = System.currentTimeMillis() - prevTime;
    	} else {
    		timeStalling = 0;
        	prevTime = System.currentTimeMillis();

    	}
    	
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinishedNR() {
    	System.out.println("Time stalling: " + timeStalling);
        return timeStalling > reqTime || Elevator.getInstance().getCurrent() > 65;
    }
}
