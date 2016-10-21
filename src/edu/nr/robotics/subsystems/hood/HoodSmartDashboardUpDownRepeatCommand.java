package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;
import edu.nr.robotics.OI;

/**
 *
 */
public class HoodSmartDashboardUpDownRepeatCommand extends NRCommand {

	double moveValue = 0;
	
    public HoodSmartDashboardUpDownRepeatCommand(double moveValue) {
        requires(Hood.getInstance());
    }
    
    @Override
    protected void onStart() {
    	if(Hood.getInstance().isProfilerEnabled())
    		Hood.getInstance().disableProfiler();
    }
    
    boolean goingUp = true;
    
    // Called repeatedly when this Command is scheduled to run
    @Override
	protected void onExecute() {
    	if(goingUp) {
    		Hood.getInstance().setMotor(moveValue);
    	} else {
    		Hood.getInstance().setMotor(-moveValue);
    	}
    	if(Hood.getInstance().isTopLimitSwitchClosed()) {
			goingUp = false;
		}

    	if(Hood.getInstance().isBotLimitSwitchClosed()) {
			goingUp = true;
		}
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
	protected boolean isFinishedNR() {
        return false;
    }
}
