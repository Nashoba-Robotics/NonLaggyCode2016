package edu.nr.robotics.auton;

import edu.nr.robotics.Robot;
import edu.nr.robotics.Robot.defense;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonFollowInstructionsForwardCommand extends CommandGroup {
    
	
	
    public  AutonFollowInstructionsForwardCommand() {
    	addSequential(new AutonForwardDefenseCommand((defense) Robot.getInstance().defensePicker.getSelected()));

    }
}
