package edu.nr.robotics.auton;

import edu.nr.robotics.subsystems.hood.HoodMoveDownUntilLimitSwitchCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonDoNothingCommand extends CommandGroup {
	
	public AutonDoNothingCommand() {
    	addSequential(new HoodMoveDownUntilLimitSwitchCommand());
	}
	
}