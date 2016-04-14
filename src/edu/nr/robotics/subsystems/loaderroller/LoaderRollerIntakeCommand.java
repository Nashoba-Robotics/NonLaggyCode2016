package edu.nr.robotics.subsystems.loaderroller;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class LoaderRollerIntakeCommand extends CommandGroup {
    
    public  LoaderRollerIntakeCommand() {
        addSequential(new LoaderRollerSpeedCommand(1));
    }
}
