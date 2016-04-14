package edu.nr.robotics.subsystems.loaderroller;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class LoaderRollerNeutralCommand extends CommandGroup {
    
    public  LoaderRollerNeutralCommand() {
        addSequential(new LoaderRollerSpeedCommand(0));
    }
}
