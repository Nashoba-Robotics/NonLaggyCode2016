package edu.nr.robotics.subsystems.hood;

import edu.nr.lib.NRCommand;

public class HoodMoveDownUntilLimitSwitchCommand extends NRCommand {

	public HoodMoveDownUntilLimitSwitchCommand() {
    	requires(Hood.getInstance());
    }

    // Called just before this Command runs the first time
    @Override
	protected void initialize() {
    }
    
    @Override
	protected void onStart() {
    	Hood.getInstance().disable();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
	protected void onExecute() {
    	Hood.getInstance().setMotor(-0.5);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
	protected boolean isFinishedNR() {
        return Hood.getInstance().isBotLimitSwitchClosed();
    }

    // Called once after isFinished returns true
    @Override
    protected void onEnd(boolean interrupted) {
    	Hood.getInstance().setSetpoint(0);
    	Hood.getInstance().enable();
    }
}
