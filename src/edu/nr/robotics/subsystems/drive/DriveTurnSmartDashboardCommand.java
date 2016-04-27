package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;
import edu.nr.robotics.RobotMap;
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
		//Drive.getInstance().setPID(RobotMap.DRIVE_TURN_P, RobotMap.DRIVE_TURN_I, RobotMap.DRIVE_TURN_D, RobotMap.DRIVE_TURN_F);
		Drive.getInstance().tankDrive(-SmartDashboard.getNumber("Turn Constant Value"), SmartDashboard.getNumber("Turn Constant Value"));
	}

	
	@Override
	protected boolean isFinishedNR() {
		return false;
	}
	
	@Override
	protected void onEnd() {
		//Drive.getInstance().setPID(RobotMap.DRIVE_P, RobotMap.DRIVE_I, RobotMap.DRIVE_D, RobotMap.DRIVE_F);

	}
    
}
