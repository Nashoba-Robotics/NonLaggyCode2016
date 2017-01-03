package edu.nr.robotics.subsystems.intakearm;

import edu.nr.lib.NRCommand;

/**
 *
 */
public class IntakeArmMoveUpUntilLimitSwitchCommand extends NRCommand {

    public IntakeArmMoveUpUntilLimitSwitchCommand() {
    	requires(IntakeArm.getInstance());
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
	protected void onExecute() {
    	IntakeArm.getInstance().setMotor(1);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
	protected boolean isFinishedNR() {
        return IntakeArm.getInstance().isTopLimitSwitchClosed();
    }

    // Called once after isFinished returns true
    @Override
    protected void onEnd() {
    	IntakeArm.getInstance().setMotor(0);
    }
}
