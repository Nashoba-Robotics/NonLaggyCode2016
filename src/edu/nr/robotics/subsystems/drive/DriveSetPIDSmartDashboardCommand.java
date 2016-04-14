package edu.nr.robotics.subsystems.drive;

import edu.nr.lib.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveSetPIDSmartDashboardCommand extends NRCommand {


	@Override
	protected void onStart() {
		Drive.getInstance().setPID(SmartDashboard.getNumber("Drive P"), SmartDashboard.getNumber("Drive I"), SmartDashboard.getNumber("Drive D"), SmartDashboard.getNumber("Drive F"));
	}
    
}
