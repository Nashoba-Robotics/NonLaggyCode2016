package edu.nr.robotics.subsystems.intakearm;

import edu.nr.lib.NRCommand;

public class IntakeArmPositionCommand extends NRCommand {

	double val;
	double threshold;
	
	public IntakeArmPositionCommand(double val) {
		this(val, 0);
	}
	
	public IntakeArmPositionCommand(double val, double threshold) {
		this.val = val;
		this.threshold = threshold;
		requires(IntakeArm.getInstance());	
	}

	@Override
	protected void onStart() {
		IntakeArm.getInstance().enable();
	}

	@Override
	protected void onExecute() {
		IntakeArm.getInstance().setSetpoint(val);
	}
	
	@Override
	protected boolean isFinishedNR() {
		System.err.println("val " + ((IntakeArm.getInstance().get() - val) < threshold));
		if(threshold != 0)
			return Math.abs(IntakeArm.getInstance().getError()) < threshold;
		return !IntakeArm.getInstance().isEnable();

	}
}
