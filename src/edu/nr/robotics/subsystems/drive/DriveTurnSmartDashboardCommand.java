package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveTurnSmartDashboardCommand extends NRCommand {	
	
    public DriveTurnSmartDashboardCommand(){
        requires(Drive.getInstance());
    }

	@Override
	protected void onStart() {
			Drive.getInstance().tankDrive(-SmartDashboard.getNumber("Turn Constant Value"), SmartDashboard.getNumber("Turn Constant Value"));
	}

	
	@Override
	protected boolean isFinishedNR() {
		return false;
	}
    
}
