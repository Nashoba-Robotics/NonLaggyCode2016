package edu.nr.robotics.auton;

import edu.nr.robotics.Robot;
import edu.nr.robotics.subsystems.hood.HoodMoveDownUntilLimitSwitchCommand;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonFollowInstructionsCommand extends CommandGroup {
    
	
	
    public  AutonFollowInstructionsCommand() {
    	addSequential(new HoodMoveDownUntilLimitSwitchCommand());
        addSequential((Command) Robot.getInstance().autoCommandPickerOne.getSelected());
        addSequential((Command) Robot.getInstance().autoCommandPickerTwo.getSelected());
        addSequential((Command) Robot.getInstance().autoCommandPickerThree.getSelected());
        addSequential((Command) Robot.getInstance().autoCommandPickerFour.getSelected());
        addSequential((Command) Robot.getInstance().autoCommandPickerFive.getSelected());
        addSequential((Command) Robot.getInstance().autoCommandPickerSix.getSelected());
    }
}
