package edu.nr.robotics.subsystems.intakeroller;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class IntakeRollerNeutralCommand extends CommandGroup {
    
    public  IntakeRollerNeutralCommand() {
        addSequential(new IntakeRollerSpeedCommand(0));
    }
}
