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
		prevVal = Hood.getInstance().getDisplacement();
		Hood.getInstance().disableProfiler();
		Hood.getInstance().enableProfilerFromDisplacement(val);
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
