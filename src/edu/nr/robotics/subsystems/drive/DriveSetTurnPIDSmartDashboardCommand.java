package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveSetTurnPIDSmartDashboardCommand extends NRCommand {


	@Override
	protected void onStart() {
		DriveGyroAngleSmartDashboardCommand.turn_p = SmartDashboard.getNumber("Turn P");
		DriveGyroAngleSmartDashboardCommand.turn_i = SmartDashboard.getNumber("Turn I");
		DriveGyroAngleSmartDashboardCommand.turn_d = SmartDashboard.getNumber("Turn D");
		
		System.out.println("Turn P: " + DriveGyroAngleSmartDashboardCommand.turn_p + " Turn I: " + DriveGyroAngleSmartDashboardCommand.turn_i + " Turn D: " +  DriveGyroAngleSmartDashboardCommand.turn_d);
	}
    
}
