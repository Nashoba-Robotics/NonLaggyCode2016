package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HoodSmartDashboardVelocityCommand extends NRCommand {
	
    public HoodSmartDashboardVelocityCommand() {
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
    	Hood.getInstance().setMotorInDPS(SmartDashboard.getNumber("Hood velocity for setting (deg per s)"));
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
	protected boolean isFinishedNR() {
        return false;
    }
}
