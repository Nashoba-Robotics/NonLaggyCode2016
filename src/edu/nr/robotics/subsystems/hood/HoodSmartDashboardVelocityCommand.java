package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HoodSmartDashboardVelocityCommand extends NRCommand {
	
    public HoodSmartDashboardVelocityCommand() {
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
    	Hood.getInstance().setMotor(SmartDashboard.getNumber("Hood velocity for setting (deg per s)")/45.0);
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
