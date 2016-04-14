package edu.nr.robotics.subsystems.climb;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ElevatorResetPart2Command extends CommandGroup {

	public ElevatorResetPart2Command() {
        addParallel(new ElevatorVoltageCommand(-0.6));
        addSequential(new ElevatorWaitForMotorStallTimeCommand(1));
        addSequential(new ElevatorResetEncoderCommand());
        addParallel(new ElevatorOffCommand());
    }
}
