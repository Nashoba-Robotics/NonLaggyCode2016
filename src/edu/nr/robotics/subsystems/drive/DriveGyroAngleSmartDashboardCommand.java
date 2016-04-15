package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.AngleUnit;
import edu.nr.lib.NRCommand;
import edu.nr.robotics.RobotMap;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveGyroAngleSmartDashboardCommand extends NRCommand {	
	
	@Override
	protected void onStart() {
		System.out.println("Starting gyro angle smart dashboard command");
		new DriveAnglePIDCommand(SmartDashboard.getNumber("Gyro Angle"), AngleUnit.DEGREE).start();
	}
    
}
