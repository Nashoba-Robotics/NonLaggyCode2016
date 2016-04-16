package edu.nr.robotics.auton;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.auton.AutonFollowInstructionsShootCommand.GetGyro;
import edu.nr.robotics.Robot.position;
import edu.nr.robotics.subsystems.drive.DriveAnglePIDCommand;
import edu.nr.robotics.subsystems.drive.DriveSimpleDistanceCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonTurnPositionCommand extends CommandGroup {

	public AutonTurnPositionCommand(position selected, GetGyro gyro) {
		switch(selected) {
		case Five:
			addSequential(new DriveAnglePIDCommand(RobotMap.AUTON_FIVE_ALIGN_ANGLE, gyro));
			addSequential(new DriveSimpleDistanceCommand(2, 0.4));
			addSequential(new DriveAnglePIDCommand(20, gyro));
			break;
		case Four:
			addSequential(new DriveAnglePIDCommand(RobotMap.AUTON_FOUR_ALIGN_ANGLE, gyro));
			break;
		case Three:
			addSequential(new DriveAnglePIDCommand(RobotMap.AUTON_THREE_ALIGN_ANGLE, gyro));
			break;
		case Two:
			addSequential(new DriveAnglePIDCommand(RobotMap.AUTON_TWO_ALIGN_ANGLE, gyro));
			break;
		case One:
			break;
		default:
			break;
		}
	}

}
