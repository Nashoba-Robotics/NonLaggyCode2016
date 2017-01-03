package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;

public class HoodPositionCommand extends NRCommand {

	double position;
	
	public HoodPositionCommand(double position) {
		this.position = position;
		requires(Hood.getInstance());
	}

	@Override
	protected void onStart() {
		Hood.getInstance().disableProfiler();
		Hood.getInstance().enableProfiler(position - Hood.getInstance().getDisplacement());
	}
	
	@Override
	protected boolean isFinishedNR() {
		return false;
	}
	
	@Override
	protected void onEnd() {
		Hood.getInstance().disableProfiler();
	}

}
