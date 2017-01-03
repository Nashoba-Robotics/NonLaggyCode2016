package edu.nr.robotics.auton;

import edu.nr.robotics.RobotMap;
import edu.nr.lib.GetGyroCommand;
import edu.nr.lib.WaitUntilGyroCommand;
import edu.nr.robotics.Robot.position;
import edu.nr.robotics.subsystems.drive.DriveAnglePIDCommand;
import edu.nr.robotics.subsystems.drive.DriveCancelCommand;
import edu.nr.robotics.subsystems.drive.DriveConstantCommand;
import edu.nr.robotics.subsystems.drive.DriveSimpleDistanceCommand;
import edu.nr.robotics.subsystems.drive.DriveSimpleDistanceWithGyroCommand;
import edu.nr.robotics.subsystems.drive.DriveTurnConstantCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonTurnPositionCommand extends CommandGroup {

	public AutonTurnPositionCommand(position selected, GetGyroCommand gyro) {
		switch(selected) {
		case Five:
			addSequential(new DriveSimpleDistanceWithGyroCommand(1,0.4));
			addParallel(new DriveConstantCommand(true, -0.1, -0.7));
			addSequential(new WaitUntilGyroCommand(RobotMap.AUTON_FIVE_ALIGN_ANGLE, gyro));
			break;
		case Four:
			addSequential(new DriveAnglePIDCommand(RobotMap.AUTON_FOUR_ALIGN_ANGLE, gyro));
			break;
		case Three:
			addSequential(new DriveAnglePIDCommand(RobotMap.AUTON_THREE_ALIGN_ANGLE, gyro));
			break;
		case Two:
			addSequential(new DriveSimpleDistanceWithGyroCommand(3,0.4));
			addSequential(new DriveAnglePIDCommand(RobotMap.AUTON_TWO_ALIGN_ANGLE, gyro));
			break;
		case One:
			addParallel(new DriveConstantCommand(true, -0.7, -0.1));
			addSequential(new WaitUntilGyroCommand(RobotMap.AUTON_ONE_ALIGN_ANGLE, gyro));
			break;
		default:
			break;
		}
		addSequential(new DriveCancelCommand());
	}
}
