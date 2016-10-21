package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;

public class HoodNeutralCommand extends NRCommand {

	public HoodNeutralCommand() {
		requires(Hood.getInstance());
	}
	
	@Override
	protected void onStart() {
		Hood.getInstance().disableProfiler();
		Hood.getInstance().setMotor(0);
	}
	
	@Override
	protected boolean isFinishedNR() {
		return false;
	}
}
