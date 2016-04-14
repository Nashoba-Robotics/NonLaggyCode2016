package edu.nr.robotics.subsystems.climb;

import edu.nr.lib.NRCommand;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.intakearm.IntakeArm;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class ElevatorUnlatchCommand extends CommandGroup {
	
	public ElevatorUnlatchCommand() {
		//addSequential(new IntakeArmMoveIfTooHighCommand());
		addParallel(new ElevatorVoltageCommand(0.6));
		addSequential(new ElevatorWaitUntilChangedByCommand(RobotMap.ELEVATOR_UNLATCH_DISTANCE));
        addParallel(new ElevatorOffCommand());
	}
	
	private class IntakeArmMoveIfTooHighCommand extends NRCommand {
		
		private IntakeArmMoveIfTooHighCommand() {
			requires(IntakeArm.getInstance());
		}
		
		@Override
		protected void onStart() {
			if(IntakeArm.getInstance().get() < RobotMap.INTAKE_TOP_POS - RobotMap.INTAKE_ARM_THRESHOLD) {
				IntakeArm.getInstance().enable();
				IntakeArm.getInstance().setSetpoint(RobotMap.INTAKE_TOP_POS);
			}
		}
		
		@Override
		protected boolean isFinishedNR() {
			if(IntakeArm.getInstance().get() < RobotMap.INTAKE_TOP_POS - RobotMap.INTAKE_ARM_THRESHOLD) {
				return false;
			} 
			return true;
		}
		
		@Override
		protected void onEnd() {
			IntakeArm.getInstance().disable();
		}
	}
}
