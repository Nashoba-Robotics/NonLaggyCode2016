package edu.nr.robotics.subsystems.intakearm;

import edu.nr.lib.NRCommand;

public class IntakeArmSetMaxSpeedCommand extends NRCommand {
	
	double maxSpeed;
	
	public IntakeArmSetMaxSpeedCommand(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	@Override
	public void onStart() {
		IntakeArm.getInstance().setMaxSpeed(maxSpeed);
	}
}
