package edu.nr.robotics.auton;

import edu.nr.robotics.subsystems.drive.DriveConstantCommand;
import edu.nr.robotics.subsystems.drive.DriveDistanceCommand;
import edu.nr.robotics.subsystems.drive.DriveSimpleDistanceCommand;
import edu.nr.robotics.subsystems.hood.HoodMoveDownUntilLimitSwitchCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutonForwardRoughTerrainCommand extends CommandGroup {

	public AutonForwardRoughTerrainCommand() {
    	addSequential(new HoodMoveDownUntilLimitSwitchCommand());
		addSequential(new DriveSimpleDistanceCommand(3.1, 0.3));
		addParallel(new DriveConstantCommand(false, true, true, 0));
		addSequential(new WaitCommand(0.2));
		addSequential(new DriveDistanceCommand(14.4, 1));
	}
	
}
