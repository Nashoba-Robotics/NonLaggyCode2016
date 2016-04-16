package edu.nr.robotics.auton;

import edu.nr.robotics.Robot;
import edu.nr.robotics.Robot.defense;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonFollowInstructionsShootCommand extends CommandGroup {
    
	
	
    public  AutonFollowInstructionsShootCommand() {
    	addSequential(new AutonForwardDefenseCommand((defense) Robot.getInstance().defensePicker.getSelected()));
        addSequential(new AutonTurnPositionCommand((Robot.position) Robot.getInstance().positionPicker.getSelected()));
		addSequential(new AutonAlignCommand());

    }
}
