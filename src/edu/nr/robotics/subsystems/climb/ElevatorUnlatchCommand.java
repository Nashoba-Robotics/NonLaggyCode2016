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
}
