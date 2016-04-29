package edu.nr.robotics.commandgroups;

import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.drive.DriveConstantCommand;
import edu.nr.robotics.subsystems.drive.DriveDistanceCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArmBottomHeightCommandGroup;
import edu.nr.robotics.subsystems.intakearm.IntakeArmMoveDownUntilLimitSwitchCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArmMoveDownUntilPositionCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArmMoveUpUntilPositionCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArmOffCommand;
import edu.nr.robotics.subsystems.intakearm.IntakeArmPositionCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class AutoShovelOfFriesCommandGroup extends CommandGroup {
	
    public  AutoShovelOfFriesCommandGroup() {
        addSequential(new IntakeArmMoveDownUntilPositionCommand(RobotMap.INTAKE_INTAKE_POS));
        addParallel(new IntakeArmMoveDownUntilLimitSwitchCommand());
        addSequential(new WaitCommand(0.5));
        addSequential(new DriveDistanceCommand(2.2,1));
        addParallel(new DriveConstantCommand(true, true, true, -0.15));
        addSequential(new IntakeArmMoveUpUntilPositionCommand(RobotMap.INTAKE_INTAKE_POS));
        addSequential(new DriveDistanceCommand(3,0.3));
    }
}
