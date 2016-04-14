package edu.nr.robotics.commandgroups;

import edu.nr.robotics.subsystems.intakeroller.IntakeRollerNeutralCommand;
import edu.nr.robotics.subsystems.loaderroller.LoaderRollerNeutralCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PukeFinishCommandGroup extends CommandGroup {
    
	double oldRampRate;
	
    public  PukeFinishCommandGroup() {
        addParallel(new LoaderRollerNeutralCommand());
        addParallel(new IntakeRollerNeutralCommand());
    }
}
