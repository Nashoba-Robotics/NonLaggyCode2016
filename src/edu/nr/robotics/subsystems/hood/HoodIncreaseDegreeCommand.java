package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;

public class HoodIncreaseDegreeCommand extends NRCommand {

	double val;
	double prevVal;
	
	public HoodIncreaseDegreeCommand(double val) {
		this.val = val;
		requires(Hood.getInstance());
	}

	@Override
	protected void onStart() {
		prevVal = Hood.getInstance().get();
		Hood.getInstance().enable();
	}

	@Override
	protected void onExecute() {
		Hood.getInstance().setSetpoint(val + prevVal);
	}

	@Override
	protected void onEnd(boolean interrupted) {
	}

	@Override
	protected boolean isFinishedNR() {
		return Math.abs(Hood.getInstance().get() - (val + prevVal)) < 0.3 || ((val > 0) ? Hood.getInstance().isTopLimitSwitchClosed() : Hood.getInstance().isBotLimitSwitchClosed());
	}

}
