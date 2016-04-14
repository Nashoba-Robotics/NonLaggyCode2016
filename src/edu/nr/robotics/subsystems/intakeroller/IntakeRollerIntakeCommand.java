package edu.nr.robotics.subsystems.intakeroller;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class IntakeRollerIntakeCommand extends CommandGroup {
    
    public  IntakeRollerIntakeCommand() {
        addSequential(new IntakeRollerSpeedCommand(-1));
    }
}
