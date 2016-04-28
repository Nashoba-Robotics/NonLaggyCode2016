package edu.nr.robotics.subsystems.intakearm;

import edu.nr.lib.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class IntakeArmSetPIDSmartDashboardCommand extends NRCommand {


	@Override
	protected void onStart() {
		IntakeArm.getInstance().setPID(SmartDashboard.getNumber("Intake Arm P"), SmartDashboard.getNumber("Intake Arm I"), SmartDashboard.getNumber("Intake Arm D"));
	}
    
}
