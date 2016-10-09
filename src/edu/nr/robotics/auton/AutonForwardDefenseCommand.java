package edu.nr.robotics.auton;

import edu.nr.lib.WaitUntilGyroCommand;
import edu.nr.lib.units.Angle;
import edu.nr.robotics.Robot;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.commandgroups.AutoShovelOfFriesCommandGroup;
import edu.nr.robotics.subsystems.drive.DriveAnglePIDCommand;
import edu.nr.robotics.subsystems.drive.DriveCancelCommand;
import edu.nr.robotics.subsystems.drive.DriveConstantCommand;
import edu.nr.robotics.subsystems.drive.DriveDistanceCommand;
import edu.nr.robotics.subsystems.drive.DriveSimpleDistanceCommand;
import edu.nr.robotics.subsystems.drive.DriveSimpleDistanceWithGyroCommand;
import edu.nr.robotics.subsystems.hood.HoodMoveDownUntilLimitSwitchCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArmBottomHeightCommandGroup;
import edu.nr.robotics.subsystems.intakearm.IntakeArmHomeHeightCommandGroup;
import edu.nr.robotics.subsystems.intakearm.IntakeArmMoveDownUntilPositionCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArmMoveUpUntilPositionCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArmPositionCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutonForwardDefenseCommand extends CommandGroup {

	
	public AutonForwardDefenseCommand(Robot.defense selected) {
		switch(selected) {
		case RoughTerrain:
			addSequential(new HoodMoveDownUntilLimitSwitchCommand());
			addSequential(new DriveSimpleDistanceWithGyroCommand(3.1, 0.3));
			addParallel(new DriveConstantCommand(false, true, true, 0));
			addSequential(new WaitCommand(0.2));
			addSequential(new DriveDistanceCommand(14.4, 1));
			break;
		case LowBar:
	    	addSequential(new HoodMoveDownUntilLimitSwitchCommand());
			addSequential(new IntakeArmPositionCommand(RobotMap.INTAKE_INTAKE_POS, 0.05));
			addSequential(new DriveDistanceCommand(14, 0.6));
			break;
		case Guillotine:
			addSequential(new HoodMoveDownUntilLimitSwitchCommand());
			addSequential(new IntakeArmBottomHeightCommandGroup());
			addSequential(new DriveSimpleDistanceWithGyroCommand(3.1, 0.3));
			addParallel(new DriveConstantCommand(false, true, true, 0));
			addSequential(new WaitCommand(0.2));
			addSequential(new DriveDistanceCommand(13.75, 1));
			break;
		case Cheval:
			addSequential(new HoodMoveDownUntilLimitSwitchCommand());
			addSequential(new DriveDistanceCommand(3.4, 0.3));
			addSequential(new WaitCommand(0.5));
			addSequential(new AutoShovelOfFriesCommandGroup());
			addSequential(new DriveDistanceCommand(8, 1));
			break;
		case Other:
			addSequential(new HoodMoveDownUntilLimitSwitchCommand());
			addSequential(new DriveSimpleDistanceWithGyroCommand(3.75, 0.3));
			addParallel(new DriveConstantCommand(false, true, true, 0));
			addSequential(new WaitCommand(0.2));
			addSequential(new DriveDistanceCommand(13.75, 1));
			break;
		default:
			break;
		
		}
	}

}
