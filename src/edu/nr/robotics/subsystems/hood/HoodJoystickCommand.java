package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;
import edu.nr.robotics.OI;

/**
 *
 */
public class HoodJoystickCommand extends NRCommand {

    public HoodJoystickCommand() {
        requires(Hood.getInstance());
    }
    
    @Override
    protected void onStart() {
    	if(Hood.getInstance().isPIDEnabled())
    		Hood.getInstance().disablePID();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
	protected void onExecute() {
    	Hood.getInstance().setMotor(OI.getInstance().getHoodMoveValue());
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
	protected boolean isFinishedNR() {
        return false;
    }
    
    @Override
	protected void onEnd(boolean interrupted) {
    	Hood.getInstance().enablePID();
    }
}
