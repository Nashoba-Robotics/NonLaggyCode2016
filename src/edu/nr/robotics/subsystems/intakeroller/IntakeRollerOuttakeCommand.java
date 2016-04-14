package edu.nr.robotics.subsystems.intakeroller;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class IntakeRollerOuttakeCommand extends CommandGroup {
    
    public  IntakeRollerOuttakeCommand() {
        addSequential(new IntakeRollerSpeedCommand(1));
    }
}
