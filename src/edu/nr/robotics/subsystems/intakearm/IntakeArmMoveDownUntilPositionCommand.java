package edu.nr.robotics.subsystems.intakearm;

import edu.nr.lib.NRCommand;

/**
 *
 */
public class IntakeArmMoveDownUntilPositionCommand extends NRCommand {

	double val;
	
    public IntakeArmMoveDownUntilPositionCommand(double val) {
    	requires(IntakeArm.getInstance());
    	this.val = val;
    }
    
    @Override
	protected void onStart() {
    	IntakeArm.getInstance().enable();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
	protected void onExecute() {
    	IntakeArm.getInstance().setSetpoint(0);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
	protected boolean isFinishedNR() {
        return IntakeArm.getInstance().get() < val;
    }

    // Called once after isFinished returns true
    @Override
    protected void onEnd() {
    	IntakeArm.getInstance().disable();
    }
}
