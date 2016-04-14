package edu.nr.robotics.commandgroups;

import edu.nr.robotics.subsystems.intakeroller.IntakeRollerNeutralCommand;
import edu.nr.robotics.subsystems.loaderroller.LoaderRollerNeutralCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class LowGoalFinishCommandGroup extends CommandGroup {
    	
    public  LowGoalFinishCommandGroup() {
        addParallel(new IntakeRollerNeutralCommand());
        addParallel(new LoaderRollerNeutralCommand());
    }
}
