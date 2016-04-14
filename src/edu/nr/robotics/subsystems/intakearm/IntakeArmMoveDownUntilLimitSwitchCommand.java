package edu.nr.robotics.subsystems.intakearm;

import edu.nr.lib.NRCommand;

/**
 *
 */
public class IntakeArmMoveDownUntilLimitSwitchCommand extends NRCommand {

    public IntakeArmMoveDownUntilLimitSwitchCommand() {
    	requires(IntakeArm.getInstance());
    }
    
    @Override
	protected void onStart() {
    	IntakeArm.getInstance().enable();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
	protected void onExecute() {
    	IntakeArm.getInstance().setSetpoint(IntakeArm.getInstance().getSetpoint() - 10);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
	protected boolean isFinishedNR() {
        return IntakeArm.getInstance().isBotLimitSwitchClosed();
    }

    // Called once after isFinished returns true
    @Override
    protected void onEnd() {
    	IntakeArm.getInstance().setSetpoint(0);
    }
}
