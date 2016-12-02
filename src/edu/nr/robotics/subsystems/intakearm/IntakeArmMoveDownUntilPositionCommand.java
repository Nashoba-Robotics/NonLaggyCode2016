package edu.nr.robotics.subsystems.intakearm;

import edu.nr.lib.NRCommand;

public class IntakeArmMoveDownUntilPositionCommand extends NRCommand {
	
	double pos;
	
	public IntakeArmMoveDownUntilPositionCommand(double pos) {
		this.pos = pos;
    	requires(IntakeArm.getInstance());
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
	protected void onExecute() {
    	IntakeArm.getInstance().setMotor(-1);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
	protected boolean isFinishedNR() {
        return IntakeArm.getInstance().getPosition() < pos;
    }

    // Called once after isFinished returns true
    @Override
    protected void onEnd() {
    	IntakeArm.getInstance().setMotor(0);
    }
}
