package edu.nr.robotics.subsystems.drive;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class DrivePulseCommand extends CommandGroup {
    
    public  DrivePulseCommand() {
        addSequential(new DriveTurnConstantCommand(.5));
        addSequential(new WaitCommand(0.25));
        addSequential(new DriveTurnConstantCommand(0));
    }
}
