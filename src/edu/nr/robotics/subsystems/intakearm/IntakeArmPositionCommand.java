package edu.nr.robotics.subsystems.intakearm;

import edu.nr.lib.NRCommand;

public class IntakeArmPositionCommand extends NRCommand {

	double position;
	
	public IntakeArmPositionCommand(double position) {
		this(position, 1);
	}
	
	public IntakeArmPositionCommand(double position, double speed) {
		this.position = position;
		requires(IntakeArm.getInstance());
	}

	@Override
	protected void onStart() {
		IntakeArm.getInstance().disableProfiler();
		IntakeArm.getInstance().enableProfiler(position);
	}
	
	@Override
	protected boolean isFinishedNR() {
		return false;
	}
	
	@Override
	protected void onEnd() {
		IntakeArm.getInstance().disableProfiler();
	}

}
