package edu.nr.robotics.subsystems.intakearm;

import edu.nr.lib.NRCommand;

public class IntakeArmOffCommand extends NRCommand {

	public IntakeArmOffCommand() {
		requires(IntakeArm.getInstance());
	}
	
	@Override
	protected void onStart() {
		IntakeArm.getInstance().disable();
	}
	
	@Override
	public boolean isFinishedNR() {
		return false;
	}

}
