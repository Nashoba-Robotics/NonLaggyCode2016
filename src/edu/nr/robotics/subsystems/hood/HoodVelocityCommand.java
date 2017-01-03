package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;
import edu.nr.robotics.OI;

/**
 *
 */
public class HoodVelocityCommand extends NRCommand {

	double moveValue = 0;
	
    public HoodVelocityCommand(double moveValue) {
        requires(Hood.getInstance());
    }
    
    @Override
    protected void onStart() {
    	if(Hood.getInstance().isProfilerEnabled())
    		Hood.getInstance().disableProfiler();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
	protected void onExecute() {
    	Hood.getInstance().setMotor(moveValue);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
	protected boolean isFinishedNR() {
        return false;
    }
    
    @Override
	protected void onEnd(boolean interrupted) {
    }
}
