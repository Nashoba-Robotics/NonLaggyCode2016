package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HoodSmartDashboardPositionCommand extends NRCommand {


	@Override
	protected void onStart() {
		new HoodPositionCommand(SmartDashboard.getNumber("Hood location for setting")).start();
	}

}
