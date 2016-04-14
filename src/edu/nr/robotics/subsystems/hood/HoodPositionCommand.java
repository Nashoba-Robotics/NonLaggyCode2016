package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;

public class HoodPositionCommand extends NRCommand {

	double val;
	double speed;
	
	double prevSpeed;
	
	public HoodPositionCommand(double val) {
		this(val, 1);
	}
	
	public HoodPositionCommand(double val, double d) {
		this.val = val;
		this.speed = d;
		requires(Hood.getInstance());

	}

	@Override
	protected void onStart() {
		Hood.getInstance().enable();
		prevSpeed = Hood.getInstance().getMaxSpeed();
		Hood.getInstance().setMaxSpeedPID(speed);
	}

	@Override
	protected void onExecute() {
		Hood.getInstance().setSetpoint(val);
	}

	@Override
	protected void onEnd(boolean interrupted) {
		Hood.getInstance().setMaxSpeedPID(prevSpeed);
	}

	@Override
	protected boolean isFinishedNR() {
		return Math.abs(Hood.getInstance().get() - val) < 0.005;
	}

}
