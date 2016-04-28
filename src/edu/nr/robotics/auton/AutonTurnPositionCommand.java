package edu.nr.robotics.auton;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.auton.AutonFollowInstructionsShootCommand.GetGyro;
import edu.nr.lib.WaitUntilGyroCommand;
import edu.nr.robotics.Robot.position;
import edu.nr.robotics.subsystems.drive.DriveAnglePIDCommand;
import edu.nr.robotics.subsystems.drive.DriveCancelCommand;
import edu.nr.robotics.subsystems.drive.DriveConstantCommand;
import edu.nr.robotics.subsystems.drive.DriveSimpleDistanceCommand;
import edu.nr.robotics.subsystems.drive.DriveTurnConstantCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonTurnPositionCommand extends CommandGroup {

	public AutonTurnPositionCommand(position selected, GetGyro gyro) {
		switch(selected) {
		case Five:
			addParallel(new DriveConstantCommand(true, 0.1, 0.7));
			addSequential(new WaitUntilGyroCommand(RobotMap.AUTON_FIVE_ALIGN_ANGLE, gyro));
			break;
		case Four:
			addParallel(new DriveTurnConstantCommand(1));
			addSequential(new WaitUntilGyroCommand(RobotMap.AUTON_FOUR_ALIGN_ANGLE, gyro));
			break;
		case Three:
			addParallel(new DriveTurnConstantCommand(-1));
			addSequential(new WaitUntilGyroCommand(RobotMap.AUTON_THREE_ALIGN_ANGLE, gyro));
			break;
		case Two:
			addParallel(new DriveTurnConstantCommand(-1));
			addSequential(new WaitUntilGyroCommand(RobotMap.AUTON_TWO_ALIGN_ANGLE, gyro));
			break;
		case One:
			addParallel(new DriveConstantCommand(true, 0.7, 0.1));
			addSequential(new WaitUntilGyroCommand(RobotMap.AUTON_ONE_ALIGN_ANGLE, gyro));
			break;
		default:
			break;
		}
		addSequential(new DriveCancelCommand());
	}

}
