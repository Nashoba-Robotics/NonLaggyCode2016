package edu.nr.robotics.commandgroups;

import edu.nr.robotics.subsystems.intakeroller.IntakeRollerOuttakeCommand;
import edu.nr.robotics.subsystems.loaderroller.LoaderRollerOuttakeCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PukeCommandGroup extends CommandGroup {
    
	double oldRampRate;
	
    public  PukeCommandGroup() {
        addParallel(new LoaderRollerOuttakeCommand());
        addParallel(new IntakeRollerOuttakeCommand());
    }
}
