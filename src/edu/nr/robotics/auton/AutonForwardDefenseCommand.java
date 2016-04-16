package edu.nr.robotics.auton;

import edu.nr.lib.AngleUnit;
import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.DriveAnglePIDCommand;
import edu.nr.robotics.subsystems.drive.DriveConstantCommand;
import edu.nr.robotics.subsystems.drive.DriveDistanceCommand;
import edu.nr.robotics.subsystems.drive.DriveSimpleDistanceCommand;
import edu.nr.robotics.subsystems.hood.HoodMoveDownUntilLimitSwitchCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArmHomeHeightCommandGroup;
import edu.nr.robotics.subsystems.intakearm.IntakeArmMoveUpUntilPositionCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArmPositionCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutonForwardDefenseCommand extends CommandGroup {

	
	public AutonForwardDefenseCommand(Robot.defense selected) {
		switch(selected) {
		case Guillotine:
	    	addParallel(new IntakeArmPositionCommand(RobotMap.INTAKE_BOTTOM_POS, 0.5));
	    	addParallel(new DriveConstantCommand(false, true, true, 0.4));
	    	addSequential(new WaitCommand(4));
	    	addParallel(new DriveConstantCommand(false, true, true, 0.4));
	    	addSequential(new WaitCommand(0.5));
	        addParallel(new DriveConstantCommand(false, true, true , 0.2));
	        addSequential(new IntakeArmMoveUpUntilPositionCommand(RobotMap.INTAKE_TOP_POS));
	        addSequential(new DriveDistanceCommand(8,0.75));
			break;
		case Other:
			addSequential(new HoodMoveDownUntilLimitSwitchCommand());
			//addSequential(new IntakeArmMoveUpUntilLimitSwitchCommand());
			addSequential(new DriveSimpleDistanceCommand(3.75, 0.3));
			addParallel(new DriveConstantCommand(false, true, true, 0));
			addSequential(new WaitCommand(0.2));
			addSequential(new DriveDistanceCommand(13.75, 1));
			addParallel(new IntakeArmHomeHeightCommandGroup());
			addSequential(new WaitCommand(0.5));
			break;
		case RoughTerrain:
			addSequential(new HoodMoveDownUntilLimitSwitchCommand());
			//addSequential(new IntakeArmMoveUpUntilLimitSwitchCommand());
			addSequential(new DriveSimpleDistanceCommand(3.1, 0.3));
			addParallel(new DriveConstantCommand(false, true, true, 0));
			addSequential(new WaitCommand(0.2));
			addSequential(new DriveDistanceCommand(14.4, 1));
			addParallel(new IntakeArmHomeHeightCommandGroup());
			addSequential(new WaitCommand(0.5));
			break;
		case LowBar:
	    	addSequential(new HoodMoveDownUntilLimitSwitchCommand());
			addSequential(new IntakeArmPositionCommand(RobotMap.INTAKE_INTAKE_POS, 0.05));
			addSequential(new DriveDistanceCommand(14, 0.6));
			break;
		default:
			break;
		
		}
	}

}